package com.example.weather.home.search;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

import android.os.Looper;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.example.weather.model.GeoZipBasedResponse;
import com.example.weather.model.entity.City;
import com.example.weather.repository.CityRepository;
import com.example.weather.repository.WeatherRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.TestScheduler;
import kotlin.Unit;

// WeatherFragmentPresenter internally use AndroidSchedulers.mainThread() which requires
// us to run the unit test with RobolectricTestRunner, don't use JUnit4 or MockitoJUnitRunner
@RunWith(RobolectricTestRunner.class)
public class SearchDialogFragmentPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private TestScheduler testScheduler = new TestScheduler();

    @SearchDialogFragmentContract.SearchDialogFragmentLifecycleOwner
    @Mock
    LifecycleOwner lifecycleOwner;
    @Mock
    SearchDialogFragmentContract.View view;
    @Mock
    WeatherRepository weatherRepository;
    @Mock
    CityRepository cityRepository;
    @Mock
    Lifecycle lifecycle;

    SearchDialogFragmentPresenter searchDialogFragmentPresenter;

    @Before
    public void setup() {
        // set calls to Schedulers.computation()/Schedulers.io() to use our test scheduler
        // RxJavaPlugins.setComputationSchedulerHandler(ignore -> testScheduler);
        RxJavaPlugins.setIoSchedulerHandler(ignore -> testScheduler);
        // RxAndroid need additionally instrument the main thread scheduler.
        RxAndroidPlugins.onMainThreadScheduler(testScheduler);

        when(lifecycleOwner.getLifecycle()).thenReturn(lifecycle);
        searchDialogFragmentPresenter = new SearchDialogFragmentPresenter(lifecycleOwner, view, weatherRepository, cityRepository);

        verify(lifecycle, times(1)).addObserver(searchDialogFragmentPresenter);
    }

    @Test
    public void searchCity_noInputs() {
        when(view.getSearchTarget()).thenReturn(new Target(null, null, null, null));

        searchDialogFragmentPresenter.searchCity();

        testScheduler.triggerActions(); // trigger action
        shadowOf(Looper.getMainLooper()).idle(); // execute any action posted in Android main thread.

        verify(weatherRepository, never()).getGeoZipBasedResponse(any(), any());
        verify(weatherRepository, never()).getGeoNameBasedResponse(any(), any(), any());
        verify(cityRepository, never()).insertCities(any());
        verify(view, times(1)).showErrorSnackbar();
    }

    private static class TestData {
        static final String CITY_NAME = "New York";
        static final String STATE = "New York";
        static final String ZIP = "10000";
        static final String COUNTRY = "us";
        static final double LATITUDE = 3.4;
        static final double LONGITUDE = 5.6;
    }
}