package com.nearsoft.farandula.flightmanagers.travelport.request.xml;

import com.nearsoft.farandula.flightmanagers.sabre.request.json.SabreJSONRequest;
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

    private static String targetBranch;

    private static Map valuesMap = new HashMap();

    private static String getRoundTrip(SearchCommand search ) {

        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        InputStream soapInputStream = TravelportXMLRequest.class
                .getResourceAsStream("/travelport/XML.request/roundTrip.xml");

        String soapEnvelope = new BufferedReader(new InputStreamReader(soapInputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        return sub.replace(soapEnvelope);

    }


    private static String getMultiCity(SearchCommand search) {
        return "";
    }

    private static String getOneWay(SearchCommand search ) {

        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        InputStream soapInputStream = TravelportXMLRequest.class
                .getResourceAsStream("/travelport/XML.request/oneWayTrip.xml");

        String soapEnvelope = new BufferedReader(new InputStreamReader(soapInputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        return sub.replace(soapEnvelope);

    }

    public static String getRequest(SearchCommand search, String targetBranchValue) {

        targetBranch = targetBranchValue;

        valuesMap.put("departureAirport", search.getDepartureAirport());
        valuesMap.put("arrivalAirport", search.getArrivalAirport());
        valuesMap.put("passengersNumber", search.getPassengers().size());
        valuesMap.put("departureDate", search.getDepartingDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        valuesMap.put("returningDate", search.getReturningDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        valuesMap.put("classTravel",   search.getCabinClass().equals("") ? "Economy" : search.getCabinClass() );
        valuesMap.put("targetBranch", targetBranch);

        String json = "";
        switch ( search.getType().toString().toLowerCase() ) {
            case "roundtrip":
                json = getRoundTrip( search );
                break;
            case "oneway":
                json = getOneWay( search );
                break;
            case "multiple":
                json = getMultiCity( search );
                break;
        }

        return json;

    }

}
