import javax.rmi.CORBA.Util;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by pruiz on 4/6/17.
 */
public class ProofConcept {

    public String getDataFromSabre() {

        String result = "";

        try {

            String json = "{\n" +
                    " \"OTA_AirLowFareSearchRQ\": {\n" +
                    "     \"Target\": \"Production\",\n" +
                    "       \"POS\": {\n" +
                    "            \"Source\": [{\n" +
                    "                \"PseudoCityCode\":\"F9CE\",\n" +
                    "                \"RequestorID\": {\n" +
                    "                    \"Type\": \"1\",\n" +
                    "                  \t\"ID\": \"1\",\n" +
                    "                    \"CompanyName\": {\n" +
                    "                        \n" +
                    "                  \t}\n" +
                    "             \t}\n" +
                    "         \t}]\n" +
                    "        },\n" +
                    "        \"OriginDestinationInformation\": [{\n" +
                    "          \"RPH\": \"1\",\n" +
                    "           \"DepartureDateTime\": \"2017-08-07T11:00:00\",\n" +
                    "           \"OriginLocation\": {\n" +
                    "             \"LocationCode\": \"BOS\"\n" +
                    "         },\n" +
                    "            \"DestinationLocation\": {\n" +
                    "                \"LocationCode\": \"MEX\"\n" +
                    "         },\n" +
                    "            \"TPA_Extensions\": {\n" +
                    "             \"SegmentType\": {\n" +
                    "                    \"Code\": \"O\"\n" +
                    "               }\n" +
                    "         }\n" +
                    "     },\n" +
                    "        {\n" +
                    "         \"RPH\": \"2\",\n" +
                    "           \"DepartureDateTime\": \"2017-08-18T11:00:00\",\n" +
                    "           \"OriginLocation\": {\n" +
                    "             \"LocationCode\": \"MEX\"\n" +
                    "         },\n" +
                    "            \"DestinationLocation\": {\n" +
                    "                \"LocationCode\": \"BOS\"\n" +
                    "         },\n" +
                    "            \"TPA_Extensions\": {\n" +
                    "             \"SegmentType\": {\n" +
                    "                    \"Code\": \"O\"\n" +
                    "               }\n" +
                    "         }\n" +
                    "     }],\n" +
                    "        \"TravelerInfoSummary\": {\n" +
                    "            \"SeatsRequested\": [1],\n" +
                    "          \"AirTravelerAvail\": [{\n" +
                    "              \"PassengerTypeQuantity\": [{\n" +
                    "                 \"Code\": \"ADT\",\n" +
                    "                    \"Quantity\": 1\n" +
                    "               }]\n" +
                    "            }]\n" +
                    "        }\n" +
                    "     }\n" +
                    " }\n" +
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
                "&return_date=2017-08-18" +
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
