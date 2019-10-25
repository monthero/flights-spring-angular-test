package com.travel.demo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import kong.unirest.GetRequest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.awt.Point;


@RestController
public class AirportController {

    private String access_token = null;

    private void generateToken() {
        JsonNode jsonResponse = Unirest.post("http://localhost:8080/oauth/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("scope", "read")
                .field("grant_type", "client_credentials")
                .basicAuth("travel-api-client", "psw")
                .asJson()
                .getBody();

        this.access_token = jsonResponse.getObject().getString("access_token");
    }

    @GetMapping(value = "/airports", produces = "application/json")
    @CrossOrigin(origins = "*")
    List<Airport> getAirportsByTerm(@RequestParam(name="search_term", required = false) String search_term) {

        // Making sure we have the oauth2 token
        this.generateToken();

        int page = 1;
        int num_pages = 1;
        String url = "http://localhost:8080/airports";
        JsonNode jsonResponse;
        ArrayList<Airport> airports = new ArrayList<>();
        while (page <= num_pages) {
            GetRequest req = Unirest.get(url)
                    .queryString("access_token", this.access_token)
                    .queryString("size", 100)
                    .queryString("page", page)
                    .queryString("lang", "en");

            if (search_term != null) {
                req.queryString("term", search_term);
            }
            jsonResponse = req.asJson().getBody();

            JSONObject res_obj = jsonResponse.getObject();

            JSONArray locations = res_obj.getJSONObject("_embedded").getJSONArray("locations");
            for (int i = 0; i < locations.length(); i++) {
                JSONObject entry = locations.getJSONObject(i);
                Coordinates coords;
                try {
                    coords = new Coordinates(
                            entry.getJSONObject("coordinates").getDouble("latitude"),
                            entry.getJSONObject("coordinates").getDouble("longitude")
                    );
                }
                catch (JSONException e1) {
                    try {
                        coords = new Coordinates(
                                entry.getJSONObject("parent").getJSONObject("coordinates").getDouble("latitude"),
                                entry.getJSONObject("parent").getJSONObject("coordinates").getDouble("longitude")
                        );
                    }
                    catch (JSONException e2) {
                        coords = new Coordinates(0.0, 0.0);
                    }
                }
                airports.add(new Airport(
                    entry.getString("code"),
                    entry.getString("name"),
                    entry.getJSONObject("parent").getString("name"),
                    new Country(
                            entry.getJSONObject("parent").getJSONObject("parent").getString("code"),
                            entry.getJSONObject("parent").getJSONObject("parent").getString("name")
                    ),
                    coords
                ));
            }

            int retrieved_num_pages = res_obj.getJSONObject("page").getInt("totalPages");
            if (num_pages != retrieved_num_pages)
                num_pages = retrieved_num_pages;
            page++;
        }
        return airports;
    }

    @GetMapping("/")
    String hallo() {
        return "halo, buenos dias.";
    }

    @GetMapping("/calculate-fare")
    @CrossOrigin(origins = "*")
    Map<String, Object> calculateFare(
            @RequestParam(name="o", required=true) String origin,
            @RequestParam(name="d", required=true) String destination,
            @RequestParam(name="p", required=true) int passengers,
            @RequestParam(name="c", required=true) String currency,
            @RequestParam(name="tt", required=true) String tripType
    ) {
        this.generateToken();
        String tt = tripType.equals("ow") ? "One-way" : "Return";
        System.out.println(tt + " trip from " + origin + " to " + destination + ", " + passengers + " passengers in " + currency);

        String url = "http://localhost:8080/fares/{org}/{dest}";
        JsonNode jsonResponse = Unirest.get(url)
                .routeParam("org", origin)
                .routeParam("dest", destination)
                .queryString("access_token", this.access_token)
                .queryString("currency", currency)
                .asJson()
                .getBody();

        HashMap<String, Object> result = new HashMap<>();
        try {
            // multiply the received amount for the number of passengers
            // multiply by 2 if its a "Return" trip
            double amount = jsonResponse.getObject().getDouble("amount") * passengers * (tripType.equals("ow") ? 1 : 2);
            result.put("amount", amount);
        }
        catch (JSONException e) {
            result.put("error", "Oops! Something went wrong when trying to calculate fare, please review your information and try again.");
        }
        return result;
    }
}
