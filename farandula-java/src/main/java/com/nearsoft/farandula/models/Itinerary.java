package com.nearsoft.farandula.models;

import java.util.List;

/**
 * Created by pruiz on 5/11/17.
 */
public class Itinerary {
    private List<AirLeg> airlegs;
    private Price price;

    public void setAirlegs(List<AirLeg> airlegs) {
        this.airlegs = airlegs;
    }

    public List<AirLeg> getAirlegs() {
        return airlegs;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
