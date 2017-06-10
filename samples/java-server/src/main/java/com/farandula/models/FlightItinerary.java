package com.farandula.models;

import com.nearsoft.farandula.models.Fares;

import java.util.List;

/**
 * Created by antoniohernandez on 5/16/17.
 */
public class FlightItinerary {

    private int key;
    private String type;
    private List<Flight> airlegs;
    private ItineraryFares fares;

    public FlightItinerary (int key, String type, List<Flight> flights, ItineraryFares fares) {
        this.key = key;
        this.type = type;
        this.airlegs = flights;
        this.fares = fares;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public ItineraryFares getFares() {
        return fares;
    }

    public void setFares(ItineraryFares fares) {
        this.fares = fares;
    }

    public List<Flight> getAirlegs() {
        return airlegs;
    }

    public void setAirlegs(List<Flight> airlegs) {
        this.airlegs = airlegs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
