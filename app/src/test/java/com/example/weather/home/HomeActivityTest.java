package com.example.weather.home;

import static com.google.common.truth.Truth.assertThat;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import com.example.weather.R;
import com.example.weather.model.entity.City;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import java.util.Arrays;
import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.HiltTestApplication;
import dagger.hilt.android.testing.UninstallModules;

@UninstallModules(HomeActivityModule.class)
@HiltAndroidTest
@Config(application = HiltTestApplication.class) // ok to not use WeatherApp.class
@RunWith(RobolectricTestRunner.class)
public class HomeActivityTest {

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    // TODO(wenyi): ideally HomeActivityContract.Presenter should be Activity-Scoped
    // but by using BindValue, I kind of make it Singleton-Component
    @BindValue HomeActivityContract.Presenter presenter = Mockito.mock(HomeActivityContract.Presenter.class);

    @Before
    public void setup() {
        // hiltRule.inject();
    }

    @Test
    public void buildVersion() {
        try (ActivityController<HomeActivity> controller = Robolectric.buildActivity(HomeActivity.class)) {
            controller.setup().visible(); // Moves Activity to RESUMED state, also VISIBLE.

            shadowOf(Looper.getMainLooper()).idle(); // execute any action posted in Android main thread.

            HomeActivity activity = controller.get();
            TextView buildVersion = activity.findViewById(R.id.build_version);
            assertThat(buildVersion).isNotNull();
            assertThat(buildVersion.getText()).isEqualTo(activity.getString(R.string.build_version, "1.0"));
        }
    }

    @Test
    public void clearHistory() {
        try (ActivityController<HomeActivity> controller = Robolectric.buildActivity(HomeActivity.class)) {
            controller.setup().visible(); // Moves Activity to RESUMED state, also VISIBLE.
            shadowOf(Looper.getMainLooper()).idle(); // execute any action posted in Android main thread.

            HomeActivity activity = controller.get();

            View clearHistory = activity.findViewById(R.id.drawer_item_clear_history);
            clearHistory.performClick();

            shadowOf(Looper.getMainLooper()).idle(); // execute any action posted in Android main thread.

            verify(presenter, times(1)).clearCityHistory();
        }
    }

    @Test
    public void showCities() {
        try (ActivityController<HomeActivity> controller = Robolectric.buildActivity(HomeActivity.class)) {
            controller.setup().visible(); // Moves Activity to RESUMED state, also VISIBLE.
            shadowOf(Looper.getMainLooper()).idle(); // execute any action posted in Android main thread.

            HomeActivity activity = controller.get();
            activity.showCities(Arrays.asList(new City(TestData.LATITUDE, TestData.LONGITUDE)));

            shadowOf(Looper.getMainLooper()).idle(); // execute any action posted in Android main thread.

            assertThat(activity.pageAdapter.getItemCount()).isEqualTo(1);

            City secondCity = new City();
            secondCity.city = TestData.CITY_NAME;
            secondCity.state = TestData.STATE;
            secondCity.country = TestData.COUNTRY;
            activity.showCities(Arrays.asList(new City(TestData.LATITUDE, TestData.LONGITUDE), secondCity));

            shadowOf(Looper.getMainLooper()).idle(); // execute any action posted in Android main thread.

            assertThat(activity.pageAdapter.getItemCount()).isEqualTo(2);
        }
    }

    private static class TestData {
        static final String CITY_NAME = "New York";
        static final String STATE = "New York";
        static final String COUNTRY = "us";
        static final double LATITUDE = 3.4;
        static final double LONGITUDE = 5.6;
    }
}