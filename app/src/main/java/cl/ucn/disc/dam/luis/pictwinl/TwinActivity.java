package cl.ucn.disc.dam.luis.pictwinl;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Luis on 20-10-2016.
 */

public class TwinActivity extends Activity {

    //@BindView(R.id.user_photo)
    ImageButton photoPreview;
    //@BindView(R.id.photoDescription)
    ListView photoDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twin);

        photoPreview = (ImageButton) findViewById(R.id.user_photo);
        photoDescription = (ListView) findViewById(R.id.photoDescription);


        Bundle extras = getIntent().getExtras();
        if(extras !=null){
            String path = extras.getString("PHOTO_SRC");
            loadPhotoData(path);


        }
    }

    /**
     * Metodo oara cargar los datos de la foto en los views
     * @param path Ubicacion de la foto
     */
    private void loadPhotoData(String path){
        if(path != null){
            File photo = new File(path);

            Picasso.with(this)
                    .load(photo)
                    .resize(600, 600)
                    .centerCrop()
                    .placeholder(R.xml.loading_animation)
                    .error(R.drawable.ic_error)
                    .into(photoPreview);

            //PhotoViewAttacher mAttacher = new PhotoViewAttacher(photoPreview);


        }
    }
}
