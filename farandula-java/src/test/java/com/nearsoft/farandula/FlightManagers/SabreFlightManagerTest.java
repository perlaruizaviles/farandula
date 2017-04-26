package com.nearsoft.farandula.FlightManagers;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.nearsoft.farandula.FlightManagers.SabreFlightManager;
import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.models.Airleg;
import com.nearsoft.farandula.models.Flight;
import com.nearsoft.farandula.models.Passenger;
import com.nearsoft.farandula.models.SearchCommand;
import okhttp3.Request;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import static com.nearsoft.farandula.models.CriteriaType.MINSTOPS;
import static com.nearsoft.farandula.models.CriteriaType.PRICE;
import static org.junit.jupiter.api.Assertions.*;

public class SabreFlightManagerTest {

    @Test
    public void fakeAvail() throws Exception {

        //TODO
        Luisa.setSupplier(() -> {
            try {
                return createSabreStub();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });

        //2017-07-07T11:00:00
        LocalDateTime departingDate = LocalDateTime.of(2017, 07 , 07, 11, 00, 00);
        LocalDateTime returningDate = departingDate.plusDays(1);
        int limit = 2;
        List<Flight> flights=  Luisa.findMeFlights()
                .departingAt ( departingDate)
                .returningAt( returningDate)
                .limitTo(limit)
                .execute();

        assertTrue( flights.size() > 0);

        Flight bestFlight = flights.get(0);

        assertNotNull( bestFlight );

        assertAll("First should be the best Flight", () -> {
            Airleg airleg = bestFlight.getLegs().get(0);
            assertEquals("DFW",   airleg.getDepartureAirportCode());
            assertEquals("CDG",   airleg.getArrivalAirportCode() );
            assertEquals( "Economy/Coach", airleg.getSegments().get(0).getTravelClass() );
        });

    }

    @Test
    public void realAvail() throws Exception {

        Luisa.setSupplier(() -> {
            try {
                return createTripManagerSabre();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

        LocalDateTime departingDate = LocalDateTime.of(2017, 07 , 07, 11, 00, 00);
        LocalDateTime returningDate = departingDate.plusDays(1);
        int limit = 2;
        List<Flight> flights=  Luisa.findMeFlights()
                .from("DFW")
                .to("CDG")
                .departingAt ( departingDate)
                .returningAt( returningDate)
                .forPassegers(Passenger.adults(1) )
                .type( "roundTrip")
                .sortBy( PRICE,MINSTOPS )
                .limitTo(limit)
                .execute(); //TODO find a better action name for the command execution `andGiveAListOfResults`, `doSearch`, `execute`

        assertTrue( flights.size() > 0);

        Flight bestFlight = flights.get(0);

        assertNotNull( bestFlight );

        assertAll("First should be the best Flight", () -> {
            Airleg airleg = bestFlight.getLegs().get(0);
            assertEquals("DFW",   bestFlight.getLegs().get(0).getDepartureAirportCode());
            assertEquals("CDG",   bestFlight.getLegs().get(0).getArrivalAirportCode() );
            assertEquals( "Economy/Coach", bestFlight.getLegs().get(0).getSegments().get(0).getTravelClass() );
        });

    }


    @Test
    void buildJsonFromSearch() throws IOException, FarandulaException {

        SabreFlightManager manager = new SabreFlightManager();
        LocalDateTime departingDate = LocalDateTime.of(2017, 07 , 07, 11, 00, 00);
        LocalDateTime returningDate = departingDate.plusDays(1);
        SearchCommand search = new SearchCommand(null);
        search
                .from("DFW")
                .to("CDG")
                .departingAt ( departingDate)
                .returningAt( returningDate)
                .forPassegers(Passenger.adults(1) )
                .type( "roundTrip")
                .sortBy( PRICE,MINSTOPS )
                .limitTo(2);

        String jsonRequestString =  manager.buildJsonFromSearch( search );
        DocumentContext jsonRequest = JsonPath.parse(jsonRequestString);
        String locationCode = jsonRequest.read( "$.OTA_AirLowFareSearchRQ.OriginDestinationInformation[0].OriginLocation.LocationCode").toString();
        assertEquals( "DFW", locationCode );

    }

    private SabreFlightManager createTripManagerSabre() throws IOException, FarandulaException {
        return new SabreFlightManager();
    }

    private SabreFlightManager createSabreStub() throws IOException {

        SabreFlightManager supplierStub = new SabreFlightManager() {
            @Override
            InputStream sendRequest(Request request) throws IOException, FarandulaException {
                return this.getClass().getResourceAsStream("/sabreAvailResponse.json");
            }
        };
        return supplierStub;

    }

    @Test
    public void  buildAvailResponse() throws IOException {

        SabreFlightManager manager = new SabreFlightManager(  );

        manager.parseAvailResponse( this.getClass().getResourceAsStream( "/sabreAvailResponse.json"  ) );

    }

}
