package com.nearsoft.farandula;

import com.nearsoft.farandula.models.SearchCommand;

import java.time.format.DateTimeFormatter;

/**
 * Created by pruiz on 4/12/17.
 */
public class SabreJSONRequest {

    public static String getRoundTrip(SearchCommand search) {

        return "{\n" +
                " \"OTA_AirLowFareSearchRQ\": {\n" +
                "     \"Target\": \"Production\",\n" +
                "       \"POS\": {\n" +
                "            \"Source\": [{\n" +
                "                \"PseudoCityCode\":\"F9CE\",\n" +
                "                \"RequestorID\": {\n" +
                "                    \"Type\": \"1\",\n" +
                "                  \"ID\": \"1\",\n" +
                "                    \"CompanyName\": {\n" +
                "                        \n" +
                "                  }\n" +
                "             }\n" +
                "         }]\n" +
                "        },\n" +
                "        \"OriginDestinationInformation\": [{\n" +
                "          \"RPH\": \"1\",\n" +
                "           \"DepartureDateTime\": \""+  search.getDepartingDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)  + "\",\n" +
                "           \"OriginLocation\": {\n" +
                "             \"LocationCode\": \"" +  search.getDepartureAirport()  +"\"\n" +
                "         },\n" +
                "            \"DestinationLocation\": {\n" +
                "                \"LocationCode\": \"" + search.getArrivalAirport() + "\"\n" +
                "         },\n" +
                "            \"TPA_Extensions\": {\n" +
                "             \"SegmentType\": {\n" +
                "                    \"Code\": \"O\"\n" +
                "               }\n" +
                "         }\n" +
                "     },\n" +
                "        {\n" +
                "         \"RPH\": \"2\",\n" +
                "           \"DepartureDateTime\": \"" +  search.getReturningDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)   + "\",\n" +
                "           \"OriginLocation\": {\n" +
                "             \"LocationCode\": \"" + search.getArrivalAirport() + "\"\n" +
                "         },\n" +
                "            \"DestinationLocation\": {\n" +
                "                \"LocationCode\": \""+ search.getDepartureAirport() + "\"\n" +
                "         },\n" +
                "            \"TPA_Extensions\": {\n" +
                "             \"SegmentType\": {\n" +
                "                    \"Code\": \"O\"\n" +
                "               }\n" +
                "         }\n" +
                "     }],\n" +
                "       \"TravelPreferences\": {\n" +
                "          \"ValidInterlineTicket\": true,\n" +
                "           \"CabinPref\": [{\n" +
                "             \"Cabin\": \"Y\",\n" +
                "             \"PreferLevel\": \"Preferred\"\n" +
                "            }],\n" +
                "           \"TPA_Extensions\": {\n" +
                "             \"TripType\": {\n" +
                "                   \"Value\": \"Return\"\n" +
                "             },\n" +
                "                \"LongConnectTime\": {\n" +
                "                    \"Min\": 780,\n" +
                "                 \"Max\": 1200,\n" +
                "                    \"Enable\": true\n" +
                "              },\n" +
                "                \"ExcludeCallDirectCarriers\": {\n" +
                "                  \"Enabled\": true\n" +
                "             }\n" +
                "         }\n" +
                "     },\n" +
                "        \"TravelerInfoSummary\": {\n" +
                "            \"SeatsRequested\": [" + search.getPassengers().size() + "],\n" +
                "          \"AirTravelerAvail\": [{\n" +
                "              \"PassengerTypeQuantity\": [{\n" +
                "                 \"Code\": \"ADT\",\n" +
                "                    \"Quantity\": 1\n" +
                "               }]\n" +
                "            }]\n" +
                "        },\n" +
                "        \"TPA_Extensions\": {\n" +
                "         \"IntelliSellTransaction\": {\n" +
                "             \"RequestType\": {\n" +
                "                    \"Name\": \"50ITINS\"\n" +
                "             }\n" +
                "         }\n" +
                "     }\n" +
                " }\n" +
                "}";

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

    private static String getMultiCity(SearchCommand search) {
        return "";
    }

    private static String getOneWay(SearchCommand search) {
        return "";
    }
}
