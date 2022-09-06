package com.raoulsson.twitter_demo;

import com.google.common.io.Resources;
import com.raoulsson.ten_years_back.oauth.OAuthPetClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public abstract class TwitterRequest {

    protected static final String USER_ID = "USER_ID";
    protected static final String CONSUMER_KEY = "CONSUMER_KEY";
    protected static final String CONSUMER_SECRET = "CONSUMER_SECRET";
    protected static final String ACCESS_TOKEN_V1 = "ACCESS_TOKEN_V1";
    protected static final String ACCESS_TOKEN_SECRET_V1 = "ACCESS_TOKEN_SECRET_V1";
    protected static final String BEARER_TOKEN = "BEARER_TOKEN";
    protected static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    protected static final String ACCESS_TOKEN_V2 = "ACCESS_TOKEN_V2";
    protected static final String ACCESS_TOKEN_SECRET_V2 = "ACCESS_TOKEN_SECRET_V2";
    protected static Map<String, String> apiAccessMap = new HashMap<>();
    protected static final String KEY_FILE_NAME = "twitter-access.secret";

    enum OAuthType {
        OAUTH_1_0_a, OAUTH_1_0_a_BEARER
    }

    public TwitterRequest() throws IOException {
        loadTwitterAppCredentials();
    }

    protected abstract String getUrl();

    protected abstract OAuthType getOAuthType();

    public abstract String executeGet() throws IOException;

    public abstract String executePost(String jsonBody) throws IOException;

    protected CloseableHttpClient prepareClient() {
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build()).build();
    }

    protected HttpPost prepareHttpPost() {
        HttpPost httpPost = new HttpPost(getUrl());

        httpPost.addHeader("Accept-Encoding", "gzip");
        httpPost.addHeader("User-Agent", "requote-v0.0.1");
        httpPost.addHeader("content-type", "application/json");

        if (getOAuthType() == OAuthType.OAUTH_1_0_a_BEARER) {
            httpPost.addHeader("Authorization", "Bearer " + apiAccessMap.get(BEARER_TOKEN));
        } else if (getOAuthType() == OAuthType.OAUTH_1_0_a) {
            throw new RuntimeException("Not expected");
        }

        return httpPost;
    }

    protected HttpGet prepareHttpGet() {
        HttpGet httpGet = new HttpGet(getUrl());

        httpGet.addHeader("Accept-Encoding", "gzip");
        httpGet.addHeader("User-Agent", "requote-v0.0.1");
        httpGet.addHeader("content-type", "application/json");

        if (getOAuthType() == OAuthType.OAUTH_1_0_a_BEARER) {

        } else if (getOAuthType() == OAuthType.OAUTH_1_0_a) {
            OAuthPetClient oAuthPetClient = new OAuthPetClient();
            String authHeader = oAuthPetClient.computeOAuthHeader(getUrl(),
                    apiAccessMap.get(CONSUMER_KEY),
                    apiAccessMap.get(CONSUMER_SECRET),
                    apiAccessMap.get(ACCESS_TOKEN_V1),
                    apiAccessMap.get(ACCESS_TOKEN_SECRET_V1));

            System.out.println(authHeader);

            httpGet.addHeader("Authorization", authHeader);
        }

        return httpGet;
    }

    private static void loadTwitterAppCredentials() {
        apiAccessMap = new HashMap<>();
        try (Stream<String> lines = Files.lines(Paths.get(Resources.getResource(KEY_FILE_NAME).toURI()))) {
            lines.filter(line -> line.contains("="))
                    .forEach(line -> {
                        String[] keyValuePair = line.split("=", 2);
                        String key = keyValuePair[0];
                        String value = keyValuePair[1];
                        apiAccessMap.put(key, value);
                    });
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
