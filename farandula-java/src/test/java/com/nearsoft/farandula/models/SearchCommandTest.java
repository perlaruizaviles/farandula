package com.nearsoft.farandula.models;

import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.sabre.SabreFlightConnector;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by pruiz on 5/18/17.
 */
class SearchCommandTest {

    @Test
    public void checkDefaultPassengerValue() throws FarandulaException, IOException {

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);
        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(departingDate.plusDays(1));

        FlightsSearchCommand search = new FlightsSearchCommand(new SabreFlightConnector())
                .from(fromList)
                .to(toList)
                .departingAt(departingDateList)
                .returningAt(returningDateList)
                .type(FlightType.ROUNDTRIP)
                .limitTo(2)
                .preferenceClass(CabinClassType.ECONOMY);

        search.execute();

        assertAll("Check the default number of passengers : ", () -> {

            assertNotNull(search.getPassengersMap().get(PassengerType.ADULTS));
            assertEquals(1, search.getPassengersMap().get(PassengerType.ADULTS).size());

        });
    }


    @Test
    public void checkNumberOfAdultsAndInfants() throws FarandulaException, IOException {

        LocalDateTime departingDate = LocalDateTime.of(2017, 07, 07, 11, 00, 00);
        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(departingDate.plusDays(1));

        FlightsSearchCommand search = new FlightsSearchCommand(new SabreFlightConnector())
                .from(fromList)
                .to(toList)
                .departingAt(departingDateList)
                .returningAt(returningDateList)
                .type(FlightType.ONEWAY)
                .limitTo(2)
                .preferenceClass(CabinClassType.ECONOMY)
                .forPassengers(Passenger.adults(2))
                .forPassengers(Passenger.infants(new int[]{1, 2, 1}));

        // more infants than adults, this should throws
        assertThrows(FarandulaException.class, () -> {
            search.execute();
        });

    }

    @Test
    public void checkInvalidOldDates() throws FarandulaException {

        LocalDateTime departingDate = LocalDateTime.now();
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate.minusDays(2));

        // this is an invalid old date
        assertThrows(FarandulaException.class, () -> {
            FlightsSearchCommand search = new FlightsSearchCommand(new SabreFlightConnector())
                    .departingAt(departingDateList);
        });

    }

    @Test
    public void checkInvalidFareAwayDates() {

        LocalDateTime departingDate = LocalDateTime.now();
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate.plusDays(332));

        // this is an invalid date
        assertThrows(FarandulaException.class, () -> {
            FlightsSearchCommand search = new FlightsSearchCommand(new SabreFlightConnector())
                    .departingAt(departingDateList);
        });
    }

    @Test
    public void checkInvalidReturningDates() throws FarandulaException {

        LocalDateTime departingDate = LocalDateTime.now().plusDays(5);
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);

        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(LocalDateTime.now().plusDays(2));

        FlightsSearchCommand search = new FlightsSearchCommand(new SabreFlightConnector())
                .departingAt(departingDateList)
                .returningAt(returningDateList)
                .type(FlightType.ROUNDTRIP);

        // this is an invalid date
        assertThrows(FarandulaException.class, () -> {
            search.execute();
        });
    }

    @Test
    public void checkFlightOneWayType() throws FarandulaException {

        LocalDateTime departingDate = LocalDateTime.now();
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        departingDateList.add(departingDate.plusDays(5));

        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(LocalDateTime.now().plusDays(2));

        FlightsSearchCommand search = new FlightsSearchCommand(new SabreFlightConnector())
                .departingAt(departingDateList)
                .returningAt(returningDateList)
                .type(FlightType.ONEWAY);

        // this is an invalid date
        assertThrows(FarandulaException.class, () -> {
            search.execute();
        });
    }

    @Test
    public void checkFlightRoundWayType() throws FarandulaException {

        LocalDateTime departingDate = LocalDateTime.now();
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);

        FlightsSearchCommand search = new FlightsSearchCommand(new SabreFlightConnector())
                .departingAt(departingDateList)
                .type(FlightType.ROUNDTRIP);

        // this is an invalid date
        assertThrows(FarandulaException.class, () -> {
            search.execute();
        });
    }


    @Test
    public void checkFlightRoundWayTypeVersion2() throws FarandulaException {

        LocalDateTime departingDate = LocalDateTime.now();
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        departingDateList.add(departingDate.plusDays(5));

        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(LocalDateTime.now().plusDays(2));

        FlightsSearchCommand search = new FlightsSearchCommand(new SabreFlightConnector())
                .departingAt(departingDateList)
                .returningAt(returningDateList)
                .type(FlightType.ROUNDTRIP);

        // this is an invalid date
        assertThrows(FarandulaException.class, () -> {
            search.execute();
        });
    }

    @Test
    public void checkFlightOpenJaw() throws FarandulaException {

        LocalDateTime departingDate = LocalDateTime.now();
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        departingDateList.add(departingDate.plusDays(5));

        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(LocalDateTime.now().plusDays(2));

        FlightsSearchCommand search = new FlightsSearchCommand(new SabreFlightConnector())
                .departingAt(departingDateList)
                .returningAt(returningDateList)
                .type(FlightType.OPENJAW);

        // is invalid to send returnig dates in openjaws
        assertThrows(FarandulaException.class, () -> {
            search.execute();
        });
    }

}