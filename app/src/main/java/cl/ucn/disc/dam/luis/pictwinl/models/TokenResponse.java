package cl.ucn.disc.dam.luis.pictwinl.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by Luis on 15-12-2016.
 */

public class TokenResponse   {
    @SerializedName("access_token")
    @Expose
    @Getter
    private String access_token;
    @SerializedName("token_type")
    @Expose
    @Getter
    private String token_type;
    @SerializedName("expires_in")
    @Expose
    @Getter
    private int expires_in;
    @SerializedName("refresh_token")
    @Expose
    @Getter
    private String refresh_token;
}
