package com.nearsoft.farandula.flightmanagers.travelport;

import com.nearsoft.farandula.Luisa;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.flightmanagers.FlightConnector;
import com.nearsoft.farandula.models.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by pruiz on 5/1/17.
 */
class TravelportFlightConnectorTest {

    static LocalDateTime departingDate;

    @BeforeAll
    public static void setup() {

        LocalDateTime toDay = LocalDateTime.now().plusMonths(1);

        departingDate = LocalDateTime.of(toDay.getYear(), toDay.getMonth(), toDay.getDayOfMonth(), 11, 00, 00);

    }

    @Test
    public void fakeAvail_OneWayTrip() throws FarandulaException, IOException {

        FlightConnector travelPortStub = createTravelPortStub();

        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");

        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);

        List<Itinerary> flights = Luisa.using(travelPortStub).findMeFlights()
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .limitTo(2)
                .execute();

        assertTrue(flights.size() > 0);

        assertAll("First should be the best Airleg", () -> {
            AirLeg airLeg = flights.get(0).getAirlegs().get(0);
            assertEquals("MUC", airLeg.getDepartureAirportCode());
            assertEquals("BCN", airLeg.getArrivalAirportCode());
            //TODO Check implementation of seat request
            assertEquals(CabinClassType.ECONOMY, airLeg.getSegments().get(0).getSeatsAvailable().get(0).getClassCabin());
            assertEquals(1, 1);
        });


    }

    @Test

    public void buildEnvelopeStringFromSearch() throws FarandulaException {

        TravelportFlightConnector travelport = new TravelportFlightConnector();

        String targetBranch = TravelportFlightConnector.getTargetBranch();

        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(  departingDate.plusDays(1) );

        FlightsSearchCommand searchCommand = Luisa.using(travelport).findMeFlights()
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .returningAt( returningDateList )
                .forPassegers(Passenger.adults(1))
                .limitTo(2);

        String request = travelport.buildEnvelopeStringFromSearch(searchCommand);

        assertEquals("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <air:LowFareSearchReq xmlns:air=\"http://www.travelport.com/schema/air_v34_0\" AuthorizedBy=\"user\" SolutionResult=\"true\" TargetBranch=\"" + targetBranch +"\" TraceId=\"trace\">\n" +
                "            <com:BillingPointOfSaleInfo xmlns:com=\"http://www.travelport.com/schema/common_v34_0\" OriginApplication=\"UAPI\"/><air:SearchAirLeg>\n" +
                "    <air:SearchOrigin>\n" +
                "        <com:Airport Code=\"DFW\" xmlns:com=\"http://www.travelport.com/schema/common_v34_0\"/>\n" +
                "    </air:SearchOrigin>\n" +
                "    <air:SearchDestination>\n" +
                "        <com:Airport Code=\"CDG\" xmlns:com=\"http://www.travelport.com/schema/common_v34_0\"/>\n" +
                "    </air:SearchDestination>\n" +
                "    <air:SearchDepTime PreferredTime=\""+  departingDate.format( DateTimeFormatter.ISO_DATE ) + "\"/>\n" +
                "    <air:AirLegModifiers>\n" +
                "        <air:PreferredCabins>\n" +
                "            <com:CabinClass Type=\"ECONOMY\" xmlns:com=\"http://www.travelport.com/schema/common_v34_0\"/>\n" +
                "        </air:PreferredCabins>\n" +
                "    </air:AirLegModifiers>\n" +
                "</air:SearchAirLeg>"+"<air:AirSearchModifiers>\n" +
                "    <air:PreferredProviders>\n" +
                "        <com:Provider xmlns:com=\"http://www.travelport.com/schema/common_v34_0\" Code=\"1G\"/>\n" +
                "    </air:PreferredProviders>\n" +
                "</air:AirSearchModifiers>"+
                "<com:SearchPassenger xmlns:com=\"http://www.travelport.com/schema/common_v34_0\" BookingTravelerRef=\"gr8AVWGCR064r57Jt0+8bA==\" Code=\"ADT\" Age=\"0\"/>"+
                "<air:AirPricingModifiers xmlns:com=\"http://www.travelport.com/schema/common_v34_0\" CurrencyType = \"USD\"/>\n"+"</air:LowFareSearchReq>\n" +
                "</soapenv:Body>\n" +
                "</soapenv:Envelope>", request);


    }


    private FlightConnector createTravelPortStub() {

        TravelportFlightConnector supplierStub = new TravelportFlightConnector() {

            @Override
            public SOAPMessage sendRequest(SOAPMessage message) throws SOAPException {
                InputStream inputStream = this.getClass().getResourceAsStream("/travelport/response/airResponseOneWay.xml");
                SOAPMessage response = null;
                try {
                    response = MessageFactory.newInstance().createMessage(null, inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return response;
            }

        };
        return supplierStub;
    }

    @Test
    public void getAvail_openJaw() throws FarandulaException, IOException {
        FlightConnector managerTravelport = createManagerTravelport();

        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");
        fromList.add("MEX");
        fromList.add("SFO");

        List<String> toList = new ArrayList<>();
        toList.add("CDG");
        toList.add("GDL");
        toList.add("HMO");

        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);
        departingDateList.add(departingDate.plusMonths(1));
        departingDateList.add(departingDate.plusMonths(2));

        List<Itinerary> flights = Luisa.using(managerTravelport).findMeFlights()
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .forPassegers(Passenger.adults(3))
                .forPassegers(Passenger.children(new int[]{8,9}))
                .type(FlightType.OPENJAW)
                .limitTo(2)
                .execute();

        assertNotNull(flights);

        assertTrue(flights.size() > 0);

        AirLeg firstAirLeg = flights.get(0).getAirlegs().get(0);
        AirLeg midAirLeg = flights.get(0).getAirlegs().get(1);
        AirLeg lastAirLeg = flights.get(0).getAirlegs().get(2);

        assertNotNull(firstAirLeg);
        assertNotNull(midAirLeg);
        assertNotNull(lastAirLeg);

        assertAll("First should be the best Airleg", () -> {
            assertEquals("DFW", firstAirLeg.getSegments().get(0).getDepartureAirportCode());
            assertEquals("CDG", firstAirLeg.getSegments().get(firstAirLeg.getSegments().size()-1).getArrivalAirportCode());
            assertEquals("MEX", midAirLeg.getSegments().get(0).getDepartureAirportCode());
            assertEquals("GDL", midAirLeg.getSegments().get(midAirLeg.getSegments().size()-1).getArrivalAirportCode());
            assertEquals("SFO", lastAirLeg.getSegments().get(0).getDepartureAirportCode());
            assertEquals("HMO", lastAirLeg.getSegments().get(lastAirLeg.getSegments().size()-1).getArrivalAirportCode());
            //assertEquals( "Economy/Coach", bestFlight.getSegments().get(0).getSeatsAvailable() );

        });

    }

    @Test
    public void getAvail_oneWay() throws FarandulaException, IOException {
        FlightConnector managerTravelport = createManagerTravelport();

        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");

        List<String> toList = new ArrayList<>();
        toList.add("CDG");

        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);

        List<Itinerary> flights = Luisa.using(managerTravelport).findMeFlights()
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .forPassegers(Passenger.adults(3))
                .forPassegers(Passenger.children(new int[]{8,9}))
                .type(FlightType.ONEWAY)
                .limitTo(2)
                .execute();

        assertNotNull(flights);

        assertTrue(flights.size() > 0);

        AirLeg airLeg = flights.get(0).getAirlegs().get(0);

        assertNotNull(airLeg);

        assertAll("First should be the best Airleg", () -> {
            assertEquals("DFW", airLeg.getSegments().get(0).getDepartureAirportCode());
            assertEquals("CDG", airLeg.getSegments().get(airLeg.getSegments().size()-1).getArrivalAirportCode());
            //assertEquals( "Economy/Coach", bestFlight.getSegments().get(0).getSeatsAvailable() );
        });

    }

    @Test
    public void getAvail_roundTrip() throws FarandulaException, IOException {
        FlightConnector managerTravelport = createManagerTravelport();

        List<String> fromList = new ArrayList<>();
        fromList.add("DFW");

        List<String> toList = new ArrayList<>();
        toList.add("CDG");

        List<LocalDateTime> departingDateList = new ArrayList<>();
        departingDateList.add(departingDate);

        List<LocalDateTime> returningDateList = new ArrayList<>();
        returningDateList.add(  departingDate.plusDays(1) );


        List<Itinerary> flights = Luisa.using(managerTravelport).findMeFlights()
                .from( fromList )
                .to( toList )
                .departingAt(departingDateList)
                .returningAt( returningDateList )
                .forPassegers(Passenger.adults(1))
                .type(FlightType.ROUNDTRIP)
                .limitTo(2)
                .execute();

        assertNotNull(flights);

        assertTrue(flights.size() > 0);

        AirLeg airLeg = flights.get(0).getAirlegs().get(0);

        AirLeg returningAirleg = flights.get(0).getAirlegs().get(1);

        assertNotNull(airLeg);

        assertAll("First should be the best Airleg", () -> {
            assertEquals("DFW", airLeg.getSegments().get(0).getDepartureAirportCode());
            assertEquals("CDG", returningAirleg.getSegments().get(0).getDepartureAirportCode());
            //assertEquals( "Economy/Coach", bestFlight.getSegments().get(0).getSeatsAvailable() );
        });

    }

    private FlightConnector createManagerTravelport() {
        return new TravelportFlightConnector();
    }

}
