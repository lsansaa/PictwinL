package cl.ucn.disc.dam.luis.pictwinl.util;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cl.ucn.disc.dam.luis.pictwinl.domain.Pic;

/**
 * Created by Luis on 10-11-2016.
 */

public class DatabaseUtils {
    //contexto de la app
    static Context context;
    //variable para saber si la base datos esta inicializada
    static boolean inicialized;

    /**
     * Constructor
     * @param context, contexto de la aplicacion
     */
    public DatabaseUtils(final Context context) {
        this.context = context;
        this.inicialized = false;
    }

    /**
     * Inicializar base de datos, solo sino ha sido ya inicizalizada
     */
    public void inicializeDB(){
        /** Si la base de datos aun no esta inicializada, la inicializa.
        *Caso contrario no hace nada
        */
         if(!this.inicialized){
            FlowManager.init(new FlowConfig.Builder(context)
                    .openDatabasesOnInit(true)
                    .build());
             this.inicialized = true;
        }

    }

    /**
     * Insertar datos de una imagen dentro de la base de datos
     * @param latitude latitud de la imagen
     * @param longitude logintud de la imagen
     * @param path ruta donde esta almacenada la imagen (SD Externa)
     */

    public void insert(Double latitude, Double longitude, String path){
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        String reportDate = df.format(today);
        //Crear un Pic
        final Pic pic = Pic.builder()
                .deviceId(DeviceUtils.getDeviceId(context))
                .latitude(latitude)
                .longitude(longitude)
                .date(reportDate)
                .url(path)
                .positive(0)
                .negative(0)
                .warning(0)
                .build();

        // Commit
        pic.save();
    }

    public List<Pic> getPics(){
        List<Pic> pics = SQLite.select().from(Pic.class).queryList();
        return pics;
    }

    /*private boolean checkDatabase(){
        Cursor mCursor = FlowManager.;
        Boolean rowExists;

        if (mCursor.moveToFirst())
        {
            // DO SOMETHING WITH CURSOR
            rowExists = true;

        } else
        {
            // I AM EMPTY
            rowExists = false;
        }
        return rowExists;
    }*/
}
