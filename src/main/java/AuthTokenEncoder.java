import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by pruiz on 4/6/17.
 */
@SuppressWarnings("ALL")
public class AuthTokenEncoder {

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
            String response =  new BufferedReader(new InputStreamReader(connection.getInputStream()))
                                .lines()
                                .collect(Collectors.joining("\n") );

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

}
