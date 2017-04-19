package com.nearsoft.farandula;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
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

public class TripManagerTest {

    @Test
    void buildJsonFromSearch() throws IOException, FarandulaException {

        TripManager manager = new TripManager(null);
        LocalDateTime departingDate = LocalDateTime.of(2017, 07 , 07, 11, 00, 00);
        LocalDateTime returningDate = departingDate.plusDays(1);
        SearchCommand search = new SearchCommand();
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

    @Test
    public void fakeAvail() throws Exception {

        TripManager.setSupplier(() -> createStub() );

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
            assertEquals("DFW",   bestFlight.getLegs().get(0).getDepartureAirportCode());
            assertEquals("CDG",   bestFlight.getLegs().get(0).getArrivalAirportCode() );
        });

    }


    @Test
    public void realAvail() throws Exception {

        TripManager.setSupplier(() -> {
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
            assertEquals("DFW",   bestFlight.getLegs().get(0).getDepartureAirportCode());
            assertEquals("CDG",   bestFlight.getLegs().get(0).getArrivalAirportCode() );
        });

    }

    private TripManager createTripManagerSabre() throws IOException, FarandulaException {

        return TripManager.sabre( );
    }

    private TripManager createStub() {
        return new TripManager(null){
            @Override
            InputStream sendRequest(Request request) throws IOException, FarandulaException {
                return this.getClass().getResourceAsStream( "/sabreAvailResponse.json"  );
            }
        };
    }

    @Test
    public void  buildAvailResponse() throws IOException {

        TripManager manager = new TripManager( null );

        manager.buildAvailResponse( this.getClass().getResourceAsStream( "/sabreAvailResponse.json"  ) );

    }

}
