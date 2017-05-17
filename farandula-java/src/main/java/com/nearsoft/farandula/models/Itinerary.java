package com.nearsoft.farandula.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pruiz on 5/11/17.
 */
public class Itinerary {
    private AirLeg departureAirleg;
    private AirLeg returningAirleg;
    private Fares price;

    public AirLeg getDepartureAirleg() {
        return departureAirleg;
    }

    public void setDepartureAirleg(AirLeg departureAirleg) {
        this.departureAirleg = departureAirleg;
    }

    public Fares getPrice() {
        return price;
    }

    public void setPrice(Fares price) {
        this.price = price;
    }

    public AirLeg getReturningAirleg() {
        return returningAirleg;
    }

    public void setReturningAirlegs(AirLeg returningAirleg) {
        this.returningAirleg = returningAirleg;
    }

}
