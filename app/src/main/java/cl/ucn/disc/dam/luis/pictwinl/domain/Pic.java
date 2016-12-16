package cl.ucn.disc.dam.luis.pictwinl.domain;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import cl.ucn.disc.dam.luis.pictwinl.Database;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Pic
 *
 * @author Diego P. Urrutia Astorga
 * @version 20161102
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        database = Database.class,
        cachingEnabled = true,
        orderedCursorLookUp = true, // https://github.com/Raizlabs/DBFlow/blob/develop/usage2/Retrieval.md#faster-retrieval
        cacheSize = Database.CACHE_SIZE
)
public class Pic extends BaseModel {

    /**
     * Identificador unico
     */
    @Getter
    @Column
    @PrimaryKey(autoincrement = true)
    Long id;

    /**
     * Identificador del dispositivo
     */
    @Getter
    @Column
    String deviceId;

    /**
     * Fecha de la foto
     */
    @Getter
    @Column
    String date;

    /**
     * URL de la foto
     */
    @Getter
    @Column
    String url;

    /**
     * Latitud
     */
    @Getter
    @Column
    Double latitude;

    /**
     * Longitud
     */
    @Getter
    @Column
    Double longitude;

    /**
     * Numero de likes
     */
    @Getter
    @Column
    Integer positive;

    /**
     * Numero de dis-likes
     */
    @Getter
    @Column
    Integer negative;

    /**
     * Numero de warnings
     */
    @Getter
    @Column
    Integer warning;




}
