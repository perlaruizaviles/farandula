package com.nearsoft.farandula;

public class AccessToken {
    private final String _accessToken;
    private final String _tokenType;
    private final int _expiresIn;

    public AccessToken(String accessToken, String tokenType, int expiresIn) {
        _accessToken = accessToken;
        _tokenType = tokenType;
        _expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return _accessToken;
    }

    public String getTokenType() {
        if (_tokenType != null && _tokenType.length() > 0) {
            return _tokenType.substring(0, 1).toUpperCase() + _tokenType.substring(1);
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format("expires_in: %s\ntoken_type: %s\naccess_token: %s", _expiresIn, _tokenType, _accessToken);
    }
}
