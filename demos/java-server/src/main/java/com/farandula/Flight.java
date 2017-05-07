package com.farandula;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * Created by antoniohernandez on 5/5/17.
 */
public class Flight {

    @Id
    private String _id;

    private int id;
    private String departureAirportCode;
    private LocalDateTime departingDate;
    private String arrivalAirportCode;
    private LocalDateTime arrivalDate;


    public Flight(int id, String departureAirportCode, LocalDateTime departingDate, String arrivalAirportCode, LocalDateTime arrivalDate){
        this.setId(id);
        this.setDepartureAirportCode(departureAirportCode);
        this.setDepartingDate(departingDate);
        this.setArrivalAirportCode(arrivalAirportCode);
        this.setArrivalDate(arrivalDate);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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


}
