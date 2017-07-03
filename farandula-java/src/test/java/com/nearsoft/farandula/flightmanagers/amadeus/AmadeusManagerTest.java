package com.nearsoft.farandula.flightmanagers.amadeus;

import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightConnector;
import com.nearsoft.farandula.models.*;
import okhttp3.Request;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by pruiz on 4/20/17.
 */
class AmadeusManagerTest {

    static LocalDateTime departingDate;

    @BeforeAll
    public static void setup() {

        LocalDateTime toDay = LocalDateTime.now().plusMonths(1);

        departingDate = LocalDateTime.of(toDay.getYear(), toDay.getMonth(), toDay.getDayOfMonth(), 11, 00, 00);

    }

    @Test
    public void fakeAvail_OneWayTrip() throws Exception {

        FlightConnector amadeusStub = createAmadeusStub();

        List<LocalDateTime> listDepartingAt = new ArrayList<>();
        listDepartingAt.add(departingDate);

        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");

        List<String> toList = new ArrayList<>();
        toList.add("CDG");


        List<Itinerary> flights = Luisa.using(amadeusStub).findMeFlights()
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

    private FlightConnector createAmadeusStub() throws IOException, FarandulaException {
        AmadeusFlightConnector manager = new AmadeusFlightConnector() {
            @Override
            public InputStream sendRequest(Request request) throws IOException, FarandulaException {
                return this.getClass().getResourceAsStream("/amadeus/response/AmadeusAvailResponse.json");
            }
        };
        return manager;
    }

    @Test
    public void realAvail_OneWayTrip() throws Exception {



        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);

        AmadeusFlightConnector amadeusFlightConnector = new AmadeusFlightConnector();
        List<Itinerary> flights = Luisa.using(amadeusFlightConnector).findMeFlights()
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .forPassengers(Passenger.adults(2))
                .forPassengers( Passenger.children( new int [] {5,7}) )
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



        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(  departingDate.plusDays(1) );
        AmadeusFlightConnector amadeusFlightConnector = new AmadeusFlightConnector();

        List<Itinerary> flights = Luisa.using(amadeusFlightConnector).findMeFlights()
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .returningAt( returningDateList )
                .forPassengers(Passenger.adults(1))
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



        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(  departingDate.plusDays(1) );

        AmadeusFlightConnector amadeusFlightConnector = new AmadeusFlightConnector();
        List<Itinerary> flights = Luisa.using(amadeusFlightConnector).findMeFlights()
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .returningAt( returningDateList )
                .forPassengers(Passenger.adults(1))
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
    public void realAvail_OpenJawTripUsingDifferentPassengers() throws Exception {



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

        AmadeusFlightConnector amadeusFlightConnector = new AmadeusFlightConnector();
        List<Itinerary> flights = Luisa.using(amadeusFlightConnector).findMeFlights()
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .returningAt( returningDateList )
                .forPassengers( Passenger.adults(1) )
                .forPassengers( Passenger.infants( new int[]{1} ) )
                .forPassengers( Passenger.children( new int[]{10, 8})  )
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


        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(  departingDate.plusDays(1) );

        FlightsSearchCommand search = new FlightsSearchCommand( new AmadeusFlightConnector() );
        search
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .returningAt( returningDateList )
                .forPassengers(Passenger.adults(1))
                .type(FlightType.ROUNDTRIP)
                .limitTo(2);

        AmadeusFlightConnector amadeusFlightConnector = new AmadeusFlightConnector();
        String searchURL = amadeusFlightConnector.buildTargetURLFromSearch(search).get(0);
        String expectedURL = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?" +
                "apikey=R6gZSs2rk3s39GPUWG3IFubpEGAvUVUA" +
                "&travel_class=ECONOMY" +
                "&origin=DFW" +
                "&destination=CDG" +
                "&departure_date=" + departingDate.format(DateTimeFormatter.ISO_DATE).toString() +
                "&return_date=" + departingDate.plusDays(1).format(DateTimeFormatter.ISO_DATE).toString() +
                "&adults=1" +
                "&number_of_results=2";
        assertEquals(expectedURL, searchURL);

    }


    //@Test
    void buildLinkFromSearchBUGAIRLINESCODES() throws IOException, FarandulaException {


        List<String> fromList = new ArrayList<>();
        fromList.add("MEX");
        List<String> toList = new ArrayList<>();
        toList.add("GDL");

        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        List<LocalDateTime> returningList = new ArrayList<>();
        returningList.add( departingDate.plusMonths(1) );

        AmadeusFlightConnector amadeusFlightConnector = new AmadeusFlightConnector();
        List<Itinerary> result = Luisa.using(amadeusFlightConnector).findMeFlights()
                .from(fromList)
                .to(toList)
                .departingAt(departingDateList)
                .returningAt( returningList )
                .forPassengers(Passenger.adults(2))
                .type( FlightType.ROUNDTRIP )
                .limitTo(50)
                .execute();

        assertNotNull( result );

    }

    @Test
    void buildLinkFromMultiCitySearch() throws IOException, FarandulaException {

        AmadeusFlightConnector amadeusFlightConnector = new AmadeusFlightConnector();

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

        FlightsSearchCommand search = new FlightsSearchCommand(amadeusFlightConnector);
        search
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .returningAt( returningDateList )
                .forPassengers(Passenger.adults(1))
                .type(FlightType.OPENJAW)
                .limitTo(2);

        List<String> searchURLList = amadeusFlightConnector.buildTargetURLFromSearch(search);

        String apiKey = amadeusFlightConnector.getApiKey();

        String expectedURL = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?" +
                "apikey=" + apiKey +
                "&travel_class=ECONOMY&origin=DFW" +
                "&destination=CDG" +
                "&departure_date=" + departingDate.format(DateTimeFormatter.ISO_DATE).toString() +
                "&adults=1" +
                "&number_of_results=2";

        assertEquals( 3, searchURLList.size() );
        assertEquals(expectedURL, searchURLList.get(0));

    }

    @Test
    public void buildAvailResponse() throws IOException {

        AmadeusFlightConnector manager = new AmadeusFlightConnector();

        manager.parseAvailResponse(this.getClass().getResourceAsStream("/amadeus/response/AmadeusAvailResponse.json"));

    }

}
