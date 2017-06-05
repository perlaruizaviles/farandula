package com.farandula;

import com.farandula.Repositories.AirportRepository;
import com.farandula.Service.FlightService;
import com.farandula.models.Airport;
import com.farandula.models.FlightItinerary;
import com.farandula.models.ItineraryFares;
import com.farandula.models.SearchRequest;
import com.nearsoft.farandula.models.Fares;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JavaFarandulaApplicationTests {

    @Autowired
    AirportRepository airportRepository;
    @Autowired
    FlightService flightService;

    @Test
    public void insertAndFindAnAirport() {
        Airport myAirport = new Airport("NearAirport", "CDMX", "Mexico", "CDM");

        airportRepository.insert(myAirport);

        List<Airport> result = airportRepository.findTop10ByCityLikeIgnoreCaseOrNameLikeIgnoreCaseOrIataLikeIgnoreCase("CDMX", "CDMX", "CDMX");

        Assert.assertEquals(1, result.size());

        Assert.assertEquals("NearAirport", result.get(0).getName());
        Assert.assertEquals("CDMX", result.get(0).getCity());
        Assert.assertEquals("Mexico", result.get(0).getCountry());
        Assert.assertEquals("CDM", result.get(0).getIata());

        airportRepository.delete(myAirport);
    }

    @Test
    public void findExactlyTenResults() {

        List<Airport> airportsList = new ArrayList<>();

        for (int i = 1; i <= 11; i++)
            airportsList.add(new Airport("Name" + i, "City" + i, "Country" + i, "AA" + 1));

        airportRepository.insert(airportsList);

        List<Airport> result = airportRepository.
                findTop10ByCityLikeIgnoreCaseOrNameLikeIgnoreCaseOrIataLikeIgnoreCase("AA", "AA", "AA");

        Assert.assertEquals(10, result.size());

        airportRepository.delete(airportsList);
    }

    @Test
    public void oneWayTest() {

        String departingDate = "2017-06-07";
        String departingTime = "00:00:00";
        String departingAirportCodes = "MEX";

        String returnDate = "";
        String returnTime = "";
        String arrivalAirportCodes = "GDL";

        String type = "oneWay";
        String passenger = "children:0,infants:0,infantsOnSeat:0,adults:2";

        String classType = "economy";
        String limit = "50";

        SearchRequest request = new SearchRequest();

        request.setDepartingAirportCodes( departingAirportCodes );
        request.setDepartingDates( departingDate );
        request.setDepartingTimes( departingTime );

        request.setArrivalAirportCodes( arrivalAirportCodes );

        request.setType( type );
        request.setCabin( classType );
        request.setPassenger( passenger );
        request.setLimit( limit );

        List<FlightItinerary> flightItineraries = flightService.getResponseFromSearch( request );

        assertNotEquals(0, flightItineraries.size());

        for( FlightItinerary flightItinerary : flightItineraries ){
            assertEquals(1, flightItinerary.getAirlegs().size());
        }
    }

    @Test
    public void roundTripTest() {
        String departingDate = "2017-06-07";
        String departingTime = "00:00:00";
        String departingAirportCodes = "DFW";

        String returnDate = "2017-07-07";
        String returnTime = "00:00:00";
        String arrivalAirportCodes = "CDG";

        String type = "roundTrip";
        String passenger = "children:0,infants:0,infantsOnSeat:0,adults:2";

        String classType = "economy";
        String limit = "50";

        SearchRequest request = new SearchRequest();

        request.setDepartingAirportCodes( departingAirportCodes );
        request.setDepartingDates( departingDate );
        request.setDepartingTimes( departingTime );

        request.setArrivalAirportCodes( arrivalAirportCodes );
        request.setReturnDates( returnDate );
        request.setReturnTimes( returnTime );

        request.setType( type );
        request.setCabin( classType );
        request.setPassenger( passenger );
        request.setLimit( limit );

        List<FlightItinerary> flightItineraries = flightService.getResponseFromSearch( request );

        assertNotEquals(0, flightItineraries.size());
    }

    @Test
    public void multiCityTest() {
        String departingDate = "2017-06-07,2017-07-07,2017-08-07";
        String departingTime = "00:00:00,00:00:00,00:00:00";
        String departingAirportCodes = "MEX,CUU,LHR";

        String returnDate = "";
        String returnTime = "";
        String arrivalAirportCodes = "GDL,MEX,DFW";

        String type = "multiCity";
        String passenger = "children:0,infants:0,infantsOnSeat:0,adults:2";

        String classType = "economy";
        String limit = "50";

        SearchRequest request = new SearchRequest();

        request.setDepartingAirportCodes( departingAirportCodes );
        request.setDepartingDates( departingDate );
        request.setDepartingTimes( departingTime );

        request.setArrivalAirportCodes( arrivalAirportCodes );
        request.setReturnDates( returnDate );
        request.setReturnTimes( returnTime );

        request.setType( type );
        request.setCabin( classType );
        request.setPassenger( passenger );
        request.setLimit( limit );

        List<FlightItinerary> flightItineraries = flightService.getResponseFromSearch( request );

        assertNotEquals(0, flightItineraries.size());
    }


    @Test
    public void checkPriceChangeTest() {

        int minSize;

        String departingDate = "2017-06-07";
        String departingTime = "00:00:00";
        String departingAirportCodes = "MEX";

        String returnDate = "";
        String returnTime = "";
        String arrivalAirportCodes = "GDL";


        String type = "oneWay";
        String passengerTestOne = "children:0,infants:0,infantsOnSeat:0,adults:2";

        String classType = "economy";
        String limit = "50";

        SearchRequest request = new SearchRequest();

        request.setDepartingAirportCodes( departingAirportCodes );
        request.setDepartingDates( departingDate );
        request.setDepartingTimes( departingTime );

        request.setArrivalAirportCodes( arrivalAirportCodes );

        request.setType( classType );
        request.setLimit( limit );

        List<FlightItinerary> flightItineraries = flightService.getResponseFromSearch( request );

        List<ItineraryFares> price = new ArrayList<>();

        for ( int i = 0; i < flightItineraries.size(); i++){
            price.add(flightItineraries.get(i).getFares());
        }

        String passengerTestTwo = "children:2,infants:0,infantsOnSeat:0,adults:2";

        List<FlightItinerary> flightItinerariesTwo = flightService.getResponseFromSearch( request );

        List<ItineraryFares> priceTwo = new ArrayList<>();

        for ( int i = 0; i < flightItinerariesTwo.size(); i++){
            priceTwo.add(flightItinerariesTwo.get(i).getFares());
        }

        minSize = (flightItineraries.size() > flightItinerariesTwo.size())
                ? flightItinerariesTwo.size()
                : flightItineraries.size();

        for ( int i = 0; i < minSize; i++){
            assertNotEquals(price.get(i).getBasePrice().getAmount(), priceTwo.get(i).getBasePrice().getAmount());
        }

    }

    @Test
    public void cloningAnAirport() {

        Airport airport = new Airport("Juriquilla", "Queretaro", "Mexico", "JUR");

        Airport airportClone = (Airport) airport.clone();

        assertEquals(airportClone.getName(),"Juriquilla");
        assertEquals(airportClone.getCity(),"Queretaro");
        assertEquals(airportClone.getCountry(),"Mexico");
        assertEquals(airportClone.getIata(),"JUR");
    }

}
