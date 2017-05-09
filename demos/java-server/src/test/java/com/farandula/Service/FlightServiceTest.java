package com.farandula.Service;

import com.nearsoft.farandula.models.AirLeg;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by enrique on 8/05/17.
 */

public class FlightServiceTest {
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
}