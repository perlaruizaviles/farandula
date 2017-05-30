package com.farandula.Helpers;

import com.farandula.models.Airport;
import com.farandula.models.Flight;
import com.farandula.models.FlightSegment;
import com.nearsoft.farandula.models.AirLeg;
import com.nearsoft.farandula.models.CabinClassType;
import com.nearsoft.farandula.models.Seat;
import com.nearsoft.farandula.models.Segment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by enrique on 23/05/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class FlightHelperTest {

    @Autowired
    FlightHelper flightHelper;

    @Test
    public void parseAirLegToFlightTest() throws Exception {

        AirLeg airLeg = new AirLeg();

        LocalDateTime departingDate = LocalDateTime.now();

        airLeg.setDepartureAirportCode("VVV");
        airLeg.setArrivalAirportCode("CCC");

        airLeg.setDepartingDate(departingDate);
        airLeg.setArrivalDate(departingDate.plusDays(1));

        List<Segment> segmentList = new ArrayList<>();

        for (int l = 0; l < 4; l++) {
            Segment segment = new Segment();

            segment.setDepartureAirportCode("VVV");
            segment.setDepartureDate(departingDate);

            segment.setArrivalAirportCode("CCC");
            segment.setArrivalDate(departingDate.plusDays(1));

            segmentList.add(segment);
        }

        airLeg.setSegments(segmentList);

        Flight flight = flightHelper.parseAirlegToFlight(airLeg);

        assertEquals(0, flight.getSegments().size());
    }

    @Test
    public void getCabinTypeFromSegmentTest() {

        Segment segment = new Segment();

        List<Seat> seatList = new ArrayList<>();

        for(int i = 0; i < 10; i++){

            Seat seat = new Seat();

            seat.setClassCabin( (i%3 == 0)
                    ? CabinClassType.ECONOMY
                    : ( i%2 == 0 )
                        ?CabinClassType.BUSINESS
                        :CabinClassType.PREMIUM_ECONOMY);

            seatList.add(seat);
        }

        segment.setSeatsAvailable(seatList);

        List<String> totalCabin = flightHelper.getCabinInformationFromSegment(segment);

        assertEquals(3, totalCabin.size());
    }
}