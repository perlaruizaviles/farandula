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


    public Flight( Airport departureAirport, LocalDateTime departureDate, Airport arrivalAirport, LocalDateTime arrivalDate) {
        this(departureAirport, departureDate, arrivalAirport, arrivalDate, null);
    }
    public Flight( Airport departureAirport, LocalDateTime departureDate, Airport arrivalAirport,
                  LocalDateTime arrivalDate, List<FlightSegment> flightSegmentList){

        this.setDepartureAirport(departureAirport);
        this.setArrivalAirport(arrivalAirport);
        this.setSegments(flightSegmentList);
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

    public void setArrivalDate(int arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public List<FlightSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<FlightSegment> segments) {
        this.segments = segments;
    }
}
