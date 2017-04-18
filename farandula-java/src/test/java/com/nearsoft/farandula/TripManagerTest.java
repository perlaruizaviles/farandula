package com.nearsoft.farandula;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.nearsoft.farandula.models.Flight;
import com.nearsoft.farandula.models.Passenger;
import com.nearsoft.farandula.models.SearchCommand;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static com.nearsoft.farandula.models.CriteriaType.MINSTOPS;
import static com.nearsoft.farandula.models.CriteriaType.PRICE;
import static org.junit.jupiter.api.Assertions.*;

public class TripManagerTest {

    @Test
    void buildJsonFromSearch() throws IOException, FarandulaException {

        TripManager manager = new TripManager(null);

        //2017-07-07T11:00:00
        LocalDateTime departingDate = LocalDateTime.of(2017, 07 , 07, 11, 00, 00);
        LocalDateTime returningDate = departingDate.plusDays(1);
        SearchCommand search = new SearchCommand();
        search
                .from("DFW")
                .to("CDG")
                .departingAt ( departingDate) //TODO ask kim about the coloquial slang for depature date
                .returningAt( returningDate)//TODO ask kim about the coloquial slang for return date
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
    public void avail() throws Exception {

        //2017-07-07T11:00:00
        LocalDateTime departingDate = LocalDateTime.of(2017, 07 , 07, 11, 00, 00);
        LocalDateTime returningDate = departingDate.plusDays(1);

        int limit = 2;
        //TODO  implement a mock
        List<Flight> flights=  Luisa.findMeFlights()
                .from("DFW")
                .to("CDG")
                .departingAt ( departingDate) //TODO ask kim about the coloquial slang for depature date
                .returningAt( returningDate)//TODO ask kim about the coloquial slang for return date
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

    @Test
    public void  buildAvailResponse() throws IOException {

        TripManager manager = new TripManager( null );

        //Todo we should create resources folder under test
        manager.buildAvailResponse( this.getClass().getResourceAsStream( "/sabreAvailResponse.json"  ) );

    }

}
