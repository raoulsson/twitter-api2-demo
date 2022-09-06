package com.raoulsson.twitter_demo;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PostTweetRequest extends TwitterRequest {

    protected static final String url = "https://api.twitter.com/2/tweets";

    public PostTweetRequest() throws IOException {
        super();
    }

    protected OAuthType getOAuthType() {
        return OAuthType.OAUTH_1_0_a_BEARER;
    }

    protected String getUrl() {
        return url;
    }

    public String executeGet() throws IOException {
        throw new RuntimeException("GET not supported");
    }

    /**
     * String json = "{\"text\":\"Hello World again!\"}";
     *
     * @param jsonBody
     * @throws IOException
     */
    public String executePost(String jsonBody) throws IOException {
        HttpPost httpPost = prepareHttpPost();

        StringEntity entity = new StringEntity(jsonBody);
        httpPost.setEntity(entity);

        CloseableHttpClient httpClient = prepareClient();
        CloseableHttpResponse response = httpClient.execute(httpPost);

        String responseBody = new String(response.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);

        httpClient.close();

        return responseBody;
    }

}
