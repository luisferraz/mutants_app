
package br.ufpr.tads.luis.mutantsapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tokens implements Serializable {

    @SerializedName("accessToken")
    @Expose
    private String accessToken;
    @SerializedName("refreshToken")
    @Expose
    private String refreshToken;
    private final static long serialVersionUID = -6600996849738169626L;

    /**
     * No args constructor for use in serialization
     */
    public Tokens() {
    }

    /**
     * @param accessToken
     * @param refreshToken
     */
    public Tokens(String accessToken, String refreshToken) {
        super();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Tokens.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("accessToken");
        sb.append('=');
        sb.append(((this.accessToken == null) ? "<null>" : this.accessToken));
        sb.append(',');
        sb.append("refreshToken");
        sb.append('=');
        sb.append(((this.refreshToken == null) ? "<null>" : this.refreshToken));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
