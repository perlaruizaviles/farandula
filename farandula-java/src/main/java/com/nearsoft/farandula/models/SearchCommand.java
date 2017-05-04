package com.nearsoft.farandula.models;

import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.nearsoft.farandula.models.CriteriaType.PRICE;

/**
 * Created by pruiz on 4/10/17.
 */
public class SearchCommand {

    private FlightType type = FlightType.ONEWAY;
    private String departureAirport;
    private String arrivalAirport;
    private LocalDateTime departingDate;
    private LocalDateTime returningDate;
    private List<Passenger> passengers = new ArrayList<>();
    private CriteriaType[] criterias  = new CriteriaType[]{PRICE};
    private int offSet;
    private CabinClassType CabinClass = CabinClassType.economy ;

    private FlightManager flightManager;

    public SearchCommand(FlightManager flightManager) {
        this.flightManager = flightManager;
    }

    public SearchCommand from(String airportCode) {
        this.departureAirport = airportCode;
        return this;
    }

    public SearchCommand to(String airportCode) {
        this.arrivalAirport = airportCode;
        return this;
    }

    public SearchCommand departingAt(LocalDateTime departingDate) {
        this.departingDate = departingDate;
        return this;
    }

    public SearchCommand returningAt(LocalDateTime returningDate) {
        this.returningDate = returningDate;
        return this;
    }

    public SearchCommand forPassegers(List<Passenger> passengerList) {
        this.passengers.addAll( passengerList );
        return this;
    }

    public SearchCommand sortBy(CriteriaType... criteriaList ) {
        this.criterias = criteriaList;
        return this;
    }

    public SearchCommand limitTo(int offsetSearch) {
        this.offSet = offsetSearch;
        return this;
    }

    public SearchCommand type(FlightType roundTrip) {
        this.type = roundTrip;
        return this;
    }

    public SearchCommand preferenceClass( CabinClassType preferenceClass ){
        this.CabinClass = preferenceClass;
        return this;
    }

    public List<AirLeg> execute() throws IOException, FarandulaException {
        //FIXME here there is a code smell , why we are passing `this` to executeAvail.
        return flightManager
                 .getAvail(this);
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public LocalDateTime getDepartingDate() {
        return departingDate;
    }

    public void setDepartingDate(LocalDateTime departingDate) {
        this.departingDate = departingDate;
    }

    public LocalDateTime getReturningDate() {
        return returningDate;
    }

    public void setReturningDate(LocalDateTime returningDate) {
        this.returningDate = returningDate;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public FlightType getType() {
        return type;
    }

    public void setType(FlightType type) { this.type = type; }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public CriteriaType[] getCriterias() {
        return criterias;
    }

    public void setCriterias(CriteriaType[] criterias) {
        this.criterias = criterias;
    }

    public int getOffSet() {
        return offSet;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }

    public CabinClassType getCabinClass() {
        return CabinClass;
    }

    public void setCabinClass(CabinClassType cabinClass) {
        this.CabinClass = cabinClass;
    }
}
