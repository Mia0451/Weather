package com.example.weather.service;

import com.example.weather.model.GeoNameBasedResponse;
import com.example.weather.model.GeoZipBasedResponse;
import com.example.weather.model.WeatherResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * API service for query open weather, more details {@see https://openweathermap.org/api}.
 */
public interface OpenWeatherService {

    @Headers("Content-Type: application/json")
    @GET("data/2.5/weather")
    Observable<WeatherResponse> getWeather(@Query(value = "q", encoded = true) String location);

    @Headers("Content-Type: application/json")
    @GET("data/2.5/weather")
    Observable<WeatherResponse> getWeatherFromLatLon(@Query(value = "lat", encoded = true) double lat, @Query(value = "lon", encoded = true) double lon);

    @Headers("Content-Type: application/json")
    @GET("geo/1.0/zip")
    Observable<GeoZipBasedResponse> geoDirectBasedOnZip(@Query(value = "zip", encoded = true) String zipCodeAndCountryCode);

    @Headers("Content-Type: application/json")
    @GET("geo/1.0/direct")
    Observable<List<GeoNameBasedResponse>> geoDirectBasedOnNames(@Query(value = "q", encoded = true) String cityStateCountry, @Query(value = "limit", encoded = true) int limit);
}
