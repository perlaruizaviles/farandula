package com.nearsoft.farandula.models;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by pruiz on 4/12/17.
 */
public class Segment {

    String airlineIconPath;
    String airlineName;
    String flightNumber;
    String departureAirportCode;
    LocalDateTime departingDate;
    String arrivalAirportCode;
    LocalDateTime arrivalDate;
    String airplaneData;
    String timeFlight;
    List<Passenger> passengersList;

    public String getAirlineIconPath() {
        return airlineIconPath;
    }

    public void setAirlineIconPath(String airlineIconPath) {
        this.airlineIconPath = airlineIconPath;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
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

    public String getAirplaneData() {
        return airplaneData;
    }

    public void setAirplaneData(String airplaneData) {
        this.airplaneData = airplaneData;
    }

    public String getTimeFlight() {
        return timeFlight;
    }

    public void setTimeFlight(String timeFlight) {
        this.timeFlight = timeFlight;
    }
}
