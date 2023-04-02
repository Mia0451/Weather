package com.example.weather.model;

public class Weather {
    public long id;
    public String main;
    public String description;
    public String icon;

    public String toString() {
        return main + ", " + description + ".";
    }
}
