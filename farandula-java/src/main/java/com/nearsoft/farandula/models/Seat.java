package com.nearsoft.farandula.models;

/**
 * Created by pruiz on 5/8/17.
 */
public class Seat {

    private CabinClassType classCabin;
    private String[] seats;

    public void setClassCabin(CabinClassType classCabin) {
        this.classCabin = classCabin;
    }

    public CabinClassType getClassCabin() {
        return classCabin;
    }

    public void setSeats(String[] seats) {
        this.seats = seats;
    }

    public String[] getSeats() {
        return seats;
    }
}
