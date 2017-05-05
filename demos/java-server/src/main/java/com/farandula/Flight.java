package com.farandula;

import org.springframework.data.annotation.Id;

/**
 * Created by antoniohernandez on 5/5/17.
 */
public class Flight {

    @Id
    private String _id;

    private int id;

    private String departureAirportCode;
    private String departingDate;
    private String arrivalAirportCode;
    private String arrivalDate;

    public Flight(int id, String departureAirportCode, String departingDate, String arrivalAirportCode, String arrivalDate){
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

    public String getDepartingDate() {
        return departingDate;
    }

    public void setDepartingDate(String departingDate) {
        this.departingDate = departingDate;
    }

    public String getArrivalAirportCode() {
        return arrivalAirportCode;
    }

    public void setArrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }


}
