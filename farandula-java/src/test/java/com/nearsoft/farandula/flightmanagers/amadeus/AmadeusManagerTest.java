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
        List<LocalDateTime> listDepartingAt = new ArrayList<>();
        listDepartingAt.add(departingDate);

        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");

        List<String> toList = new ArrayList<>();
        toList.add("CDG");

        List<Itinerary> flights = Luisa.findMeFlights()
                .from( fromList )
                .to( toList )
                .departingAt( listDepartingAt )
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
    public void realAvail_OneWayTrip() throws Exception {

        initAmadeusSupplierForLuisa();

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);

        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);

        List<Itinerary> flights = Luisa.findMeFlights()
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .forPassegers(Passenger.adults(2))
                .forPassegers( Passenger.children( new int [] {5,7}) )
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


    @Test
    public void realAvail_OpenJawTrip() throws Exception {

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
    public void realAvail_OpenJawTripUsingDifferentPassengers() throws Exception {

        initAmadeusSupplierForLuisa();

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);
        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        fromList.add("MEX");
        fromList.add("BOS");

        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        toList.add("LAX");
        toList.add("LHR");

        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        departingDateList.add( departingDate.plusDays(7) );
        departingDateList.add( departingDate.plusDays(15) );

        List<LocalDateTime> returningDateList = new ArrayList<>();

        List<Itinerary> flights = Luisa.findMeFlights()
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .returningAt( returningDateList )
                .forPassegers( Passenger.adults(1) )
                .forPassegers( Passenger.infants( new int[]{1} ) )
                .forPassegers( Passenger.children( new int[]{10, 8})  )
                .type(FlightType.OPENJAW)
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

        SearchCommand search = new SearchCommand( Luisa.getInstance() );
        search
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .returningAt( returningDateList )
                .forPassegers(Passenger.adults(1))
                .type(FlightType.ROUNDTRIP)
                .limitTo(2);

        String searchURL = ((AmadeusFlightManager)Luisa.getInstance()).buildTargetURLFromSearch(search).get(0);
        String expectedURL = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?" +
                "apikey=R6gZSs2rk3s39GPUWG3IFubpEGAvUVUA" +
                "&travel_class=ECONOMY" +
                "&origin=DFW" +
                "&destination=CDG" +
                "&departure_date=2017-07-07" +
                "&return_date=2017-07-08" +
                "&adults=1" +
                "&number_of_results=2";
        assertEquals(expectedURL, searchURL);

    }

    //@Test
    void buildLinkFromSearchBUGAIRLINESCODES() throws IOException, FarandulaException {

        Luisa.setSupplier(() ->
                new AmadeusFlightManager()
        );

        LocalDateTime departingDate = LocalDateTime.of(2017, 06, 07, 00, 00, 00);
        List<String> fromList = new ArrayList<>();
        fromList.add("MEX");
        List<String> toList = new ArrayList<>();
        toList.add("GDL");

        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);

        List<Itinerary> result = Luisa.findMeFlights()
                .from(fromList)
                .to(toList)
                .departingAt(departingDateList)
                .forPassegers(Passenger.adults(2))
                .forPassegers(Passenger.infants(new int[]{1, 2}))
                .limitTo(50)
                .execute();

        assertNotNull( result );

    }

    @Test
    void buildLinkFromMultiCitySearch() throws IOException, FarandulaException {

        Luisa.setSupplier(() ->
                new AmadeusFlightManager()
        );

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);
        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        fromList.add("MEX");
        fromList.add("BOS");

        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        toList.add("LAX");
        toList.add("LHR");

        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        departingDateList.add( departingDate.plusDays(7) );
        departingDateList.add( departingDate.plusDays(15) );

        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(  departingDate.plusDays(1) );
        returningDateList.add(  departingDate.plusDays(8) );
        returningDateList.add(  departingDate.plusDays(16) );

        SearchCommand search = new SearchCommand(Luisa.getInstance());
        search
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .returningAt( returningDateList )
                .forPassegers(Passenger.adults(1))
                .type(FlightType.OPENJAW)
                .limitTo(2);

        List<String> searchURLList = ( (AmadeusFlightManager)(Luisa.getInstance()) ).buildTargetURLFromSearch(search);

        String apiKey = ( (AmadeusFlightManager)(Luisa.getInstance()) ).getApiKey();

        String expectedURL = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?" +
                "apikey=" + apiKey +
                "&travel_class=ECONOMY&origin=DFW" +
                "&destination=CDG" +
                "&departure_date=2017-07-07" +
                "&adults=1" +
                "&number_of_results=2";

        assertEquals( 3, searchURLList.size() );
        assertEquals(expectedURL, searchURLList.get(0));

    }

    @Test
    public void buildAvailResponse() throws IOException {

        AmadeusFlightManager manager = new AmadeusFlightManager();

        manager.parseAvailResponse(this.getClass().getResourceAsStream("/amadeus/response/AmadeusAvailResponse.json"));

    }

}
