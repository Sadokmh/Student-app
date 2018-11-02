package com.example.sadokmm.student.Gallery_multiple_photos;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.example.sadokmm.student.R;

import java.io.File;
import java.util.ArrayList;

public class Gallery extends AppCompatActivity {

    ArrayList<String> filePaths = new ArrayList<>();
    GridView gv ;
    Toolbar toolbar;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Galerie");

        gv = (GridView) findViewById(R.id.gv);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filePaths.clear();


            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case 11 :
                if(resultCode==RESULT_OK && data!=null)
                {
                    //filePaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);
                    Spacecraft s;
                    ArrayList<Spacecraft> spacecrafts=new ArrayList<>();

                    try
                    {
                        for (String path:filePaths) {
                            s=new Spacecraft("",null);
                            s.setName(path.substring(path.lastIndexOf("/")+1));

                            s.setUri(Uri.fromFile(new File(path)));
                            spacecrafts.add(s);
                        }

                        gv.setAdapter(new GalleryAdapter(this,spacecrafts));
                        Toast.makeText(Gallery.this, "Total = "+String.valueOf(spacecrafts.size()), Toast.LENGTH_SHORT).show();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

        }

    }
}
