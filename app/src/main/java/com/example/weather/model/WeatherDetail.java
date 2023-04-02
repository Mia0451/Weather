package com.example.weather.model;

public class WeatherDetail {
    public double temp;
    public double feels_like;
    public double temp_min;
    public double temp_max;
    public double pressure;
    public int humidity;

    public String toString() {
        return new StringBuilder("Temperature: ")
                .append(temp)
                .append(", Lowest temperature: ")
                .append(temp_min)
                .append(", Highest temperature: ")
                .append(temp_max)
                .append(", Humidity: ")
                .append(humidity)
                .append(".")
                .toString();
    }
}
