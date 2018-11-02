package com.example.sadokmm.student.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.misc.ImageUtils;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sadokmm.student.Adapters.ImgPostRv;
import com.example.sadokmm.student.Gallery_multiple_photos.Gallery;
import com.example.sadokmm.student.Objects.Post;
import com.example.sadokmm.student.R;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.getResizedBitmap;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class AjoutPost extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView ajouterImage;

    private TextView suppSelctPhotos;

    private EditText txtPost;
    private Bitmap myNewImage;
    //private ImageView imgShow;
    final static int OPENCAM_CODE = 22;

    private  ProgressDialog prgDialog ;


    //multp
    final int PICK_IMAGE_MULTIPLE = 1;

    private ArrayList<Bitmap> bitmapArrayList;

    private BitmapFactory.Options options ;

    private RecyclerView imgPostRv;
    private ImgPostRv adapterImgPostRv;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_post);

        bitmapArrayList = new ArrayList<>();
        options= new BitmapFactory.Options();
        options.inSampleSize = 5;


        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ajouter une publication");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgPostRv = (RecyclerView) findViewById(R.id.imgPostRv);
        adapterImgPostRv = new ImgPostRv(this);
        imgPostRv.setLayoutManager(new GridLayoutManager(this,3));
        imgPostRv.setAdapter(adapterImgPostRv);

        suppSelctPhotos = (TextView) findViewById(R.id.suppSelectPhotos);
        txtPost = (EditText) findViewById(R.id.txtPost);
        ajouterImage = (ImageView) findViewById(R.id.ajouterImage);
        ajouterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();
                /*Intent intent = new Intent(AjoutPost.this,Gallery.class);
                startActivity(intent);*/

            }
        });

        suppSelctPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmapArrayList.clear();
                adapterImgPostRv.getListImg().clear();
                adapterImgPostRv.notifyDataSetChanged();
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
            Post post = new Post(txtPost.getText().toString(), admin.getEmail(), null , "" , this);
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

        /*Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(pickPhoto.createChooser(pickPhoto,"Select picture"), OPENGALLERY_CODE);//one can be replaced with any action code
        */
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    try {
    switch (requestCode) {
        case OPENCAM_CODE:
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                myNewImage = getResizedBitmap((Bitmap) extras.get("data"), 900);
                bitmapArrayList.add(myNewImage);
                //imgShow.setImageBitmap(myNewImage);
            }

            break;
        case PICK_IMAGE_MULTIPLE:

            if (resultCode == RESULT_OK  && data != null){



                if(data.getClipData() != null){

                    int count = data.getClipData().getItemCount();
                    for (int i=0; i<count; i++){

                        Uri imageUri = data.getClipData().getItemAt(i).getUri();

                        Bitmap bitmap = ImageUtils.decodeStream(this.getContentResolver(),imageUri,options);
                        bitmapArrayList.add(bitmap);

                    }
                }
                else if(data.getData() != null){

                    Uri imgUri = data.getData();
                    myNewImage = ImageUtils.decodeStream(this.getContentResolver(),imgUri,options);
                    bitmapArrayList.add(myNewImage);
                }
            }


            break;
    }

    adapterImgPostRv.setListImg(bitmapArrayList);
    adapterImgPostRv.notifyDataSetChanged();

    }
    catch (Exception e) {

    }

        super.onActivityResult(requestCode, resultCode, data);


    }


    //Post publication to DB

    public void postPub(String txtpost, String emailusr, String datepost) throws FileNotFoundException {
        try {
            SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, publicUrl + "student/postp",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("Response", response);

                            prgDialog.dismiss();

                            Toast.makeText(getApplicationContext(), "Okey", Toast.LENGTH_LONG).show();
                            finish();


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    prgDialog.dismiss();
                    Toast.makeText(getApplicationContext(), error.getMessage() +5, Toast.LENGTH_LONG).show();
                }
            });


            smr.addStringParam("txtpost", txtpost);
            smr.addStringParam("emailusr", emailusr);
            smr.addStringParam("datepost", datepost);

            for (int i=0 ; i<bitmapArrayList.size();i++) {
                smr.addFile("imgpost"+i,saveImage(bitmapArrayList.get(i),"imgpost"+i+i));
            }




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
                "/Posts";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, "Posts-" + filename + ".png");
        FileOutputStream fOut = new FileOutputStream(file);

        bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filePath = file.getPath();



        return filePath;

    }







}
