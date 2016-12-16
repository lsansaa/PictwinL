package cl.ucn.disc.dam.luis.pictwinl.util;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Luis on 16-12-2016.
 */

public class SavePhotoTask extends AsyncTask<byte[],String,String> {
    @Override
    protected String doInBackground(byte[]... jpeg) {

        twinPath = new File(Environment.getExternalStorageDirectory(), "photo.jpg");

        if (twinPath.exists()) {
            twinPath.delete();
        }

        try {
            FileOutputStream fos=new FileOutputStream(twinPath.getPath());

            fos.write(jpeg[0]);
            fos.close();
        }
        catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }

        return(null);
    }

    private File twinPath;

    public File getTwinPath(byte[] jpeg){
        this.execute(jpeg);
        return this.twinPath;
    }
}
