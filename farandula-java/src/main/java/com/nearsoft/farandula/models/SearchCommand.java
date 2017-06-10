package com.nearsoft.farandula.models;

import com.nearsoft.farandula.exceptions.ErrorType;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.soap.SOAPException;
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

    //LOGGER
    public static Logger LOGGER = LoggerFactory.getLogger( SearchCommand.class );

    private List<String> departureAirports = new ArrayList<>();
    private List<String> arrivalAirports = new ArrayList<>();
    private List<LocalDateTime> departingDates = new ArrayList<>();
    private List<LocalDateTime> returningDates = new ArrayList<>();
    private Map<PassengerType, List<Passenger>> passengersMap = new HashMap<>();
    private List<Passenger> passengers = new ArrayList<>();
    private FlightManager flightManager;

    //with default values
    private int offSet = 50;
    private CabinClassType CabinClass = CabinClassType.ECONOMY;
    private FlightType type = FlightType.ONEWAY;

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
        validDates(departingDate);
        this.departingDates = departingDate;
        return this;
    }

    public SearchCommand returningAt(List<LocalDateTime> returningDate) throws FarandulaException {
        validDates(returningDate);
        this.returningDates = returningDate;
        return this;
    }

    public SearchCommand forPassegers(List<Passenger> passengerList) throws FarandulaException {

        if (this.getPassengers().size() + passengerList.size() > 6) {
            throw new FarandulaException(ErrorType.ACCESS_ERROR, "Is not possible to search up to 6 passengers.");
        }

        this.passengers.addAll(passengerList);

        if (!passengerList.isEmpty()) {

            if (!passengersMap.containsKey(passengerList.get(0).getType())) {
                // to initialize.
                passengersMap.put(passengerList.get(0).getType(), new ArrayList<>());
            }

            passengersMap.get(passengerList.get(0).getType()).addAll(passengerList);
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

        LOGGER.info("Search Command info: " + this.toString() );
        checkValidSearchCommand();

        return flightManager.getAvail(this);
    }

    public List<String> getDepartureAirports() {
        return departureAirports;
    }

    public List<String> getArrivalAirports() {
        return arrivalAirports;
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

    private void checkValidSearchCommand() throws FarandulaException {

        if (this.getPassengers().size() == 0) {
            //case when user does not set any passenger.
            this.forPassegers(Passenger.adults(1));
        }

        // case when there is more infants than adults
        if (this.getPassengersMap().containsKey(PassengerType.INFANTS)) {

            if (this.getPassengersMap().get(PassengerType.INFANTS).size() >
                    this.getPassengersMap().get(PassengerType.ADULTS).size()) {

                throw new FarandulaException(ErrorType.VALIDATION,
                        "Search parameters are not valid, HINT: each infant has to be assigned to an adult.");
            }

        }

        Map<Integer, Integer> map  = new HashMap<>();

        if (this.getDepartingDates().size() == 0) {
            throw new FarandulaException(ErrorType.VALIDATION,
                    "Search parameters are not valid, HINT: the departing date is mandatory.");
        }

        // case when the returning is before the departing
        if (this.getType() == FlightType.ROUNDTRIP) {

            if ( this.getDepartureAirports().size()!= 1 || this.getArrivalAirports().size()!=1  ){
                throw new FarandulaException(ErrorType.VALIDATION,
                        "Search parameters are not valid, HINT: in round trips the origin and arrival airport are mandatory.");
            }

            if ( this.getReturningDates() ==  null || this.getReturningDates().size() == 0) {
                throw new FarandulaException(ErrorType.VALIDATION,
                        "Search parameters are not valid, HINT: in round trips the departing and returning date are mandatory.");
            }

            if( this.getReturningDates().size() > 1 && this.getDepartingDates().size() > 1 ){
                throw new FarandulaException(ErrorType.VALIDATION,
                        "Search parameters are not valid, HINT: round trips only contains one departing date and one returning date.");
            }

            if (this.getDepartingDates().get(0).isAfter(this.getReturningDates().get(0))) {
                throw new FarandulaException(ErrorType.VALIDATION,
                        "Search parameters are not valid, HINT: the returning date has to be after the departing date.");
            }

        }else{

            if ( this.getType() == FlightType.ONEWAY ){

                if( this.getDepartingDates().size() > 1 || this.getReturningDates().size() > 0 ){
                    throw new FarandulaException(ErrorType.VALIDATION,
                            "Search parameters are not valid, HINT: in one way trips only departing date is mandatory.");
                }
            }else{

                //open jaw
                if ( this.getReturningDates().size() > 0 ){
                    throw new FarandulaException(ErrorType.VALIDATION,
                            "Search parameters are not valid, HINT: in open jaw trips does not have returning dates.");
                }

                if ( (this.getDepartureAirports().size() != this.getArrivalAirports().size()) ||
                        (  this.getArrivalAirports().size()!= this.getDepartingDates().size() )
                   ){
                    throw new FarandulaException(ErrorType.VALIDATION,
                            "Search parameters are not valid, HINT: in open jaw trips is mandatory to have te same number of arrival and destination values.");
                }


            }

        }

    }

    private void validDates(List<LocalDateTime> datesList) throws FarandulaException {

        for (LocalDateTime date : datesList) {

            if ( date.isBefore( LocalDateTime.now().minusDays(1) ) ) {

                throw new FarandulaException(ErrorType.VALIDATION, "Is impossible to search in past dates.");

            } else {
                flightManager.validateDate(date);
            }

        }

    }

    @Override
    public String toString() {
        return "SearchCommand{" +
                "departureAirports=" + departureAirports +
                ", arrivalAirports=" + arrivalAirports +
                ", departingDates=" + departingDates +
                ", returningDates=" + returningDates +
                ", passengersMap=" + passengersMap +
                ", passengers=" + passengers +
                ", flightManager=" + flightManager +
                ", offSet=" + offSet +
                ", CabinClass=" + CabinClass +
                ", type=" + type +
                '}';
    }
}






