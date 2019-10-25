package com.travel.demo;

import lombok.Data;


@Data
public class Airport {
    private String code;
    private String name;
    private String city;
    private Country country;
    private Coordinates coordinates;

    Airport() {}

    Airport(String code, String name, String city, String country_code, String country_name, double lat, double lon) {
        this.code = code;
        this.name = name;
        this.city = city;
        this.country = new Country(country_code, country_name);
        this.coordinates = new Coordinates(lat, lon);
    }

    Airport(String code, String name, String city, Country country, Coordinates coords) {
        this.code = code;
        this.name = name;
        this.city = city;
        this.country = country;
        this.coordinates = coords;
    }

    public void setCoordinates(double lat, double lon) {
        this.coordinates = new Coordinates(lat, lon);
    }
}
