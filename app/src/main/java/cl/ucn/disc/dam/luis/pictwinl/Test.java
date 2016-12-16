package cl.ucn.disc.dam.luis.pictwinl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cl.ucn.disc.dam.luis.pictwinl.domain.Pic;
import cl.ucn.disc.dam.luis.pictwinl.domain.Pic_Table;
import cl.ucn.disc.dam.luis.pictwinl.domain.Twin;
import cl.ucn.disc.dam.luis.pictwinl.util.DeviceUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Test principal del backend.
 *
 * @author Diego P. Urrutia Astorga
 * @version 20161102
 */
@Slf4j
public final class Test {

    /**
     * Testing the bd.
     *
     * @param context
     */
    public static void testDatabase(final Context context) {

        // Remove database
        context.deleteDatabase(Database.NAME + ".db");

        log.debug("Testing database ..");

        // Inicializacion
        {
            FlowManager.init(new FlowConfig.Builder(context)
                    .openDatabasesOnInit(true)
                    .build());

            log.debug("DB initialized.");
        }

        // Insert into db
        {
            // Ciclo para insertar 100 objetos en la bd
            for (int i = 1; i <= 4; i++) {

                //Obtener imagen i
                String path = Environment.getExternalStorageDirectory()+"/Android/data/cl.ucn.disc.dam.luis.pictwinl/files/Pictures/samples/sample"+i+".jpg";
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                byte [] data = getBitmapAsByteArray(bitmap);
                // Create an instance of SimpleDateFormat used for formatting
                // the string representation of date (month/day/year)
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

                // Get the date today using Calendar object.
                Date today = Calendar.getInstance().getTime();
                // Using DateFormat format method we can create a string
                // representation of a date with the defined format.
                String reportDate = df.format(today);
                final Pic pic = Pic.builder()
                        .deviceId(DeviceUtils.getDeviceId(context))
                        .latitude(RandomUtils.nextDouble())
                        .longitude(RandomUtils.nextDouble())
                        .date(reportDate)
                        .url("http://" + RandomStringUtils.randomAlphabetic(20))
                        .positive(RandomUtils.nextInt(0, 100))
                        .negative(RandomUtils.nextInt(0, 100))
                        .warning(RandomUtils.nextInt(0, 2))
                        .build();

                // Commit
                pic.save();

                log.debug("Saved.");
            }

        }

        // Select from database
        {
            List<Pic> pics = SQLite.select().from(Pic.class).queryList();
            log.debug("Result: {}", pics.size());


            for (final Pic p : pics) {
                log.debug("{}", p);
            }
        }

        // Relations
        {
            for (long i = 1; i <= 4; i = i + 2) {
                final Pic local = SQLite.select().from(Pic.class).where(Pic_Table.id.is(i)).querySingle();
                final Pic remote = SQLite.select().from(Pic.class).where(Pic_Table.id.is(i + 1)).querySingle();

                final Twin twin = Twin.builder().local(local).remote(remote).build();
                log.debug("Twin: {}", twin);

                twin.save();
            }

            log.debug("Relation.");
        }

        // Get from relation
        {
            final List<Twin> twins = SQLite.select().from(Twin.class).queryList();
            log.debug("Twin size: {} .", twins.size());

            for (final Twin t : twins) {
                log.debug("Twin: {}.", t);
            }
        }

        // Destroy the world.
        {
            FlowManager.destroy();

            log.debug("Finished.");
        }

    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
