package com.example.sadokmm.student.Activities;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.example.sadokmm.student.R;

import java.io.FileNotFoundException;

import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class FullScreenImage extends AppCompatActivity {


    private ImageView imgFullScreenView ;
    private AQuery aq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        imgFullScreenView = (ImageView) findViewById(R.id.imgFullScreenView);

        aq = new AQuery(this);

        final String urlImg = getIntent().getExtras().getString("imgurl");

        Toast.makeText(this,"Appuyer longtemps sur l'image pour le télécharger" , Toast.LENGTH_SHORT).show();

        imgFullScreenView.setFocusableInTouchMode(true);
        aq.id(imgFullScreenView).image(urlImg);

        imgFullScreenView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                telechergerImg(urlImg);
                return true;
            }
        });


    }


    private void telechergerImg(final String url){

        new AlertDialog.Builder(this)
                .setTitle("Télécharger l'image")
                .setPositiveButton("Télécharger", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        DownloadManager.Request request = new DownloadManager.Request(
                                Uri.parse(url));

                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, url);
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(request);
                        Toast.makeText(getApplicationContext(), "Téléchargement de fichier ...", Toast.LENGTH_LONG).show();

                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();

    }
    }

