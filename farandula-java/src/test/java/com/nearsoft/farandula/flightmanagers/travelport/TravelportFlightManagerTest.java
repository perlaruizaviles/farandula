package com.nearsoft.farandula.flightmanagers.travelport;

import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightManager;
import com.nearsoft.farandula.models.*;
import org.junit.jupiter.api.Test;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

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

        List<Itinerary> flights = Luisa.findMeFlights()
                .from("DFW")
                .to("CDG")
                .departingAt(departingDate)
                .returningAt(departingDate.plusDays(1))
                .limitTo(2)
                .execute();

        assertTrue(flights.size() > 0);

        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0).getDepartureAirlegs().get(0);
            assertEquals("DFW", airLeg.getDepartureAirportCode());
            assertEquals("CDG", airLeg.getArrivalAirportCode());
            assertEquals(CabinClassType.BUSINESS, airLeg.getSegments().get(0).getSeatsAvailable().get(0).getClassCabin());
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
    public void getAvail_roundTrip() throws FarandulaException, IOException {

        Luisa.setSupplier(() -> {
            try {
                return createManagerTravelport();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);
        List<Itinerary> flights = Luisa.findMeFlights()
                .from("DFW")
                .to("CDG")
                .departingAt(departingDate)
                .returningAt(departingDate.plusDays(1))
                .forPassegers(Passenger.adults(1))
                .type(FlightType.ROUNDTRIP)
                .limitTo(2)
                .execute();

        assertNotNull(flights);

        assertTrue(flights.size() > 0);

        AirLeg airLeg = flights.get(0).getDepartureAirlegs().get(0);

        AirLeg returningAirleg = flights.get(0).getReturningAirlegs().get(0);

        assertNotNull(airLeg);

        assertAll("First should be the best Airleg", () -> {
            assertEquals("DFW", airLeg.getSegments().get(0).getDepartureAirportCode());
            assertEquals("CDG", returningAirleg.getSegments().get(0).getDepartureAirportCode());
            //assertEquals( "Economy/Coach", bestFlight.getSegments().get(0).getSeatsAvailable() );
        });

    }

    private FlightManager createManagerTravelport() {
        return new TravelportFlightManager();
    }

}
