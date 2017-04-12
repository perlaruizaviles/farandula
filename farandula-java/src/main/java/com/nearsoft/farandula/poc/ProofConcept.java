package com.nearsoft.farandula.poc;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by pruiz on 4/6/17.
 */
public class ProofConcept {

    public String getDataFromSabre() {

        String result = "";

        try {
            
            String json = "{\n" + "\n" + " \"OTA_AirLowFareSearchRQ\": {\n" + "\n" + "     \"Target\": \"Production\",\n" + "\n" + "       \"POS\": {\n" + "\n" +
                    "            \"Source\": [{\n" + "\n" + "                \"PseudoCityCode\":\"F9CE\",\n" + "\n" + "                \"RequestorID\": {\n" +
                    "\n" + "                    \"Type\": \"1\",\n" + "\n" + "                  \"ID\": \"1\",\n" + "\n" +
                    "                    \"CompanyName\": {\n" + "\n" + "                        \n" + "\n" + "                  }\n" + "\n" +
                    "             }\n" + "\n" + "         }]\n" + "\n" + "        },\n" + "\n" + "        \"OriginDestinationInformation\": [{\n" + "\n" +
                    "          \"RPH\": \"1\",\n" + "\n" + "           \"DepartureDateTime\": \"2017-08-07T11:00:00\",\n" + "\n" +
                    "           \"OriginLocation\": {\n" + "\n" + "             \"LocationCode\": \"MEX\"\n" + "\n" + "         },\n" + "\n" +
                    "            \"DestinationLocation\": {\n" + "\n" + "                \"LocationCode\": \"BOS\"\n" + "\n" + "         },\n" + "\n" +
                    "            \"TPA_Extensions\": {\n" + "\n" + "             \"SegmentType\": {\n" + "\n" + "                    \"Code\": \"O\"\n" + "\n" +
                    "               }\n" + "\n" + "         }\n" + "\n" + "     },\n" + "\n" + "        {\n" + "\n" + "         \"RPH\": \"2\",\n" + "\n" +
                    "           \"DepartureDateTime\": \"2017-08-08T11:00:00\",\n" + "\n" + "           \"OriginLocation\": {\n" + "\n" +
                    "             \"LocationCode\": \"BOS\"\n" + "\n" + "         },\n" + "\n" + "            \"DestinationLocation\": {\n" + "\n" +
                    "                \"LocationCode\": \"MEX\"\n" + "\n" + "         },\n" + "\n" + "            \"TPA_Extensions\": {\n" + "\n" +
                    "             \"SegmentType\": {\n" + "\n" + "                    \"Code\": \"O\"\n" + "\n" + "               }\n" + "\n" + "         }\n" +
                    "\n" + "     }],\n" + "\n" + "       \"TravelPreferences\": {\n" + "\n" + "          \"ValidInterlineTicket\": true,\n" + "\n" +
                    "           \"CabinPref\": [{\n" + "\n" + "             \"Cabin\": \"Y\",\n" + "\n" + "             \"PreferLevel\": \"Preferred\"\n" + "\n" +
                    "            }],\n" + "\n" + "           \"TPA_Extensions\": {\n" + "\n" + "             \"TripType\": {\n" + "\n" +
                    "                   \"Value\": \"Return\"\n" + "\n" + "             },\n" + "\n" + "                \"LongConnectTime\": {\n" + "\n" +
                    "                    \"Min\": 780,\n" + "\n" + "                 \"Max\": 1200,\n" + "\n" + "                    \"Enable\": true\n" + "\n" +
                    "              },\n" + "\n" + "                \"ExcludeCallDirectCarriers\": {\n" + "\n" + "                  \"Enabled\": true\n" + "\n" +
                    "             }\n" + "\n" + "         }\n" + "\n" + "     },\n" + "\n" + "        \"TravelerInfoSummary\": {\n" + "\n" +
                    "            \"SeatsRequested\": [1],\n" + "\n" + "          \"AirTravelerAvail\": [{\n" + "\n" +
                    "              \"PassengerTypeQuantity\": [{\n" + "\n" + "                 \"Code\": \"ADT\",\n" + "\n" +
                    "                    \"Quantity\": 1\n" + "\n" + "               }]\n" + "\n" + "            }]\n" + "\n" + "        },\n" + "\n" +
                    "        \"TPA_Extensions\": {\n" + "\n" + "         \"IntelliSellTransaction\": {\n" + "\n" + "             \"RequestType\": {\n" + "\n" +
                    "                    \"Name\": \"50ITINS\"\n" + "\n" + "             }\n" + "\n" + "         }\n" + "\n" + "     }\n" + "\n" + " }\n" + "\n" +
                    "}";
            
            URL url = new URL("https://api.test.sabre.com/v3.1.0/shop/flights?mode=live&limit=50&offset=1\n");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // --------- credentials
            String kClientKey = "V1:6ixbash6wgsu7g9r:DEVCENTER:EXT";
            String kClientSecret = "RH0m4mCj";
            String authToken = AuthTokenEncoder.getAuthToken( kClientKey, kClientSecret );
            conn.setRequestProperty("Authorization", "Bearer " + authToken);
            // --------- credentials

            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            result = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n") );
            result = prettyFormatJSONString( result );

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    public String getDataFromAmadeus(){

        // --------- credentials
        String key = "R6gZSs2rk3s39GPUWG3IFubpEGAvUVUA";
        // --------- credentials

        String api = "https://api.sandbox.amadeus.com/v1.2/flights/low-fare-search?";
        //api = "https://api.sandbox.amadeus.com/v1.2/flights/inspiration-search?";
        //api = "https://api.sandbox.amadeus.com/v1.2/flights/extensive-search?";
        //api = "https://api.sandbox.amadeus.com/v1.2/flights/inspiration-search?";

        String url_api = api +
                "apikey=" + key +
                "&origin=MEX" +
                "&destination=LON" +
                "&departure_date=2017-08-07" +
                "&return_date=2017-08-08" +
                "&adults=1";

        String result = "";
        try {

            URL url = new URL( url_api );
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            result = new BufferedReader(new InputStreamReader(connection.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n") );
            result = prettyFormatJSONString( result );

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;


    }

    // Formats JSON strings to make them more readable
    private static String prettyFormatJSONString(String json) {
        json = json.replace('{', '\n');
        json = json.replace(',', '\n');
        json = json.replace('}', '\n');
        return json;
    }

    public static  void main( String[] args) throws IOException {

        Date today = new Date();

        BufferedWriter writer = new BufferedWriter(
                new FileWriter( new File(  today.toString() + " sabre-file.json")  ) );

        ProofConcept poc = new ProofConcept();

        String response =  poc.getDataFromSabre();

        writer.write( "Response Sabre:"  + response );

        writer.close();

        writer = new BufferedWriter(
                new FileWriter( new File(  today.toString() + " amadeus-file.json")  ) );

        response =  poc.getDataFromAmadeus();

        writer.write( "Response Amadeus:"  + response );

        writer.close();

    }

}
