package com.farandula.Service;

import com.farandula.Configuration.Application;
import com.farandula.JavaFarandulaApplication;
import com.farandula.models.Flight;
import com.farandula.models.FlightItinerary;
import com.nearsoft.farandula.models.AirLeg;
import com.nearsoft.farandula.models.Itinerary;
import com.nearsoft.farandula.models.Segment;
import org.junit.Assert;
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
    FlightService flightService;

    @Test
    public void getPassengersFromString() throws Exception {
        String passengerString = "children:5,adults:3";

        Integer[] numberOfPassengers = FlightService.getPassengersFromString(passengerString);
        Assert.assertEquals(3, (int) numberOfPassengers[0]);
        Assert.assertEquals(5, (int) numberOfPassengers[1]);

    }

    @Test
    public void validateDateTime(){

        FlightService flightService = new FlightService();

        LocalDateTime result = flightService.parseDateTime("2011-12-03", "10:15:30");

        LocalDateTime expected = LocalDateTime.of(2011, 12, 03, 10, 15, 30);

        Assert.assertEquals( expected, result );
    }



    @Test
    public void parseItineraryIntoFlightItinerary(){

        List<Itinerary> itineraryList = new ArrayList<>();

        List<Segment> segmentList = new ArrayList<>();
        for (int k = 0; k < 5; k++) {
            Itinerary itinerary = new Itinerary();

            for(int i = 0; i < 2; i++) {

                AirLeg leg = new AirLeg();

                LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);

                leg.setDepartureAirportCode("DFW");
                leg.setDepartingDate( departingDate );
                leg.setArrivalAirportCode("CDG");
                leg.setArrivalDate( departingDate.plusDays(1) );

                for(int j = 0; j < 4; j++){
                    Segment segment = new Segment();

                    segment.setDepartureAirportCode("DFW");
                    segment.setDepartureDate( departingDate );

                    segment.setArrivalAirportCode("CDG");
                    segment.setArrivalDate( departingDate.plusDays(1) );

                    segmentList.add(segment);
                }

                leg.setSegments(segmentList);

                if (i == 0) {
                    itinerary.setDepartureAirleg(leg);

                } else {
                    itinerary.setReturningAirlegs(leg);
                }

            }
            itineraryList.add(itinerary);

        }



        List<FlightItinerary> flightItineraries = flightService.getFlightItineraryFromItinerary(itineraryList);

        assertEquals(itineraryList.size(), flightItineraries.size());
        assertEquals(itineraryList.get(0).getDepartureAirleg().getDepartureAirportCode(),
                flightItineraries.get(0).getDepartureAirleg().getDepartureAirport().getIata());


    }
    @Test
    public void parseItineraryIntoFlightItineraryOneWay(){

        List<Itinerary> itineraryList = new ArrayList<>();

        List<Segment> segmentList = new ArrayList<>();
        for (int k = 0; k < 5; k++) {
            Itinerary itinerary = new Itinerary();



                AirLeg leg = new AirLeg();

                LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);

                leg.setDepartureAirportCode("DFW");
                leg.setDepartingDate( departingDate );
                leg.setArrivalAirportCode("CDG");
                leg.setArrivalDate( departingDate.plusDays(1) );

                for(int j = 0; j < 4; j++){
                    Segment segment = new Segment();

                    segment.setDepartureAirportCode("DFW");
                    segment.setDepartureDate( departingDate );

                    segment.setArrivalAirportCode("CDG");
                    segment.setArrivalDate( departingDate.plusDays(1) );

                    segmentList.add(segment);
                }

                leg.setSegments(segmentList);
                itinerary.setDepartureAirleg(leg);




            itineraryList.add(itinerary);

        }



        List<FlightItinerary> flightItineraries = flightService.getFlightItineraryFromItinerary(itineraryList);

        assertEquals(itineraryList.size(), flightItineraries.size());
        assertEquals(itineraryList.get(0).getDepartureAirleg().getDepartureAirportCode(),
                flightItineraries.get(0).getDepartureAirleg().getDepartureAirport().getIata());
        assertEquals(null, flightItineraries.get(0).getReturningAirleg());


    }
}