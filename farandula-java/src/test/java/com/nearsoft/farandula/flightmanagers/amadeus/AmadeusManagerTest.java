package com.nearsoft.farandula.flightmanagers.amadeus;

import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightManager;
import com.nearsoft.farandula.models.*;
import okhttp3.Request;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by pruiz on 4/20/17.
 */
class AmadeusManagerTest {

    @Test
    public void fakeAvail_OneWayTrip() throws Exception {

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

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);

        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(  departingDate.plusDays(1) );

        List<Itinerary> flights = Luisa.findMeFlights()
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .returningAt( returningDateList )
                .type(FlightType.ONEWAY)
                .preferenceClass(CabinClassType.ECONOMY)
                .execute();

        assertTrue(flights.size() > 0);

        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0).getAirlegs().get(0);
            assertEquals("DFW", airLeg.getDepartureAirportCode());
            assertEquals("CDG", airLeg.getArrivalAirportCode());
            assertEquals(CabinClassType.ECONOMY, airLeg.getSegments().get(0).getSeatsAvailable().get(0).getClassCabin());
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
    public void realAvail_RoundWayTrip() throws Exception {

        initAmadeusSupplierForLuisa();

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);

        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(  departingDate.plusDays(1) );

        List<Itinerary> flights = Luisa.findMeFlights()
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .returningAt( returningDateList )
                .forPassegers(Passenger.adults(1))
                .type(FlightType.ROUNDTRIP)
                .preferenceClass(CabinClassType.ECONOMY)
                .limitTo(2)
                .execute();

        assertTrue(flights.size() > 0);

        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0).getAirlegs().get(0);
            assertEquals("DFW", airLeg.getDepartureAirportCode());
            assertEquals("CDG", airLeg.getArrivalAirportCode());
            assertEquals(CabinClassType.ECONOMY, airLeg.getSegments().get(0).getSeatsAvailable().get(0).getClassCabin());
        });
    }

    private void initAmadeusSupplierForLuisa() {
        Luisa.setSupplier(() -> {
            try {
                return new AmadeusFlightManager();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Test
    public void realAvail_OneWayTripUsingDifferentPassengers() throws Exception {

        initAmadeusSupplierForLuisa();

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);

        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(  departingDate.plusDays(1) );

        List<Itinerary> flights = Luisa.findMeFlights()
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .returningAt( returningDateList )
                .forPassegers( Passenger.adults(1) )
                .forPassegers( Passenger.infants( new int[]{1} ) )
                .forPassegers( Passenger.children( new int[]{10, 8})  )
                .type(FlightType.ROUNDTRIP)
                .preferenceClass(CabinClassType.ECONOMY)
                .limitTo(3)
                .execute();

        assertTrue(flights.size() > 0);

        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0).getAirlegs().get(0);
            assertEquals("DFW", airLeg.getDepartureAirportCode());
            assertEquals("CDG", airLeg.getArrivalAirportCode());
            assertEquals(CabinClassType.ECONOMY, airLeg.getSegments().get(0).getSeatsAvailable().get(0).getClassCabin());
        });
    }


    @Test
    void buildLinkFromSearch() throws IOException, FarandulaException {

        Luisa.setSupplier(() ->
                new AmadeusFlightManager()
        );

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);
        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(  departingDate.plusDays(1) );

        SearchCommand search = new SearchCommand(null);
        search
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .returningAt( returningDateList )
                .forPassegers(Passenger.adults(1))
                .type(FlightType.ROUNDTRIP)
                .limitTo(2);

        AmadeusFlightManager manager = new AmadeusFlightManager();
        String searchURL = manager.buildTargetURLFromSearch(search).get(0);
        String expectedURL = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?" +
                "apikey=R6gZSs2rk3s39GPUWG3IFubpEGAvUVUA" +
                "&origin=DFW" +
                "&destination=CDG" +
                "&departure_date=2017-07-07" +
                "&return_date=2017-07-08" +
                "&adults=1" +
                "&number_of_results=2" +
                "&travel_class=ECONOMY";
        assertEquals(expectedURL, searchURL);

    }

    @Test
    public void buildAvailResponse() throws IOException {

        AmadeusFlightManager manager = new AmadeusFlightManager();

        manager.parseAvailResponse(this.getClass().getResourceAsStream("/amadeus/response/AmadeusAvailResponse.json"));

    }

}
