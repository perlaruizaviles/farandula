package com.farandula.models;

import com.farandula.Helpers.DateParser;
import com.nearsoft.farandula.models.AirLeg;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by antoniohernandez on 5/10/17.
 */
public class FlightSegment {

    private Airport departureAirport;
    private int departureDate;
    private Airport arrivalAirport;
    private int arrivalDate;
    private long duration;

    private String airLineMarketingName;
    private String airLineOperationName;
    private String airplaneData;

    private List<String> cabinTypes;

    public Airport getDepartureAirport() {
        return departureAirport;
    }

    public FlightSegment setDepartureAirport(Airport departureAirport) {
        this.departureAirport = departureAirport;
        return this;
    }

    public int getDepartureDate() {
        return departureDate;
    }

    public FlightSegment setDepartureDate(int departureDate) {
        this.departureDate = departureDate;
        return this;
    }

    public FlightSegment setDepartureDate(LocalDateTime departureDate) {
        int timestamp = DateParser.dateToTimestampSeconds(departureDate);
        this.departureDate = timestamp;
        return this;
    }

    public Airport getArrivalAirport() {
        return arrivalAirport;
    }

    public FlightSegment setArrivalAirport(Airport arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
        return this;
    }

    public int getArrivalDate() {
        return arrivalDate;
    }

    public FlightSegment setArrivalDate (int arrivalDate) {
        this.arrivalDate = arrivalDate;
        return this;
    }

    public FlightSegment setArrivalDate (LocalDateTime arrivalDate) {
        int timestamp = DateParser.dateToTimestampSeconds(arrivalDate);
        this.arrivalDate = timestamp;
        return this;
    }

    public long getDuration() {
        return duration;
    }

    public FlightSegment setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public String getAirLineMarketingName() {
        return airLineMarketingName;
    }

    public FlightSegment setAirLineMarketingName(String airLineName) {
        this.airLineMarketingName = airLineName;
        return this;
    }

    public String getAirplaneData() {
        return airplaneData;
    }

    public FlightSegment setAirplaneData(String airplaneData) {
        this.airplaneData = airplaneData;
        return this;
    }

    public String getAirLineOperationName() {
        return airLineOperationName;
    }

    public FlightSegment setAirLineOperationName(String airLineOperationName) {
        this.airLineOperationName = airLineOperationName;
        return this;
    }

    public List<String> getCabinTypes() {
        return cabinTypes;
    }

    public FlightSegment setCabinTypes(List<String> cabinTypes) {
        this.cabinTypes = cabinTypes;
        return this;
    }
}
