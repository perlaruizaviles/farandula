package com.farandula.Service;

import com.farandula.Configuration.Application;
import com.farandula.Helpers.DateParser;
import com.farandula.Helpers.FlightHelper;
import com.farandula.Helpers.PassengerHelper;
import com.farandula.JavaFarandulaApplication;
import com.farandula.models.Flight;
import com.farandula.models.FlightItinerary;
import com.nearsoft.farandula.models.*;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by enrique on 8/05/17.
 */
//@ContextConfiguration(classes = JavaFarandulaApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FlightServiceTest {

    @Autowired
    FlightHelper flightHelper;
    @Autowired
    DateParser dateParser;

    static List<Itinerary> itineraryListRoundTrip = new ArrayList<>();
    static List<Itinerary> itineraryListOneWay= new ArrayList<>();

    @Test
    public void validateDateTime() {

        FlightService flightService = new FlightService();

        LocalDateTime result = dateParser.parseDateTime("2011-12-03", "10:15:30");

        LocalDateTime expected = LocalDateTime.of(2011, 12, 03, 10, 15, 30);

        Assert.assertEquals(expected, result);
    }

    @BeforeClass public static void prepareFlightItineraryLists() {

        List<Segment> segmentList = new ArrayList<>();
        for(int i = 1; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                Itinerary itinerary = new Itinerary();

                for (int k = 0; k < i ; k++) {

                    AirLeg leg = new AirLeg();

                    LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);

                    leg.setDepartureAirportCode("DFW");
                    leg.setDepartingDate(departingDate);
                    leg.setArrivalAirportCode("CDG");
                    leg.setArrivalDate(departingDate.plusDays(1));

                    for (int l = 0; l < 4; l++) {
                        Segment segment = new Segment();

                        segment.setDepartureAirportCode("DFW");
                        segment.setDepartureDate(departingDate);

                        segment.setArrivalAirportCode("CDG");
                        segment.setArrivalDate(departingDate.plusDays(1));

                        segment.setSeatsAvailable(new ArrayList<>());

                        segmentList.add(segment);
                    }

                    leg.setSegments(segmentList);

                    itinerary.getAirlegs().add(leg);

                }
                itinerary.setPrice(new Fares());
                if (i == 1) {
                    itineraryListOneWay.add(itinerary);
                }
                else {
                    itineraryListRoundTrip.add(itinerary);
                }
            }
        }
    }

    @AfterClass public static void cleanItineraryLists() {
        itineraryListOneWay = null;
        itineraryListRoundTrip = null;
    }

    @Test
    public void parseItineraryIntoFlightItinerary() {

        List<FlightItinerary> flightItineraries = flightHelper.getFlightItineraryFromItinerary(itineraryListRoundTrip, "round");

        assertEquals(itineraryListRoundTrip.size(), flightItineraries.size());
        for( int i = 0; i < flightItineraries.size(); i++ ) {
            assertEquals(itineraryListRoundTrip.get(i).getAirlegs().get(0).getDepartureAirportCode(),
                    flightItineraries.get(i).getAirlegs().get(0).getDepartureAirport().getIata());
        }

    }

    @Test
    public void parseItineraryIntoFlightItineraryOneWay() {

        List<FlightItinerary> flightItineraries = flightHelper.getFlightItineraryFromItinerary(itineraryListOneWay, "oneWay");

        assertEquals(itineraryListOneWay.size(), flightItineraries.size());

        for (int i = 0; i < flightItineraries.size(); i++){
            assertEquals(itineraryListOneWay.get(i).getAirlegs().get(0).getDepartureAirportCode(),
                    flightItineraries.get(i).getAirlegs().get(0).getDepartureAirport().getIata());
        }
    }
}