package com.example.weather;

import com.example.weather.service.OpenWeatherService;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@InstallIn(SingletonComponent.class)
@Module
public class ServiceModule {

    @Provides
    OpenWeatherService provideOpenWeatherService(Retrofit retrofit) {
        return retrofit.create(OpenWeatherService.class);
    }
}
