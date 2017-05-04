package com.nearsoft.farandula.flightmanagers.amadeus;

import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightManager;
import com.nearsoft.farandula.models.AirLeg;
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

/**
 * Created by pruiz on 4/20/17.
 */
class AmadeusManagerTest {

    //TODO #10 we need to make sure that that we execute at least a round trip search

    //TODO verify why tis test is slow
    @Test
    public void fakeAvail() throws Exception {

        //TODO
        Luisa.setSupplier(() -> {
            try {
                return createAmadeusStub();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FarandulaException e) {
                e.printStackTrace();
            }
            return null;
        });

        //2017-07-07T11:00:00
        LocalDateTime departingDate = LocalDateTime.of(2017, 07 , 07, 11, 00, 00);
        LocalDateTime returningDate = departingDate.plusDays(1);
        int limit = 2;
        List<AirLeg> flights=  Luisa.findMeFlights()
                .departingAt ( departingDate)
                .returningAt( returningDate)
                .limitTo(limit)
                .execute();

        assertTrue( flights.size() > 0);


        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0);
            assertEquals("DFW",   airLeg.getDepartureAirportCode());
            assertEquals("CDG",   airLeg.getArrivalAirportCode() );
        });

    }

    private FlightManager createAmadeusStub() throws IOException, FarandulaException {
        AmadeusFlightManager manager = new AmadeusFlightManager() {
            @Override
            public InputStream sendRequest(Request request) throws IOException, FarandulaException {
                return this.getClass().getResourceAsStream("/amadeus/response/AmadeusAvailResponse.json");
            }
        };
        return manager;
    }

    @Test
    public void realAvail() throws Exception {

        Luisa.setSupplier(() -> {
            try {
                return  new AmadeusFlightManager();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

        //2017-07-07T11:00:00
        LocalDateTime departingDate = LocalDateTime.of(2017, 07 , 07, 11, 00, 00);
        LocalDateTime returningDate = departingDate.plusDays(1);
        int limit = 2;
        List<AirLeg> flights=  Luisa.findMeFlights()
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


        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0);
            assertEquals("DFW",   airLeg.getDepartureAirportCode());
            assertEquals("CDG",   airLeg.getArrivalAirportCode() );
        });
    }

    @Test
    void buildLinkFromSearch() throws IOException, FarandulaException {

        Luisa.setSupplier( ()->
            new AmadeusFlightManager()
        );

        AmadeusFlightManager manager = new AmadeusFlightManager();
        LocalDateTime departingDate = LocalDateTime.of(2017, 07 , 07, 11, 00, 00);
        LocalDateTime returningDate = departingDate.plusDays(1);

        SearchCommand search = new SearchCommand( Luisa.getInstance() )
                .from("DFW")
                .to("CDG")
                .departingAt ( departingDate)
                .returningAt( returningDate)
                .forPassegers(Passenger.adults(1) )
                .type( "roundTrip")
                .sortBy( PRICE,MINSTOPS )
                .limitTo(2);

        String searchURL =  manager.buildTargetURLFromSearch( search );
        String expectedURL = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?" +
                "apikey=R6gZSs2rk3s39GPUWG3IFubpEGAvUVUA" +
                "&origin=DFW" +
                "&destination=CDG" +
                "&departure_date=2017-07-07" +
                "&return_date=2017-07-08" +
                "&adults=1" +
                "&number_of_results=2";
        assertEquals(expectedURL, searchURL );

    }

    @Test
    public void  buildAvailResponse() throws IOException {

        AmadeusFlightManager manager = new AmadeusFlightManager( );

        manager.parseAvailResponse( this.getClass().getResourceAsStream("/amadeus/response/AmadeusAvailResponse.json") );

    }

}
