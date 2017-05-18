package com.farandula.Helpers;

import com.farandula.Repositories.AirportRepository;
import com.farandula.models.Airport;
import com.farandula.models.Flight;
import com.farandula.models.FlightSegment;
import com.farandula.models.ItineraryFares;
import com.nearsoft.farandula.models.AirLeg;
import com.nearsoft.farandula.models.Fares;
import com.nearsoft.farandula.models.Segment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by antoniohernandez on 5/16/17.
 */
@Component
public class FlightHelper {

    @Autowired
    AirportRepository airportRepository;

    public Flight parseAirlegToFlight(AirLeg airLeg) {

        Airport departureAirport = airportRepository.findByIataLikeIgnoreCase(airLeg.getDepartureAirportCode()).get(0);
        Airport arrivalAirport = airportRepository.findByIataLikeIgnoreCase(airLeg.getArrivalAirportCode()).get(0);

        LocalDateTime departureDate = airLeg.getDepartingDate();
        LocalDateTime arrivalDate = airLeg.getArrivalDate();

        List<FlightSegment> flightSegmentList = airLeg
                .getSegments()
                .stream()
                .map(this::parseSegmentToFlightSegment)
                .collect(Collectors.toList());

        Flight flight = new Flight(departureAirport, departureDate, arrivalAirport,
                arrivalDate, flightSegmentList);
        return flight;

    }

    public FlightSegment parseSegmentToFlightSegment(Segment segment) {

        Airport departureSegmentAirport = airportRepository.findByIataLikeIgnoreCase(segment.getDepartureAirportCode()).get(0);
        Airport arrivalSegmentAirport = airportRepository.findByIataLikeIgnoreCase(segment.getArrivalAirportCode()).get(0);

        LocalDateTime departureSegmentDate = segment.getDepartureDate();
        LocalDateTime arrivalSegmentDate = segment.getArrivalDate();

        long duration = segment.getDuration();

        FlightSegment flightSegment = new FlightSegment(departureSegmentAirport, departureSegmentDate, arrivalSegmentAirport,
                arrivalSegmentDate, duration);
        return flightSegment;

    }

    public ItineraryFares parseFaresToItineraryFares( Fares fares ) {
        ItineraryFares itineraryFares = new ItineraryFares();

        itineraryFares.setBasePrice(fares.getBasePrice());
        itineraryFares.setTaxesPrice(fares.getTaxesPrice());
        itineraryFares.setTotalPrice(fares.getTotalPrice());

        return itineraryFares;
    }
}
