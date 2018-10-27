package com.example.sadokmm.student.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.misc.ImageUtils;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.sadokmm.student.Objects.Post;
import com.example.sadokmm.student.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.getResizedBitmap;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class AjoutPost extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView ajouterImage;

    private EditText txtPost;
    private Bitmap myNewImage;
    private ImageView imgShow;
    final static int OPENCAM_CODE = 22;
    final static int OPENGALLERY_CODE = 23;
    private Uri filepath;

    private String fileePath;

    ProgressDialog prgDialog ;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_post);


        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ajouter une publication");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtPost = (EditText) findViewById(R.id.txtPost);
        imgShow = (ImageView) findViewById(R.id.imgShow);
        ajouterImage = (ImageView) findViewById(R.id.ajouterImage);
        ajouterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();
            }
        });


    }

    public void publierBtn(View view) throws FileNotFoundException {

        if (txtPost.getText().toString().length() < 8) {
            Snackbar.make(view, "Veuiller saisir au moins 8 caractères !", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            prgDialog = new ProgressDialog(this);
            prgDialog.setMessage("chargement en cours ...");
            prgDialog.setIndeterminate(false);
            //prgDialog.setMax(100);
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.setCancelable(true);
            prgDialog.show();
            Post post = new Post(txtPost.getText().toString(), admin.getEmail(), saveImage(myNewImage, "ffff") , "" , this);
            postPub(post.getTxtpost(), post.getEmailusr(), post.getDatepost());
        }

    }


    //Ajout photo

    private void selectImage() {

        final String[] items = {"Camera", "Galerie"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisir le source: ");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //boolean result=Utility.checkPermission(getContext());
                if (items[item].equals("Camera")) {
                    openCam();
                } else {
                    openGallery();

                }
            }
        });

        builder.show();
    }


    private void openCam() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, OPENCAM_CODE);

        }
    }


    private void openGallery() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, OPENGALLERY_CODE);//one can be replaced with any action code
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case OPENCAM_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    myNewImage = getResizedBitmap((Bitmap)extras.get("data"),500);
                    imgShow.setImageBitmap(myNewImage);
                }

                break;
            case OPENGALLERY_CODE:
                if (resultCode == RESULT_OK) {

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;

                    myNewImage = getResizedBitmap(ImageUtils.decodeStream(getContentResolver(),data.getData(),options),500);
                    imgShow.setImageBitmap(myNewImage);

                }
                break;
        }


    }


    //Post publication to DB


    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


    public void postPub(String txtpost, String emailusr, String datepost) throws FileNotFoundException {
        try {
            SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, publicUrl + "student/postp",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response);

                            prgDialog.dismiss();

                            Toast.makeText(getApplicationContext(), "Okey", Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    prgDialog.dismiss();
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });


            smr.addStringParam("txtpost", txtpost);
            smr.addStringParam("emailusr", emailusr);
            smr.addStringParam("datepost", datepost);


            smr.addFile("imgpost", fileePath);

            RequestQueue mRequestQueue = Volley.newRequestQueue(this);
            mRequestQueue.add(smr);
        }
        catch (Exception e) {
            txtPost.setText(e.toString());
        }

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



    private String saveImage(Bitmap bmp, String filename) throws FileNotFoundException {




        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Student";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, "Students-" + filename + ".png");
        FileOutputStream fOut = new FileOutputStream(file);

        bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filePath = file.getPath();
        fileePath = filePath;


        return filePath;

    }

}
