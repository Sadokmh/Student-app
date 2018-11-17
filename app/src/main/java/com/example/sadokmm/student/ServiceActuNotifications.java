package com.example.sadokmm.student;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.example.sadokmm.student.Objects.Actualite;
import com.example.sadokmm.student.Objects.InfoPostCom;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;


public class ServiceActuNotifications extends Service {

    public static Boolean SERVICE_IS_RUN = false ;
    public String titreOf = " " ;
    public ArrayList<Actualite> listAct= new ArrayList<>();

    private Timer mTimer;

    private final String FILE_LAST_ACTU = "lastactu";

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sp = getSharedPreferences(FILE_LAST_ACTU,MODE_PRIVATE);
        Boolean verif = sp.getBoolean("verif",false);
        if (!verif) {
            chargerActu();
        }
        else {
            titreOf = sp.getString("lastact","null");
            mTimer = new Timer();
            mTimer.schedule(timerTask,30000,2*10000);
        }



    }


    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {

            {

                SERVICE_IS_RUN = true;
                    String url = publicUrl + "student/getisgnews/hh";

                    try {
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                JSONArray tab = null;
                                try {
                                    tab = new JSONArray(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (tab.length() != 0) {
                                    try {
                                        for (int i = 0; i < tab.length(); i++) {
                                            JSONObject actu = null;

                                            actu = tab.getJSONObject(i);

                                            Actualite act = new Actualite(actu.getString("titre"), actu.getString("txt"), actu.getString("date"), actu.getString("lien"));
                                            listAct.add(act);

                                        }
                                        titreOf = tab.getJSONObject(0).getString("titre");
                                    } catch (JSONException e) {
                                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                    }

                                    // creat new intent
                                    Intent intent = new Intent();
                                    //set the action that will receive our broadcast
                                    intent.setAction("com.example.sadokmm.student.Broadcasts");
                                    // add data to the bundle
                                    intent.putExtra("liste", titreOf);
                                    // send the data to broadcast
                                    sendBroadcast(intent);
                                    //delay for 50000ms

                                } else {

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });

                        smr.addStringParam("title", titreOf);
                        requestQueue.add(smr);


                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }

                    try {
                        Thread.sleep(7000);
                    } catch (Exception ex) {
                    }

                }

            }

        //}
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);



        return START_STICKY;
    }


    public void work() {



    }


    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    private void chargerActu(){



        String url = publicUrl + "student/getisgnews";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String text , titre , date , lien ;
                try {


                    for (int i=0 ; i<response.length() ; i++) {
                        JSONObject act = response.getJSONObject(i);
                        titre = act.getString("titre");
                        text = act.getString("txt");
                        date = act.getString("date");
                        lien = act.getString("lien");


                        listAct.add(new Actualite(titre,text,date,lien));

                    }


                    titreOf = listAct.get(0).getTitre();
                    saveToSharedPref(titreOf);
                    mTimer = new Timer();
                    mTimer.schedule(timerTask,2000,2*1000);

                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }



    public void saveToSharedPref(String lastAct){

        SharedPreferences.Editor editor = getSharedPreferences(FILE_LAST_ACTU,MODE_PRIVATE).edit();
        editor.clear();

        editor.putString("lastact",lastAct);
        editor.putBoolean("verif",true);
        editor.commit();

    }


    public String readFromSharedPref() {
        //ArrayList<String> list = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences(FILE_LAST_ACTU,MODE_PRIVATE);
        if (!sp.getBoolean("verif",false)) {
            return null;
        }
        else {
            String lp = sp.getString("lastact","null");

            return lp;
        }
    }

}

