package com.farandula.models;

import java.time.LocalDateTime;

/**
 * Created by antoniohernandez on 5/10/17.
 */
public class FlightSegment {

    private Airport departureAirport;
    private LocalDateTime departureDate;
    private Airport arrivalAirport;
    private LocalDateTime arrivalDate;
    private long duration;

    public FlightSegment( Airport departureAirport, LocalDateTime departingDate, Airport arrivalAirport, LocalDateTime arrivalDate, long duration){

        this.setDepartureAirport(departureAirport);
        this.setDepartureDate(departingDate);
        this.setArrivalAirport(arrivalAirport);
        this.setArrivalDate(arrivalDate);
        this.setDuration(duration);


    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
