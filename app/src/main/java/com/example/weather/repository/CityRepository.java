package com.example.weather.repository;

import com.example.weather.model.entity.City;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface CityRepository {

    /**
     * Get all saved cities.
     *
     * @return A observable
     */
    Observable<List<City>> getCities();

    Single<List<Long>> insertCities(List<City> cities);

    Completable deleteAllCities();
}
