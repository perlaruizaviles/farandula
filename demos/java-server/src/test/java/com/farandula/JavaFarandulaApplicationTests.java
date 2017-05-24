package com.farandula;

import com.farandula.Repositories.AirportRepository;
import com.farandula.Service.FlightService;
import com.farandula.models.Airport;
import com.farandula.models.FlightItinerary;
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
		Airport myAirport = new Airport(10000, "NearAirport", "CDMX", "Mexico", "CDM");

		airportRepository.insert(myAirport);

		List<Airport> result = airportRepository.findTop10ByCityLikeIgnoreCaseOrNameLikeIgnoreCaseOrIataLikeIgnoreCase("CDMX", "CDMX", "CDMX");

		Assert.assertEquals(1, result.size());

		Assert.assertEquals(10000, result.get(0).getId());
		Assert.assertEquals("NearAirport", result.get(0).getName());
		Assert.assertEquals("CDMX", result.get(0).getCity());
		Assert.assertEquals("Mexico", result.get(0).getCountry());
		Assert.assertEquals("CDM", result.get(0).getIata());

		airportRepository.delete(myAirport);
	}

	@Test
	public void findExactlyTenResults(){

	    List<Airport> airportsList = new ArrayList<>();

	    for(int i = 1; i <= 11; i++)
	        airportsList.add(new Airport(i+10000, "Name"+i, "City"+i, "Country"+i, "AA"+1));

	    airportRepository.insert( airportsList );

		List<Airport> result = airportRepository.
				findTop10ByCityLikeIgnoreCaseOrNameLikeIgnoreCaseOrIataLikeIgnoreCase("AA", "AA", "AA");

		Assert.assertEquals(10, result.size());

		airportRepository.delete(airportsList);
	}

	@Test
	public void oneWayTest(){

		String departingDate = "2017-05-25";
		String departingTime = "00:00:00";
		String dapartingAirportCodes = "CDMX,GDL";

		String returnDate = "";
		String returnTime = "";
		String returnAirportCodes = "";

		String type = "oneWay";
		String passenger = "children:,infants:1;2,infantsOnSeat:,adults:2";

		List<FlightItinerary> flightItineraries = flightService.getResponseFromSearch(dapartingAirportCodes,
				departingDate, departingTime, returnAirportCodes, returnDate, returnTime, type, passenger);

		assertNotEquals(0, flightItineraries);

	}
}
