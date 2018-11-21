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
import com.android.volley.request.JsonArrayRequest;
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

import static com.example.sadokmm.student.Activities.firstActivity.contextFirst;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class ServiceCommentNotifcation extends Service {

    public static Boolean SERVICE_IS_RUN = false;
    private Timer timer ;
    private ArrayList<Post> listPost ;
    public static String idUsr;
    private ArrayList<InfoPostCom> listInfo = new ArrayList<>();

    public static Boolean verif ;

    private final String  POST_ADMIN_FILE = "post_file";

    AQuery aq ;
    private RequestQueue requestQueueNouvCom ;
    private RequestQueue requestQueueCherchUsr ;
    private RequestQueue requestQueuePostUser ;
    private RequestQueue requestQueueChargLikes;
    private final String urlChargLikes = publicUrl + "student/postlikes/";
    private final String urlNouvCom = publicUrl + "student/findpostcomment";
    private final String urlCherchUser= publicUrl +"student/getuser/";
    private final String urlPostUser = publicUrl + "student/findpostuser";



    TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {

            SERVICE_IS_RUN = true;
            for (int i = 0; i < listInfo.size(); i++) {
                chercherNouvComm(listInfo.get(i).getIdPoste(), listInfo.get(i).getIdCom());
                chargerLikes(listInfo.get(i).getIdPoste(),listInfo.get(i).getLike());
            }


        }


    };


    public ServiceCommentNotifcation() {
    }


    @Override
    public void onCreate() {

        super.onCreate();

        aq = new AQuery(getApplicationContext());
        requestQueueNouvCom = Volley.newRequestQueue(getApplicationContext());
        requestQueueCherchUsr = Volley.newRequestQueue(getApplicationContext());
        requestQueuePostUser = Volley.newRequestQueue(getApplicationContext());
        requestQueueChargLikes = Volley.newRequestQueue(getApplicationContext());



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




        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, urlPostUser,  new Response.Listener<String>() {
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
        requestQueuePostUser.add(smr);


    }








    public void charger(String user_id){

        listInfo = new ArrayList<>();

        for(int i=0 ; i<listPost.size() ; i++){

            if (listPost.get(i).getIdusr().equals(user_id)) {
                if (listPost.get(i).getCommentList().size() == 0) {
                    if (listPost.get(i).getCommentList().size() == 0) {
                        InfoPostCom info = new InfoPostCom(listPost.get(i).getId(), "null", "null");
                        listInfo.add(info);
                    }
                    else {
                        InfoPostCom info = new InfoPostCom(listPost.get(i).getId(), "null", listPost.get(i).getListLikes().get(listPost.get(i).getListLikes().size() - 1));
                        listInfo.add(info);
                    }
                }
                else {
                    if (listPost.get(i).getListLikes().size() == 0) {
                        InfoPostCom info = new InfoPostCom(listPost.get(i).getId(), listPost.get(i).getCommentList().get(listPost.get(i).getCommentList().size() - 1).getId(), "null");
                        listInfo.add(info);
                    }
                    else {
                        InfoPostCom info = new InfoPostCom(listPost.get(i).getId(), listPost.get(i).getCommentList().get(listPost.get(i).getCommentList().size() - 1).getId(), listPost.get(i).getListLikes().get(listPost.get(i).getListLikes().size() - 1));
                        listInfo.add(info);
                    }
                }
            }
            saveToSharedPref(listInfo);

        }


        timer = new Timer();
        timer.schedule(timerTask,2000,2*1000);
    }









        //charger les j'aimes de la publication
        public void chargerLikes(final String idPost , final String like){



            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlChargLikes + idPost, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {



                    // Toast.makeText(context,response.length()+"" , Toast.LENGTH_LONG).show();

                    try {
                            if (response.length() != 0) {
                                String usrId = response.get(response.length()-1).toString();
                                if (!usrId.equals(like)) {
                                    for (int i = 0; i < listInfo.size(); i++) {
                                        if (listInfo.get(i).getIdPoste().equals(idPost)) {
                                            listInfo.get(i).setLike(usrId);
                                            saveToSharedPref(listInfo);
                                            break;
                                        }
                                    }
                                    if (!usrId.equals(idUsr)) {

                                        chercherUserById(usrId,idPost,"like");

                                    }

                                }

                            }




                    }
                    catch (JSONException e) {

                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            //requestQueueChargLikes.getCache().clear();
            requestQueueChargLikes.add(jsonArrayRequest);

        }




    public void chercherNouvComm(final String idPost , String idCom) {


        requestQueueNouvCom = Volley.newRequestQueue(getApplicationContext());
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, urlNouvCom, new Response.Listener<String>() {
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

                                chercherUserById(id_user,idPost,"comment");

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

        requestQueueNouvCom.add(smr);

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






    public void chercherUserById(final String idd , final String idPost , final String type) {





        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlCherchUser+idd, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {

                    String  nom , prenom  , imgUrl ;
                    nom=jsonObject.getString("nom");
                    prenom=jsonObject.getString("prenom");
                    imgUrl=publicUrl+jsonObject.getString("img");


                    String msg = "";

                    if (type.equals("comment")) {
                        msg = prenom + " " + nom + " à commenté votre publication" ;
                    }
                    else {
                        msg = prenom + " " + nom + " à aimé votre publication" ;
                    }
                    composerMsg(msg, imgUrl , idPost );



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


        requestQueueCherchUsr.add(jsonObjectRequest);


    }



    private void composerMsg(final String msg , final String imgUrl , final String idPost) {




        aq.ajax(imgUrl, Bitmap.class, 0, new AjaxCallback<Bitmap>() {
            @Override
            public void callback(String url, Bitmap object, AjaxStatus status) {
                super.callback(url, object, status);
                NotificationComment n = new NotificationComment(getApplicationContext(),"ISG SOUSSE",msg,object,idPost);
                n.createNotification();

            }
        });



    }


}
