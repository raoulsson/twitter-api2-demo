package com.raoulsson.ten_years_back.oauth;

import com.google.api.client.util.escape.PercentEscaper;

import java.security.GeneralSecurityException;
import java.util.Iterator;
import java.util.List;

/**
 * User: raoulsson
 * Date: 2013-08-09
 */
public class OAuthHeaderBuilder {

    private OAuthSignatureBuilder oAuthSignatureBuilder = new OAuthSignatureBuilder();
    private PercentEscaper percentEscaper = new PercentEscaper("-._~", false);

    public String buildHeader(String httpMethod, String url, String consumerKey, String consumerSecret, String accessToken, String accessSecret) throws GeneralSecurityException {
        final List<OAuthParameter> params = oAuthSignatureBuilder.getSortedOAuthParameters(consumerKey, accessToken);

        oAuthSignatureBuilder.addUrlParameters(url, params);

        String signature = oAuthSignatureBuilder.sign(httpMethod, url, consumerSecret, accessSecret, params);

        String authorizationHeader = computeAuthorizationHeader(params, signature);

        return authorizationHeader;
    }

    protected String computeAuthorizationHeader(List<OAuthParameter> params, String signature) {
        StringBuilder authorizationHeader = new StringBuilder();
        authorizationHeader.append("OAuth ");
        Iterator<OAuthParameter> iterator = params.iterator();
        while (iterator.hasNext()) {
            OAuthParameter entry = iterator.next();
            if (entry.getKey().startsWith("oauth_")) {
                authorizationHeader.append(percentEscaper.escape(entry.getKey()));
                authorizationHeader.append("=\"");
                authorizationHeader.append(percentEscaper.escape(entry.getValue()));
                authorizationHeader.append("\", ");
            }
        }
        authorizationHeader.append("oauth_signature=\"");
        authorizationHeader.append(percentEscaper.escape(signature));
        authorizationHeader.append("\"");
        return authorizationHeader.toString();
    }

    public void setOAuthSignatureBuilder(OAuthSignatureBuilder oAuthSignatureBuilder) {
        this.oAuthSignatureBuilder = oAuthSignatureBuilder;
    }

    public String escapeUrlParamsUTF8(String url) {
        StringBuffer sb = new StringBuffer();
        if(url.contains("?")) {
            sb.append(url.substring(0, url.indexOf("?")));
            sb.append("?");
            String urlParamString = url.substring(url.indexOf("?") + 1, url.length());
            String[] pairs = urlParamString.split("&");
            for(int i = 0; i < pairs.length; i++) {
                String[] parts = pairs[i].split("=");
                sb.append(percentEscaper.escape(parts[0]));
                sb.append("=");
                if(parts.length > 1) {
                    sb.append(percentEscaper.escape(parts[1]));
                }
                sb.append("&");
            }
            String result = sb.toString();
            return result.substring(0, result.length() - 1);
        }
        return url;
    }
}
