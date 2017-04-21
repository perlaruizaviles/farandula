package com.nearsoft.farandula;

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

/**
 * Created by pruiz on 4/20/17.
 */
class AmadeusManagerTest {

    @Test
    void executeAvail() throws FarandulaException, IOException {

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);
        LocalDateTime returningDate = departingDate.plusDays(1);
        SearchCommand search = new SearchCommand(null)
                .from("DFW")
                .to("CDG")
                .departingAt(departingDate)
                .returningAt(returningDate)
                .forPassegers(Passenger.adults(1))
                .type("roundTrip")
                .sortBy(PRICE, MINSTOPS)
                .limitTo(50);

        Manager amadeus = new AmadeusManager();
        List<Flight> flightList = amadeus.getAvail(search);

        System.out.println("Check, flightList.size:"+ flightList.size());
    }

    @Test
    public void fakeAvail() throws Exception {

        Luisa.setSupplier(() -> {
            try {
                return createStub();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FarandulaException e) {
                e.printStackTrace();
            }
            return null;
        });

        //2017-07-07T11:00:00
        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);
        LocalDateTime returningDate = departingDate.plusDays(1);
        int limit = 2;
        List<Flight> flights = Luisa.findMeFlights()
                .departingAt(departingDate)
                .returningAt(returningDate)
                .limitTo(limit)
                .execute();

        assertTrue(flights.size() > 0);

        Flight bestFlight = flights.get(0);

        assertNotNull(bestFlight);

        assertAll("First should be the best Flight", () -> {
            assertEquals("DFW", bestFlight.getLegs().get(0).getDepartureAirportCode());
            assertEquals("CDG", bestFlight.getLegs().get(0).getArrivalAirportCode());
        });

    }

    private Manager createStub() throws IOException, FarandulaException {
        return new AmadeusManager() {

            @Override
            InputStream sendRequest(Request request) throws IOException, FarandulaException {
                return this.getClass().getResourceAsStream("/AmadeusAvailResponse.json");
            }
        };
    }


}
