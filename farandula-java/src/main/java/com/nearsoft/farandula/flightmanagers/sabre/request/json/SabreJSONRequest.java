package com.nearsoft.farandula.flightmanagers.sabre.request.json;

import com.nearsoft.farandula.models.*;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
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

        //passengers information
        String passengersData = getPassengerDetails( search );
        valuesMap.put("passengersData", passengersData ) ;

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

        //request creation.
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


    private static String getPassengerDetails(SearchCommand search) {

        String passengerDetails = "";

        for (Map.Entry<PassengerType, List<Passenger>> entryType : search.getPassengersMap() .entrySet() ){

            if ( entryType.getKey()!= PassengerType.CHILD ) {

                passengerDetails = getPassengerObject(  passengerDetails, entryType.getValue().get(0), entryType.getValue().size() );
            }else{

                //all these can have different ages
                for ( Passenger pass : entryType.getValue()  ){

                    passengerDetails = getPassengerObject( passengerDetails, pass, 1 );
                }

            }

        }

        return passengerDetails;
    }

    private static String getPassengerObject(String passengerDetails, Passenger pass , int count) {

        if (passengerDetails.isEmpty()) {
            passengerDetails += "{";
        } else {
            passengerDetails += ",{";
        }

        String seatCode = getPassengerSeatCode( pass ) ;

        passengerDetails +=
                "\"Code\": \"" + seatCode + "\"," +
                        "\"Quantity\":" + count;

        passengerDetails += "}";
        return passengerDetails;
    }

    private static String getPassengerSeatCode(Passenger passenger) {

        PassengerType passengerType = passenger.getType();
        String passengerTypeString = "";
        switch ( passengerType ){
            case ADULT:
                passengerTypeString="ADT";
                break;
            case CHILD:
                passengerTypeString = "C" + String.format("%02d",  passenger.getAge() );
                break;
            case INFANT:
                passengerTypeString = "INF";
                break;
            case INFANTONSEAT:
                passengerTypeString = "INS";
        }

        return passengerTypeString;

    }


}
