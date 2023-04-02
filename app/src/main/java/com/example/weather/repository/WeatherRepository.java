package com.example.weather.repository;

import androidx.annotation.NonNull;
import com.example.weather.model.GeoNameBasedResponse;
import com.example.weather.model.GeoZipBasedResponse;
import com.example.weather.model.WeatherResponse;
import java.util.List;
import io.reactivex.rxjava3.core.Observable;

/**
 * Repository for weather data.
 *
 * Ideally there should be API both for local data-storage and remote service api call. For this
 * short term task, we will NOT use local data-storage, also weather data saved into local storage
 * and later retrieved should be meaningless.
 */
public interface WeatherRepository {

    /**
     * Get weather data for specified location.
     *
     * This is a remote api call (as indicated by the function name suffix 'remote').
     *
     * @return A observable
     */
    Observable<WeatherResponse> getWeatherRemote(String city, String state, String country);

    /**
     * Get weather data for specified location which indicated by {@param lon} and {@param lat}.
     */
    Observable<WeatherResponse> getWeatherRemote(double lat, double lon);

    Observable<GeoZipBasedResponse> getGeoZipBasedResponse(@NonNull String countryCode, @NonNull String zipCode);

    Observable<List<GeoNameBasedResponse>> getGeoNameBasedResponse(String city, String state, String countryCode);
}
