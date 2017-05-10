package com.nearsoft.farandula.flightmanagers.travelport;

import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightManager;
import com.nearsoft.farandula.models.AirLeg;
import com.nearsoft.farandula.models.CabinClassType;
import com.nearsoft.farandula.models.FlightType;
import com.nearsoft.farandula.models.Passenger;
import org.junit.jupiter.api.Test;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import static com.nearsoft.farandula.models.CriteriaType.MINSTOPS;
import static com.nearsoft.farandula.models.CriteriaType.PRICE;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by pruiz on 5/1/17.
 */
class TravelportFlightManagerTest {

    @Test
    public void fakeAvail_OneWayTrip() throws FarandulaException, IOException {

        Luisa.setSupplier(() -> {
            return createTravelPortStub();
        });

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);

        List<AirLeg> flights = Luisa.findMeFlights()
                .from("DFW")
                .to("CDG")
                .departingAt(departingDate)
                .returningAt(departingDate.plusDays(1))
                .limitTo(2)
                .execute();

        assertTrue(flights.size() > 0);

        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0);
            assertEquals("DFW", airLeg.getDepartureAirportCode());
            assertEquals("CDG", airLeg.getArrivalAirportCode());
            assertEquals(CabinClassType.BUSINESS,  airLeg.getSegments().get(0).getSeatsAvailable().get(0).getClassCabin() );
            assertEquals(1, 1);
        });

    }

    private FlightManager createTravelPortStub() {

        TravelportFlightManager supplierStub = new TravelportFlightManager() {

            @Override
            public SOAPMessage sendRequest(SOAPMessage message) throws SOAPException {
                InputStream inputStream = this.getClass().getResourceAsStream("/travelport/response/airResponseOneWay.xml");
                SOAPMessage response = null;
                try {
                    response = MessageFactory.newInstance().createMessage(null, inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return response;
            }

        };
        return supplierStub;
    }

    @Test
    void getAvail_roundTrip() throws FarandulaException, IOException {

        Luisa.setSupplier(() -> {
            try {
                return createManagerTravelport();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);
        List<AirLeg> flights = Luisa.findMeFlights()
                .from("DFW")
                .to("CDG")
                .departingAt(departingDate)
                .returningAt(departingDate.plusDays(1))
                .forPassegers(Passenger.adults(1) )
                .type(FlightType.ROUNDTRIP)
                .sortBy(PRICE, MINSTOPS)
                .limitTo(2)
                .execute();

        assertNotNull(flights);

        assertTrue(flights.size() > 0);

        AirLeg bestFlight = flights.get(0);

        assertNotNull(bestFlight);

        assertAll("First should be the best Airleg", () -> {
            assertEquals("DFW", bestFlight.getSegments().get(0).getDepartureAirportCode());
            assertEquals("CDG", bestFlight.getSegments().get(0).getArrivalAirportCode());
            //assertEquals( "Economy/Coach", bestFlight.getSegments().get(0).getSeatsAvailable() );
            assertEquals(2, 2);
        });

    }

    private FlightManager createManagerTravelport() {
        return new TravelportFlightManager();
    }

    //@Test
    void buildEnvelopeStringFromSearch() {

    }

}
