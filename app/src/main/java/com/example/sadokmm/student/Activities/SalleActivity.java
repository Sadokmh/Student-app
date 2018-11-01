package com.example.sadokmm.student.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.sadokmm.student.R;

public class SalleActivity extends AppCompatActivity {

    private ImageView imgSalle;
    private String nomSalle;
    private Toolbar toolbar;
    private TextView dialog;
    private Bitmap isgplan,a,b,c,d,e1,e2,e3,e4,e5,e6,e8,gs2,gs3,gs5,gs6,amphi1,amphi2;
    private LinearLayout ll;
    private ScrollView scrollView;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salle);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgSalle = (ImageView) findViewById(R.id.imgSalle);
        dialog = (TextView) findViewById(R.id.dialog);

        ll = (LinearLayout) findViewById(R.id.ll);
        scrollView = (ScrollView) findViewById(R.id.sv);



        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog();
            }
        });



        nomSalle = getIntent().getExtras().getString("nom_salle");
        getSupportActionBar().setTitle(getIntent().getExtras().getString("nom_salle_correcte"));

            if (!nomSalle.equals("Plan de l'ISGs")) {
                //dialog.setVisibility(View.INVISIBLE);

            }

            else {
                 isgplan = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.isgplan),900);
                imgSalle.setImageBitmap(isgplan);
            }

            if ( nomSalle.equals("gs2") ) {
                gs2 = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.gsdeux),900);
                imgSalle.setImageBitmap(gs2);
            }
            else if (nomSalle.equals("amphi1" )) {
                amphi1 = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.amphiun),900);
                imgSalle.setImageBitmap(amphi1);
            }
            else if (nomSalle.equals("amphi2") ) {
                amphi2 = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.amphideux),900);
                imgSalle.setImageBitmap(amphi2);
            }
            else if (nomSalle.equals("d"))  {
                d = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.d),900);
               imgSalle.setImageBitmap(d);
            }
            else if (nomSalle.equals("c")) {
                c= getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.c),900);
                imgSalle.setImageBitmap(c);
            }
            else if (nomSalle.equals("b") ) {
                b = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.b),900);
                imgSalle.setImageBitmap(b);
            }
            else if (nomSalle.equals("a"))  {
                a = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.a),900);
                imgSalle.setImageBitmap(a);
            }
            else if (nomSalle.equals("e1")) {
                e1 = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.eun),900);
                imgSalle.setImageBitmap(e1);
            }
            else if (nomSalle.equals("gs3"))  {
                gs3 = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.gstrois),900);
                imgSalle.setImageBitmap(e3);
            }
            else if (nomSalle.equals("e4") ) {
                e4 = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.equatre),900);
                imgSalle.setImageBitmap(e4);
            }
            else if (nomSalle.equals("e5"))  {
                e5 = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ecinq),900);
               imgSalle.setImageBitmap(e5);
            }
            else if (nomSalle.equals("e6")) {
                e6 = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.esix),900);
               imgSalle.setImageBitmap(e6);
            }
            else if (nomSalle.equals("e8"))  {
                e8 = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ehuite),900);
                imgSalle.setImageBitmap(e8);
            }
            else if (nomSalle.equals("gs5"))  {
                gs5 = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.gscinq),900);
               imgSalle.setImageBitmap(gs5);
            }
            else if (nomSalle.equals("e3") ) {
                e3 = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.etrois),900);
                imgSalle.setImageBitmap(e3);
            }
            else if (nomSalle.equals("e2") ) {
                e2 = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.edeux),900);
                imgSalle.setImageBitmap(e2);
            }
            else if (nomSalle.equals("gs6"))  {
                gs6 = getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.gssix),900);
                imgSalle.setImageBitmap(gs6);
            }

    }



    private void dialog(){

        final AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
        alertDialog.setTitle("Détails");
        alertDialog.setCancelable(true);
        alertDialog.setMessage("ADMIN : Administration\u0003\n" +
                "BIBLIO : Bibliothèque\u0003\n" +
                "AE : Affaire des étudiants\u0003\n" +
                "TB : Terrain de basketball\u0003\n" +
                "GS : Grande salle\u0003\n" +
                "BS : Bureau de suivie\u0003\n" +
                "SE : Salle des enseignants\u0003\n" +
                "SEL : SE des  langues\u0003\n" +
                "C&A : Club et associations\u0003\n" +
                "BD : Bureau de directeur\u0003\n" +
                "TG : Tirage\u0003\n" +
                "A : Bloc d’enseignement A\u0003\n" +
                "B : Bloc d’enseignement B\u0003\n" +
                "C : Bloc d’enseignement C\u0003\n" +
                "D : Bloc d’enseignement D");
        alertDialog.setPositiveButton("Fermer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();


    }



    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }


}
