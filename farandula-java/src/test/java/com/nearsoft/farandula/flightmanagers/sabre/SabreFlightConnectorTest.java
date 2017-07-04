package com.nearsoft.farandula.flightmanagers.sabre;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.models.*;
import okhttp3.Request;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SabreFlightConnectorTest {

    static LocalDateTime departingDate;

    @BeforeAll
    public static void setup() {

        LocalDateTime toDay = LocalDateTime.now().plusMonths(1);

        departingDate = LocalDateTime.of(toDay.getYear(), toDay.getMonth(), toDay.getDayOfMonth(), 11, 00, 00);

    }


    @Test
    public void fakeAvail_OneWayTrip() throws Exception {


        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(departingDate.plusDays(1));

        List<Itinerary> flights = Luisa.using(createSabreStub()).findMeFlights()
                .from(fromList)
                .to(toList)
                .departingAt(departingDateList)
                .returningAt(returningDateList)
                .type(FlightType.ROUNDTRIP)
                .execute();

        assertTrue(flights.size() > 0);

        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0).getAirlegs().get(0);
            assertEquals("DFW", airLeg.getDepartureAirportCode());
            assertEquals("CDG", airLeg.getArrivalAirportCode());
            assertEquals(CabinClassType.ECONOMYCOACH, airLeg.getSegments().get(0).getSeatsAvailable().get(0).getClassCabin());
        });

    }

    @Test
    public void fakeAvail_MultiCityTrip() throws Exception {

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
        departingDateList.add(departingDate.plusDays(7));
        departingDateList.add(departingDate.plusDays(15));

        List<Itinerary> flights = Luisa.using(createSabreStubMultiCity()).findMeFlights()
                .from(fromList)
                .to(toList)
                .departingAt(departingDateList)
                .type(FlightType.OPENJAW)
                .limitTo(2)
                .execute();

        assertTrue(flights.size() > 0);

        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0).getAirlegs().get(0);
            assertEquals("DFW", airLeg.getDepartureAirportCode());
            assertEquals("CDG", airLeg.getArrivalAirportCode());
            assertEquals(CabinClassType.ECONOMYCOACH, airLeg.getSegments().get(0).getSeatsAvailable().get(0).getClassCabin());
        });

    }

    @Test
    public void defaultAvail_OneWayTrip() throws Exception {


        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);


        List<Itinerary> flights = Luisa.using().findMeFlights()
                .from(fromList)
                .to(toList)
                .departingAt(departingDateList)
                .execute();

        assertTrue(flights.size() > 0 && flights.size() <= 50);

        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0).getAirlegs().get(0);
            assertEquals("DFW", airLeg.getDepartureAirportCode());
            assertEquals("CDG", airLeg.getArrivalAirportCode());
            assertEquals(CabinClassType.ECONOMYCOACH, airLeg.getSegments().get(0).getSeatsAvailable().get(0).getClassCabin());
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
        returningDateList.add(departingDate.plusDays(1));

        List<Itinerary> flights = Luisa.using(createTripManagerSabre()).findMeFlights()
                .from(fromList)
                .to(toList)
                .departingAt(departingDateList)
                .returningAt(returningDateList)
                .forPassengers(Passenger.adults(1))
                .type(FlightType.ROUNDTRIP)
                .limitTo(10)
                .preferenceClass(CabinClassType.ECONOMY)
                .execute();

        assertTrue(flights.size() > 0);

        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0).getAirlegs().get(0);
            assertEquals("DFW", airLeg.getDepartureAirportCode());
            assertEquals("CDG", airLeg.getArrivalAirportCode());
            assertEquals(CabinClassType.ECONOMYCOACH, airLeg.getSegments().get(0).getSeatsAvailable().get(0).getClassCabin());
        });

    }

    @Test
    public void realAvail_OneWayTripDifferentPassengers() throws Exception {

        List<String> fromList = new ArrayList<>();
        fromList.add("MEX");
        List<String> toList = new ArrayList<>();
        toList.add("LAX");

        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);

        List<Itinerary> flights = Luisa.using(createTripManagerSabre()).findMeFlights()
                .from(fromList)
                .to(toList)
                .departingAt(departingDateList)
                .forPassengers(Passenger.adults(2))
                .forPassengers(Passenger.children(new int[]{12, 16}))
                .forPassengers(Passenger.infantsOnSeat(new int[]{1}))
                .forPassengers(Passenger.infants(new int[1]))
                .limitTo(5)
                .preferenceClass(CabinClassType.ECONOMY)
                .execute();

        assertTrue(flights.size() > 0);

        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0).getAirlegs().get(0);
            assertEquals("MEX", airLeg.getDepartureAirportCode());
            assertEquals("LAX", airLeg.getArrivalAirportCode());
            assertEquals(CabinClassType.ECONOMYCOACH, airLeg.getSegments().get(0).getSeatsAvailable().get(0).getClassCabin());
        });

    }


    @Test
    public void realAvail_MultiCityTripDifferentPassengers() throws Exception {


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
        departingDateList.add(departingDate.plusDays(7));
        departingDateList.add(departingDate.plusDays(15));

        List<Itinerary> flights = Luisa.using(createTripManagerSabre()).findMeFlights()
                .from(fromList)
                .to(toList)
                .departingAt(departingDateList)
                .forPassengers(Passenger.adults(2))
                .forPassengers(Passenger.children(new int[]{12, 16}))
                //.forPassengers( Passenger.infantsOnSeat( new int[]{1})  )
                .forPassengers(Passenger.infants(new int[1]))
                .limitTo(5)
                .type(FlightType.OPENJAW)
                .preferenceClass(CabinClassType.ECONOMY)
                .execute();

        assertTrue(flights.size() > 0);

        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0).getAirlegs().get(0);
            assertEquals("DFW", airLeg.getDepartureAirportCode());
            assertEquals("CDG", airLeg.getArrivalAirportCode());
            assertEquals(CabinClassType.ECONOMYCOACH, airLeg.getSegments().get(0).getSeatsAvailable().get(0).getClassCabin());
        });

    }

    @Test
    void buildJsonRequestFromSearch() throws Exception {

        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(departingDate.plusDays(1));

        FlightsSearchCommand search = new FlightsSearchCommand(new SabreFlightConnector());
        search
                .from(fromList)
                .to(toList)
                .departingAt(departingDateList)
                .returningAt(returningDateList)
                .forPassengers(Passenger.adults(1))
                .type(FlightType.ONEWAY)
                .limitTo(2);

        String jsonRequestString = SabreFlightConnector.buildJsonFromSearch(search);
        DocumentContext jsonRequest = JsonPath.parse(jsonRequestString);
        String locationCode = jsonRequest.read("$.OTA_AirLowFareSearchRQ.OriginDestinationInformation[0].OriginLocation.LocationCode").toString();
        assertEquals("DFW", locationCode);

    }

    @Test
    void buildJsonRequestFromMultiCitySearch() throws Exception {

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
        departingDateList.add(departingDate.plusDays(7));
        departingDateList.add(departingDate.plusDays(15));

        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(departingDate.plusDays(1));
        returningDateList.add(departingDate.plusDays(8));
        returningDateList.add(departingDate.plusDays(16));

        FlightsSearchCommand search = new FlightsSearchCommand(new SabreFlightConnector());
        search
                .from(fromList)
                .to(toList)
                .departingAt(departingDateList)
                .returningAt(returningDateList)
                .forPassengers(Passenger.adults(1))
                .type(FlightType.OPENJAW)
                .limitTo(10);

        String jsonRequestString = SabreFlightConnector.buildJsonFromSearch(search);
        DocumentContext jsonRequest = JsonPath.parse(jsonRequestString);
        String locationCode = jsonRequest.read("$.OTA_AirLowFareSearchRQ.OriginDestinationInformation[0].OriginLocation.LocationCode").toString();
        assertEquals(fromList.get(0), locationCode);

    }

    private SabreFlightConnector createTripManagerSabre() throws IOException, FarandulaException {
        return new SabreFlightConnector();
    }

    private SabreFlightConnector createSabreStub() throws IOException {

        SabreFlightConnector supplierStub = new SabreFlightConnector() {

            @Override
            public InputStream sendRequest(Request request) throws IOException, FarandulaException {
                return this.getClass().getResourceAsStream("/sabre/response/flights/sabreAvailResponse.json");
            }

        };
        return supplierStub;

    }


    private SabreFlightConnector createSabreStubMultiCity() throws IOException {

        SabreFlightConnector supplierStub = new SabreFlightConnector() {

            @Override
            public InputStream sendRequest(Request request) throws IOException, FarandulaException {
                return this.getClass().getResourceAsStream("/sabre/response/flights/SabreAvailMultiCityResponse.json");
            }

        };
        return supplierStub;

    }

    @Test
    public void buildAvailResponse() throws IOException, FarandulaException {

        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(departingDate.plusDays(1));

        SabreFlightConnector manager = new SabreFlightConnector();
        FlightsSearchCommand search = new FlightsSearchCommand(manager);
        search
                .from(fromList)
                .to(toList)
                .departingAt(departingDateList)
                .returningAt(returningDateList)
                .forPassengers(Passenger.adults(1))
                .type(FlightType.ONEWAY)
                .limitTo(2);
        manager.parseAvailResponse(this.getClass().getResourceAsStream("/sabre/response/flights/sabreAvailResponse.json"), search);

    }

}
