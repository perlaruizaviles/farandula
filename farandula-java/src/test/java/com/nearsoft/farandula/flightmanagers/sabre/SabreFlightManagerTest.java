package com.nearsoft.farandula.flightmanagers.sabre;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.models.*;
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
    public void fakeAvail_OneWayTrip() throws Exception {

        Luisa.setSupplier(() -> {
            try {
                return createSabreStub();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);
        List<AirLeg> flights = Luisa.findMeFlights()
                .from("DFW")
                .to("CDG")
                .departingAt(departingDate)
                .returningAt(departingDate.plusDays(1))
                .type(FlightType.ROUNDTRIP)
                .limitTo(2)
                .execute();

        assertTrue(flights.size() > 0);

        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0);
            assertEquals("DFW", airLeg.getDepartureAirportCode());
            assertEquals("CDG", airLeg.getArrivalAirportCode());
            assertEquals(CabinClassType.ECONOMYCOACH, airLeg.getSegments().get(0).getSeatsAvailable().get(0).getClassCabin() );
        });

    }

    @Test
    public void realAvail_RoundWayTrip() throws Exception {

        Luisa.setSupplier(() -> {
            try {
                return createTripManagerSabre();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);
        List<AirLeg> flights = Luisa.findMeFlights()
                .from("DFW")
                .to("CDG")
                .departingAt(departingDate)
                .returningAt(departingDate.plusDays(1))
                .forPassegers(Passenger.adults(1))
                .type(FlightType.ROUNDTRIP)
                .sortBy(PRICE, MINSTOPS)
                .limitTo(2) //TODO check limit does not work.
                .preferenceClass(CabinClassType.ECONOMY)
                .execute(); //TODO find a better action name for the command execution `andGiveAListOfResults`, `doSearch`, `execute`

        assertTrue(flights.size() > 0);

        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0);
            assertEquals("DFW", airLeg.getDepartureAirportCode());
            assertEquals("CDG", airLeg.getArrivalAirportCode());
            assertEquals(CabinClassType.ECONOMYCOACH, airLeg.getSegments().get(0).getSeatsAvailable().get(0).getClassCabin() );
        });

    }


    @Test
    void buildJsonFromSearch() throws IOException, FarandulaException, Exception {

        SabreFlightManager manager = new SabreFlightManager();
        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);
        SearchCommand search = new SearchCommand(null);
        search
                .from("DFW")
                .to("CDG")
                .departingAt(departingDate)
                .returningAt(departingDate.plusDays(1))
                .forPassegers(Passenger.adults(1))
                .type(FlightType.ONEWAY)
                .sortBy(PRICE, MINSTOPS)
                .limitTo(2);

        String jsonRequestString = manager.buildJsonFromSearch(search);
        DocumentContext jsonRequest = JsonPath.parse(jsonRequestString);
        String locationCode = jsonRequest.read("$.OTA_AirLowFareSearchRQ.OriginDestinationInformation[0].OriginLocation.LocationCode").toString();
        assertEquals("DFW", locationCode);

    }

    private SabreFlightManager createTripManagerSabre() throws IOException, FarandulaException {
        return new SabreFlightManager();
    }

    private SabreFlightManager createSabreStub() throws IOException {

        SabreFlightManager supplierStub = new SabreFlightManager() {

            @Override
            public InputStream sendRequest(Request request) throws IOException, FarandulaException {
                return this.getClass().getResourceAsStream("/sabre/response/sabreAvailResponse.json");
            }

        };
        return supplierStub;

    }

    @Test
    public void buildAvailResponse() throws IOException {

        SabreFlightManager manager = new SabreFlightManager();

        manager.parseAvailResponse(this.getClass().getResourceAsStream("/sabre/response/sabreAvailResponse.json"));

    }

}
