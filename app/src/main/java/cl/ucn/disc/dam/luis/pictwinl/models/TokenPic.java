package cl.ucn.disc.dam.luis.pictwinl.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by Luis on 15-12-2016.
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenPic {
    /**
     * Identificador unico
     */
    @Getter
    @Setter
    Long id;

    /**
     * Identificador del dispositivo
     */
    @Getter
    @Setter
    String deviceId;

    /**
     * Fecha de la foto
     */
    @Getter
    @Setter
    String date;

    /**
     * URL de la foto
     */
    @Getter
    @Setter
    String url;

    /**
     * Latitud
     */
    @Getter
    @Setter
    Double latitude;

    /**
     * Longitud
     */
    @Getter
    @Setter
    Double longitude;

    /**
     * Numero de likes
     */
    @Getter
    @Setter
    Integer positive;

    /**
     * Numero de dis-likes
     */
    @Getter
    @Setter
    Integer negative;

    /**
     * Numero de warnings
     */
    @Getter
    @Setter
    Integer warning;

    /**
     * archivo imagen
     */
    @Getter
    @Setter
    String file;
}
