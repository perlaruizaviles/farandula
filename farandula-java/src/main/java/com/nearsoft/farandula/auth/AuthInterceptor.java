package com.nearsoft.farandula.auth;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final AccessToken _token;

    public AuthInterceptor(AccessToken accessToken) {
        this._token = accessToken;
    }

    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        // Request customization: add request headers
        Request.Builder requestBuilder = original.newBuilder() //
            .header("Accept", "application/json")              //
            .header("Authorization", String.format("%s %s", _token.getTokenType(), _token.getAccessToken()));

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}