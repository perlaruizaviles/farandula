package com.farandula.Helpers;

import com.farandula.Exceptions.AirportException;
import com.farandula.Repositories.AirportRepository;
import com.farandula.models.Airport;
import com.farandula.models.Flight;
import com.farandula.models.FlightSegment;
import com.farandula.models.ItineraryFares;
import com.nearsoft.farandula.models.AirLeg;
import com.nearsoft.farandula.models.Fares;
import com.nearsoft.farandula.models.Itinerary;
import com.nearsoft.farandula.models.Segment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by antoniohernandez on 5/16/17.
 */
@Component
public class FlightHelper {

    @Autowired
    AirportRepository airportRepository;

    public Flight parseAirlegToFlight(AirLeg airLeg) {

        Airport departureAirport = airportRepository.findByIataLikeIgnoreCase(airLeg.getDepartureAirportCode())
                .stream()
                .findFirst()
                .orElse(null);

        Airport arrivalAirport = airportRepository.findByIataLikeIgnoreCase(airLeg.getArrivalAirportCode())
                .stream()
                .findFirst()
                .orElse(null);

        LocalDateTime departureDate = airLeg.getDepartingDate();
        LocalDateTime arrivalDate = airLeg.getArrivalDate();

        List<FlightSegment> flightSegmentList = airLeg
                .getSegments()
                .stream()
                .map(this::parseSegmentToFlightSegment)
                .collect(Collectors.toList());

        Flight flight = new Flight()
                .setDepartureAirport(departureAirport)
                .setDepartureDate(departureDate)
                .setArrivalAirport(arrivalAirport)
                .setArrivalDate(arrivalDate)
                .setSegments(flightSegmentList);

        return flight;

    }


    public FlightSegment parseSegmentToFlightSegment(Segment segment) {

        Airport departureSegmentAirport;
        Airport arrivalSegmentAirport;

        Optional<Airport> optionalAirportDeparture = airportRepository.findByIataLikeIgnoreCase(segment.getDepartureAirportCode())
                .stream()
                .findFirst();

        try {

            if (optionalAirportDeparture.isPresent()) {
                departureSegmentAirport = optionalAirportDeparture.get();
            } else {
                throw new AirportException(AirportException.AirportErrorType.AIRPORT_NOT_FOUND, "Departure Airpot Not Found");
            }

            Optional<Airport> optionalAirportArrival = airportRepository.findByIataLikeIgnoreCase(segment.getArrivalAirportCode())
                    .stream()
                    .findFirst();

            if (optionalAirportArrival.isPresent()) {
                arrivalSegmentAirport = optionalAirportArrival.get();
            } else {
                throw new AirportException(AirportException.AirportErrorType.AIRPORT_NOT_FOUND, "Arrival Airpot Not Found");
            }

            LocalDateTime departureSegmentDate = segment.getDepartureDate();
            LocalDateTime arrivalSegmentDate = segment.getArrivalDate();

            long duration = segment.getDuration();

            FlightSegment flightSegment = new FlightSegment(departureSegmentAirport, departureSegmentDate, arrivalSegmentAirport,
                    arrivalSegmentDate, duration);
            return flightSegment;

        } catch (AirportException e) {
            Logger.getAnonymousLogger().warning(e.toString());
            return null;
        }

    }


    public ItineraryFares parseFaresToItineraryFares(Fares fares) {
        ItineraryFares itineraryFares = new ItineraryFares();

        itineraryFares.setBasePrice(fares.getBasePrice());
        itineraryFares.setTaxesPrice(fares.getTaxesPrice());
        itineraryFares.setTotalPrice(fares.getTotalPrice());

        return itineraryFares;
    }

    public List<Flight> getFlightsFromItinerary(Itinerary itinerary) {
        return itinerary.getAirlegs()
                .stream()
                .map(airLeg -> this.parseAirlegToFlight(airLeg))
                .collect(Collectors.toList());
    }
}
