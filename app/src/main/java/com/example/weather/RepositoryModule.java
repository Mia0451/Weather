package com.example.weather;

import com.example.weather.repository.CityRepository;
import com.example.weather.repository.CityRepositoryImpl;
import com.example.weather.repository.WeatherRepository;
import com.example.weather.repository.WeatherRepositoryImpl;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module
public class RepositoryModule {

    @Provides
    WeatherRepository provideWeatherRepository(WeatherRepositoryImpl impl) {
        return impl;
    }

    @Provides
    CityRepository provideCityRepository(CityRepositoryImpl impl) {
        return impl;
    }
}
