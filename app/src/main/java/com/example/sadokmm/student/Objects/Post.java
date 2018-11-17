package com.example.sadokmm.student.Objects;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.contextFirst;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class Post {

    private String id;
    private String txtpost;
    private String idusr;
    private ArrayList<String> imgpost;
    private ArrayList<Commentaire> commentList;
    private String datepost;
    private ArrayList<String> listLikes;




    public Post(){

    }


    public Post(String txtpost, String idusr, ArrayList<String> imgpost , String id) {
        this.id = id;
        this.txtpost = txtpost;
        this.idusr = idusr;
        this.imgpost = imgpost;
        this.datepost = getStringDate(Calendar.getInstance().getTime());
        this.listLikes = new ArrayList<>();
        this.commentList = new ArrayList<>();

       //charger les j'aimes:
        chargerLikes();
        chargerComm();
    }

    private String getStringDate(Date dateNow) {
        DateFormat df = new SimpleDateFormat("d MMM yyyy");
        String date = df.format(dateNow);
        return date;
    }



   //charger les j'aimes de la publication
    public void chargerLikes(){

        RequestQueue requestQueue = Volley.newRequestQueue(contextFirst);

        String url = publicUrl + "student/postlikes/" + this.id;


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (listLikes.size() != 0 ) listLikes.clear();

               // Toast.makeText(context,response.length()+"" , Toast.LENGTH_LONG).show();

                try {
                    for (int i = 0; i < response.length(); i++) {
                        String usrId = response.get(i).toString();
                        //Toast.makeText(,userEmail,Toast.LENGTH_LONG).show();
                        listLikes.add(usrId);
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

        requestQueue.getCache().clear();
        requestQueue.add(jsonArrayRequest);

    }


    public Boolean userlike(){

        for (int i=0 ; i<listLikes.size();i++) {
            if (listLikes.get(i).equals(admin.getId()))
                return true;
        }
        return false;

    }


    //import commentaires :

    public void chargerComm () {
        AQuery aq=new AQuery(contextFirst);
        String url = publicUrl + "student/getallcom/" + this.id;

        aq.ajax(url,JSONArray.class,this,"chargercallback");

    }

    public void chargercallback(String url , JSONArray response , AjaxStatus status){

        if (response != null) {
            try {

                //Toast.makeText(context,response.length() + " ",Toast.LENGTH_LONG).show();

                for (int i = 0; i < response.length(); i++) {
                    JSONObject CommJson = response.getJSONObject(i);
                    String idComm  , txtComm, dateComm, idusr;
                    idComm = CommJson.getString("_id");
                    txtComm = CommJson.getString("txtcom");
                    dateComm = CommJson.getString("datecom");
                    idusr = CommJson.getString("idusr");

                    Commentaire commentaire = new Commentaire(idComm ,txtComm, idusr, this.id );
                    commentaire.setDateComm(dateComm);
                    commentList.add(commentaire);




                }
            } catch (JSONException e) {
                Toast.makeText(contextFirst, e.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(contextFirst, e.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(contextFirst, e.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(contextFirst, "kiiiii5", Toast.LENGTH_LONG).show();
            }
        }

        else {
            Toast.makeText(contextFirst,"NULL ",Toast.LENGTH_LONG).show();
        }



    }





    public String getTxtpost() {
        return txtpost;
    }

    public void setTxtpost(String txtpost) {
        this.txtpost = txtpost;
    }

    public String getIdusr() {
        return idusr;
    }

    public void setIdusr(String emailusr) {
        this.idusr = emailusr;
    }

    public ArrayList<String> getImgpost() {
        return imgpost;
    }

    public void setImgpost(ArrayList<String> imgpost) {
        this.imgpost = imgpost;
    }

    public String getDatepost() {
        return datepost;
    }

    public void setDatepost(String datepost) {
        this.datepost = datepost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getListLikes() {
        return listLikes;
    }

    public void setListLikes(ArrayList<String> listLikes) {
        this.listLikes = listLikes;
    }

    public ArrayList<Commentaire> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<Commentaire> commentList) {
        this.commentList = commentList;
    }
}
