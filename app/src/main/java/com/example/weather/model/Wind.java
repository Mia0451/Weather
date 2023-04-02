package com.example.weather.model;

public class Wind {
    public double speed;
    double deg;

    public String toString() {
        return new StringBuilder("Wind speed: ").append(speed).append(".").toString();
    }
}
