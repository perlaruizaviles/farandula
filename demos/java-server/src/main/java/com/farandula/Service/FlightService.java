package com.farandula.Service;

import com.farandula.Exceptions.ParameterException;
import com.farandula.Helpers.AgeManager;
import com.farandula.Helpers.DateParser;
import com.farandula.Helpers.FlightHelper;
import com.farandula.Helpers.PassengerHelper;
import com.farandula.Repositories.AirportRepository;
import com.farandula.models.*;
import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.flightmanagers.amadeus.AmadeusFlightManager;
import com.nearsoft.farandula.flightmanagers.sabre.SabreFlightManager;
import com.nearsoft.farandula.flightmanagers.travelport.TravelportFlightManager;
import com.nearsoft.farandula.models.*;
import com.nearsoft.farandula.utilities.CabinClassParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by antoniohernandez on 5/5/17.
 */
@Component
public class FlightService {

    @Autowired
    AirportRepository airportRepository;
    @Autowired
    FlightHelper flightHelper;
    @Autowired
    PassengerHelper passengerHelper;
    @Autowired
    DateParser dateParser;

    public static boolean validIataLength(String iata) {
        return (iata.length() == 3) || (iata.length() == 2);
    }

    public List<FlightItinerary> getResponseFromSearch(String departureAirportCode,
                                                       String departingDate,
                                                       String departingTime,
                                                       String arrivalAirportCode,
                                                       String returnDate,
                                                       String returnTime,
                                                       String type,
                                                       String passenger,
                                                       String cabin,
                                                       String limit ) {

        Logger.getAnonymousLogger().warning( "Departing Date: " + departingDate );

        //Declaring for departing parameters
        String[] departingDates = departingDate.split(",");
        String[] departingTimes = departingTime.split(",");
        String[] departingAirportCodeArray = departureAirportCode.split(",");

        for (String airportCode : departingAirportCodeArray) {
            if (!validIataLength(airportCode))
                return new ArrayList<>();
        }

        List<String> departingAirportCodes = Arrays.asList(departingAirportCodeArray);

        //Declaring for returning parameters (Airport code only)
        String[] arrivalAirportCodesArray = arrivalAirportCode.split(",");
        List<String> arrivalAirportCodes = Arrays.asList(arrivalAirportCodesArray);

        for (String airportCode : arrivalAirportCodes) {
            if (!validIataLength(airportCode))
                return new ArrayList<>();
        }

        Luisa.setSupplier(() -> {
            try {
                return new SabreFlightManager();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

        List<Itinerary> flights;

        try {
            AgeManager ageManager = passengerHelper.getPassengersFromString(passenger);

            SearchCommand command = Luisa.findMeFlights();

            //Prepare departure dates
            List<LocalDateTime> localDepartureDates = dateParser.parseStringDatesTimes(departingDates, departingTimes);

            //Fill the command with common information
            command.from(departingAirportCodes)
                    .to(arrivalAirportCodes)
                    .departingAt(localDepartureDates)
                    .forPassegers(Passenger.children(ageManager.getChildAges()))
                    .forPassegers(Passenger.infants(ageManager.getInfantAges()))
                    .forPassegers(Passenger.infantsOnSeat(ageManager.getInfantOnSeatAges()))
                    .forPassegers(Passenger.adults(ageManager.getNumberAdults()))
                    .limitTo(flightHelper.getLimitOfFlightsFromString(limit))
                    .preferenceClass(CabinClassParser.getCabinClassType(cabin));

            switch (type) {
                case "oneWay":
                    if (command.getDepartureAirports().size() == 1 && command.getArrivalAirports().size() == 1)
                        command.type(FlightType.ONEWAY);
                    else
                        throw new ParameterException(ParameterException.ParameterErrorType.ERROR_ON_AIRPORT_CODES, "Invalid quantity of airport codes for one way trip");
                    break;

                case "roundTrip":

                    if (returnDate.isEmpty() || returnTime.isEmpty())
                        throw new ParameterException(ParameterException.ParameterErrorType.ERROR_ON_DATES, "Empty returning dates");

                    if (command.getDepartureAirports().size() == 1 && command.getArrivalAirports().size() == 1)
                        command.type(FlightType.ONEWAY);
                    else
                        throw new ParameterException(ParameterException.ParameterErrorType.ERROR_ON_AIRPORT_CODES, "Invalid quantity of airport codes for round trip");

                    //Prepare departure dates
                    List<LocalDateTime> localReturnDates = dateParser.parseStringDatesTimes(returnDate, returnTime);

                    command
                            .returningAt(localReturnDates)
                            .type(FlightType.ROUNDTRIP);
                    break;

                case "multiCity":
                    if (command.getDepartureAirports().size() == command.getArrivalAirports().size())
                        command.type(FlightType.OPENJAW);
                    else
                        throw new ParameterException(ParameterException.ParameterErrorType.ERROR_ON_AIRPORT_CODES, "Invalid quantity of airport codes for muli city trip");
                    break;

                default:
                    break;
            }

            flights = command.execute();
            return this.getFlightItineraryFromItinerary(flights, type);

        } catch (Exception e) {

            List<String> stackTrace = Arrays.stream(e.getStackTrace())
                    .map(stackTraceElement -> stackTraceElement.toString() + "\n")
                    .collect(Collectors.toList());

            Logger.getLogger("Flight Service").warning(stackTrace.toString());
            Logger.getLogger("Flight Service").warning(e.toString());
        }

        return new ArrayList<>();
    }

    public List<FlightItinerary> getFlightItineraryFromItinerary(List<Itinerary> itineraryList, String type) {
        //TODO: Build fares object

        List<FlightItinerary> flightItineraries = itineraryList
                .stream()
                .map((Itinerary itinerary) -> {

                    Fares fareFromItinerary = itinerary.getPrice();
                    //TODO Implement sum of all segment's price in case of null on fareFromItinerary
                    ItineraryFares itineraryFares = ( fareFromItinerary == null )
                            ? new ItineraryFares()
                            : flightHelper.parseFaresToItineraryFares(fareFromItinerary);

                    List<Flight> flightList = flightHelper.getFlightsFromItinerary(itinerary);

                    FlightItinerary flightItinerary = new FlightItinerary(12345, type, flightList, itineraryFares);

                    return flightItinerary;
                })
                .collect(Collectors.toList());

        return flightItineraries;
    }
}