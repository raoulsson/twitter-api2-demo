package com.raoulsson.ten_years_back.oauth;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * User: raoulsson
 * Date: 2013-08-09
 */
public class OAuthSignatureValidation {

    private final OAuthSignatureBuilder oAuthSignatureBuilder = new OAuthSignatureBuilder();

    private String consumerSecret = "";
    private String accessSecret = "";

    /**
     * Missing validation for:
     * <p/>
     * - nonce reuse
     * - timestamp within 5 minutes
     * - httpMethod must be GET
     * - oauth version
     * - oauth method
     * - no extra oauth fields
     *
     * @param timestamp
     * @param nonce
     * @param accessToken
     * @param consumerKey
     * @param oauth_signature
     * @param host
     * @param httpMethod
     * @param uri
     * @return
     * @throws GeneralSecurityException
     */
    protected boolean validatePercentDecoded(String timestamp, String nonce, String accessToken, String consumerKey, String oauth_signature, String host, String httpMethod, String uri) throws GeneralSecurityException {
        final List<OAuthParameter> params = oAuthSignatureBuilder.getSortedOAuthParameters(consumerKey, accessToken, nonce, timestamp);
        oAuthSignatureBuilder.addUrlParameters(uri, params);
        String signature = oAuthSignatureBuilder.sign(httpMethod, host, consumerSecret, accessSecret, params);
        System.out.println("oauth_signature = " + oauth_signature);
        System.out.println("signature       = " + signature);
        if (signature.equals(oauth_signature)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean validate(String timestamp, String nonce, String accessToken, String consumerKey, String oauth_signature, String host, String httpMethod, String uri) throws GeneralSecurityException, UnsupportedEncodingException {
        return validatePercentDecoded(decodeUrl(timestamp), decodeUrl(nonce), decodeUrl(accessToken), decodeUrl(consumerKey), decodeUrl(oauth_signature), decodeUrl(host), decodeUrl(httpMethod), uri);
    }

    public String decodeUrl(String url) throws UnsupportedEncodingException {
//        System.out.println(url);
        return java.net.URLDecoder.decode(url, "UTF-8");
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

}
