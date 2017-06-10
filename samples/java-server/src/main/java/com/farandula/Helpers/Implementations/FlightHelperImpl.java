package com.farandula.Helpers.Implementations;

import com.farandula.Exceptions.AirportException;
import com.farandula.Helpers.AirportsSource;
import com.farandula.Helpers.FlightHelper;
import com.farandula.Repositories.AirportRepository;
import com.farandula.models.*;
import com.nearsoft.farandula.models.AirLeg;
import com.nearsoft.farandula.models.Fares;
import com.nearsoft.farandula.models.Itinerary;
import com.nearsoft.farandula.models.Segment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by enrique on 5/06/17.
 */
@Component
public class FlightHelperImpl implements FlightHelper {

    @Autowired
    AirportRepository airportRepository;

    public Flight parseAirlegToFlight(AirLeg airLeg) {

        Airport departureAirport = AirportsSource.getAirport(airLeg.getDepartureAirportCode());

        Airport arrivalAirport = AirportsSource.getAirport(airLeg.getArrivalAirportCode());

        LocalDateTime departureDate = airLeg.getDepartingDate();
        LocalDateTime arrivalDate = airLeg.getArrivalDate();

        List<FlightSegment> flightSegmentList = airLeg
                .getSegments()
                .stream()
                .map(this::parseSegmentToFlightSegment)
                .filter(segment -> segment != null)
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

        Airport departureSegmentAirport = AirportsSource.getAirport(segment.getDepartureAirportCode());
        Airport arrivalSegmentAirport = AirportsSource.getAirport(segment.getArrivalAirportCode());

        try {

            if (departureSegmentAirport == null) {
                throw new AirportException(AirportException.AirportErrorType.AIRPORT_NOT_FOUND, "Departure Airpot Not Found");
            }

            if (arrivalSegmentAirport == null) {
                throw new AirportException(AirportException.AirportErrorType.AIRPORT_NOT_FOUND, "Arrival Airpot Not Found");
            }

            LocalDateTime departureSegmentDate = segment.getDepartureDate();
            LocalDateTime arrivalSegmentDate = segment.getArrivalDate();

            long duration = segment.getDuration();

            String airlineMarketing = segment.getMarketingAirlineName();
            String airlineOperating = segment.getOperatingAirlineCode();
            String airplane = segment.getAirplaneData();
            List<String> cabinTypes = this.getCabinInformationFromSegment(segment);

            FlightSegment flightSegment = new FlightSegment()
                    .setDepartureAirport(departureSegmentAirport)
                    .setDepartureDate(departureSegmentDate)
                    .setArrivalAirport(arrivalSegmentAirport)
                    .setArrivalDate(arrivalSegmentDate)
                    .setDuration(duration)
                    .setAirLineMarketingName(airlineMarketing)
                    .setAirLineOperationName(airlineOperating)
                    .setAirplaneData(airplane)
                    .setCabinTypes(cabinTypes);

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

    public List<String> getCabinInformationFromSegment(Segment segment) {

        if (segment.getSeatsAvailable() == null) {
            List<String> emptySeat = new ArrayList<>();
            emptySeat.add("NON_AVAILABLE");
            return emptySeat;
        }

        return segment.
                getSeatsAvailable()
                .stream()
                .map(seat -> seat.getClassCabin().toString())
                .distinct()
                .collect(Collectors.toList());
    }

    public int getLimitOfFlightsFromString(String limitString) {

        try {
            if (limitString == null)
                throw new NumberFormatException();
            return Integer.parseInt(limitString);

        } catch (NumberFormatException e) {
            return 50;
        }
    }

    public List<FlightItinerary> getFlightItineraryFromItinerary(List<Itinerary> itineraryList, String type) {
        //TODO: Build fares object

        List<FlightItinerary> flightItineraries = itineraryList
                .stream()
                .map((Itinerary itinerary) -> {

                    Fares fareFromItinerary = itinerary.getPrice();
                    //TODO Implement sum of all segment's price in case of null on fareFromItinerary
                    ItineraryFares itineraryFares = (fareFromItinerary == null)
                            ? new ItineraryFares()
                            : this.parseFaresToItineraryFares(fareFromItinerary);

                    List<Flight> flightList = this.getFlightsFromItinerary(itinerary);

                    FlightItinerary flightItinerary = new FlightItinerary(12345, type, flightList, itineraryFares);

                    return flightItinerary;
                })
                .collect(Collectors.toList());

        return flightItineraries;
    }

    public boolean validIataLength(String iata) {
        return (iata.length() == 3) || (iata.length() == 2);
    }
}
