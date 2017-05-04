package com.nearsoft.farandula.models;

import java.time.LocalDateTime;

/**
 * Created by pruiz on 4/12/17.
 */
public class Segment {


    String airlineIconPath;
    String operatingAirline;
    String marketingAirline;
    String marketingFlightNumber;
    String departureAirportCode;
    String departureTerminal;
    LocalDateTime departingDate;
    String arrivalAirportCode;
    String arrivalTerminal;
    LocalDateTime arrivalDate;
    String airplaneData;
    long duration;
    String travelClass;
    String operatingFlightNumber;
    private String operatingAirlineName;

    public String getAirlineIconPath() {
        return airlineIconPath;
    }

    public void setAirlineIconPath(String airlineIconPath) {
        this.airlineIconPath = airlineIconPath;
    }

    public String getOperatingAirline() {
        return operatingAirline;
    }

    public void setOperatingAirline(String operatingAirline) {
        this.operatingAirline = operatingAirline;
    }

    public String getMarketingAirline() {
        return marketingAirline;
    }

    public void setMarketingAirline(String marketingAirline) {
        this.marketingAirline = marketingAirline;
    }

    public String getMarketingFlightNumber() {
        return marketingFlightNumber;
    }

    public void setMarketingFlightNumber(String marketingFlightNumber) {
        this.marketingFlightNumber = marketingFlightNumber;
    }

    public String getTravelClass() {
        return travelClass;
    }

    //TODO the travel class should be a collection of cabins of a given enum type
    public void setTravelClass(String travelClass) {
        this.travelClass = travelClass;
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

    public String getDepartureTerminal() {
        return departureTerminal;
    }

    public void setDepartureTerminal(String departureTerminal) {
        this.departureTerminal = departureTerminal;
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

    public String getArrivalTerminal() {
        return arrivalTerminal;
    }

    public void setArrivalTerminal(String arrivalTerminal) {
        this.arrivalTerminal = arrivalTerminal;
    }

    public String getAirplaneData() {
        return airplaneData;
    }

    public void setAirplaneData(String airplaneData) {
        this.airplaneData = airplaneData;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setOperatingFlightNumber(String operatingFlightNumber) {
        this.operatingFlightNumber = operatingFlightNumber;
    }
    public String getOperatingFlightNumber() {
        return operatingFlightNumber;
    }

    //TODO we should have a reference airline code/name map
    public void setOperatingAirlineName(String operatingAirlineName) {
        this.operatingAirlineName = operatingAirlineName;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "departureAirportCode='" + departureAirportCode + '\'' +
                ", arrivalAirportCode='" + arrivalAirportCode + '\'' +
                '}';
    }
}
