package com.nearsoft.farandula.flightmanagers.travelport;

import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightManager;
import com.nearsoft.farandula.models.AirLeg;
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

    public void fakeAvail() throws Exception {

        //TODO
        Luisa.setSupplier(() -> {
            return createTravelPortStub();
        });

        //2017-07-07T11:00:00
        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);
        LocalDateTime returningDate = departingDate.plusDays(1);
        int limit = 2;
        List<AirLeg> flights = Luisa.findMeFlights()
                .from("DFW")
                .to("CDG")
                .departingAt(departingDate)
                .returningAt(returningDate)
                .limitTo(limit)
                .execute();

        assertTrue(flights.size() > 0);

        //TODO need to verify that we read correctly the legs /segments

        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0);
            assertEquals("DFW", airLeg.getDepartureAirportCode());
            assertEquals("CDG", airLeg.getArrivalAirportCode());
            assertEquals("Economy/Coach", airLeg.getSegments().get(0).getTravelClass());
            assertEquals(1, 1);
        });

    }

    private FlightManager createTravelPortStub() {
        TravelportFlightManager supplierStub = new TravelportFlightManager() {

            @Override
            public SOAPMessage sendRequest(SOAPMessage message, String url_api) throws SOAPException {
                InputStream inputStream = this.getClass().getResourceAsStream("/travelport/response/airResponse.xml");
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
    void getAvail() throws IOException, FarandulaException {

        Luisa.setSupplier(() -> {
            try {
                return createManagerTravelport();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);
        LocalDateTime returningDate = departingDate.plusDays(1);
        int limit = 2;
        List<AirLeg> flights = Luisa.findMeFlights()
                .from("DFW")
                .to("CDG")
                .departingAt(departingDate)
                .returningAt(returningDate)
                .forPassegers(Passenger.adults(1))
                .type("roundTrip")
                .sortBy(PRICE, MINSTOPS)
                .limitTo(limit)
                .execute(); //TODO find a better action name for the command execution `andGiveAListOfResults`, `doSearch`, `execute`


        assertNotNull(flights);

       /* assertTrue( flights.size() > 0);

        Airleg bestFlight = flights.get(0);

        assertNotNull( bestFlight );

        assertAll("First should be the best Airleg", () -> {
            Airleg airleg = bestFlight.getLegs().get(0);
            assertEquals("DFW",   bestFlight.getLegs().get(0).getDepartureAirportCode());
            assertEquals("CDG",   bestFlight.getLegs().get(0).getArrivalAirportCode() );
            assertEquals( "Economy/Coach", bestFlight.getLegs().get(0).getSegments().get(0).getTravelClass() );
            assertEquals( 2,2 );
        });*/

    }

    private FlightManager createManagerTravelport() {
        return new TravelportFlightManager();
    }

    //@Test
    void buildEnvelopeStringFromSearch() {

    }

}
