package com.nearsoft.farandula.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pruiz on 5/11/17.
 */
public class Itinerary {

    private List<AirLeg> airlegs = new ArrayList<>();
    private Fares price;

    public List<AirLeg> getAirlegs() {
        return airlegs;
    }

    public void setAirlegs( List<AirLeg> airleg) {
        this.airlegs = airlegs;
    }

    public Fares getPrice() {
        return price;
    }

    public void setPrice(Fares price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Itinerary{" +
                "airlegs=" + airlegs +
                ", price=" + price +
                '}';
    }
}