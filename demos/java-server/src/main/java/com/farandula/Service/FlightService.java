package com.farandula.Service;

import com.farandula.Helpers.AgeManager;
import com.farandula.Helpers.FlightHelper;
import com.farandula.Helpers.PassengerHelper;
import com.farandula.Repositories.AirportRepository;
import com.farandula.models.*;
import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.amadeus.AmadeusFlightManager;
import com.nearsoft.farandula.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    public static boolean validIataLength(String iata) {
        return (iata.length() == 3) || (iata.length() == 2);
    }

    public static LocalDateTime parseDateTime(String date, String time) {

        String dateTime = date + "T" + time;

        LocalDateTime departureDateTime = LocalDateTime.parse(
                dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        return departureDateTime;
    }

    public List<FlightItinerary> getResponseFromSearch(String departureAirportCode,
                                                       String departingDate,
                                                       String departingTime,
                                                       String arrivalAirportCode,
                                                       String arrivalDate,
                                                       String arrivalTime,
                                                       String type,
                                                       String passenger) {

        if (FlightService.validIataLength(departureAirportCode) && FlightService.validIataLength(arrivalAirportCode)) {

            LocalDateTime departDateTime = FlightService.parseDateTime(departingDate, departingTime);
            LocalDateTime arrivalDateTime = FlightService.parseDateTime(arrivalDate, arrivalTime);

            Luisa.setSupplier(() -> {
                try {
                    return new AmadeusFlightManager();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });

            try {
                AgeManager ageManager = passengerHelper.getPassengersFromString(passenger);
                List<Itinerary> flights;
                SearchCommand command = Luisa.findMeFlights()
                        .from(departureAirportCode)
                        .to(arrivalAirportCode)
                        .departingAt(departDateTime)
                        .returningAt(arrivalDateTime)
                        .forPassegers(Passenger.children(ageManager.getChildAges()))
                        .forPassegers(Passenger.infants(ageManager.getInfantAges()))
                        .forPassegers(Passenger.infantsOnSeat(ageManager.getInfantOnSeatAges()))
                        .forPassegers(Passenger.adults(ageManager.getNumberAdults()))
                        .limitTo(2);
                command = ("oneWay".equals(type))
                        ? command.type(FlightType.ONEWAY)
                        : command.type(FlightType.ROUNDTRIP);

                flights = command.execute();
                return this.getFlightItineraryFromItinerary(flights, type);

                //TODO Parse response for passengers

            } catch (FarandulaException e) {
                Logger.getAnonymousLogger().warning(e.toString());
            } catch (IOException o) {
                Logger.getAnonymousLogger().warning(o.toString());
            }

        }
        return null;
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

    public List<Flight> getFlightsFromAirlegList(List<AirLeg> airLegList) {
        List<Flight> flights = new ArrayList<>();
        for (AirLeg airleg : airLegList) {

            Airport departureAirport = airportRepository.findByIataLikeIgnoreCase(airleg.getDepartureAirportCode()).get(0);
            Airport arrivalAirport = airportRepository.findByIataLikeIgnoreCase(airleg.getArrivalAirportCode()).get(0);

            LocalDateTime departureDate = airleg.getDepartingDate();
            LocalDateTime arrivalDate = airleg.getArrivalDate();

            List<FlightSegment> flightSegments = new ArrayList<>();

            for (Segment segment : airleg.getSegments()) {

                Airport departureSegmentAirport = airportRepository.findByIataLikeIgnoreCase(segment.getDepartureAirportCode()).get(0);
                Airport arrivalSegmentAirport = airportRepository.findByIataLikeIgnoreCase(segment.getArrivalAirportCode()).get(0);

                LocalDateTime departureSegmentDate = segment.getDepartureDate();
                LocalDateTime arrivalSegmentDate = segment.getArrivalDate();

                long duration = segment.getDuration();

                FlightSegment flightSegment = new FlightSegment(departureSegmentAirport, departureSegmentDate, arrivalSegmentAirport,
                        arrivalSegmentDate, duration);

                flightSegments.add(flightSegment);

            }

            Flight flight = new Flight()
                    .setDepartureAirport(departureAirport)
                    .setDepartureDate(departureDate)
                    .setArrivalAirport(arrivalAirport)
                    .setArrivalDate(arrivalDate)
                    .setSegments(flightSegments);

            flights.add(flight);
        }
        return flights;
    }
}
