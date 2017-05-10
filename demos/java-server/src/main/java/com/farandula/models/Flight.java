package com.farandula.models;


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
    private LocalDateTime departureDate;
    private Airport arrivalAirport;
    private LocalDateTime arrivalDate;
    private List<FlightSegment> segments;


    public Flight( Airport departureAirport, LocalDateTime departureDate, Airport arrivalAirport, LocalDateTime arrivalDate){

        this.setDepartureAirport(departureAirport);
        this.setDepartingDate(departureDate);
        this.setArrivalAirport(arrivalAirport);
        this.setArrivalDate(arrivalDate);


    }
    public Flight( Airport departureAirport, LocalDateTime departingDate, Airport arrivalAirport,
                  LocalDateTime arrivalDate, List<FlightSegment> flightSegmentList){

        this.setDepartureAirport(departureAirport);
        this.setDepartingDate(departingDate);
        this.setArrivalAirport(arrivalAirport);
        this.setArrivalDate(arrivalDate);
        this.setSegments(flightSegmentList);


    }


    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
    }

    public LocalDateTime getDepartingDate() {
        return departureDate;
    }

    public void setDepartingDate(LocalDateTime departingDate) {
        this.departureDate = departingDate;
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


    public List<FlightSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<FlightSegment> segments) {
        this.segments = segments;
    }
}
