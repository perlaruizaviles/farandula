package com.nearsoft.farandula.models;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by pruiz on 4/12/17.
 */
public class AirLeg {

    String id;
    String departureAirportCode;
    LocalDateTime departingDate;
    String arrivalAirportCode;
    LocalDateTime arrivalDate;
    List<Segment> segments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }
    public LocalDateTime getDepartingDate() {
        return departingDate;
    }

    public void setDepartingDate(LocalDateTime departingDate) {
        this.departingDate = departingDate;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

}

