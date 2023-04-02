package com.example.weather.home.page;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;
import android.os.Looper;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.example.weather.model.WeatherResponse;
import com.example.weather.model.entity.City;
import com.example.weather.repository.WeatherRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.TestScheduler;

// WeatherFragmentPresenter internally use AndroidSchedulers.mainThread() which requires
// us to run the unit test with RobolectricTestRunner, don't use JUnit4 or MockitoJUnitRunner
@RunWith(RobolectricTestRunner.class)
public class WeatherFragmentPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    private TestScheduler testScheduler = new TestScheduler();

    @WeatherFragmentContract.WeatherFragmentLifecycleOwner
    @Mock LifecycleOwner lifecycleOwner;
    @Mock WeatherFragmentContract.View view;
    @Mock WeatherRepository weatherRepository;
    @Mock Lifecycle lifecycle;

    WeatherFragmentPresenter weatherFragmentPresenter;

    @Before
    public void setup() {
        // set calls to Schedulers.computation()/Schedulers.io() to use our test scheduler
        // RxJavaPlugins.setComputationSchedulerHandler(ignore -> testScheduler);
        RxJavaPlugins.setIoSchedulerHandler(ignore -> testScheduler);
        // RxAndroid need additionally instrument the main thread scheduler.
        RxAndroidPlugins.onMainThreadScheduler(testScheduler);

        when(lifecycleOwner.getLifecycle()).thenReturn(lifecycle);
        weatherFragmentPresenter = new WeatherFragmentPresenter(lifecycleOwner, view, weatherRepository);

        verify(lifecycle, times(1)).addObserver(weatherFragmentPresenter);
    }

    @Test
    public void subscribe_cityWithLatLon_weatherDataReadSuccessfully() {
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setName(TestData.CITY_NAME);
        when(weatherRepository.getWeatherRemote(TestData.LATITUDE, TestData.LONGITUDE)).thenReturn(Observable.just(weatherResponse));

        weatherFragmentPresenter.setCity(new City(TestData.LATITUDE, TestData.LONGITUDE));
        weatherFragmentPresenter.subscribe();

        testScheduler.triggerActions(); // trigger action
        shadowOf(Looper.getMainLooper()).idle(); // execute any action posted in Android main thread.

        verify(view, never()).showErrorSnackbar();
        verify(view, times(1)).showWeatherData(weatherResponse);
    }

    @Test
    public void subscribe_cityWithLatLon_weatherDataReadFailed() {
        IllegalStateException error = new IllegalStateException("RPC error");
        when(weatherRepository.getWeatherRemote(TestData.LATITUDE, TestData.LONGITUDE)).thenReturn(Observable.error(error));

        weatherFragmentPresenter.setCity(new City(TestData.LATITUDE, TestData.LONGITUDE));
        weatherFragmentPresenter.subscribe();

        testScheduler.triggerActions(); // trigger action
        shadowOf(Looper.getMainLooper()).idle(); // execute any action posted in Android main thread.

        verify(view, times(1)).showErrorSnackbar();
        verify(view, never()).showWeatherData(any());
    }

    @Test
    public void subscribe_cityWithoutLatLon_weatherDataReadSuccessfully() {
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setName(TestData.CITY_NAME);
        when(weatherRepository.getWeatherRemote(TestData.CITY_NAME, TestData.STATE, TestData.COUNTRY)).thenReturn(Observable.just(weatherResponse));

        City city = new City();
        city.city = TestData.CITY_NAME;
        city.state = TestData.STATE;
        city.country = TestData.COUNTRY;
        weatherFragmentPresenter.setCity(city);
        weatherFragmentPresenter.subscribe();

        testScheduler.triggerActions(); // trigger action
        shadowOf(Looper.getMainLooper()).idle(); // execute any action posted in Android main thread.

        verify(view, never()).showErrorSnackbar();
        verify(view, times(1)).showWeatherData(weatherResponse);
    }

    @Test
    public void subscribe_cityWithoutLatLon_weatherDataReadFailed() {
        IllegalStateException error = new IllegalStateException("RPC error");
        when(weatherRepository.getWeatherRemote(TestData.CITY_NAME, TestData.STATE, TestData.COUNTRY)).thenReturn(Observable.error(error));

        City city = new City();
        city.city = TestData.CITY_NAME;
        city.state = TestData.STATE;
        city.country = TestData.COUNTRY;
        weatherFragmentPresenter.setCity(city);
        weatherFragmentPresenter.subscribe();

        testScheduler.triggerActions(); // trigger action
        shadowOf(Looper.getMainLooper()).idle(); // execute any action posted in Android main thread.

        verify(view, times(1)).showErrorSnackbar();
        verify(view, never()).showWeatherData(any());
    }

    private static class TestData {
        static final String CITY_NAME = "New York";
        static final String STATE = "New York";
        static final String COUNTRY = "us";
        static final double LATITUDE = 3.4;
        static final double LONGITUDE = 5.6;
    }
}