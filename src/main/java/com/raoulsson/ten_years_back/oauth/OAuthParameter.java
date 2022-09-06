package com.raoulsson.ten_years_back.oauth;

import com.google.api.client.util.escape.PercentEscaper;

import java.util.Map;

/**
 * User: raoulsson
 * Date: 2013-08-09
 */
public class OAuthParameter implements Map.Entry<String, String>, Comparable<OAuthParameter> {

    PercentEscaper percentEscaper = new PercentEscaper("-._~", false);

    public OAuthParameter(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private final String key;

    private String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String setValue(String value) {
        try {
            return this.value;
        } finally {
            this.value = value;
        }
    }

    @Override
    public String toString() {
        return getKey() + '=' + getValue();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final OAuthParameter that = (OAuthParameter) obj;
        if (key == null) {
            if (that.key != null)
                return false;
        } else if (!key.equals(that.key))
            return false;
        if (value == null) {
            if (that.value != null)
                return false;
        } else if (!value.equals(that.value))
            return false;
        return true;
    }

    @Override
    public int compareTo(OAuthParameter other) {
        if (other == null) {
            return 1;
        }
        return this.key.compareTo(other.key);
    }
}

