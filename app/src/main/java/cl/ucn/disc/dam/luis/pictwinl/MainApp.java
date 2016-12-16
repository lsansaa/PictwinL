package cl.ucn.disc.dam.luis.pictwinl;

import android.app.Application;
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
import cl.ucn.disc.dam.luis.pictwinl.util.DeviceUtils;

/**
 * Created by Luis on 17-11-2016.
 */

public class MainApp extends Application {

    static boolean inicialized;

    @Override
    public void onCreate() {
        super.onCreate();
        inicialized = false;
        inicializeDB();

    }
    public static boolean databaseExist(){
        return inicialized;
    }

    /**
     * Inicializar base de datos, solo sino ha sido ya inicizalizada
     */
    public void inicializeDB(){
        /** Si la base de datos aun no esta inicializada, la inicializa.
         *Caso contrario no hace nada
         */
        if(!this.inicialized){
            FlowManager.init(new FlowConfig.Builder(this)
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

    public static Pic insert(Context context, Double latitude, Double longitude, String path){
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
        return pic;
    }

    public static List<Pic> getPics(){
        List<Pic> pics = SQLite.select().from(Pic.class).queryList();
        return pics;
    }


}
