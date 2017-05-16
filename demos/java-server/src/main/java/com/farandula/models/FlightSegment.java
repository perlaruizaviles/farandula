package com.farandula.models;

import com.farandula.Helpers.DateParser;

import java.time.LocalDateTime;

/**
 * Created by antoniohernandez on 5/10/17.
 */
public class FlightSegment {

    private Airport departureAirport;
    private int departureDate;
    private Airport arrivalAirport;
    private int arrivalDate;
    private long duration;

    public FlightSegment( Airport departureAirport, LocalDateTime departureDate, Airport arrivalAirport, LocalDateTime arrivalDate, long duration){

        this.setDepartureAirport(departureAirport);
        this.setArrivalAirport(arrivalAirport);
        this.setDuration(duration);
        this.setArrivalDate(DateParser.dateToTimestampSeconds(arrivalDate));
        this.setDepartureDate(DateParser.dateToTimestampSeconds(departureDate));
    }

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public int getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(int departureDate) {
        this.departureDate = departureDate;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public int getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate (int arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
