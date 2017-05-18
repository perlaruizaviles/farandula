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

        for ( int i = 0; i < search.getArrivalAirports().size() ; i++  ){

            valuesMap.put("id", i + 1 );
            valuesMap.put("departureAirport", search.getDepartureAirports().get(i) );
            valuesMap.put("arrivalAirport", search.getArrivalAirports().get(i) );
            valuesMap.put("departureDate", search.getDepartingDates().get(i).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            sub = new StrSubstitutor(valuesMap);
            destinationsInfo += sub.replace( leg ) ;
        }

        if ( search.getType() == FlightType.ROUNDTRIP ){

            valuesMap.put("id", search.getArrivalAirports().size()  + 1 );
            valuesMap.put("departureAirport", search.getArrivalAirports().get( 0 ));
            valuesMap.put("arrivalAirport", search.getDepartureAirports().get(0) );
            valuesMap.put("departureDate", search.getReturningDates().get(0).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            sub = new StrSubstitutor(valuesMap);
            destinationsInfo += "," + sub.replace( leg );

        }

        return destinationsInfo;
    }

    public static String getRequest(SearchCommand search) {

        String result = "";

        //passengers information
        String passengersData = getPassengerDetails( search );
        valuesMap.put("passengersData", passengersData ) ;
        String numberOfSeats = search.getPassengers().size() + "" ;
        if ( search.getPassengersMap().containsKey(PassengerType.INFANTS) ){
            numberOfSeats = String.valueOf(  search.getPassengers().size() - search.getPassengersMap().get(PassengerType.INFANTS).size() );
        }
        valuesMap.put("passengersNumber", numberOfSeats );

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
        //request creation.
        String header  = new BufferedReader(new InputStreamReader(
                SabreJSONRequest.class.getClass().getResourceAsStream("/Sabre/JSON/request/requestHeader.json") ))
                .lines()
                .collect(Collectors.joining("\n") );

        StrSubstitutor sub = new StrSubstitutor(valuesMap);

        result += sub.replace(header);

        result += getDestinationInformation( search );

        result += sub.replace(
                new BufferedReader(new InputStreamReader(
                SabreJSONRequest.class.getClass().getResourceAsStream("/Sabre/JSON/request/requestTail.json") ))
                .lines()
                .collect(Collectors.joining("\n") )
        );

        return result ;

    }


    private static String getPassengerDetails(SearchCommand search) {

        String passengerDetails = "";

        for (Map.Entry<PassengerType, List<Passenger>> entryType : search.getPassengersMap() .entrySet() ){

            passengerDetails = getPassengerObject(  passengerDetails, entryType );

        }

        return passengerDetails;
    }

    private static String getPassengerObject(String passengerDetails, Map.Entry<PassengerType, List<Passenger>> entry) {

        if (passengerDetails.isEmpty()) {
            passengerDetails += "{";
        } else {
            passengerDetails += ",{";
        }

        String seatCode = getPassengerSeatCode( entry.getKey() ) ;

        passengerDetails +=
                "\"Code\": \"" + seatCode + "\"," +
                        "\"Quantity\":" + entry.getValue().size();

        passengerDetails += "}";
        return passengerDetails;
    }

    private static String getPassengerSeatCode(  PassengerType passengerType ) {

        String passengerTypeString = "";
        switch ( passengerType ){
            case ADULTS:
                passengerTypeString="ADT";
                break;
            case CHILDREN:
                passengerTypeString = "CNN";
                break;
            case INFANTS:
                passengerTypeString = "INF";
                break;
            case INFANTSONSEAT:
                passengerTypeString = "INS";
        }

        return passengerTypeString;

    }


}
