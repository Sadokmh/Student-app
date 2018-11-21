package com.example.sadokmm.student.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.ImageRequest;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.example.sadokmm.student.Adapters.PageAdapterMain;
import com.example.sadokmm.student.Fragments.NavigationDrawerFragment;
import com.example.sadokmm.student.Objects.Emploi;
import com.example.sadokmm.student.Objects.Info;
import com.example.sadokmm.student.Objects.Jour;
import com.example.sadokmm.student.Objects.Seance;
import com.example.sadokmm.student.R;
import com.google.gson.Gson;
//import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.sadokmm.student.Activities.firstActivity.EMPLOI_FILE;
import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.monEmploi;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;
import static com.example.sadokmm.student.Fragments.PostFragment.postAdapter;


public class MainActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;


    public static List<Info> myList = new ArrayList<>();
    public static String currentTime;
    public static int currentSession;
    public static String currentFullTime;
    public static String currentDay;
    public static String currentDate;
    public static Date laDate;
    public static int jourNum;
    public static Seance seanceActuelle,seanceVide,weekend;
    public static int currentHour ;
    //public static Emploi monEmploi;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapterMain pageAdapter;

    ProgressDialog prgDialog;

    private RequestQueue requestQueue;

    public static android.support.v7.widget.SearchView searchView;
    private int currentFragment;



    final static String LUNDI = "lundi/monday/lun./mon.";
    final static String MARDI = "mardi/tuesday/mar./tue.";
    final static String MERCREDI = "mercredi/wednesday/mer./wed.";
    final static String JEUDI = "jeudi/thursday/jeu./thu.";
    final static String VENDREDI = "vendredi/friday/ven./fri.";
    final static String SAMEDI = "samedi/saturday/sam./sat.";
    final static String DIMANCHE = "dimanche/sunday/dim./sun.";

    private AQuery aq ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aq = new AQuery(this);
        requestQueue = Volley.newRequestQueue(this);
        setUpFab();

        getStringDate();

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Student");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout=(TabLayout)findViewById(R.id.tabLayoutMain);
        viewPager=(ViewPager) findViewById(R.id.viewPagerMain);


        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.addTab(tabLayout.newTab().setText(""));

        pageAdapter=new PageAdapterMain(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        searchView = new SearchView(this);


        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.settUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerLayout), toolbar);

        weekend = new Seance("Bon Weekend " , "","","",0,false);
        seanceVide = new Seance("Pas de cours pour l'instant","","","",0,false);
        seanceActuelle = seanceVide;
        getJourNum();
        getTime();







    }

    @Override
    public void onBackPressed() {



    }

    public void chargerMonEmploi() {

        String filiere = admin.getFiliere();
        int niveau = admin.getNiveau();
        int groupe = admin.getGroupe();

        String url = publicUrl + "student/getemploi/"+filiere+"/"+niveau+"/"+groupe;
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Connexion en cours ...");
        prgDialog.setIndeterminate(false);
        //prgDialog.setMax(100);
        prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgDialog.setCancelable(false);
        prgDialog.show();



        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject myObject) {

                try {


                    String id = myObject.getString("_id");
                    String maFiliere = myObject.getString("filiere");
                    int niveau = Integer.parseInt(myObject.getString("niveau"));
                    int groupe = Integer.parseInt(myObject.getString("groupe"));

                    monEmploi = new Emploi(id, maFiliere, niveau, groupe);

                   // Toast.makeText(getApplicationContext(), "sna3t groupe", Toast.LENGTH_LONG).show();

                    JSONArray joursArray = myObject.getJSONArray("jours");

                    //Toast.makeText(this, "sna3t groupejour", Toast.LENGTH_LONG).show();

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


    public void chEmp(String url, JSONObject myObject, AjaxStatus status) {


        SharedPreferences.Editor editor2=getSharedPreferences(EMPLOI_FILE,MODE_PRIVATE).edit();
        editor2.clear();
        editor2.commit();

        if (myObject != null)


        {

            //Toast.makeText(this, "not null", Toast.LENGTH_LONG).show();

            try {

                //JSONArray jsonArray = jsonObject.getJSONArray("data");
                //JSONObject myObject = jsonArray.getJSONObject(0);


                String id = myObject.getString("_id");
                String maFiliere = myObject.getString("filiere");
                int niveau = Integer.parseInt(myObject.getString("niveau"));
                int groupe = Integer.parseInt(myObject.getString("groupe"));

                monEmploi = new Emploi(id, maFiliere, niveau, groupe);

                //Toast.makeText(this, "sna3t groupe", Toast.LENGTH_LONG).show();

                JSONArray joursArray = myObject.getJSONArray("jours");

                //Toast.makeText(this, "sna3t groupejour", Toast.LENGTH_LONG).show();

                String nomJour;

                prgDialog = new ProgressDialog(this);
                prgDialog.setMessage("Connexion en cours ...");
                prgDialog.setIndeterminate(false);
                //prgDialog.setMax(100);
                prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                prgDialog.setCancelable(true);
                prgDialog.show();

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
                    //Toast.makeText(this, "Emploi ajouté avec succées", Toast.LENGTH_LONG).show();
                    prgDialog.dismiss();

                }

              //Enregistrer une copie local de l'emploi sur le téléphone
                Gson gson = new Gson();
                String monEmploiEnJson = gson.toJson(monEmploi);

                SharedPreferences.Editor editor=getSharedPreferences(EMPLOI_FILE,MODE_PRIVATE).edit();
                editor.putString("emploi",monEmploiEnJson);
                editor.commit();


                //Toast.makeText(this,monEmploi.getJours().size()+" ",Toast.LENGTH_LONG).show();
            } catch (JSONException e) {

                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

            }
        } else {
            //Toast.makeText(this, "null", Toast.LENGTH_LONG).show();
        }


        //actuelle();


    }


    public String getStringDate() {
        Date mydate = Calendar.getInstance().getTime();


        SimpleDateFormat sdfHour = new SimpleDateFormat("HH");
        currentHour = Integer.parseInt(sdfHour.format(mydate));

        DateFormat df = new SimpleDateFormat("EEE");
        String date = df.format(mydate);

        DateFormat currentDf = new SimpleDateFormat("HH:mm");
        currentTime = currentDf.format(mydate);
        //Toast.makeText(this, "current time: " + currentTime, Toast.LENGTH_LONG).show();

        DateFormat currentFulldf = new SimpleDateFormat("EEEEEE, d MMM yyyy");
        currentFullTime = currentFulldf.format(mydate);

        DateFormat dateCurrent = new SimpleDateFormat("d MMM yyyy");
        currentDate = dateCurrent.format(mydate);

        currentDay = date;

        return date;
    }


    public void getTime() {
        String pattern = "HH:mm";

        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {

            Date current = sdf.parse(currentTime);

            Date s1Deb = sdf.parse("08:00");
            Date s1Fin = sdf.parse("09:30");

            Date s2Deb = sdf.parse("09:35");
            Date s2Fin = sdf.parse("11:05");

            Date s3Deb = sdf.parse("11:10");
            Date s3Fin = sdf.parse("12:40");

            Date s4Deb = sdf.parse("13:15");
            Date s4Fin = sdf.parse("14:45");

            Date s5Deb = sdf.parse("14:50");
            Date s5Fin = sdf.parse("16:20");

            Date s6Deb = sdf.parse("16:25");
            Date s6Fin = sdf.parse("17:55");

            //Vendredi soir
            Date s4DebVen = sdf.parse("14:00");
            Date s4FinVen = sdf.parse("15:30");

            Date s5DebVen = sdf.parse("15:35");
            Date s5FinVen = sdf.parse("17:05");


            if (jourNum != 4 ) {
                if ((current.compareTo(s1Deb) == 1 || current.compareTo(s1Deb) == 0) && current.compareTo(s1Fin) == -1) {
                    currentSession = 1;
                } else if ((current.compareTo(s2Deb) == 1 || current.compareTo(s2Deb) == 0) && current.compareTo(s2Fin) == -1) {
                    currentSession = 2;
                } else if ((current.compareTo(s3Deb) == 1 || current.compareTo(s3Deb) == 0) && current.compareTo(s3Fin) == -1) {
                    currentSession = 3;
                } else if ((current.compareTo(s4Deb) == 1 || current.compareTo(s4Deb) == 0) && current.compareTo(s4Fin) == -1) {
                    currentSession = 4;
                } else if ((current.compareTo(s5Deb) == 1 || current.compareTo(s5Deb) == 0) && current.compareTo(s5Fin) == -1) {
                    currentSession = 5;
                } else if ((current.compareTo(s6Deb) == 1 || current.compareTo(s6Deb) == 0) && current.compareTo(s6Fin) == -1) {
                    currentSession = 6;
                } else {
                    currentSession = 0;
                }
            }
            else {
                if ((current.compareTo(s1Deb) == 1 || current.compareTo(s1Deb) == 0) && current.compareTo(s1Fin) == -1) {
                    currentSession = 1;
                } else if ((current.compareTo(s2Deb) == 1 || current.compareTo(s2Deb) == 0) && current.compareTo(s2Fin) == -1) {
                    currentSession = 2;
                } else if ((current.compareTo(s3Deb) == 1 || current.compareTo(s3Deb) == 0) && current.compareTo(s3Fin) == -1) {
                    currentSession = 3;
                } else if ((current.compareTo(s4DebVen) == 1 || current.compareTo(s4DebVen) == 0) && current.compareTo(s4FinVen) == -1) {
                    currentSession = 4;
                } else if ((current.compareTo(s5DebVen) == 1 || current.compareTo(s5DebVen) == 0) && current.compareTo(s5FinVen) == -1) {
                    currentSession = 5;
                }  else {
                    currentSession = 0;
                }
            }




        } catch (ParseException e) {
            Log.d("err", e.toString());
        }
    }


    public void getJourNum() {

        if (LUNDI.contains(currentDay.toLowerCase())) {

            jourNum = 0;


        } else if (MARDI.contains(currentDay.toLowerCase())) {

            jourNum = 1;
        } else if (MERCREDI.contains(currentDay.toLowerCase())) {

            jourNum = 2;
        } else if (JEUDI.contains(currentDay.toLowerCase())) {

            jourNum = 3;
        } else if (VENDREDI.contains(currentDay.toLowerCase())) {

            jourNum = 4;
        } else if (SAMEDI.contains(currentDay.toLowerCase())) {

            jourNum = 5;
        } else {
            jourNum = 6;
        }

    }














    //utilisé par addTouchListener du recyclerView en NavigationDrawerFragment
    public void deconnexion(){

            finish();


    }


    //SETUP the FAB
    private void setUpFab(){

        FloatingActionButton fab = findViewById(R.id.fab);
        //fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AjoutPost.class);

                startActivity(intent);
            }
        });


    }



    //download img using volley
        public void dimg(String url) {
            // Initialize a new RequestQueue instance
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            // Initialize a new ImageRequest
            ImageRequest imageRequest = new ImageRequest(url,getResources(),getContentResolver(),new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //mmm.setImageBitmap(response);
                }
            },0,0, ImageView.ScaleType.CENTER_CROP,Bitmap.Config.RGB_565,new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                }
            });


            // Add ImagFeRequest to the RequestQueue
            requestQueue.add(imageRequest);
        }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);

        MenuItem search=menu.findItem(R.id.search_action);
        search.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent searchIntent = new Intent(getApplicationContext(),SearchActivity.class);
                if (viewPager.getCurrentItem() == 0) startActivity(searchIntent);
                return false;
            }
        });
        searchView=(android.support.v7.widget.SearchView)search.getActionView();
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (viewPager.getCurrentItem() == 0 ) {

                }*/
                Intent searchIntent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                currentFragment = viewPager.getCurrentItem();


                if (currentFragment == 1) {
                    postAdapter.getFilter().filter(s);
                }




                return false;
            }


            });
        return true;
    }
}