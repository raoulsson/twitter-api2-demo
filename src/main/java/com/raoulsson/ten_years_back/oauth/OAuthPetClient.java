package com.raoulsson.ten_years_back.oauth;

import com.google.api.client.util.escape.PercentEscaper;

import java.security.GeneralSecurityException;

/**
 * Facade class for documentation purposes. This is how you use the rest.
 *
 * User: raoulsson
 * Date: 2013-10-19
 */
public class OAuthPetClient {

    private final OAuthHeaderBuilder oAuthHeaderBuilder = new OAuthHeaderBuilder();
    private final PercentEscaper percentEscaper = new PercentEscaper("-._~", false);

    private String method = "GET";
    private String consumerToken;
    private String consumerSecret;
    private String accessToken;
    private String accessSecret;

    public OAuthPetClient() {
        this(null, null, null, null);
    }

    public OAuthPetClient(String consumerToken, String consumerSecret, String accessToken, String accessSecret) {
        this.consumerToken = consumerToken;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessSecret = accessSecret;
    }

    public String computeOAuthHeader(String url,
                                   String consumerToken,
                                   String consumerSecret,
                                   String accessToken,
                                   String accessSecret) {
        if (url == null || consumerToken == null || consumerSecret == null || accessToken == null || accessSecret == null) {
            throw new RuntimeException("None of the parameters can be null. Set via constructor or use overloaded method.");
        }
        try {
            return oAuthHeaderBuilder.buildHeader(method, url, consumerToken, consumerSecret, accessToken, accessSecret);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public String escape(String s) {
        return percentEscaper.escape(s);
    }

    public String computeOAuthHeader(String url) {
        return computeOAuthHeader(url, this.consumerToken, this.consumerSecret, this.accessToken, this.accessSecret);
    }

    public void setConsumerToken(String consumerToken) {
        this.consumerToken = consumerToken;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }
}
