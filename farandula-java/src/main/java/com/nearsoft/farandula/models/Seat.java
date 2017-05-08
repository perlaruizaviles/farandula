package com.nearsoft.farandula.models;

/**
 * Created by pruiz on 5/8/17.
 */
public class Seat {

    private CabinClassType classCabin;
    private String place;

    public void setClassCabin(CabinClassType classCabin) {
        this.classCabin = classCabin;
    }

    public CabinClassType getClassCabin() {
        return classCabin;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
