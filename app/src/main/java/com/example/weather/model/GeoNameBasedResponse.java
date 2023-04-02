package com.example.weather.model;

import com.example.weather.model.entity.City;

import java.util.Map;

public class GeoNameBasedResponse {
    String name;
    Map<String, String> local_names;
    public double lat;
    public double lon;
    String country;
    String state;

    public City toCity() {
        return new City(name, state, country, lat, lon);
    }
}
