package com.example.sadokmm.student.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
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
import com.android.volley.toolbox.Volley;
import com.example.sadokmm.student.Adapters.ImgPostRv;
import com.example.sadokmm.student.FileUtils;
import com.example.sadokmm.student.Objects.Post;
import com.example.sadokmm.student.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;
import static com.example.sadokmm.student.FileUtils.getPathFromUri;

public class AjoutPost extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView ajouterImage;
    private ImageView ajouterFichier;

    private TextView suppSelctPhotos;

    private EditText txtPost;
    private Bitmap myNewImage;
    private String filePath;
    private String fileName;
    private String TYPE_FILE_OR_IMAGE;


    //private ImageView imgShow;
    final static int OPENCAM_CODE = 22;
    final static int PICK_FILE = 44;

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


        TYPE_FILE_OR_IMAGE = "nofile";
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
        ajouterFichier = (ImageView) findViewById(R.id.ajouterFichier);
        ajouterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectImage();
                /*Intent intent = new Intent(AjoutPost.this,Gallery.class);
                startActivity(intent);*/

            }
        });


        ajouterFichier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFiles();
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
            Post post = new Post(txtPost.getText().toString(), admin.getId(), null , "" );
            if (TYPE_FILE_OR_IMAGE.equals("image") || TYPE_FILE_OR_IMAGE.equals("nofile"))
                postPubImage(post.getTxtpost(), post.getIdusr(), post.getDatepost());
            else
                postPubFile(post.getTxtpost(), post.getIdusr(), post.getDatepost());
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


    private void openFiles() {

        /*Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(pickPhoto.createChooser(pickPhoto,"Select picture"), OPENGALLERY_CODE);//one can be replaced with any action code
        */
        Intent intent = new Intent();
        intent.setType("application/*");
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_FILE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    try {
    switch (requestCode) {
        case OPENCAM_CODE:
            if (resultCode == RESULT_OK) {
                TYPE_FILE_OR_IMAGE = "image";
                Bundle extras = data.getExtras();
                myNewImage = getResizedBitmap((Bitmap) extras.get("data"), 900);
                bitmapArrayList.add(myNewImage);
                //imgShow.setImageBitmap(myNewImage);
            }

            break;
        case PICK_IMAGE_MULTIPLE:

            if (resultCode == RESULT_OK  && data != null){


                TYPE_FILE_OR_IMAGE = "image";
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

        case PICK_FILE :
            if (resultCode == RESULT_OK) {
                TYPE_FILE_OR_IMAGE = "file";
                // Get the Uri of the selected file
                Uri uri = data.getData();
                String uriString = uri.toString();
                txtPost.setText(uriString);
                File myFile = new File (getPathFromUri(this,uri));


                filePath = myFile.getPath();
                fileName = null;

                if (uriString.startsWith("content://")) {
                    Cursor cursor = null;
                    try {
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        }
                    } finally {
                        cursor.close();
                    }
                } else if (uriString.startsWith("file://")) {
                    fileName = myFile.getName();
                }
                bitmapArrayList.add(getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.file),500));
                adapterImgPostRv.setFileName(fileName);
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

    public void postPubImage(String txtpost, String idusr, String datepost) throws FileNotFoundException {
        try {
            Toast.makeText(this,"ggg",Toast.LENGTH_LONG).show();
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
                    //Toast.makeText(getApplicationContext(), error.getMessage() +5, Toast.LENGTH_LONG).show();
                }
            });


            smr.addStringParam("txtpost", txtpost);
            smr.addStringParam("idusr", idusr);
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



    public void postPubFile(String txtpost, String idusr, String datepost) throws FileNotFoundException {
        try {
            SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, publicUrl + "student/postpwithfile",
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
                    //Toast.makeText(getApplicationContext(), error.getMessage() +5, Toast.LENGTH_LONG).show();
                }
            });


            smr.addStringParam("txtpost", txtpost);
            smr.addStringParam("idusr", idusr);
            smr.addStringParam("datepost", datepost);



                Toast.makeText(this,fileName,Toast.LENGTH_LONG).show();
                smr.addFile("imgpost",filePath);
                txtPost.setText(filePath);






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







    // Trouver le path complet du fichier séléctionné
    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }







}
