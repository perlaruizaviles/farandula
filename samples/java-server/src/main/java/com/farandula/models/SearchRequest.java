package com.farandula.models;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by emote on 29/05/17.
 */
public class SearchRequest {

    @NotNull
    private
    String departingAirportCodes;

    @NotNull
    private
    String departingDates;

    @NotNull
    private
    String departingTimes;

    @NotNull
    private
    String arrivalAirportCodes;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private
    String returnDates;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private
    String returnTimes;

    @NotNull
    private
    String type;

    @NotNull
    private
    String passenger;

    @NotNull
    @Pattern(regexp = "(economy|premium|first|business|other)")
    private
    String cabin;

    @NotNull
    @NumberFormat
    private
    String limit;

    private String gds = "";

    public String getDepartingAirportCodes() {
        return departingAirportCodes;
    }

    public void setDepartingAirportCodes(String departingAirportCodes) {
        this.departingAirportCodes = departingAirportCodes;
    }

    public String getDepartingDates() {
        return departingDates;
    }

    public void setDepartingDates(String departingDates) {
        this.departingDates = departingDates;
    }

    public String getDepartingTimes() {
        return departingTimes;
    }

    public void setDepartingTimes(String departingTimes) {
        this.departingTimes = departingTimes;
    }

    public String getArrivalAirportCodes() {
        return arrivalAirportCodes;
    }

    public void setArrivalAirportCodes(String arrivalAirportCodes) {
        this.arrivalAirportCodes = arrivalAirportCodes;
    }

    public String getReturnDates() {
        return returnDates;
    }

    public void setReturnDates(String returnDates) {
        this.returnDates = returnDates;
    }

    public String getReturnTimes() {
        return returnTimes;
    }

    public void setReturnTimes(String returnTimes) {
        this.returnTimes = returnTimes;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public String getCabin() {
        return cabin;
    }

    public void setCabin(String cabin) {
        this.cabin = cabin;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getGds() {
        return gds;
    }

    public void setGds(String gds) {
        this.gds = gds;
    }
}
