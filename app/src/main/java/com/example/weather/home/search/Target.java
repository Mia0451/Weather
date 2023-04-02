package com.example.weather.home.search;

class Target {
    final String city;
    final String state;
    final String country;
    final String zipCode;

    Target(String city, String state, String country, String zipCode) {
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipCode = zipCode;
    }
}
