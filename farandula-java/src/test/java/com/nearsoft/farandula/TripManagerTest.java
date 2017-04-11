package com.nearsoft.farandula;


import com.nearsoft.farandula.models.Flight;
import com.nearsoft.farandula.models.Passenger;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.nearsoft.farandula.models.CriteriaType.MINSTOPS;
import static com.nearsoft.farandula.models.CriteriaType.PRICE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TripManagerTest {

    @Test
    public void avail(){

        LocalDate departingDate = LocalDate.now();
        LocalDate returningDate = departingDate.plusDays(2);
        List<Flight> flights=  Luisa.findMeFlights()
                .from("HMO")
                .to("LON")
                .departingAt (departingDate) //TODO ask kim about the coloquial slang for depature date
                .returningAt( returningDate)//TODO ask kim about the coloquial slang for return date
                .forPassegers(Passenger.adults(2) )
                .sortBy( PRICE,MINSTOPS )
                .limitTo(2)
                .execute(); //TODO find a better action name for the command execution `andGiveAListOfResults`, `doSearch`, `execute`

        assertThat( flights.size() , equalTo(2)  );
        Flight bestFlight = flights.get(0);


        assertAll("First should be the best Flight", () -> {
            assertEquals("HMO",   bestFlight.getDepartureAirport());
            assertEquals("LON",   bestFlight.getArrivalAirport());
        });


        //I Want to find flights from here to there on this dates for this passengers
    }

}
