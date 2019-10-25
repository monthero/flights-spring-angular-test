package com.travel.demo;

import lombok.Data;

@Data
public class Country {

    private String iso2_code;
    private String name;

    Country() {}

    Country(String code, String name) {
        this.iso2_code = code;
        this.name = name;
    }
}
