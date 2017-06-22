package com.farandula.models;


import com.farandula.Helpers.DateParser;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by antoniohernandez on 5/5/17.
 */
public class Flight {

    @Id
    private String _id;


    private Airport departureAirport;
    private int departureDate;
    private Airport arrivalAirport;
    private int arrivalDate;
    private List<FlightSegment> segments;

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public Flight setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
        return this;
    }

    public int getDepartureDate() {
        return departureDate;
    }

    public Flight setDepartureDate(int departureDate) {
        this.departureDate = departureDate;
        return this;
    }

    public Flight setDepartureDate(LocalDateTime date){
        int timeStamp = DateParser.dateToTimestampSeconds(date);
        this.departureDate = timeStamp;
        return this;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public Flight setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
        return this;
    }

    public int getArrivalDate() {
        return arrivalDate;
    }

    public Flight setArrivalDate(int arrivalDate) {
        this.arrivalDate = arrivalDate;
        return this;
    }

    public Flight setArrivalDate(LocalDateTime date){
        int timeStamp = DateParser.dateToTimestampSeconds(date);
        this.arrivalDate = timeStamp;
        return this;
    }

    public List<FlightSegment> getSegments() {
        return segments;
    }

    public Flight setSegments(List<FlightSegment> segments) {
        this.segments = segments;
        return this;
    }

}
