package com.farandula.models;

import com.nearsoft.farandula.models.AirLeg;
import com.nearsoft.farandula.models.Fares;
import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by antoniohernandez on 5/16/17.
 */
public class FlightItinerary {

    private int key;
    private Flight departureAirleg;
    private Flight returningAirleg;
    private List<Fares> fares;

    public FlightItinerary (int key, Flight  departureAirleg,  List<Fares> fares) {

        this(key, departureAirleg, null, fares);

    }
    public FlightItinerary (int key, Flight  departureAirleg, Flight returningAirleg, List<Fares> fares) {

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

    public List<Fares> getFares() {
        return fares;
    }

    public void setFares(List<Fares> fares) {
        this.fares = fares;
    }
}
