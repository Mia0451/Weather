package com.example.weather.model.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weather.model.entity.City;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface CityDao {

    @Query("SELECT * FROM City ORDER BY id DESC")
    Observable<List<City>> getCities();

    @Insert(onConflict = OnConflictStrategy.ABORT)
    Single<List<Long>> insert(List<City> cities);

    @Query("DELETE FROM City")
    Completable deleteAll();
}
