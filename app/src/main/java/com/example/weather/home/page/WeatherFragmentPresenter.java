package com.example.weather.home.page;

import androidx.lifecycle.LifecycleOwner;

import com.example.weather.model.entity.City;
import com.example.weather.repository.WeatherRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeatherFragmentPresenter extends WeatherFragmentContract.Presenter {

    private final WeatherFragmentContract.View view;
    private final WeatherRepository weatherRepository;

    private City city;

    @Inject
    WeatherFragmentPresenter(
            @WeatherFragmentContract.WeatherFragmentLifecycleOwner LifecycleOwner lifecycleOwner,
            WeatherFragmentContract.View view,
            WeatherRepository weatherRepository) {
        super(lifecycleOwner);
        this.view = view;
        this.weatherRepository = weatherRepository;
    }

    // This function should be treated as initialization-effect code, called only once, asap.
    @Override
    void setCity(City city) {
        this.city = city;
    }

    @Override
    public void subscribe() {
        if (city.hasLatLon()) {
            addDisposable(
                    weatherRepository
                            .getWeatherRemote(city.latitude, city.longitude)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    weatherResponse -> view.showWeatherData(weatherResponse),
                                    throwable -> view.showErrorSnackbar()
                            )
            );
        } else {
            addDisposable(
                    weatherRepository
                            .getWeatherRemote(city.city, city.state, city.country)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    weatherResponse -> view.showWeatherData(weatherResponse),
                                    throwable -> view.showErrorSnackbar()
                            )
            );
        }

    }
}
