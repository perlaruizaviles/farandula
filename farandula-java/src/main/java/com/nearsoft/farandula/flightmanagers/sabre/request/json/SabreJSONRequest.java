package com.nearsoft.farandula.flightmanagers.sabre.request.json;

import com.nearsoft.farandula.flightmanagers.travelport.request.xml.TravelportXMLRequest;
import com.nearsoft.farandula.models.CabinClassType;
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
 * Created by pruiz on 4/12/17.
 */
public class SabreJSONRequest {

    private static Map valuesMap = new HashMap();
    private static StrSubstitutor sub;

    private static String getDestinationInformation(SearchCommand search) {
        InputStream airLegInputStream = SabreJSONRequest.class
                .getResourceAsStream("/Sabre/JSON/request/requestDestinationInformation.json");
        String leg = new BufferedReader(new InputStreamReader(airLegInputStream))
                .lines()
                .collect(Collectors.joining("\n"));

        String destinationsInfo = "";

        valuesMap.put("id",1 );
        valuesMap.put("departureAirport", search.getDepartureAirport() );
        valuesMap.put("arrivalAirport", search.getArrivalAirport() );
        valuesMap.put("departureDate", search.getDepartingDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        sub = new StrSubstitutor(valuesMap);
        destinationsInfo += sub.replace( leg ) ;

        if ( search.getType() == FlightType.ROUNDTRIP ){

            valuesMap.put("id",2 );
            valuesMap.put("departureAirport", search.getArrivalAirport());
            valuesMap.put("arrivalAirport", search.getDepartureAirport() );
            valuesMap.put("departureDate", search.getReturningDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            sub = new StrSubstitutor(valuesMap);
            destinationsInfo += "," + sub.replace( leg );

        }

        return destinationsInfo;
    }

    public static String getRequest(SearchCommand search) {

        //TODO in future we can check this using 'handlebars' or another lib, research is required.

        valuesMap.put("departureAirport", search.getDepartureAirport());
        valuesMap.put("arrivalAirport", search.getArrivalAirport());
        valuesMap.put("passengersNumber", search.getPassengers().size());
        valuesMap.put("departingDate", search.getDepartingDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        valuesMap.put("returningDate", search.getReturningDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        String classTravel = "";
        switch ( search.getCabinClass() ){
            case ECONOMY :
                classTravel="Y";
                break;
            case FIRST :
                classTravel = "F";
                break;
            case OTHER:
                classTravel = "Y";
        }

        valuesMap.put("classTravel", classTravel);
        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        String header  = new BufferedReader(new InputStreamReader(
                SabreJSONRequest.class.getClass().getResourceAsStream("/Sabre/JSON/request/requestHeader.json") ))
                .lines()
                .collect(Collectors.joining("\n") );

        String destinationInfo = getDestinationInformation( search );

        String tail  = new BufferedReader(new InputStreamReader(
                SabreJSONRequest.class.getClass().getResourceAsStream("/Sabre/JSON/request/requestTail.json") ))
                .lines()
                .collect(Collectors.joining("\n") );


        return sub.replace(header +  destinationInfo + tail );


    }

}
