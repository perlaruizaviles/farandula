package com.nearsoft.farandula.models;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by pruiz on 4/12/17.
 */
public class Segment {

    private String key;
    String operatingAirlineCode;
    String operatingAirlineName;
    String operatingFlightNumber;
    String marketingAirlineCode;
    String marketingAirlineName;
    String marketingFlightNumber;

    String departureAirportCode;
    String departureTerminal;
    LocalDateTime departureDate;

    String arrivalAirportCode;
    String arrivalTerminal;
    LocalDateTime arrivalDate;

    String airplaneData;
    long duration;
    List<Seat> seatsAvailable;

    //todo: this only works for travelport :S
    Fares price;

    public String getOperatingAirlineCode() {
        return operatingAirlineCode;
    }

    public void setOperatingAirlineCode(String operatingAirlineCode) {
        this.operatingAirlineCode = operatingAirlineCode;
    }

    public String getMarketingAirlineCode() {
        return marketingAirlineCode;
    }

    public void setMarketingAirlineCode(String marketingAirlineCode) {
        this.marketingAirlineCode = marketingAirlineCode;
    }

    public String getMarketingAirlineName() {
        return marketingAirlineName;
    }

    public void setMarketingAirlineName(String marketingAirlineName) {
        this.marketingAirlineName = marketingAirlineName;
    }

    public String getMarketingFlightNumber() {
        return marketingFlightNumber;
    }

    public void setMarketingFlightNumber(String marketingFlightNumber) {
        this.marketingFlightNumber = marketingFlightNumber;
    }

    public List<Seat> getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(List<Seat> seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public String getDepartureAirportCode() {
        return departureAirportCode;
    }

    public void setDepartureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
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

    public void setOperatingAirlineName(String operatingAirlineName) {
        this.operatingAirlineName = operatingAirlineName;
    }

    public String getOperatingAirlineName() {
        return operatingAirlineName;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "departureAirportCode='" + departureAirportCode + '\'' +
                ", arrivalAirportCode='" + arrivalAirportCode + '\'' +
                '}';
    }

    public Fares getPrice() {
        return price;
    }

    public void setPrice(Fares price) {
        this.price = price;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
