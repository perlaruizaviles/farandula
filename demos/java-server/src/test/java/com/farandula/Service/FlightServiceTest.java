package com.farandula.Service;

import com.farandula.Configuration.Application;
import com.farandula.JavaFarandulaApplication;
import com.farandula.models.Flight;
import com.nearsoft.farandula.models.AirLeg;
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
    public void separateDepartAirLegs(){

        List<AirLeg> legs = new ArrayList<>();

        for(int i = 0; i < 10; i++){
            AirLeg leg = new AirLeg();

            leg.setId( "" + i );

            leg.setDepartureAirportCode("DD" + i);
            leg.setArrivalAirportCode("AA" + i);

            leg.setDepartingDate(LocalDateTime.now());
            leg.setArrivalDate(LocalDateTime.now().plusDays(2));

            legs.add( leg );
        }

        FlightService flightService = new FlightService();

        List<AirLeg> resultDepart = flightService.getDepartAirLegs( legs );
        List<AirLeg> resultReturn = flightService.getReturnAirLegs( legs );

        Assert.assertEquals( legs.size() / 2, resultDepart.size() );
        Assert.assertEquals( "0", resultDepart.get(0).getId() );

        Assert.assertEquals( legs.size() / 2, resultReturn.size() );
        Assert.assertEquals( "1", resultReturn.get(0).getId() );
    }

    @Test
    public void parseAirlegsIntoFlights(){

        List<AirLeg> airLegList = new ArrayList<>();

        List<Segment> segmentList = new ArrayList<>();

        for(int i = 0; i < 10; i++){

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

            airLegList.add(leg);
        }

        List<Flight> flightList = flightService.getFlightsFromAirlegList(airLegList);

        Assert.assertEquals( airLegList.size(), flightList.size() );

        Assert.assertEquals( airLegList.get(0).getDepartureAirportCode(),
                flightList.get(0).getDepartureAirport().getIata() );

        Assert.assertEquals( airLegList.get(0).getSegments().get(0).getDepartureAirportCode(),
                flightList.get(0).getSegments().get(0).getDepartureAirport().getIata());
    }
}