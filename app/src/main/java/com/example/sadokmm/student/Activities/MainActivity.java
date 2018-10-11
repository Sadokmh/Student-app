package com.example.sadokmm.student.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.sadokmm.student.Fragments.NavigationDrawerFragment;
import com.example.sadokmm.student.Objects.Emploi;
import com.example.sadokmm.student.Objects.Groupe;
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

import static com.example.sadokmm.student.Fragments.LoginFragment.admin;


public class MainActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar toolbar;

    private RecyclerView rv;
    private MainRvAdapter mAdapter;
    public static List<Info> myList = new ArrayList<>();
    public static Info maFiliere;
    public static Groupe monGroupe;
    public static String currentTime;
    public static int currentSession;
    public static String currentFullTime;
    public static String currentDay;
    public static String currentDate;
    public static Date laDate;
    public static int jourNum;
    public static Seance seanceActuelle,seanceVide,weekend;
    public static Emploi monEmploi;

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

        mmm=(ImageView)findViewById(R.id.mmmm);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Student");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Toast.makeText(this, getStringDate(), Toast.LENGTH_LONG).show();
        getTime();

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.settUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerLayout), toolbar);

        weekend = new Seance("Bon Weekend " , "","","",0,false);
        seanceVide = new Seance("Pas de cours pour l'instant","","","",0,false);
        seanceActuelle = seanceVide;
        getJourNum();

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


        mmm.setImageBitmap(admin.getImg());


    }



    //charger personnes:
       /* public void chargerUsers(){
        AQuery aq = new AQuery(this);
        String url = "http://192.168.2.127:8080/student/getusers";
        aq.ajax(url,JSONArray.class,this,"userCallback");
        }

        public void userCallback(String url,JSONArray jsonArray,AjaxStatus status){


            if (jsonArray != null ) {
                try {
                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                    String nomprenom = jsonObject.getString("nom");
                    nomprenom += " " + jsonObject.getString("prenom");

                    AQuery aq = new AQuery(this);
                    //aq.id(R.id.myimg).image("");

                    Toast.makeText(this, nomprenom, Toast.LENGTH_LONG).show();
                }
                catch (JSONException e) {
                    Toast.makeText(this,e.toString() +" " ,Toast.LENGTH_LONG).show();
                }
            }

            else {
                Toast.makeText(this,"null",Toast.LENGTH_LONG).show();
            }

        }*/





    public void chargerMonEmploi() {

        String url = "http://192.168.2.127:8080/student/gett";

        aq.ajax(url, JSONObject.class, this, "chEmp");

    }


    public void chEmp(String url, JSONObject jsonObject, AjaxStatus status) {


        if (jsonObject != null)


        {

            Toast.makeText(this, "not null", Toast.LENGTH_LONG).show();

            try {

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                JSONObject myObject = jsonArray.getJSONObject(0);


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
                        Boolean pq=false;
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
                editor.apply();


                Toast.makeText(this,monEmploi.getJours().size()+" ",Toast.LENGTH_LONG).show();
            } catch (JSONException e) {

                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(this, "null", Toast.LENGTH_LONG).show();
        }


        actuelle();


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


    void actuelle() {

        TextView numSeance, matiere, enseignant, salle, parQuinzaine, type, sTime;
        LinearLayout parQuinzaineLayout, actuelle, coursLayout, profLayout, salleLayout;

        numSeance = (TextView) findViewById(R.id.numSeance);
        matiere = (TextView) findViewById(R.id.matiere);
        enseignant = (TextView) findViewById(R.id.enseignant);
        salle = (TextView) findViewById(R.id.salle);
        type = (TextView) findViewById(R.id.type);
        parQuinzaine = (TextView) findViewById(R.id.parQuinziane);
        parQuinzaineLayout = (LinearLayout) findViewById(R.id.parQuinzaineLayout);
        actuelle = (LinearLayout) findViewById(R.id.actuelle);
        sTime = (TextView) findViewById(R.id.sTime);
        salleLayout = (LinearLayout) findViewById(R.id.salleLayout);
        profLayout = (LinearLayout) findViewById(R.id.profLayout);
        coursLayout = (LinearLayout) findViewById(R.id.coursLayout);

        if (currentSession != 0) {
            if (jourNum == 0) {

                for (int i = 0; i < monEmploi.getJours().get(0).getListSeance().size(); i++) {

                    if (monEmploi.getJours().get(0).getListSeance().get(i).getNumSeance() == currentSession) {
                        //Toast.makeText(this,"hani lenaAA",Toast.LENGTH_LONG).show();
                        seanceActuelle = monEmploi.getJours().get(0).getListSeance().get(i);
                        //Toast.makeText(this,"hani lena" + monEmploi.getJours().get(0).getListSeance().get(i).getMatiere(),Toast.LENGTH_LONG).show();

                        break;
                    }
                }


            } else if (jourNum == 1) {

                for (int i = 0; i < monEmploi.getJours().get(1).getListSeance().size(); i++) {
                    if (monEmploi.getJours().get(1).getListSeance().get(i).getNumSeance() == currentSession) {
                        seanceActuelle = monEmploi.getJours().get(1).getListSeance().get(i);
                        break;
                    }


                }
            } else if (jourNum == 2) {

                for (int i = 0; i < monEmploi.getJours().get(2).getListSeance().size(); i++) {
                    if (monEmploi.getJours().get(2).getListSeance().get(i).getNumSeance() == currentSession) {
                        seanceActuelle = (monEmploi.getJours().get(2).getListSeance().get(i));
                    }
                }
            } else if (jourNum == 3) {

                for (int i = 0; i < monEmploi.getJours().get(3).getListSeance().size(); i++) {
                    if (monEmploi.getJours().get(3).getListSeance().get(i).getNumSeance() == currentSession) {
                        seanceActuelle = (monEmploi.getJours().get(3).getListSeance().get(i));
                    }
                }
            } else if (jourNum == 4) {

                for (int i = 0; i < monEmploi.getJours().get(4).getListSeance().size(); i++) {
                    if (monEmploi.getJours().get(4).getListSeance().get(i).getNumSeance() == currentSession) {
                        seanceActuelle = (monEmploi.getJours().get(4).getListSeance().get(i));
                    }
                }
            } else if (jourNum == 5) {

                for (int i = 0; i < monEmploi.getJours().get(5).getListSeance().size(); i++) {
                    if (monEmploi.getJours().get(5).getListSeance().get(i).getNumSeance() == currentSession) {
                        seanceActuelle = (monEmploi.getJours().get(5).getListSeance().get(i));
                    }
                }
            }
        } else {
            matiere.setText("Pas de cours maintenant");
            seanceActuelle = seanceVide;
        }




        matiere.setText(seanceActuelle.getMatiere());
        enseignant.setText(seanceActuelle.getEnseignant());
        type.setText(seanceActuelle.getType());
        salle.setText(seanceActuelle.getSalle());


        if (seanceActuelle.getNumSeance() == 1) {
            numSeance.setText("1ère Séance : ");
            sTime.setText("8:00 - 9:30");
        } else if (seanceActuelle.getNumSeance() == 2) {
            numSeance.setText("2ème Séance : ");
            sTime.setText("9:35 - 11:05");
        } else if (seanceActuelle.getNumSeance() == 3) {
            numSeance.setText("3ème Séance : ");
            sTime.setText("11:10 - 12:40");
        } else if (seanceActuelle.getNumSeance() == 4) {
            numSeance.setText("4ème Séance : ");
            sTime.setText("13:15 - 14:45");
        } else if (seanceActuelle.getNumSeance() == 5) {
            numSeance.setText("5ème Séance : ");
            sTime.setText("14:50 - 16:20");
        } else if (seanceActuelle.getNumSeance() == 6) {
            numSeance.setText("6ème Séance : ");
            sTime.setText("16:25 - 17:55");
        } else {
            numSeance.setText("");
            sTime.setText("");
            coursLayout.setVisibility(View.INVISIBLE);
            profLayout.setVisibility(View.INVISIBLE);
            salleLayout.setVisibility(View.INVISIBLE);
        }


        if (seanceActuelle.getParQuanzaine()) {
            parQuinzaineLayout.setVisibility(View.VISIBLE);
        } else {
            parQuinzaineLayout.setVisibility(View.INVISIBLE);
        }


        if (currentSession == seanceActuelle.getNumSeance()) {
            actuelle.setVisibility(View.VISIBLE);
        }

        Toast.makeText(this,"hggg" + jourNum,Toast.LENGTH_LONG).show();

    }


    //test connexion

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();


    }

    public void voirJournee(View view) {

        Intent intent = new Intent(getApplicationContext(), AfficheJournee.class);
        startActivity(intent) ;

    }




    // charger liste STATIQUE
        public void chargerListeStatique(){
        /*Info info=new Info("LFSI",3,2);
        info.getEmploi().add(new Groupe("G2"));
        info.getEmploi().get(0).getJourListe().add(new Jour("Lundi"));
        info.getEmploi().get(0).getJourListe().get(0).getListSeance().add(new Seance("Principe de Gestion","Hanen Bedoui","Amphi B","cours",1,false));
        info.getEmploi().get(0).getJourListe().get(0).getListSeance().add(new Seance("Microeconomie","Hanen Bedoui","GS5","cours",2,false));
        info.getEmploi().get(0).getJourListe().get(0).getListSeance().add(new Seance("ASD","Hanen Bedoui","Amphi A","cours",3,false));
        info.getEmploi().get(0).getJourListe().get(0).getListSeance().add(new Seance("Prog C","Hanen Bedoui","D14","TD",4,false));
        info.getEmploi().get(0).getJourListe().get(0).getListSeance().add(new Seance("Maths","Hanen Bedoui","A21","TD",5,false));


        info.getEmploi().get(0).getJourListe().add(new Jour("Mardi"));
        info.getEmploi().get(0).getJourListe().get(1).getListSeance().add(new Seance("Maths MARDI" , "Majdi Agroubi","E6","Cours",1,true));
        info.getEmploi().get(0).getJourListe().get(1).getListSeance().add(new Seance("Prog C MARDI" , "Tarek Berhouma","C14","TD",3,false));
        info.getEmploi().get(0).getJourListe().get(1).getListSeance().add(new Seance("Compilation MARDI" , "Souad Ghorbel","c14","TP",4,false));
        info.getEmploi().get(0).getJourListe().get(1).getListSeance().add(new Seance("Techniques Multimédia MARDI" , "Neila Hochlef","c14","TP",5,false));
        info.getEmploi().get(0).getJourListe().get(1).getListSeance().add(new Seance("Sécurite informatique MARDI" , "Yamen Mkadem","c14","cours",6,true));

        info.getEmploi().get(0).getJourListe().add(new Jour("Mercredi"));
        info.getEmploi().get(0).getJourListe().get(2).getListSeance().add(new Seance("Maths MERCREDI" , "Majdi Agroubi","E6","Cours",1,true));
        info.getEmploi().get(0).getJourListe().get(2).getListSeance().add(new Seance("Prog C MERCREDI" , "Tarek Berhouma","C14","TD",3,false));
        info.getEmploi().get(0).getJourListe().get(2).getListSeance().add(new Seance("Compilation MERCREDI" , "Souad Ghorbel","c14","TP",4,false));
        info.getEmploi().get(0).getJourListe().get(2).getListSeance().add(new Seance("Techniques Multimédia MERCREDI" , "Neila Hochlef","c14","TP",5,false));
        info.getEmploi().get(0).getJourListe().get(2).getListSeance().add(new Seance("Sécurite informatique MERCREDI" , "Yamen Mkadem","c14","cours",6,true));

        info.getEmploi().get(0).getJourListe().add(new Jour("Jeudi"));
        info.getEmploi().get(0).getJourListe().get(3).getListSeance().add(new Seance("Maths JEUDI" , "Majdi Agroubi","E6","Cours",1,true));
        info.getEmploi().get(0).getJourListe().get(3).getListSeance().add(new Seance("Prog C JEUDI" , "Tarek Berhouma","C14","TD",3,false));
        info.getEmploi().get(0).getJourListe().get(3).getListSeance().add(new Seance("Compilation JEUDI" , "Souad Ghorbel","c14","TP",4,false));
        info.getEmploi().get(0).getJourListe().get(3).getListSeance().add(new Seance("Techniques Multimédia JEUDI" , "Neila Hochlef","c14","TP",5,false));
        info.getEmploi().get(0).getJourListe().get(3).getListSeance().add(new Seance("Sécurite informatique JEUDI" , "Yamen Mkadem","c14","cours",6,true));

        info.getEmploi().get(0).getJourListe().add(new Jour("Vendredi"));
        info.getEmploi().get(0).getJourListe().get(4).getListSeance().add(new Seance("Maths VENDREDI" , "Majdi Agroubi","E6","Cours",1,true));
        info.getEmploi().get(0).getJourListe().get(4).getListSeance().add(new Seance("Prog C VENDREDI" , "Tarek Berhouma","C14","TD",3,false));
        info.getEmploi().get(0).getJourListe().get(4).getListSeance().add(new Seance("Compilation VENDREDI" , "Souad Ghorbel","c14","TP",4,false));
        info.getEmploi().get(0).getJourListe().get(4).getListSeance().add(new Seance("Techniques Multimédia VENDREDI" , "Neila Hochlef","c14","TP",5,false));
        info.getEmploi().get(0).getJourListe().get(4).getListSeance().add(new Seance("Sécurite informatique VENDREDI" , "Yamen Mkadem","c14","cours",6,true));

        info.getEmploi().get(0).getJourListe().add(new Jour("Samedi"));
        info.getEmploi().get(0).getJourListe().get(5).getListSeance().add(new Seance("Maths SAMEDI" , "Majdi Agroubi","E6","Cours",1,true));
        info.getEmploi().get(0).getJourListe().get(5).getListSeance().add(new Seance("Prog C SAMEDI" , "Tarek Berhouma","C14","TD",3,false));
        info.getEmploi().get(0).getJourListe().get(5).getListSeance().add(new Seance("Compilation SAMEDI" , "Souad Ghorbel","c14","TP",4,false));
        info.getEmploi().get(0).getJourListe().get(5).getListSeance().add(new Seance("Techniques Multimédia SAMEDI" , "Neila Hochlef","c14","TP",5,false));
        info.getEmploi().get(0).getJourListe().get(5).getListSeance().add(new Seance("Sécurite informatique SAMEDI" , "Yamen Mkadem","c14","cours",6,true));






        info.getEmploi().add(new Groupe("G1"));
        info.getEmploi().get(1).getJourListe().add(new Jour("lun"));
        info.getEmploi().get(1).getJourListe().get(0).getListSeance().add(new Seance("PPPG1","Hanen Bedoui","Amphi B","cours",1,false));
        info.getEmploi().get(1).getJourListe().get(0).getListSeance().add(new Seance("Eco G1","Hanen Bedoui","GS5","cours",2,false));
        info.getEmploi().get(1).getJourListe().get(0).getListSeance().add(new Seance("ASD G1","Hanen Bedoui","Amphi A","cours",3,false));
        info.getEmploi().get(1).getJourListe().get(0).getListSeance().add(new Seance("G1 Prog C","Hanen Bedoui","D14","TD",4,false));
        info.getEmploi().get(1).getJourListe().get(0).getListSeance().add(new Seance("G1 Maths","Hanen Bedoui","A21","TD",5,false));


        info.getEmploi().get(1).getJourListe().add(new Jour("mar"));
        info.getEmploi().get(1).getJourListe().get(1).getListSeance().add(new Seance("Maths" , "Majdi Agroubi","E6","Cours",1,true));
        info.getEmploi().get(1).getJourListe().get(1).getListSeance().add(new Seance("Prog C" , "Tarek Berhouma","C14","TD",3,false));
        info.getEmploi().get(1).getJourListe().get(1).getListSeance().add(new Seance("Techniques Multimédia" , "Neila Hochlef","c14","TP",4,false));


        myList.add(info);


        getJourNum();


        String grp = "G2";
        String fil = "LFSI";

        for (int i = 0; i < myList.size(); i++) {
            if (myList.get(i).getFiliere().equals(fil)) {
                maFiliere = myList.get(i);
            }
        }

        for (int i = 0; i < maFiliere.getEmploi().size(); i++) {
            if (maFiliere.getEmploi().get(i).getNom().equals(grp)) {
                monGroupe = maFiliere.getEmploi().get(i);
            }
        }*/
        }


}