package com.nearsoft.farandula.Auth;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.nearsoft.farandula.exceptions.FarandulaException;
import com.nearsoft.farandula.exceptions.ErrorType;
import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AccessManager {

    private static final OkHttpClient authClient = new OkHttpClient();

    private final Creds _creds;

    public AccessManager(Creds creds) {
        _creds = creds;
    }

    public AccessToken getAccessToken() throws FarandulaException {

        final FormBody.Builder formBuilder = new FormBody.Builder().add("grant_type", "client_credentials");
        final Request.Builder requestBuilder = new Request.Builder()                                   //
            .url("https://api.test.sabre.com/v2/auth/token")                                           //
            .header("Content-Type", "application/x-www-form-urlencoded")                               //
            .header("Accept", "application/json")                                                      //
            .header("Authorization", buildCredentials(_creds.getClientId(), _creds.getClientSecret())) //
            .post(formBuilder.build());                                                                //

        try {
            final Response response = authClient.newCall(requestBuilder.build()).execute();
            if (!response.isSuccessful()) {
                throw new FarandulaException(ErrorType.ACCESS_ERROR, "response was not successful");
            }
            final DocumentContext parse = JsonPath.parse(response.body().byteStream());
            return new AccessToken(parse.read("$.access_token"), parse.read("$.token_type"), parse.read("$.expires_in"));
        } catch (IOException e) {
            throw new FarandulaException(e, ErrorType.ACCESS_ERROR, "response error");
        }
    }

    private String buildCredentials(String clientId, String clientSecret) {
        return Credentials.basic(                                        //
            Base64.getEncoder().encodeToString(clientId.getBytes()),     //
            Base64.getEncoder().encodeToString(clientSecret.getBytes()), //
            Charset.defaultCharset());
    }
}
