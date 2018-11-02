package com.example.sadokmm.student.Gallery_multiple_photos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.example.sadokmm.student.R;

import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Spacecraft> spacecrafts;
    private AQuery aq;

   public GalleryAdapter(Context context, ArrayList<Spacecraft> spacecrafts) {
       this.context=context;
       this.spacecrafts = spacecrafts;
       this.aq = new AQuery(context);
   }

    @Override
    public int getCount() {
        return spacecrafts.size();
    }

    @Override
    public Object getItem(int i) {
        return spacecrafts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

       if (view == null ) {
           view = LayoutInflater.from(context).inflate(R.layout.model_img,viewGroup,false);

       }

       final Spacecraft s = (Spacecraft) this.getItem(i);
        TextView nameText = (TextView) view.findViewById(R.id.nameTxt);
        ImageView imageView = (ImageView) view.findViewById(R.id.spacecraftImg);

        //Bind DATA
        nameText.setText(s.getName());
        imageView.setImageURI(s.getUri());

        //View ITEM CLICK
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,s.getName(),Toast.LENGTH_LONG).show();
            }
        });
        return view;

    }
}
