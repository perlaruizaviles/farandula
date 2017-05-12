package com.nearsoft.farandula.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pruiz on 5/11/17.
 */
public class Itinerary {
    private List<AirLeg> departureAirlegs = new ArrayList<>();
    private List<AirLeg> returningAirlegs = new ArrayList<>();
    private Price price;

    public List<AirLeg> getDepartureAirlegs() {
        return departureAirlegs;
    }

    public void setDepartureAirlegs(List<AirLeg> departureAirlegs) {
        this.departureAirlegs = departureAirlegs;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public List<AirLeg> getReturningAirlegs() {
        return returningAirlegs;
    }

    public void setReturningAirlegs(List<AirLeg> returningAirlegs) {
        this.returningAirlegs = returningAirlegs;
    }

}
