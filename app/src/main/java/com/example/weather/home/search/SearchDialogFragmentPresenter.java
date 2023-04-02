package com.example.weather.home.search;

import android.database.sqlite.SQLiteConstraintException;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.example.weather.model.GeoNameBasedResponse;
import com.example.weather.model.entity.City;
import com.example.weather.repository.CityRepository;
import com.example.weather.repository.WeatherRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class SearchDialogFragmentPresenter extends SearchDialogFragmentContract.Presenter {

    private final SearchDialogFragmentContract.View view;
    private final WeatherRepository weatherRepository;
    private final CityRepository cityRepository;

    @Inject
    SearchDialogFragmentPresenter(
            @SearchDialogFragmentContract.SearchDialogFragmentLifecycleOwner LifecycleOwner lifecycleOwner,
            SearchDialogFragmentContract.View view,
            WeatherRepository weatherRepository,
            CityRepository cityRepository) {
        super(lifecycleOwner);

        Log.e("here", "here in SearchFragmentPresenter.constructor()");
        this.view = view;
        this.weatherRepository = weatherRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public void subscribe() {
    }

    @Override
    void searchCity() {
        Target target = view.getSearchTarget();
        Log.e("here", "here in Search.searchCity()");
        if (!TextUtils.isEmpty(target.zipCode) && !TextUtils.isEmpty(target.country)) {
            Log.e("here", "here in Search.searchCity(), zip and country");
            addDisposable(weatherRepository
                    .getGeoZipBasedResponse(target.country, target.zipCode)
                    .subscribeOn(Schedulers.io())
                    .flatMapSingle(geoZipResponse -> cityRepository.insertCities(Arrays.asList(geoZipResponse.toCity())))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(unused -> {view.dismiss();
                        Log.e("here", "here in Search.searchCity(), dismiss");}, throwable -> {
                        Log.e("here", "here in Search.searchCity(), throwable, " + throwable);
                        if (throwable instanceof SQLiteConstraintException || throwable.getCause() instanceof SQLiteConstraintException) {
                            view.searchedCityAlreadyExist();
                        } else {
                            view.showErrorSnackbar();
                        }
                    }));
        } else if (!TextUtils.isEmpty(target.city) && (!TextUtils.isEmpty(target.state) || !TextUtils.isEmpty(target.country))) {
            addDisposable(weatherRepository
                    .getGeoNameBasedResponse(target.city, target.state, target.country)
                    .subscribeOn(Schedulers.io())
                    .flatMapSingle(geoNameBasedResponses -> {
                        //TODO(wenyi): Convert to stream()?
                        List<City> cityList = new ArrayList<>();
                        for (GeoNameBasedResponse item : geoNameBasedResponses) {
                            cityList.add(item.toCity());
                        }
                        return cityRepository.insertCities(cityList);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(unused -> view.dismiss(), throwable -> {
                        if (throwable instanceof SQLiteConstraintException || throwable.getCause() instanceof SQLiteConstraintException) {
                            view.searchedCityAlreadyExist();
                        } else {
                            view.showErrorSnackbar();
                        }

                    }));
        } else {
            view.showErrorSnackbar();
        }
    }
}
