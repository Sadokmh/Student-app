package com.example.sadokmm.student.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.sadokmm.student.Adapters.PageAdapterFirst;
import com.example.sadokmm.student.Objects.Emploi;
import com.example.sadokmm.student.Objects.Jour;
import com.example.sadokmm.student.Objects.Seance;
import com.example.sadokmm.student.Objects.User;
import com.example.sadokmm.student.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class firstActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapterFirst pageAdapter;

    public static Emploi monEmploi;


    public final static String publicUrl = "http://192.168.1.7:8080/";

    public static Activity myActivity;

    public static List<User> listUser = new ArrayList<>();

    //public static final String MY_SP_FILE = "com.example.sadokmm.student.activities.monemploi";

    public static User admin ;

    public static final String SESSION = "session" ;
    public static final String EMPLOI_FILE = "emploi_file";

    String nom,prenom,imgUrl,email,filiere,mdp,id;
    int niveau,groupe;

    ProgressDialog prgDialog;

    AQuery aq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        aq=new AQuery(this);


        SharedPreferences sp = getSharedPreferences(SESSION, Context.MODE_PRIVATE);
        Boolean verif = sp.getBoolean("statut", false);

        if (!verif) {
            Toast.makeText(this,"Pas d'utilisateur connecté",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this,"Bienvenue",Toast.LENGTH_LONG).show();


            if (!isNetworkAvailable()) {
                admin = new User("0", sp.getString("nom", "nom"), sp.getString("prenom", "prenom"), sp.getString("email", "email"),  sp.getString("img","img"), sp.getString("filiere", "filiere"), sp.getInt("groupe", 0), sp.getInt("niveau", 0));
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("type","first");
                startActivity(intent);
            }

            else {
                chercherUserByEmail(sp.getString("email","email"));

            }
        }


        tabLayout=(TabLayout)findViewById(R.id.tabLayoutFirst);
        viewPager=(ViewPager) findViewById(R.id.viewPagerFirst);

        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.addTab(tabLayout.newTab().setText(""));

        pageAdapter=new PageAdapterFirst(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);

       // myActivity=this;





    }



    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();


    }


    public void chercherUserByEmail(final String email) {


        String url= publicUrl +"student/getuser/"+email;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {


                    id=jsonObject.getString("_id");
                    nom=jsonObject.getString("nom");
                    prenom=jsonObject.getString("prenom");
                    filiere=jsonObject.getString("filiere");
                    imgUrl=publicUrl+jsonObject.getString("img");


                    niveau = Integer.parseInt(jsonObject.getString("niveau"));
                    groupe = Integer.parseInt(jsonObject.getString("groupe"));

                    admin=new User(id,nom,prenom,email,imgUrl,filiere,groupe,niveau);




                    chargerMonEmploi();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("type","first");
                    startActivity(intent);
                    //imm.setImageBitmap(img);




                    //Intent intent = new Intent(getContext(), MainActivity.class);

                    //admin=new User(id,nom,prenom,email.getText().toString(),mdp,img,filiere,groupe,niveau);

                    //startActivity(intent);
                }

                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);


    }

    public void emailCallback(String url , JSONObject jsonObject , AjaxStatus status){




            try {
                String pas = jsonObject.getString("pass");


                    //Shared Preferences to save connectUser
                    /*SharedPreferences.Editor editor = getActivity().getSharedPreferences("userFile", Context.MODE_PRIVATE).edit();
                    editor.putString("connectUser", email.getText().toString());
                    editor.commit();*/


                    email=jsonObject.getString("email");
                     id=jsonObject.getString("_id");
                 nom=jsonObject.getString("nom");
                 prenom=jsonObject.getString("prenom");
                 filiere=jsonObject.getString("filiere");
                 imgUrl=publicUrl+jsonObject.getString("img");


                     niveau = Integer.parseInt(jsonObject.getString("niveau"));
                     groupe = Integer.parseInt(jsonObject.getString("groupe"));

                    admin=new User(id,nom,prenom,email,imgUrl,filiere,groupe,niveau);





                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("type","first");
                startActivity(intent);
                    //imm.setImageBitmap(img);




                    //Intent intent = new Intent(getContext(), MainActivity.class);

                    //admin=new User(id,nom,prenom,email.getText().toString(),mdp,img,filiere,groupe,niveau);

                    //startActivity(intent);
                }

            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
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


    public void chargerMonEmploi() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String filiere = admin.getFiliere();
        int niveau = admin.getNiveau();
        int groupe = admin.getGroupe();

        String url = publicUrl + "student/getemploi/"+filiere+"/"+niveau+"/"+groupe;
        //String url = publicUrl + "student/getemploi/"+filiere+"/"+niveau+"/"+groupe;
        Toast.makeText(this,url,Toast.LENGTH_LONG).show();

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Connexion en cours ...");
        prgDialog.setIndeterminate(false);
        //prgDialog.setMax(100);
        prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgDialog.setCancelable(false);
        prgDialog.show();


//        Snackbar.make(getCurrentFocus(),url,Snackbar.LENGTH_LONG).show();

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject myObject) {


                Toast.makeText(getApplicationContext(), "not null", Toast.LENGTH_LONG).show();

                try {

                    //JSONArray jsonArray = jsonObject.getJSONArray("data");
                    //JSONObject myObject = jsonArray.getJSONObject(0);


                    String id = myObject.getString("_id");
                    String maFiliere = myObject.getString("filiere");
                    int niveau = Integer.parseInt(myObject.getString("niveau"));
                    int groupe = Integer.parseInt(myObject.getString("groupe"));

                    monEmploi = new Emploi(id, maFiliere, niveau, groupe);

                    // Toast.makeText(getApplicationContext(), "sna3t groupe", Toast.LENGTH_LONG).show();

                    JSONArray joursArray = myObject.getJSONArray("jours");

                    //Toast.makeText(this, "sna3t groupejour", Toast.LENGTH_LONG).show();

                    String nomJour;

                    /*prgDialog = new ProgressDialog(getApplicationContext());
                    prgDialog.setMessage("Connexion en cours ...");
                    prgDialog.setIndeterminate(false);
                    //prgDialog.setMax(100);
                    prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    prgDialog.setCancelable(false);
                    prgDialog.show();*/

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

                        /*if (seance.getString("parQuinzaine").equals("false"))
                            pq = false;
                        else
                            pq = true;*/
                            if (!(matiere.equals(""))) {
                                Seance s = new Seance(matiere, enseignant, salle, type, numSeance, pq);
                                jj.getListSeance().add(s);
                            };

                        }
                        monEmploi.getJours().add(jj);

                    }

                    //Enregistrer une copie local de l'emploi sur le téléphone
                    Gson gson = new Gson();
                    String monEmploiEnJson = gson.toJson(monEmploi);

                    SharedPreferences.Editor editor=getSharedPreferences(EMPLOI_FILE,MODE_PRIVATE).edit();
                    editor.putString("emploi",monEmploiEnJson);
                    editor.commit();

                    prgDialog.dismiss();



                    Toast.makeText(getApplicationContext(),monEmploiEnJson,Toast.LENGTH_LONG).show();
                } catch (JSONException e) {

                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_LONG).show();

            }
        });

        requestQueue.add(jsonObjectRequest);

    }


}
