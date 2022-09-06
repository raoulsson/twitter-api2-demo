### Twitter API2 Demo

The "Twitter API V2" for Java is *at least* badly documented. And so is all. How to get OAuth right? Different for almost every endpoint. Getting a working Bearer Token for **OAuth1.0a Client Context**? No, not the one from the Developer Portal!

I suffered and then remembered I had written some code ten years back, in 2013, to do OAuth1.0b between two servers. Adapted the code tonight to work with twitter. I will need this for a fun project [(miniraoul-requote)](https://twitter.com/miniraoul).

LoadTweetsRequest needs a different OAuth method than PostTweetRequest. This should get you going. 

Pack tokens into: *src/main/java/resources/twitter-access.secret*

---

[Go to the API "complaining" list](https://twittercommunity.com/t/403-error-when-trying-to-post-to-2-tweets-using-example-in-documentation/165675/6) to get the best hint ever on how to get a working bearer token without  and post example with auth1.0a and so called "auth1.0a client context". See the hassle. Just to test first. You can then build the token request workflow later, if you need to work **on behalf of** your users.


>@BarjeshBarry
>Apr 13
>You may get the bearer token for your account(user account) by following the below link:
>
>developer.twitter.com
>
>Twitter API Documentation 87
>Programmatically analyze, learn from, and engage with the conversation on Twitter. Explore Twitter API documentation now.
>
>then click on “Try a live request”
>Click on three dots on the right-hand side. it will open up a window.
>Click on the “Include access token” button and it will show your bear token in 
>your curl command.
>
>I just found it the hard way, after trying it for three days.
>
>I hope it will help.