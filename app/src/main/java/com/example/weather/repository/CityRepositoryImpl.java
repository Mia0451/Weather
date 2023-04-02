package com.example.weather.repository;

import com.example.weather.model.dao.CityDao;
import com.example.weather.model.entity.City;
import java.util.List;
import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class CityRepositoryImpl implements CityRepository {

    private final CityDao cityDao;

    @Inject
    CityRepositoryImpl(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @Override
    public Observable<List<City>> getCities() {
        return cityDao.getCities();
    }

    @Override
    public Single<List<Long>> insertCities(List<City> city) {
        return cityDao.insert(city);
    }

    @Override
    public Completable deleteAllCities() {
        return cityDao.deleteAll();
    }
}
