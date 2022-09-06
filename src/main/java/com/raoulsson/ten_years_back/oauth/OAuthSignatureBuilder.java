package com.raoulsson.ten_years_back.oauth;

import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.util.escape.PercentEscaper;

import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * User: raoulsson
 * Date: 2013-08-09
 */
public class OAuthSignatureBuilder {

    private IDataProvider dataProvider = new DataProvider();
    private final PercentEscaper percentEscaper = new PercentEscaper("-._~", false);

    public static final String SIGNATURE_METHOD = "HMAC-SHA1";
    public static final String VERSION = "1.0";

    private final OAuthHmacSigner oAuthHmacSigner = new OAuthHmacSigner();

    public String sign(String httpMethod, String url, String consumerSecret, String accessSecret, List<OAuthParameter> params) throws GeneralSecurityException {

        String parameterString = computeParameterString(params);
//        System.out.println("baseString = " + parameterString);

        String signatureBaseString = computeSignatureBaseString(httpMethod, stripOffUrlParams(url), parameterString);
//        System.out.println("signatureBaseString = " + signatureBaseString);

        String signature = calculateHmacSha1(consumerSecret, accessSecret, signatureBaseString);
//        System.out.println("signature = " + signature);

//        System.out.println(toLogString(httpMethod, url, consumerSecret, accessSecret, params));

        return signature;
    }

    protected String stripOffUrlParams(String url) {
        if(url.contains("?")) {
            return url.substring(0, url.indexOf("?"));
        }
        return url;
    }

    protected String calculateHmacSha1(String consumerSecret, String accessSecret, String signatureBaseString) throws GeneralSecurityException {
        oAuthHmacSigner.clientSharedSecret = consumerSecret;
        oAuthHmacSigner.tokenSharedSecret = accessSecret;
        return oAuthHmacSigner.computeSignature(signatureBaseString);
    }

    protected String computeSignatureBaseString(String httpMethod, String url, String parameterString) {
        String signatureBaseString = httpMethod.toUpperCase() +
                "&" +
                percentEscaper.escape(url) +
                "&" +
//        System.out.println("vorher  = " + parameterString);
//        System.out.println("nachher = " + percentEscaper.escape(parameterString));
                percentEscaper.escape(parameterString);
        return signatureBaseString;
    }

    protected String computeParameterString(List<OAuthParameter> params) {
        StringBuilder baseString = new StringBuilder("");
        for (OAuthParameter entry : params) {
            //            entry.setValue(percentEscaper.escape(entry.getValue()));
            entry.setValue(entry.getValue());
            baseString.append(entry.getKey());
            baseString.append("=");
            baseString.append(entry.getValue());
            baseString.append("&");
        }
        return baseString.substring(0, baseString.length() -1);
    }

    public List<OAuthParameter> getSortedOAuthParameters(String consumerKey, String accessToken) {
        String nonce = dataProvider.getNonce();
        String timestamp = dataProvider.getTimestamp() + "";
        return getSortedOAuthParameters(consumerKey, accessToken, nonce, timestamp);
    }

    public List<OAuthParameter> getSortedOAuthParameters(String consumerKey, String accessToken, String nonce, String timestamp) {
        final List<OAuthParameter> params = new LinkedList<>();
        params.add(new OAuthParameter("oauth_signature_method", SIGNATURE_METHOD));
        params.add(new OAuthParameter("oauth_consumer_key", consumerKey));
        params.add(new OAuthParameter("oauth_nonce", nonce));
        params.add(new OAuthParameter("oauth_timestamp", timestamp));
        params.add(new OAuthParameter("oauth_token", accessToken));
        params.add(new OAuthParameter("oauth_version", VERSION));

        Collections.sort(params);
        return params;
    }

    private String toLogString(String httpMethod, String url, String consumerSecret, String accessSecret, List<OAuthParameter> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("OAuthData [\n");
        sb.append("httpMethod=\"");
        sb.append(httpMethod);
        sb.append("\", \n");
        sb.append("url=\"");
        sb.append(url);
        sb.append("\", \n");
        sb.append("consumerSecret=\"");
        sb.append(consumerSecret);
        sb.append("\", \n");
        sb.append("accessSecret=\"");
        sb.append(accessSecret);
        sb.append("\", \n");
        for (OAuthParameter entry : params) {
            sb.append(entry.getKey());
            sb.append("=\"");
            sb.append(entry.getValue());
            sb.append("\", \n");
        }
        sb.append("]");
        return sb.toString();
    }

    public void setDataProvider(IDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public void addUrlParameters(String url, List<OAuthParameter> params) {
        if(url.contains("?")) {
            String urlParamString = url.substring(url.indexOf("?") + 1);
            String[] pairs = urlParamString.split("&");
            for (String pair : pairs) {
                String[] entry = pair.split("=");
                String key = entry[0];
                String value = "";
                if (entry.length > 1) {
                    value = entry[1];
                }
                params.add(new OAuthParameter(key, value));
            }
            Collections.sort(params);
        }
    }
}
