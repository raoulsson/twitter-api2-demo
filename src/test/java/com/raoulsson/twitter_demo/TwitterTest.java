package com.raoulsson.twitter_demo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class TwitterTest {

    @Test
    @Disabled // Let's not create more. It works
    public void createTweetTest() throws IOException {
        String jsonBody = "{\"text\":\"They already checked my aliveness...\"}";

        PostTweetRequest postTweetRequest = new PostTweetRequest();
        String responseBody = postTweetRequest.executePost(jsonBody);

        System.out.println("responseBody = " + responseBody);
    }

    @Test
    public void listTweetsTest() throws IOException {
        LoadTweetsRequest loadTweetsRequest = new LoadTweetsRequest();
        String responseBody = loadTweetsRequest.executeGet();

        System.out.println("responseBody = " + responseBody);
    }

}
