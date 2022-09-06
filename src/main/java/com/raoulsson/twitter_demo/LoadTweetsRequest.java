package com.raoulsson.twitter_demo;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class LoadTweetsRequest extends TwitterRequest {

    protected static final String url = "https://api.twitter.com/2/users/:id/tweets";

    public LoadTweetsRequest() throws IOException {
        super();
    }

    protected OAuthType getOAuthType() {
        return OAuthType.OAUTH_1_0_a;
    }

    protected String getUrl() {
        return url.replace(":id", apiAccessMap.get(USER_ID));
    }

    public String executeGet() throws IOException {
        HttpGet httpGet = prepareHttpGet();

        CloseableHttpClient httpClient = prepareClient();
        CloseableHttpResponse response = httpClient.execute(httpGet);

        String text = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

        System.out.println("response = " + text);

        httpClient.close();

        return text;
    }

    /**
     * String json = "{\"text\":\"Hello World again!\"}";
     *
     * @param jsonBody
     * @throws IOException
     */
    public String executePost(String jsonBody) throws IOException {
        throw new RuntimeException("POST not supported");
    }

}
