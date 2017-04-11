package com.nearsoft.farandula.models;

/**
 * Created by pruiz on 4/10/17.
 */
public class Flight {
    private String departureAirport;
    private String arrivalAirport;

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }
}
