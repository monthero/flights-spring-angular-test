package com.travel.demo;

import lombok.Data;

@Data
public class Coordinates {
    private double latitude;
    private double longitude;

    Coordinates() {}

    Coordinates(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }
}
