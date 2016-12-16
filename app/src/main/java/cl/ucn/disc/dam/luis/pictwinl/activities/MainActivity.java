package cl.ucn.disc.dam.luis.pictwinl.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cl.ucn.disc.dam.luis.pictwinl.MainApp;
import cl.ucn.disc.dam.luis.pictwinl.R;
import cl.ucn.disc.dam.luis.pictwinl.TwinAdapter;
import cl.ucn.disc.dam.luis.pictwinl.domain.Pic;
import cl.ucn.disc.dam.luis.pictwinl.domain.Twin;
import cl.ucn.disc.dam.luis.pictwinl.models.TokenPic;
import cl.ucn.disc.dam.luis.pictwinl.models.TokenResponsePic;
import cl.ucn.disc.dam.luis.pictwinl.services.APIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static cl.ucn.disc.dam.luis.pictwinl.MainApp.insert;

public class MainActivity extends Activity {



    //Linear Layout donde se guardan los nuevos hermanos y los nuevos layout
    @BindView(R.id.twins_list)
    ListView twinList;
    //guarda la ultima ruta de la ultima imagen creada
    String mCurrentPhotoPath;

    //Arreglo de twins, que guardan la relacion entre
    // los pics que están en la base de datos
    ArrayList<Twin> twins;

    //Adaptador personalizado para el listview
    TwinAdapter twinAdapter;

    //Archivo donde se guarda la foto tomada
    File photoFile;
    //Coordenadas de la foto tomada
    Double[] picCoordinates;

    //Pic creado y guardado en el servidor
    Pic picGuardado = null;
    //Pic obtenido desde el servidor
    Pic picObtenido = null;
    //REQUESTS
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 120;
    private static final int REQUEST_EXTERNAL_STORAGE = 121;
    private static final int REQUEST_INTERNET = 122;
    private static final int REQUEST_NETWORK_STATE = 123;
    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 200;
    //IP server
    static final String IP_SERVER = "http://192.168.42.85:80";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);

        //Inicializacion del Arreglo de twins
        twins = new ArrayList<Twin>();
        getTwinsFromDataBase();

        //Inicializacion del Adapter
        twinAdapter = new TwinAdapter(this,twins);

        //Vincular el listView al Adapter
        twinList.setAdapter(twinAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.requestPermissions(this);
    }

    /*
         *Método para crear y agregar elementos a la lista "twins"
         *
        */
    private void createTwins(Pic pic) {
        Twin newTwin = new Twin();
        newTwin.setLocal(pic);
        twins.add(newTwin);

//        if(picObtenido!=null&&picGuardado!=null){
//            Twin newTwin = new Twin();
//            newTwin.setLocal(picGuardado);
//            newTwin.setRemote(picObtenido);
//            twins.add(newTwin);
//            picGuardado = null;
//            picObtenido = null;
//        }

    }

    /*
    *Obtiene la imagen de la camara, guarda la ultima imagen creada
    *y la hace visible, guarda el gemelo y lo hace visible,
    * quita el boton de la camara del layout, y
    * finalmente crear otro par de fotos.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            //insertar pic en base de datos local
            Pic pic = insert(this, this.picCoordinates[0], this.picCoordinates[1], this.mCurrentPhotoPath);
            postPic(pic);
            createTwins(pic);
            ((BaseAdapter) twinList.getAdapter()).notifyDataSetChanged();

        }

    }


    /**
     * Metodo para la creacion de archivos
     * @return archivo creado
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Metodo listener, para la toma de la foto
     * @param view View que gatilla del evento
     */
    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                this.photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                this.photoFile = null;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Double[] coordinates = getCoordinates();
                if(coordinates!=null){
                    this.picCoordinates = coordinates;
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "cl.ucn.disc.dam.luis.pictwinl",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }
    }

    /**
     * Función para obtener las coordenadas y almacenarlas dentro de un arreglo
     */
    public Double[] getCoordinates() {
        //Inicializa el Location manager
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        //Verifica que el GPS este habilidado
        while (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        //Verifica que los permisos para la ubicación estén habilitados
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Double[] coordinates = new Double[]{0.0, 0.0};
            return coordinates;

        }
        //Obtiene la ultima ubicación obtenida del GPS
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //Guarda la longitud y la latitud en el arreglo de coordenadas
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Double[] coordinates = new Double[]{longitude, latitude};
        //retorna las coordenadas
        return coordinates;

    }

    /**
    *Alerta para iniciar el GPS
     */
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Obtener Pic desde la base de datos
     */
    private void getTwinsFromDataBase(){
        //Cargar los datos desde la base datos y los almacena en una lista
        List<Pic> pics = MainApp.getPics();
        //Agrega las rutas al lista de rutas photoPaths
        if(pics.size()>0){
            for(Pic pic :pics){
                Twin newTwins = new Twin();
                newTwins.setLocal(pic);
                //newTwins.setRemote(pic);
                this.twins.add(newTwins);
            }
        }
    }

    /**
     * Metodo autogenerado para despues de haber pedido permiso para usar el GPS del telefono
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
        }
    }

    /**
     * Metodo para requerir permisos
     * @param activity
     */
    private static void requestPermissions(Activity activity) {

        String locationPerm = Manifest.permission.ACCESS_FINE_LOCATION;
        String writeExPerm = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String internetPerm = Manifest.permission.INTERNET;
        String accesNSPerm = Manifest.permission.ACCESS_NETWORK_STATE;

        List<String> permissions = new ArrayList<>();
        Boolean askForPermissions = false;
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity,
                locationPerm)
                != PackageManager.PERMISSION_GRANTED){
            permissions.add(locationPerm);
            askForPermissions = true;
        }
        if(ContextCompat.checkSelfPermission(activity,
                        writeExPerm)
                        != PackageManager.PERMISSION_GRANTED){
            permissions.add(writeExPerm);
            askForPermissions = true;
        }
        if(ContextCompat.checkSelfPermission(activity,
                        internetPerm)
                        != PackageManager.PERMISSION_GRANTED){
            permissions.add(internetPerm);
            askForPermissions = true;
        }
        if(ContextCompat.checkSelfPermission(activity,
                        accesNSPerm)
                        != PackageManager.PERMISSION_GRANTED){
            permissions.add(internetPerm);
            askForPermissions = true;
        }
        if(askForPermissions){
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    locationPerm)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                int itemsInList = permissions.size();
                //String [] permissionsArray = new String[itemsInList];
                String spliter = "";
                for(String item : permissions){
                    spliter = spliter + item + ",";
                }
                String [] permissionsArray = spliter.split(",");

                ActivityCompat.requestPermissions(activity, permissionsArray,
                        ASK_MULTIPLE_PERMISSION_REQUEST_CODE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    /**
     * Metodo para enviar los parametros del pic al servidor
     * @param pic
     */
   private void postPic(Pic pic){
       String url = IP_SERVER;
       Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
       APIService service = retrofit.create(APIService.class);

       //Codificacion base 64 de la imagen
       Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
       ByteArrayOutputStream byteAOS = new ByteArrayOutputStream();
       bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteAOS); //bm is the bitmap object
       byte[] b = byteAOS.toByteArray();

       String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
       final TokenPic tokenPic = new TokenPic().builder()
               .id(pic.getId())
               .deviceId(pic.getDeviceId())
               .date(pic.getDate())
               .latitude(pic.getLatitude())
               .longitude(pic.getLongitude())
               .url(pic.getUrl())
               .negative(pic.getNegative())
               .positive(pic.getPositive())
               .warning(pic.getWarning())
               .file(encodedImage)
               .build();


       Call<TokenResponsePic> tokenResponseCall = service.insertPicInfo(tokenPic);;
       //guardar pic guardado en el servidor
       this.picGuardado = pic;

       tokenResponseCall.enqueue(new Callback<TokenResponsePic>() {
            @Override
            public void onResponse(Call<TokenResponsePic> call, retrofit2.Response<TokenResponsePic> response) {
                int statusCode = response.code();
//                //Guardar body en un token response para pics
//                TokenResponsePic tokenResponsePic = response.body();
//                //Obtener datos del body
//                String deviceId = tokenResponsePic.getDeviceId();
//                Double latitude = tokenResponsePic.getLatitude();
//                Double longitude = tokenResponsePic.getLongitude();
//                String date = tokenResponsePic.getDate();
//                int positive = tokenResponsePic.getPositive();
//                int negative = tokenResponsePic.getNegative();
//                int warning = tokenResponsePic.getWarning();
//                String file = tokenResponsePic.getFile();
//                //decodificar archivo imagen
//                byte[] decodeImage = Base64.decode(file,Base64.DEFAULT);
//                //guardar archivo imagen y obtener su path
//                File twinFile = new SavePhotoTask().getTwinPath(decodeImage);
//                String url = twinFile.getAbsolutePath();
//
//                //guardar pic obtenido del servidor
//                picObtenido = Pic.builder()
//                        .deviceId(deviceId)
//                        .latitude(latitude)
//                        .longitude(longitude)
//                        .date(date)
//                        .url(url)
//                        .positive(positive)
//                        .negative(negative)
//                        .warning(warning)
//                        .build();
//
//                //Crear twins
//                createTwins();
                Log.d("LoginActivity", "onResponse:" + statusCode);
            }

            @Override
            public void onFailure(Call<TokenResponsePic> call, Throwable t) {
                Log.d("LoginActivity", "onResponse:" + t.getMessage());
            }
        });

    }


}
