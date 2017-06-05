package com.farandula.Helpers;

import com.farandula.Exceptions.AirportException;
import com.farandula.Repositories.AirportRepository;
import com.farandula.Service.AirportService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by antoniohernandez on 5/16/17.
 */
public interface FlightHelper {
    Flight parseAirlegToFlight(AirLeg airleg);

    FlightSegment parseSegmentToFlightSegment(Segment segment);

    ItineraryFares parseFaresToItineraryFares(Fares fares);

    List<Flight> getFlightsFromItinerary(Itinerary itinerary);

    List<String> getCabinInformationFromSegment(Segment segment);

    int getLimitOfFlightsFromString(String limitString);
}
