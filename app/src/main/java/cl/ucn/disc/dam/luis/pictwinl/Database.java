package cl.ucn.disc.dam.luis.pictwinl;


/**
 * Created by Luis on 10-11-2016.
 */
@com.raizlabs.android.dbflow.annotation.Database(name = Database.NAME, version = Database.VERSION)
public class Database {
    /**
     * Key de la base de datos
     */
    public static final String NAME = "Database";

    /**
     * Version de la BD
     */
    public static final int VERSION = 1;

    /**
     * Tamanio del cache
     */
    public static final int CACHE_SIZE = 0;
}
