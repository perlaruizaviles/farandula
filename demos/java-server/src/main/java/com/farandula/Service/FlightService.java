package com.farandula.Service;

import com.farandula.Exceptions.ParameterException;
import com.farandula.Helpers.AgeManager;
import com.farandula.Helpers.DateParser;
import com.farandula.Helpers.FlightHelper;
import com.farandula.Helpers.PassengerHelper;
import com.farandula.Repositories.AirportRepository;
import com.farandula.models.*;
import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.amadeus.AmadeusFlightManager;
import com.nearsoft.farandula.models.*;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                                                       String returningAirportCode,
                                                       String returnDate,
                                                       String returnTime,
                                                       String type,
                                                       String passenger) {

        if (this.validIataLength(departureAirportCode) && this.validIataLength(returningAirportCode)) {

            String [] departingDates = departingDate.split(",");
            String [] departingTimes = departingTime.split(",");
            String [] deparingAirportCodeArray = departureAirportCode.split(",");

            List<LocalDateTime> localDepartureDates;
            List<String> departingAirportCodes = Arrays.asList(deparingAirportCodeArray);

            Luisa.setSupplier(() -> {
                try {
                    return new AmadeusFlightManager();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });

            List<Itinerary> flights;

            try {
                AgeManager ageManager = passengerHelper.getPassengersFromString(passenger);

                SearchCommand command = Luisa.findMeFlights();

                //Declaring departure dates
                localDepartureDates = dateParser.parseStringDatesTimes( departingDates, departingTimes );

                command
                        .from(departingAirportCodes)
                        .departingAt(localDepartureDates);

                switch (type){
                    case "oneWay" :
                        command.type(FlightType.ONEWAY);
                        break;

                    case "roundTrip" :

                        String [] airportCodesArray = returningAirportCode.split(",");
                        List<String> returnAirportCodes = Arrays.asList(airportCodesArray);

                        String [] returnDatesArray = returnDate.split(",");
                        String [] returnTimesArray = returnTime.split(",");

                        List<LocalDateTime> returnDateTimes = dateParser.parseStringDatesTimes(returnDatesArray, returnTimesArray);

                        command
                                .to(returnAirportCodes)
                                .returningAt(returnDateTimes)
                                .type(FlightType.ROUNDTRIP);

                        break;

                    case "multiCity" :
                        command.type(FlightType.OPENJAW);
                        break;

                    default:
                        break;
                }

                command
                        .forPassegers(Passenger.children(ageManager.getChildAges()))
                        .forPassegers(Passenger.infants(ageManager.getInfantAges()))
                        .forPassegers(Passenger.infantsOnSeat(ageManager.getInfantOnSeatAges()))
                        .forPassegers(Passenger.adults(ageManager.getNumberAdults()))
                        .limitTo(50);

                flights = command.execute();
                return this.getFlightItineraryFromItinerary(flights, type);

            } catch (Exception e) {
                Logger.getAnonymousLogger().warning(e.toString());
            }
        }
        return new ArrayList<>();
    }

    public List<FlightItinerary> getFlightItineraryFromItinerary(List<Itinerary> itineraryList, String type) {
        //TODO: Build fares object

        return itineraryList
                .stream()
                .map((Itinerary itinerary) -> {
                    ItineraryFares itineraryFares = flightHelper.parseFaresToItineraryFares(itinerary.getPrice());

                    List<Flight> flightList = flightHelper.getFlightsFromItinerary(itinerary);

                    FlightItinerary flightItinerary = new FlightItinerary(12345, type, flightList, itineraryFares);

                    return flightItinerary;
                })
                .collect(Collectors.toList());
    }
}
