package com.example.weather.home;

import androidx.lifecycle.LifecycleOwner;

import com.example.weather.model.entity.City;
import com.example.weather.repository.CityRepository;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomeActivityPresenter extends HomeActivityContract.Presenter {

    private final HomeActivityContract.View view;
    private final CityRepository cityRepository;

    @Inject
    HomeActivityPresenter(
            @HomeActivityContract.HomeActivityLifecycleOwner LifecycleOwner lifecycleOwner,
            HomeActivityContract.View view,
            CityRepository cityRepository) {
        super(lifecycleOwner);
        this.view = view;
        this.cityRepository = cityRepository;
    }

    @Override
    public void subscribe() {
        addDisposable(
                Observable.combineLatest(
                                view.getLocalCity(),
                                cityRepository.getCities(),
                                new BiFunction<City, List<City>, List<City>>() {
                                    @Override
                                    public List<City> apply(City local, List<City> cities) {
                                        // 1) If user never searched any location, we will show local
                                        //      location only
                                        // 2) If user ever searched any location, first location we shown
                                        //      will be the last one user searched, the second one will
                                        //      be user's local location.
                                        if (cities.isEmpty()) {
                                            return Arrays.asList(local);
                                        }
                                        cities.add(1, local);
                                        return cities;
                                    }
                                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(cities -> view.showCities(cities), throwable -> view.showErrorSnackbar()));
    }

    @Override
    void clearCityHistory() {
        addDisposable(cityRepository.deleteAllCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> view.deleteHistorySuccessfully(), throwable -> view.showErrorSnackbar())
        );
    }
}
