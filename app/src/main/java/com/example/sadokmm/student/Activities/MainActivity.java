package com.example.sadokmm.student.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.example.sadokmm.student.Adapters.MainRvAdapter;
import com.example.sadokmm.student.Adapters.PageAdapterFirst;
import com.example.sadokmm.student.Adapters.PageAdapterMain;
import com.example.sadokmm.student.Adapters.PostAdapter;
import com.example.sadokmm.student.Fragments.NavigationDrawerFragment;
import com.example.sadokmm.student.Objects.Emploi;
import com.example.sadokmm.student.Objects.Groupe;
import com.example.sadokmm.student.Objects.Info;
import com.example.sadokmm.student.Objects.Jour;
import com.example.sadokmm.student.Objects.Post;
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

import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;


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
    public static Emploi monEmploi;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PageAdapterMain pageAdapter;

    public static final String MY_SP_FILE = "com.example.sadokmm.student.activities.monemploi";


    private TextView afficheJournee;

    private ImageView mmm;


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

        Toast.makeText(this, getStringDate(), Toast.LENGTH_LONG).show();


        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.settUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerLayout), toolbar);

        weekend = new Seance("Bon Weekend " , "","","",0,false);
        seanceVide = new Seance("Pas de cours pour l'instant","","","",0,false);
        seanceActuelle = seanceVide;
        getJourNum();
        getTime();

        if (isNetworkAvailable()) {


            Toast.makeText(this,"Connecté ! ",Toast.LENGTH_SHORT).show();

            chargerMonEmploi();

        }

        else {

            Toast.makeText(this,"non connecté !",Toast.LENGTH_SHORT).show();


            SharedPreferences sp=getSharedPreferences("monemploi",MODE_PRIVATE);
            String monemploi = sp.getString("emploi","a");

            if (monemploi.equals("a")) {
                Toast.makeText(this,"Pas d'emploi enregistré " , Toast.LENGTH_LONG).show();
            }

            else {

                Gson gson = new Gson();

                monEmploi = gson.fromJson(monemploi, Emploi.class);

            }


        }


        afficheJournee = (TextView) findViewById(R.id.afficheJournee);

    }


    public void chargerMonEmploi() {

        String filiere = admin.getFiliere();
        int niveau = admin.getNiveau();
        int groupe = admin.getGroupe();

        String url = publicUrl + "getemploi/"+filiere+"/"+niveau+"/"+groupe;

//        Snackbar.make(getCurrentFocus(),url,Snackbar.LENGTH_LONG).show();

        aq.ajax(url, JSONObject.class, this, "chEmp");

    }


    public void chEmp(String url, JSONObject myObject, AjaxStatus status) {


        if (myObject != null)


        {

            Toast.makeText(this, "not null", Toast.LENGTH_LONG).show();

            try {

                //JSONArray jsonArray = jsonObject.getJSONArray("data");
                //JSONObject myObject = jsonArray.getJSONObject(0);


                String id = myObject.getString("_id");
                String maFiliere = myObject.getString("filiere");
                int niveau = Integer.parseInt(myObject.getString("niveau"));
                int groupe = Integer.parseInt(myObject.getString("groupe"));

                monEmploi = new Emploi(id, maFiliere, niveau, groupe);

                Toast.makeText(this, "sna3t groupe", Toast.LENGTH_LONG).show();

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
                    Toast.makeText(this, "Emploi ajouté avec succées", Toast.LENGTH_LONG).show();
                }

              //Enregistrer une copie local de l'emploi sur le téléphone
                Gson gson = new Gson();
                String monEmploiEnJson = gson.toJson(monEmploi);

                SharedPreferences.Editor editor=getSharedPreferences("monemploi",Context.MODE_PRIVATE).edit();
                editor.putString("emploi",monEmploiEnJson);
                editor.commit();


                Toast.makeText(this,monEmploi.getJours().size()+" ",Toast.LENGTH_LONG).show();
            } catch (JSONException e) {

                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(this, "null", Toast.LENGTH_LONG).show();
        }


        //actuelle();


    }


    public String getStringDate() {
        Date mydate = Calendar.getInstance().getTime();

        DateFormat df = new SimpleDateFormat("EEE");
        String date = df.format(mydate);

        DateFormat currentDf = new SimpleDateFormat("HH:mm");
        currentTime = currentDf.format(mydate);
        Toast.makeText(this, "current time: " + currentTime, Toast.LENGTH_LONG).show();

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





    //test connexion

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();


    }









    //utilisé par addTouchListener du recyclerView en NavigationDrawerFragment
    public void deconnexion(){

            finish();


    }

}