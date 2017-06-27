package com.nearsoft.farandula.flightmanagers.travelport.request.xml;

import com.nearsoft.farandula.models.*;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by pruiz on 5/4/17.
 */
public class TravelportXMLRequest {

    private static Map valuesMap = new HashMap();

    private static StrSubstitutor sub;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public static String getRequest(FlightsSearchCommand search, String targetBranchValue) {

        valuesMap.put("passengersNumber", search.getPassengers().size());
        valuesMap.put("classTravel",   search.getCabinClass().equals("") ? "Economy" : search.getCabinClass() );
        valuesMap.put("targetBranch", targetBranchValue);

        sub = new StrSubstitutor(valuesMap);

        InputStream soapInputStream = TravelportXMLRequest.class
                .getResourceAsStream("/travelport/XML.request/requestHeader.xml");
        String header = new BufferedReader(new InputStreamReader(soapInputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        header = sub.replace( header );

        String xml = getSearchAirLegs( search );

        //Passengers
        String passengers = getSearchPassengers(search.getPassengersMap());

        //Search Modifier Fragment
        soapInputStream = TravelportXMLRequest.class
                .getResourceAsStream("/travelport/XML.request/searchModifier.xml");
        String modifier = new BufferedReader(new InputStreamReader(soapInputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        soapInputStream = TravelportXMLRequest.class
                .getResourceAsStream("/travelport/XML.request/requestTail.xml");
        String tail = new BufferedReader(new InputStreamReader(soapInputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        return header + xml + modifier + passengers + tail;

    }

    private static String getSearchPassengers(Map<PassengerType, List<Passenger>> passengersMap){
        InputStream passengerInputStream = TravelportXMLRequest.class
                .getResourceAsStream("/travelport/XML.request/requestPassenger.xml");

        String passengerXML = new BufferedReader(new InputStreamReader(passengerInputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        String passengers = "";

        for(Map.Entry<PassengerType, List<Passenger>> entry : passengersMap.entrySet()){
            passengers += getPassengerDetail(passengerXML, entry);
        }

        return passengers;
    }

    private static String getPassengerDetail(String passengerXML, Map.Entry<PassengerType, List<Passenger>> entry){;

        String passengers = "";

        for(Passenger passenger : entry.getValue()){
            valuesMap.put("passengerType", passenger.getType().getTravelportCode());
            valuesMap.put("passengerAge", passenger.getAge());

            sub = new StrSubstitutor(valuesMap);

            passengers += sub.replace(passengerXML);
        }

        return passengers;
    }

    private static String getSearchAirLegs(FlightsSearchCommand search) {

        InputStream airLegInputStream = TravelportXMLRequest.class
                .getResourceAsStream("/travelport/XML.request/requestSearchAirLeg.xml");
        String leg = new BufferedReader(new InputStreamReader(airLegInputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        String airlegs = "";

        for (int i = 0; i<search.getDepartingDates().size(); i++) {
            valuesMap.put("departureAirport", search.getDepartureAirports().get(i) );
            valuesMap.put("arrivalAirport", search.getArrivalAirports().get(i) );
            valuesMap.put("departureDate", search.getDepartingDates().get(i).format(DateTimeFormatter.ISO_LOCAL_DATE));
            sub = new StrSubstitutor(valuesMap);
            airlegs += sub.replace( leg );
        }



        if ( search.getType() == FlightType.ROUNDTRIP ){

            valuesMap.put("departureAirport", search.getArrivalAirports().get(0));
            valuesMap.put("arrivalAirport", search.getDepartureAirports().get(0) );
            valuesMap.put("departureDate", search.getReturningDates().get(0).format(DateTimeFormatter.ISO_LOCAL_DATE));
            sub = new StrSubstitutor(valuesMap);
            airlegs += sub.replace( leg );

        }

        return airlegs;
    }

    public static String getRequest(Segment seg, String targetBranchValue, TravelportFlightDetails details) {

        InputStream soapInputStream = TravelportXMLRequest.class
                .getResourceAsStream("/travelport/XML.request/airPricing.xml");
        String header = new BufferedReader(new InputStreamReader(soapInputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        valuesMap.clear();

        String carrier = seg.getOperatingAirlineCode() != null ? seg.getOperatingAirlineCode() : seg.getMarketingAirlineCode();
        String flightNumber = seg.getOperatingFlightNumber()!= null? seg.getOperatingFlightNumber() : seg.getMarketingFlightNumber();

        valuesMap.put("key", seg.getKey() );
        valuesMap.put("group", details.getGroup() );
        valuesMap.put("carrier", carrier );
        valuesMap.put("flightNumber", flightNumber  );
        valuesMap.put("origin", seg.getDepartureAirportCode() );
        valuesMap.put("destination", seg.getArrivalAirportCode() );
        valuesMap.put("departureTime", seg.getDepartureDate().format( DateTimeFormatter.ISO_DATE_TIME) );
        valuesMap.put("arrivalTime", seg.getArrivalDate().format(DateTimeFormatter.ISO_DATE_TIME) );
        valuesMap.put("flightTime", seg.getDuration() );
        valuesMap.put("travelTime", seg.getDuration() );
        valuesMap.put("equipment", seg.getAirplaneData());
        valuesMap.put("targetBranch", targetBranchValue);

        sub = new StrSubstitutor(valuesMap);

        header = sub.replace( header );

        return header ;

    }

}
