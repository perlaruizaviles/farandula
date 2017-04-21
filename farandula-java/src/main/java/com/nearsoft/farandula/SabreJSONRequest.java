package com.nearsoft.farandula;

import com.nearsoft.farandula.models.SearchCommand;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by pruiz on 4/12/17.
 */
public class SabreJSONRequest {

    public static String getRoundTrip(SearchCommand search) {

        //TODO in future we can check this using 'handlebars' or another lib, research is required.

        Map valuesMap = new HashMap();
        valuesMap.put("departureAirport",  search.getDepartureAirport()  );
        valuesMap.put("arrivalAirport",  search.getArrivalAirport() );
        valuesMap.put("passengersNumber",  search.getPassengers().size() );
        valuesMap.put("departingDate", search.getDepartingDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) );
        valuesMap.put("returningDate", search.getReturningDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        String jsonFileContent  = new BufferedReader(new InputStreamReader(
                    SabreJSONRequest.class.getClass().getResourceAsStream( "/SabreJSON/request/RoundTrip.json" ) ))
                .lines()
                .collect(Collectors.joining("\n") );

        return sub.replace(jsonFileContent);

    }


    private static String getMultiCity(SearchCommand search) {
        return "";
    }

    private static String getOneWay(SearchCommand search) {
        return "";
    }

    public static String getRequest(SearchCommand search) {

        String json = "";
        switch ( search.getType() ) {
            case "roundTrip":
                json = getRoundTrip( search );
                break;
            case "oneWay":
                json = getOneWay( search );
                break;
            case "multiCity":
                json = getMultiCity( search );
                break;
        }

        return json;

    }

}
