Twitter API V2 Java Impl is badly documented. And so is all. 

Found some 10 year old code in package oauth. 

Pack tokens into: twitter-access.secret

Here a get and post example with auth1.0a and so called "auth1.0a client context". See below how to get the bearer token.

https://twittercommunity.com/t/403-error-when-trying-to-post-to-2-tweets-using-example-in-documentation/165675/6

BarjeshBarry
Apr 13
You may get the bearer token for your account(user account) by following the below link:


developer.twitter.com

Twitter API Documentation 87
Programmatically analyze, learn from, and engage with the conversation on Twitter. Explore Twitter API documentation now.

then click on “Try a live request”
Click on three dots on the right-hand side. it will open up a window.
Click on the “Include access token” button and it will show your bear token in your curl command.

I just found it the hard way, after trying it for three days.

I hope it will help.