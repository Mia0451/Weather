package com.example.weather.model;

import com.example.weather.model.entity.City;

public class GeoZipBasedResponse {
    public String zip;
    public String name;
    public double lat;
    public double lon;
    public String country;

    public City toCity() {
        return new City(name, "", country, lat, lon);
    }
}
