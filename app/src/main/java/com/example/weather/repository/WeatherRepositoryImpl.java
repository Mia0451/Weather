package com.example.weather.repository;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.example.weather.model.GeoNameBasedResponse;
import com.example.weather.model.GeoZipBasedResponse;
import com.example.weather.model.WeatherResponse;
import com.example.weather.service.OpenWeatherService;

import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import kotlin.jvm.functions.FunctionN;

public class WeatherRepositoryImpl implements WeatherRepository {

    private final OpenWeatherService openWeatherService;

    @Inject
    WeatherRepositoryImpl(OpenWeatherService openWeatherService) {
        this.openWeatherService = openWeatherService;
    }

    @Override
    public Observable<WeatherResponse> getWeatherRemote(String city, String state, String country) {
        StringBuilder sb = new StringBuilder("");
        if (!TextUtils.isEmpty(city)) {
            sb.append(city);
        }
        if (!TextUtils.isEmpty(state)) {
            sb.append(",").append(state);
        }
        if (!TextUtils.isEmpty(country)) {
            sb.append(",").append(country);
        }
        return openWeatherService.getWeather(sb.toString());
    }

    @Override
    public Observable<WeatherResponse> getWeatherRemote(double lat, double lon) {
        return openWeatherService.getWeatherFromLatLon(lat, lon);
    }

    @Override
    public Observable<GeoZipBasedResponse> getGeoZipBasedResponse(String countryCode, String zipCode) {
        Observable<GeoZipBasedResponse> response = openWeatherService.geoDirectBasedOnZip(new StringBuilder(zipCode).append(",").append(countryCode).toString());
        return response.map(geoZipBasedResponse -> {
            geoZipBasedResponse.lat = Math.floor(geoZipBasedResponse.lat * 100) / 100;
            geoZipBasedResponse.lon = Math.floor(geoZipBasedResponse.lon * 100) / 100;
            return geoZipBasedResponse;
        });
    }

    @Override
    public Observable<List<GeoNameBasedResponse>> getGeoNameBasedResponse(@NonNull String city, String state, String countryCode) {
        StringBuilder sb = new StringBuilder(city);
        if (!TextUtils.isEmpty(state)) {
            sb.append(",").append(state);
        }
        if (!TextUtils.isEmpty(countryCode)) {
            sb.append(",").append(countryCode);
        }
        // TODO(wenyi): for now we limit 1, re-think whether we want to allow more.
        Observable<List<GeoNameBasedResponse>> arr = openWeatherService.geoDirectBasedOnNames(sb.toString(), /* limit= */ 1);
        return arr.map(geoNameBasedResponses -> {
            for (GeoNameBasedResponse geo : geoNameBasedResponses) {
                geo.lat = Math.floor(geo.lat * 100) / 100;
                geo.lon = Math.floor(geo.lon * 100) / 100;
            }
            return geoNameBasedResponses;
        });
    }
}
