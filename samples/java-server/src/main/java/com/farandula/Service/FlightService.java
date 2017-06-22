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
    FlightHelper flightHelper;
    @Autowired
    PassengerHelper passengerHelper;
    @Autowired
    DateParser dateParser;

    private void setSupplier(String gds) {
        Luisa.setSupplier(() -> {
            try {
                switch (gds) {
                    case "sabre":
                        return new SabreFlightManager();

                    case "amadeus":
                        return new AmadeusFlightManager();

                    case "travelport":
                        return new TravelportFlightManager();

                    default:
                        return new SabreFlightManager();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private void setCommandFlightType(SearchCommand command, SearchRequest request) throws ParameterException, FarandulaException {
        switch (request.getType()) {
            case "oneWay":
                if (command.getDepartureAirports().size() == 1 && command.getArrivalAirports().size() == 1)
                    command.type(FlightType.ONEWAY);
                else
                    throw new ParameterException(ParameterException.ParameterErrorType.ERROR_ON_AIRPORT_CODES, "Invalid quantity of airport codes for one way trip");
                break;

            case "roundTrip":

                if (request.getReturnDates().isEmpty() || request.getReturnTimes().isEmpty())
                    throw new ParameterException(ParameterException.ParameterErrorType.ERROR_ON_DATES, "Empty returning dates");

                if (command.getDepartureAirports().size() == 1 && command.getArrivalAirports().size() == 1)
                    command.type(FlightType.ONEWAY);
                else
                    throw new ParameterException(ParameterException.ParameterErrorType.ERROR_ON_AIRPORT_CODES, "Invalid quantity of airport codes for round trip");

                //Prepare departure dates
                List<LocalDateTime> localReturnDates = dateParser.parseStringDatesTimes(request.getReturnDates(), request.getReturnTimes());

                command
                        .returningAt(localReturnDates)
                        .type(FlightType.ROUNDTRIP);
                break;

            case "multiCity":
                if ("travelport".equals(request.getGds()))
                    throw new ParameterException(ParameterException.ParameterErrorType.UNAVAILABLE_REQUEST, "Multi City request is not available for TravelPort");

                if (command.getDepartureAirports().size() == command.getArrivalAirports().size())
                    command.type(FlightType.OPENJAW);
                else
                    throw new ParameterException(ParameterException.ParameterErrorType.ERROR_ON_AIRPORT_CODES, "Invalid quantity of airport codes for muli city trip");
                break;

            default:
                break;
        }
    }

    public List<String> prepareAirportCodes(String airportCodes) {

        String[] departingAirportCodeArray = airportCodes.split(",");

        for (String airportCode : departingAirportCodeArray) {
            if (!flightHelper.validIataLength(airportCode))
                return new ArrayList<>();
        }

        return Arrays.asList(departingAirportCodeArray);
    }

    public List<LocalDateTime> prepareDepartureDates(String departingDates, String departingTimes) throws ParameterException {
        return dateParser.parseStringDatesTimes(departingDates.split(","), departingTimes.split(","));
    }

    public List<FlightItinerary> getResponseFromSearch(SearchRequest request) {

        List<FlightItinerary> response = new ArrayList<>();
        //Declaring for departing parameters
        List<String> departingAirportCodes = prepareAirportCodes(request.getDepartingAirportCodes());

        //Declaring for returning parameters (Airport code only)
        List<String> arrivalAirportCodes = prepareAirportCodes(request.getArrivalAirportCodes());
        setSupplier(request.getGds());
        try {
            AgeManager ageManager = passengerHelper.getPassengersFromString(request.getPassenger());

            //Prepare departure dates
            List<LocalDateTime> localDepartureDates = prepareDepartureDates(request.getDepartingDates(), request.getDepartingTimes());

            //Fill the command with common information
            SearchCommand command = Luisa.findMeFlights()
                    .from(departingAirportCodes)
                    .to(arrivalAirportCodes)
                    .departingAt(localDepartureDates)
                    .forPassegers(Passenger.children(ageManager.getChildAges()))
                    .forPassegers(Passenger.infants(ageManager.getInfantAges()))
                    .forPassegers(Passenger.infantsOnSeat(ageManager.getInfantOnSeatAges()))
                    .forPassegers(Passenger.adults(ageManager.getNumberAdults()))
                    .limitTo(flightHelper.getLimitOfFlightsFromString(request.getLimit()))
                    .preferenceClass(CabinClassParser.getCabinClassType(request.getCabin()));

            setCommandFlightType(command, request);
            List<Itinerary> flights = command.execute();
            response.addAll(flightHelper.getFlightItineraryFromItinerary(flights, request.getType()));

        } catch (Exception e) {
            Logger.getLogger("Flight Service").warning(e.toString());
        }

        return response;
    }


}