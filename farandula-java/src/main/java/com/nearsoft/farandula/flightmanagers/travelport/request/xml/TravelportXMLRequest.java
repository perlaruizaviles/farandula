package com.nearsoft.farandula.flightmanagers.travelport.request.xml;

import com.nearsoft.farandula.flightmanagers.sabre.request.json.SabreJSONRequest;
import com.nearsoft.farandula.models.FlightType;
import com.nearsoft.farandula.models.SearchCommand;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by pruiz on 5/4/17.
 */
public class TravelportXMLRequest {

    private static Map valuesMap = new HashMap();

    private static StrSubstitutor sub;

    public static String getRequest(SearchCommand search, String targetBranchValue) {

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

        soapInputStream = TravelportXMLRequest.class
                .getResourceAsStream("/travelport/XML.request/requestTail.xml");
        String tail = new BufferedReader(new InputStreamReader(soapInputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        return header + xml + tail;

    }

    private static String getSearchAirLegs(SearchCommand search) {

        InputStream airLegInputStream = TravelportXMLRequest.class
                .getResourceAsStream("/travelport/XML.request/requestSearchAirLeg.xml");
        String leg = new BufferedReader(new InputStreamReader(airLegInputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        String airlegs = "";

        valuesMap.put("departureAirport", search.getDepartureAirport() );
        valuesMap.put("arrivalAirport", search.getArrivalAirport() );
        valuesMap.put("departureDate", search.getDepartingDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        sub = new StrSubstitutor(valuesMap);
        airlegs += sub.replace( leg );

        if ( search.getType() == FlightType.ROUNDTRIP ){

            valuesMap.put("departureAirport", search.getArrivalAirport());
            valuesMap.put("arrivalAirport", search.getDepartureAirport() );
            valuesMap.put("departureDate", search.getReturningDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
            sub = new StrSubstitutor(valuesMap);
            airlegs += sub.replace( leg );

        }

        return airlegs;
    }

}
