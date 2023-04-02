package com.example.weather.model;

public class SystemInfo {
    int type;
    int id;
    public String country;
    public long sunrise;
    public long sunset;

    public String toString() {
        return new StringBuilder(country).toString();
    }
}
