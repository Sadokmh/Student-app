package com.example.sadokmm.student.Services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.sadokmm.student.NotificationComment;
import com.example.sadokmm.student.Objects.InfoPostCom;
import com.example.sadokmm.student.Objects.Post;
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

public class ServiceCommentNotifcation extends Service {

    public static Boolean SERVICE_IS_RUN = false;
    private Timer timer ;
    private ArrayList<Post> listPost ;
    public static String idUsr;
    private ArrayList<InfoPostCom> listInfo = new ArrayList<>();

    private final String  POST_ADMIN_FILE = "post_file";


    TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {

            SERVICE_IS_RUN = true;
            for (int i = 0; i < listInfo.size(); i++) {
                chercherNouvComm(listInfo.get(i).getIdPoste(), listInfo.get(i).getIdCom());
            }


        }


    };


    public ServiceCommentNotifcation() {
    }


    @Override
    public void onCreate() {

        super.onCreate();
        //composerMsg("Sadok","Mhiri","https://cdn-images-1.medium.com/max/1000/1*L6n8KHbg7qaOX1j4LhJcHA.jpeg","tetetete");




        SharedPreferences sp = getSharedPreferences(POST_ADMIN_FILE,MODE_PRIVATE);
        Boolean verif = sp.getBoolean("verif",false);
        ArrayList<InfoPostCom> ltPost = readFromSharedPref();

        if (!verif) {
            chargerPost();
        }
        else {
            listInfo = ltPost;
            timer = new Timer();
            timer.schedule(timerTask,2000,2*1000);
        }



    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }




    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }






    public void chargerPost(){

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = publicUrl + "student/findpostuser";

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, url,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listPost = new ArrayList<>();

                try {
                    //if (listPost.size() != 0 ) listPost.clear();

                    JSONArray tab = new JSONArray(response);

                    for (int i = 0; i < tab.length(); i++) {
                        JSONObject postJson = tab.getJSONObject(i);
                        String id , txtpost, datepost, id_usr;
                        id = postJson.getString("_id");
                        txtpost = postJson.getString("txtpost");
                        datepost = postJson.getString("datepost");
                        id_usr = postJson.getString("idusr");
                        JSONArray imgpost = postJson.getJSONArray("imgpost");
                        ArrayList<String> imgListPost = new ArrayList<>();
                        for (int j=0 ; j<imgpost.length() ; j++) {
                            imgListPost.add(imgpost.getString(j));
                        }

                        //getImageByUrl(publicUrl + imgpost, i);
                        Post p = new Post(txtpost, id_usr, imgListPost , id );
                        p.setDatepost(datepost);
                        listPost.add(p);



                    }
                    Toast.makeText(getApplicationContext(),listPost.size()+ " size ",Toast.LENGTH_LONG).show();
                    charger(idUsr);

                    //Toast.makeText(getContext(),,Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        smr.addStringParam("iduser",idUsr);
        requestQueue.add(smr);


    }








    public void charger(String user_id){

        listInfo = new ArrayList<>();

        for(int i=0 ; i<listPost.size() ; i++){

            if (listPost.get(i).getIdusr().equals(user_id)) {
                if (listPost.get(i).getCommentList().size() == 0) {
                    InfoPostCom info = new InfoPostCom(listPost.get(i).getId(), "null");
                    listInfo.add(info);
                }
                else {
                    InfoPostCom info = new InfoPostCom(listPost.get(i).getId(), listPost.get(i).getCommentList().get(listPost.get(i).getCommentList().size() - 1).getId());
                    listInfo.add(info);
                }
            }
            saveToSharedPref(listInfo);

        }


        timer = new Timer();
        timer.schedule(timerTask,2000,2*1000);
    }







    public void chercherNouvComm(final String idPost , String idCom) {

        String url = publicUrl + "student/findpostcomment";
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (!response.equals("null")) {

                    try {
                        JSONObject CommJson = new JSONObject(response);
                        {
                            String idComm, txtComm, dateComm, id_user;
                            idComm = CommJson.getString("_id");
                            txtComm = CommJson.getString("txtcom");
                            dateComm = CommJson.getString("datecom");
                            id_user = CommJson.getString("idusr");


                            for (int i = 0; i < listInfo.size(); i++) {
                                if (listInfo.get(i).getIdPoste().equals(idPost)) {
                                    listInfo.get(i).setIdCom(idComm);
                                    saveToSharedPref(listInfo);
                                    break;
                                }
                            }

                            if (!id_user.equals(idUsr)) {

                                chercherUserById(id_user,idPost);

                            }

                        }

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        smr.addStringParam("idpost",idPost);
        smr.addStringParam("idcomment",idCom);

        requestQueue.add(smr);

    }





    public void saveToSharedPref(ArrayList<InfoPostCom> posts){

        SharedPreferences.Editor editor = getSharedPreferences(POST_ADMIN_FILE,MODE_PRIVATE).edit();
        editor.clear();
        Gson gson = new Gson();
        String listePost = gson.toJson(posts);
        editor.putString("listpost",listePost);
        editor.putBoolean("verif",true);
        editor.commit();

    }


    public ArrayList<InfoPostCom> readFromSharedPref() {
        ArrayList<InfoPostCom> list = new ArrayList<>();
        SharedPreferences sp = getSharedPreferences(POST_ADMIN_FILE,MODE_PRIVATE);
        if (!sp.getBoolean("verif",false)) {
            return null;
        }
        else {
            String lp = sp.getString("listpost","null");
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<InfoPostCom>>(){}.getType();
            list = gson.fromJson(lp,type);
            return (list);
        }
    }






    public void chercherUserById(final String idd , final String idPost) {


        String url= publicUrl +"student/getuser/"+idd;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {

                    String  nom , prenom  , imgUrl ;
                    nom=jsonObject.getString("nom");
                    prenom=jsonObject.getString("prenom");
                    imgUrl=publicUrl+jsonObject.getString("img");





                    composerMsg(nom , prenom , imgUrl , idPost );



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



    private void composerMsg(String nom , String prenom , final String imgUrl , final String idPost) {

        final String msg = prenom + " " + nom + " a commenté votre publication";

        AQuery aq = new AQuery(this);
        aq.ajax(imgUrl, Bitmap.class, 0, new AjaxCallback<Bitmap>() {
            @Override
            public void callback(String url, Bitmap object, AjaxStatus status) {
                super.callback(url, object, status);
                NotificationComment n = new NotificationComment(getApplicationContext(),"ISG SOUSSE",msg,object,idPost);
                n.createNotification();

            }
        });


        // creat new intent
        /*Intent intent = new Intent();
        //set the action that will receive our broadcast
        intent.setAction("com.example.sadokmm.student.Broadcasts");
        // add data to the bundle
        intent.putExtra("liste", msg);
        intent.putExtra("img",imgUrl);
        intent.putExtra("idpost",idPost);
        // send the data to broadcast
        sendBroadcast(intent);*/
        //delay for 50000ms


    }


}
