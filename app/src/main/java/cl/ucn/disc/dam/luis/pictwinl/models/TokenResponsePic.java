package cl.ucn.disc.dam.luis.pictwinl.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by Luis on 16-12-2016.
 */

public class TokenResponsePic {
    /**
     * Identificador unico
     */
    @SerializedName("id")
    @Expose
    @Getter
    Long id;

    /**
     * Identificador del dispositivo
     */
    @Getter
    @SerializedName("deviceId")
    @Expose
    String deviceId;

    /**
     * Fecha de la foto
     */
    @Getter
    @SerializedName("date")
    @Expose
    String date;

    /**
     * URL de la foto
     */
    @Getter
    @SerializedName("url")
    @Expose
    String url;

    /**
     * Latitud
     */
    @Getter
    @SerializedName("latitude")
    @Expose
    Double latitude;

    /**
     * Longitud
     */
    @Getter
    @SerializedName("longitude")
    @Expose
    Double longitude;

    /**
     * Numero de likes
     */
    @Getter
    @SerializedName("positive")
    @Expose
    Integer positive;

    /**
     * Numero de dis-likes
     */
    @Getter
    @SerializedName("negative")
    @Expose
    Integer negative;

    /**
     * Numero de warnings
     */
    @Getter
    @SerializedName("warning")
    @Expose
    Integer warning;

    /**
     * archivo imagen
     */
    @Getter
    @SerializedName("file")
    @Expose
    String file;
}
