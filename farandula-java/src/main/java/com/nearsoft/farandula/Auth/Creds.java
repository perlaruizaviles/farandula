package com.nearsoft.farandula.Auth;

public class Creds {

    private final String _clientId;
    private final String _clientSecret;

    public Creds(String clientId, String clientSecret) {
        _clientId = clientId;
        _clientSecret = clientSecret;
    }

    public String getClientId() {
        return _clientId;
    }

    public String getClientSecret() {
        return _clientSecret;
    }
}
