package com.farandula.models;

import com.nearsoft.farandula.models.Fares;

import java.util.List;

/**
 * Created by antoniohernandez on 5/16/17.
 */
public class FlightItinerary {

    private int key;
    private Flight departureAirleg;
    private Flight returningAirleg;
    private ItineraryFares fares;

    public FlightItinerary (int key, Flight  departureAirleg,  ItineraryFares fares) {

        this(key, departureAirleg, null, fares);

    }
    public FlightItinerary (int key, Flight  departureAirleg, Flight returningAirleg, ItineraryFares fares) {

        this.setKey(key);
        this.setDepartureAirleg(departureAirleg);
        this.setReturningAirleg(returningAirleg);
        this.setFares(fares);

    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public Flight getDepartureAirleg() {
        return departureAirleg;
    }

    public void setDepartureAirleg(Flight departureAirleg) {
        this.departureAirleg = departureAirleg;
    }

    public Flight getReturningAirleg() {
        return returningAirleg;
    }

    public void setReturningAirleg(Flight returningAirleg) {
        this.returningAirleg = returningAirleg;
    }

    public ItineraryFares getFares() {
        return fares;
    }

    public void setFares(ItineraryFares fares) {
        this.fares = fares;
    }
}
