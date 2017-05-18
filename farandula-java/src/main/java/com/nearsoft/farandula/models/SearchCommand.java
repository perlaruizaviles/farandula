package com.nearsoft.farandula.models;

import com.nearsoft.farandula.exceptions.ErrorType;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pruiz on 4/10/17.
 */
public class SearchCommand {

    private FlightType type = FlightType.ONEWAY;
    private List<String> departureAirports;
    private List<String> arrivalAirports;
    private List<LocalDateTime> departingDates;
    private List<LocalDateTime> returningDates;
    private Map<PassengerType, List<Passenger>> passengersMap =  new HashMap<>();
    private List<Passenger> passengers = new ArrayList<>();
    private int offSet;
    private CabinClassType CabinClass = CabinClassType.ECONOMY;
    private FlightManager flightManager;

    public SearchCommand(FlightManager flightManager) {
        this.flightManager = flightManager;
    }

    public SearchCommand from(List<String> airportCode) {
        this.departureAirports = airportCode;
        return this;
    }

    public SearchCommand to(List<String> airportCode) throws FarandulaException {
        this.arrivalAirports = airportCode;
        return this;
    }

    public SearchCommand departingAt(List<LocalDateTime> departingDate) throws FarandulaException {
        this.departingDates = departingDate;
        return this;
    }

    public SearchCommand returningAt(List<LocalDateTime> returningDate) throws FarandulaException {
        this.returningDates = returningDate;
        return this;
    }

    public SearchCommand forPassegers(List<Passenger> passengerList) throws FarandulaException {

        if ( this.getPassengers().size() + passengerList.size() > 6 ){
            throw new FarandulaException( ErrorType.ACCESS_ERROR, "Is not possible to search up to 6 passengers.") ;
        }

        this.passengers.addAll(passengerList);

        if ( !passengerList.isEmpty() ){

            if ( !passengersMap.containsKey( passengerList.get(0).getType()  ) ){
                // to initialize.
                passengersMap.put( passengerList.get(0).getType() , new ArrayList<>() );
            }

            passengersMap.get( passengerList.get(0).getType() ).addAll( passengerList );
        }

        return this;
    }

    public SearchCommand limitTo(int offsetSearch) {
        this.offSet = offsetSearch;
        return this;
    }

    public SearchCommand type(FlightType roundTrip) throws FarandulaException {

        this.type = roundTrip;
        return this;
    }

    public SearchCommand preferenceClass(CabinClassType preferenceClass) {
        this.CabinClass = preferenceClass;
        return this;
    }

    public List<Itinerary> execute() throws IOException, FarandulaException {
        //FIXME here there is a code smell , why we are passing `this` to executeAvail.
        return flightManager
                .getAvail(this);
    }

    public List<String> getDepartureAirports() {
        return departureAirports;
    }

    public List<String> getArrivalAirports() {
        return arrivalAirports;
    }

    public void setArrivalAirports(List<String> arrivalAirports) throws FarandulaException {
        this.arrivalAirports = arrivalAirports;
    }

    public List<LocalDateTime> getDepartingDates() {
        return departingDates;
    }

    public List<LocalDateTime> getReturningDates() {
        return returningDates;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public FlightType getType() {
        return type;
    }

    public void setType(FlightType type) throws FarandulaException {
        this.type = type;
    }

    public Map<PassengerType, List<Passenger>> getPassengersMap() {
        return passengersMap;
    }

    public int getOffSet() {
        return offSet;
    }

    public CabinClassType getCabinClass() {
        return CabinClass;
    }

}
