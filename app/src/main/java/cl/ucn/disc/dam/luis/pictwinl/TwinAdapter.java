package cl.ucn.disc.dam.luis.pictwinl;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import cl.ucn.disc.dam.luis.pictwinl.domain.Twin;

/**
 * Created by Luis on 23-11-2016.
 */

public class TwinAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Twin> twins;
   //private LayoutInflater inflater;


    public TwinAdapter(Context context, ArrayList<Twin> twins){
        this.context = context;
        //this.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.twins = twins;
    }

    @Override
    public int getCount(){
        return this.twins.size();
    }
    @Override
    public Object getItem(int position){
        return twins.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                        inflate(R.layout.layout_list_view_row_items, parent, false);
        }
        // get current item to be displayed
        Twin currentItem = (Twin) getItem(position);

        if(currentItem.getLocal()!=null){
            // get the ImageButtons for item name and item description
            ImageButton picImageButton = (ImageButton)
                    convertView.findViewById(R.id.ibPic);
            //sets the src for pic and his twin
            final String picPath = currentItem.getLocal().getUrl();

            File photoFile = new File(picPath);

            Picasso
                    .with(this.context)
                    .load(photoFile)
                    .resize(600, 600)
                    .centerCrop()
                    .placeholder(R.xml.loading_animation)
                    .error(R.drawable.ic_error)
                    .into(picImageButton);
            picImageButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, TwinActivity.class);
                    intent.putExtra("PHOTO_SRC",picPath);
                    context.startActivity(intent);

                }
            });
        }
        //If the pic has a twin, then will load the twin into the other ImageButton
        if(currentItem.getRemote()!=null){
            ImageButton twinImageButton = (ImageButton)
                    convertView.findViewById(R.id.ibTwin);
            final String twinPath = currentItem.getRemote().getUrl();

            File twinFile = new File(twinPath);

            Picasso
                    .with(this.context)
                    .load(twinFile)
                    .resize(600, 600)
                    .centerCrop()
                    .placeholder(R.xml.loading_animation)
                    .error(R.drawable.ic_error)
                    .into(twinImageButton);
            twinImageButton.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context, TwinActivity.class);
                    context.startActivity(intent);
                    intent.putExtra("PHOTO_SRC",twinPath);

                }
            });

        }

        // returns the view for the current row
        return convertView;
    }

}
