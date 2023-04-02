package com.example.weather.model;

public class WeatherResponse {
    Coordinate coord;
    Weather[] weather;
    String base;
    WeatherDetail main;
    int visibility;
    Wind wind;
    Clouds clouds;
    long dt;
    SystemInfo sys;
    long timezone;
    int id;
    String name;
    int cod;

    public Coordinate getCoord() {
        return coord;
    }

    public void setCoord(Coordinate coord) {
        this.coord = coord;
    }

    public Weather[] getWeather() {
        return weather;
    }

    public void setWeather(Weather[] weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public WeatherDetail getMain() {
        return main;
    }

    public void setMain(WeatherDetail main) {
        this.main = main;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public SystemInfo getSys() {
        return sys;
    }

    public void setSys(SystemInfo sys) {
        this.sys = sys;
    }

    public long getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(name).append(", ").append(sys.toString()).append(" ");

        for (Weather w : weather) {
            sb.append(w).append(", ");
        }

        return sb.append(main.toString()).append(", ").append(wind.toString()).toString();
    }
}
