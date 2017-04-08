/**
 * Created by pruiz on 4/6/17.
 */


//import sun.misc.BASE64Encoder;
import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.regex.*;
//import java.util.HashMap;


public class Main {


        public static void main(String[] args) {

                String kClientKey = "V1:6ixbash6wgsu7g9r:DEVCENTER:EXT";
                String kClientSecret = "RH0m4mCj";
                String kAuthToken = getAuthToken( kClientKey, kClientSecret );

                System.out.println("Auth token: " + kAuthToken);

                String URL_FLIGHTS = "https://api.test.sabre.com/v1/shop/flights?" +
                        "origin=JFK&destination=LAX" +
                        "&departuredate=2017-07-07" +
                        "&returndate=2017-07-08" +
                        "&onlineitinerariesonly=N" +
                        "&limit=10&offset=1" +
                        "&eticketsonly=N" +
                        "&sortby=totalfare" +
                        "&order=asc&sortby2=departuretime" +
                        "&order2=asc" +
                        "&pointofsalecountry=US" +
                        "&passengercount=1";

                String destinations = testGetDestinations( URL_FLIGHTS, kAuthToken );
                System.out.println("Destinations:");

                System.out.println(prettyFormatJSONString(destinations));
        }

        public static String getAuthToken( String kClientKey, String kClientSecret ) {

                Base64.Encoder base64 = Base64.getEncoder();
                String encodedClientKey = base64.encodeToString((kClientKey).getBytes());
                String encodedClientSecret = base64.encodeToString((kClientSecret).getBytes());

                // Convert the encoded concatenated string to a single base64 encoded string, as per Sabre's docs
                String authTokenParam = base64.encodeToString((encodedClientKey + ":" + encodedClientSecret).getBytes());

                try {

                        URL url = new URL("https://api.test.sabre.com/v2/auth/token");
                        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                        // Value we created above is used here in the Authorization header
                        connection.setRequestProperty("Authorization", "Basic " + authTokenParam);
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        connection.setRequestProperty("Accept", "application/json");
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setUseCaches(false);
                        // Make token request
                        DataOutputStream dataOut = new DataOutputStream(connection.getOutputStream());
                        dataOut.writeBytes("grant_type=client_credentials");
                        dataOut.flush();
                        dataOut.close();
                        // Get the JSON as a string
                        String response = stringFromConnection(connection);
                        // Extract the auth token with regex
                        Pattern pattern = Pattern.compile("\\{\"access_token\":\"([\\d\\w+*//*]+)\"");
                        Matcher matcher = pattern.matcher(response);
                        if (matcher.find()) {
                                return matcher.group(1);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }

                return null;
        }

        public static String testGetDestinations( String url_api, String authToken ) {
                try {
                        URL url = new URL( url_api );
                        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                        connection.setRequestProperty("Authorization", "Bearer " + authToken);
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        connection.setRequestProperty("Accept", "application/json");
                        return stringFromConnection(connection);
                } catch (Exception e) { }

                return null;
        }

        // Gets the response from an HTTP connection as a String
        private static String stringFromConnection(HttpURLConnection connection) {
                try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String chunk;
                        while ((chunk = reader.readLine()) != null) {
                                sb.append(chunk);
                        }

                        return sb.toString();

                } catch (Exception e) {

                }

                return null;
        }

        // Formats JSON strings to make them more readable
        private static String prettyFormatJSONString(String json) {
                json = json.replace('{', '\n');
                json = json.replace(',', '\n');
                json = json.replace('}', '\n');
                return json;
        }

        // Unused at the moment
        private class Destination {
                String location;
                String airport;
                String city;
                String countryCode;
                String countryName;
                String region;
                String type;
                Destination() {
                }
        }

}
