package com.nearsoft.farandula.models;

import java.util.List;

/**
 * Created by pruiz on 4/10/17.
 */
public class Flight {

    String id;
    List<Airleg> legs;
    String PNR;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Airleg> getLegs() {
        return legs;
    }

    public void setLegs(List<Airleg> legs) {
        this.legs = legs;
    }

    public String getPNR() {
        return PNR;
    }

    public void setPNR(String PNR) {
        this.PNR = PNR;
    }
}
