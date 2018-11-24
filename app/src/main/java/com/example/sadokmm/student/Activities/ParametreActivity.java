package com.example.sadokmm.student.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.misc.ImageUtils;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.example.sadokmm.student.Objects.Emploi;
import com.example.sadokmm.student.Objects.Jour;
import com.example.sadokmm.student.Objects.Seance;
import com.example.sadokmm.student.Objects.User;
import com.example.sadokmm.student.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sadokmm.student.Activities.AjoutPost.OPENCAM_CODE;
import static com.example.sadokmm.student.Activities.firstActivity.EMPLOI_FILE;
import static com.example.sadokmm.student.Activities.firstActivity.SESSION;
import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.getResizedBitmap;
import static com.example.sadokmm.student.Activities.firstActivity.monEmploi;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;
import static com.example.sadokmm.student.Fragments.NavigationDrawerFragment.mAdap;
import static com.example.sadokmm.student.Fragments.RegisterFragment.OPENGALLERY_CODE;

public class ParametreActivity extends AppCompatActivity {

    private ArrayList<String> listNiveau;
    private ArrayList<String> listFiliere;
    private ArrayList<String> listGroupe;
    private String niveau , filieretxt , mdp ;
    private int niveauFil , groupe ;
    private ImageView suppImage;

    private EditText nomP,prenomP,emailP;
    private Spinner spinnerFiliereP,spinnerGroupeP,spinnerNiveauP;
    private CircleImageView imageP;
    private TextView btnMAJp,errorText;
    private Bitmap myNewImage;

    private Boolean ok;
    private ProgressDialog prgDialog;
    private Toolbar toolbar;

    private AQuery aq;



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);


        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Paramètres");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        aq = new AQuery(this);

        nomP = (EditText) findViewById(R.id.nomP);
        prenomP = (EditText) findViewById(R.id.prenomP);
        emailP = (EditText) findViewById(R.id.emailP);

        imageP = (CircleImageView) findViewById(R.id.photoP);
        suppImage = (ImageView) findViewById(R.id.suppImage);

        btnMAJp = (TextView) findViewById(R.id.btnMAJp);
        errorText = (TextView) findViewById(R.id.errorTxtP);

        spinnerFiliereP = (Spinner) findViewById(R.id.spinnerFiliereP);
        spinnerGroupeP = (Spinner) findViewById(R.id.spinnerGroupeP);
        spinnerNiveauP = (Spinner) findViewById(R.id.spinnerNiveauP);


        nomP.setText(admin.getNom());
        prenomP.setText(admin.getPrenom());
        emailP.setText(admin.getEmail());
        aq.id(imageP).image(publicUrl+admin.getImg());

        listNiveau = new ArrayList<>();
        listFiliere = new ArrayList<>();
        listGroupe = new ArrayList<>();
        remplirSpinnerNiveau();

        if (admin.getNiveau() == 1 )
            selectSpinnerValue(spinnerNiveauP,"1ère année");
        if (admin.getNiveau() == 2 )
            selectSpinnerValue(spinnerNiveauP,"2ème année");
        if (admin.getNiveau() == 3 )
            selectSpinnerValue(spinnerNiveauP,"3ème année");







        ok= false;
        prgDialog = new ProgressDialog(this);

        //remplirSpinnerNiveau();

        myNewImage=null;

        imageP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });




        btnMAJp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ok=true;
                final String nomtxt,prenomtxt,emailtxt;
                nomtxt=nomP.getText().toString();
                prenomtxt=prenomP.getText().toString();
                emailtxt=emailP.getText().toString();



                errorText.setText("");


                if (nomtxt.isEmpty())
                {
                    errorText.setText("Veuillez saisir votre nom");
                    ok=false;
                }
                if (!isAlpha(nomtxt))
                {
                    errorText.setText("nom invalide");
                }
                if (prenomtxt.isEmpty())
                {
                    errorText.setText("Veuillez saisir votre prénom");
                    ok=false;
                }
                if (!isAlpha(prenomtxt))
                {
                    errorText.setText("prénom invalide");
                }
                if (emailtxt.isEmpty())
                {
                    errorText.setText("Veuillez saisir votre email");
                    ok=false;
                }
                if (!verifEmail(emailtxt))
                {
                    errorText.setText("Email invalide");
                    ok=false;
                }




                if (ok){

                        verifMdp(nomtxt,prenomtxt,emailtxt,filieretxt,String.valueOf(niveauFil),String.valueOf(groupe));

                }




            }
        });


        suppImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myNewImage = null;
                imageP.setImageResource(R.drawable.ic_photo_cam);
            }
        });


    }





    public void remplirSpinnerNiveau(){

        listNiveau.add("1ère année");
        listNiveau.add("2ème année");
        listNiveau.add("3ème année");
        listNiveau.add("M1");
        listNiveau.add("M2");

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this.getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,listNiveau);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNiveauP.setAdapter(arrayAdapter);

        spinnerNiveauP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                niveau=adapterView.getItemAtPosition(i).toString();
                remplirListFiliere(niveau);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



    public void remplirListFiliere(String niveau) {

        listFiliere.clear();

        if (niveau.equals("1ère année"))
        {
            listFiliere.add("LAG");
            listFiliere.add("LFE");
            listFiliere.add("LFIG");
            listFiliere.add("LFG");

            niveauFil=1;
        }

        if (niveau.equals("2ème année"))
        {
            listFiliere.add("LFE EQ");
            listFiliere.add("LFE FI");
            listFiliere.add("LFG");
            listFiliere.add("LFIG");
            listFiliere.add("LAC");
            listFiliere.add("LAF");
            listFiliere.add("LAIG");
            listFiliere.add("LA MGT");
            listFiliere.add("LA MKG");

            niveauFil=2;
        }

        if (niveau.equals("3ème année"))
        {
            listFiliere.add("LFC");
            listFiliere.add("LFE EQ");
            listFiliere.add("LFE FI");
            listFiliere.add("LFF");
            listFiliere.add("LFIG");
            listFiliere.add("LF MGT");
            listFiliere.add("LF MKG");
            listFiliere.add("LA MGT");
            listFiliere.add("LA MKG");
            listFiliere.add("LAC");
            listFiliere.add("LAF");
            listFiliere.add("LAIG");

            niveauFil=3;

        }

        if (niveau.equals("M1"))
        {
            listFiliere.add("M PRO GHT");
            listFiliere.add("M PRO ING FIN");
            listFiliere.add("M PRO MKG");
            listFiliere.add("M RECH COMPT");
            listFiliere.add("M RECH SIAD");

            niveauFil=1;
        }

        if (niveau.equals("M2"))
        {
            listFiliere.add("M PRO GHT");
            listFiliere.add("M PRO ING FIN");
            listFiliere.add("M PRO MKG");
            listFiliere.add("M PRO COMPT");
            listFiliere.add("M RECH COMPT");
            listFiliere.add("M RECH SIAD");
            listFiliere.add("M RECH FIN");
            listFiliere.add("M RECH ENT");

            niveauFil=2;

        }


        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this.getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,listFiliere);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFiliereP.setAdapter(arrayAdapter);

        selectSpinnerValue(spinnerFiliereP,admin.getFiliere().trim().toUpperCase());


        spinnerFiliereP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filieretxt=adapterView.getItemAtPosition(i).toString().toLowerCase();
                remplirListeGroupe();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }



    public void remplirListeGroupe() {

        listGroupe.clear();
        listGroupe.add("1");
        listGroupe.add("2");
        listGroupe.add("3");
        listGroupe.add("4");

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,listGroupe);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroupeP.setAdapter(arrayAdapter);

        selectSpinnerValue(spinnerGroupeP,String.valueOf(admin.getGroupe()));

        spinnerGroupeP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                groupe=Integer.parseInt(adapterView.getItemAtPosition(i).toString().toLowerCase());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }






    public void updateUser(String nom , String prenom , final String email  , final String filiere , final String niveau , final String groupe) throws FileNotFoundException {
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, publicUrl+"student/userupdate",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("null"))
                            Toast.makeText(getApplicationContext(), "Mot de passe incorrecte",Toast.LENGTH_LONG).show();
                        else {
                            Toast.makeText(getApplicationContext(), "Modifications enregistrées", Toast.LENGTH_LONG).show();
                            chargerMonEmploi(filiere, Integer.parseInt(niveau), Integer.parseInt(groupe), email);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString() + "error hihihi", Toast.LENGTH_LONG).show();
            }
        });
        if (!nom.trim().isEmpty())
            smr.addStringParam("nom", nom);
        if (!prenom.trim().isEmpty())
            smr.addStringParam("prenom", prenom);
        if (!email.trim().isEmpty())
            smr.addStringParam("email", email);
        if (!filiere.trim().isEmpty())
            smr.addStringParam("filiere", filiere);
        if (!niveau.trim().isEmpty())
            smr.addStringParam("niveau", niveau);
        if (!groupe.trim().isEmpty())
            smr.addStringParam("groupe", groupe);
        if (myNewImage != null )
            smr.addFile("img", saveImage(myNewImage,"user"));
        smr.addStringParam("id",admin.getId());
        smr.addStringParam("mdp",mdp);

        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.add(smr);



        //admin=new User("0",nom,prenom,email,pass,myNewImage,filiere,Integer.parseInt(groupe),Integer.parseInt(niveau));




    }




    public void chargerMonEmploi(String filiere , int niveau , int groupe , final String mail) {



        String url = publicUrl + "student/getemploi/"+filiere+"/"+niveau+"/"+groupe;


        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject myObject) {



                try {


                    String id = myObject.getString("_id");
                    String maFiliere = myObject.getString("filiere");
                    int niveau = Integer.parseInt(myObject.getString("niveau"));
                    int groupe = Integer.parseInt(myObject.getString("groupe"));

                    monEmploi = new Emploi(id, maFiliere, niveau, groupe);

                    JSONArray joursArray = myObject.getJSONArray("jours");

                    String nomJour;

                    JSONArray seanceArray;
                    for (int i = 0; i < joursArray.length(); i++) {
                        nomJour = joursArray.getJSONObject(i).getString("nom");
                        Jour jj = new Jour(nomJour);
                        seanceArray = joursArray.getJSONObject(i).getJSONArray("seances");

                        for (int j = 0; j < seanceArray.length(); j++) {
                            JSONObject seance = seanceArray.getJSONObject(j);
                            String matiere = seance.getString("mat");
                            String enseignant = seance.getString("enseignant");
                            String salle = seance.getString("salle");
                            int numSeance = Integer.parseInt(seance.getString("numseance"));
                            String type = seance.getString("type");
                            String pqn= seance.getString("pq");
                            Boolean pq;
                            if (pqn.equals("false"))
                                pq = false ;
                            else pq=true;

                            if (!(matiere.equals(""))) {
                                Seance s = new Seance(matiere, enseignant, salle, type, numSeance, pq);
                                jj.getListSeance().add(s);
                            }

                        }
                        monEmploi.getJours().add(jj);

                    }

                    //Enregistrer une copie local de l'emploi sur le téléphone
                    Gson gson = new Gson();
                    String monEmploiEnJson = gson.toJson(monEmploi);

                    SharedPreferences.Editor editor=getApplicationContext().getSharedPreferences(EMPLOI_FILE,MODE_PRIVATE).edit();
                    editor.putString("emploi",monEmploiEnJson);
                    editor.commit();

                    chargerUser(mail);




                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }






    public void chargerUser(String emailusr ){

        String url = publicUrl + "student/getuser/"+emailusr;


        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                String id , email , nom , prenom , filiere  , imgUrl ;
                int niveau , groupe ;


                try {
                    id = jsonObject.getString("_id");
                    nom = jsonObject.getString("nom");
                    prenom = jsonObject.getString("prenom");
                    filiere = jsonObject.getString("filiere");
                    imgUrl = publicUrl + jsonObject.getString("img");
                    email = jsonObject.getString("email");
                    niveau = Integer.parseInt(jsonObject.getString("niveau"));
                    groupe = Integer.parseInt(jsonObject.getString("groupe"));
                    admin = new User(id, nom, prenom, email, imgUrl, filiere, groupe, niveau);

                    SharedPreferences.Editor editor=getApplicationContext().getSharedPreferences(SESSION,Context.MODE_PRIVATE).edit();
                    editor.putBoolean("statut",true);
                    editor.putString("email",admin.getEmail());
                    editor.putString("prenom",admin.getPrenom());
                    editor.putString("nom" , admin.getNom());
                    editor.putString("filiere" , admin.getFiliere());
                    editor.putString("img",admin.getImg());
                    editor.putInt("groupe",admin.getGroupe());
                    editor.putInt("niveau",admin.getNiveau());
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("type","parametres");
                    intent.putExtra("email",email);
                    mAdap.notifyDataSetChanged();
                    startActivity(intent);





                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

        //return user;

    }




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
                    imageP.setImageBitmap(myNewImage);
                    //filepath = data.getData();
                }

                break;
            case OPENGALLERY_CODE:
                if (resultCode == RESULT_OK) {

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;

                    myNewImage = getResizedBitmap(ImageUtils.decodeStream(getContentResolver(),data.getData(),options),500);
                    imageP.setImageBitmap(myNewImage);
                    //filepath= data.getData();
                }
                break;
        }


    }



    boolean verifEmail(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isAlpha(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if(!Character.isLetter(c)) {
                if (!Character.isSpaceChar(c)) return false;

            }
        }

        return true;
    }



    private String saveImage(Bitmap bmp, String filename) throws FileNotFoundException {




        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Student";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, "user-" + filename + ".png");
        FileOutputStream fOut = new FileOutputStream(file);

        bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filePath = file.getPath();

        return filePath;

    }



    private void selectSpinnerValue(Spinner spinner, String myString)
    {
        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equals(myString)){
                spinner.setSelection(i);
                break;
            }
        }
    }


    private void verifMdp(final String nom , final String prenom , final String email  , final String filiere , final String niveau , final String groupe){

        final EditText mdpTxt = new EditText(this);

        // Set the default text to a link of the Queen
        mdpTxt.setHint("Votre Mot de passe");

        new AlertDialog.Builder(this)
                .setTitle("Veuiller saisir votre mot de passe")
                .setView(mdpTxt)
                .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        mdp = mdpTxt.getText().toString();
                        try {
                            updateUser(nom,prenom,email,filiere,niveau,groupe);
                        } catch (FileNotFoundException e) {
                            Toast.makeText(getApplicationContext(),"erreur update user appel" + e.toString() , Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();

    }

}
