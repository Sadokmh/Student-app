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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class Post {

    private String id;
    private String txtpost;
    private String emailusr;
    private String imgpost;
    private String datepost;
    private ArrayList<String> listLikes;

    private RequestQueue requestQueue;

    private Context context;


    public Post(String txtpost, String emailusr, String imgpost , String id , Context context) {
        this.id = id;
        this.txtpost = txtpost;
        this.emailusr = emailusr;
        this.imgpost = imgpost;
        this.datepost = getStringDate(Calendar.getInstance().getTime());
        this.listLikes = new ArrayList<>();
        this.context = context;
        this.requestQueue= Volley.newRequestQueue(context);
       //charger les j'aimes:
        chargerLikes();
    }

    private String getStringDate(Date dateNow) {
        DateFormat df = new SimpleDateFormat("d MMM yyyy");
        String date = df.format(dateNow);
        return date;
    }



   //charger les j'aimes de la publication
    public void chargerLikes(){

        String url = publicUrl + "student/postlikes/" + this.id;

       // Toast.makeText(context,"je charge post" , Toast.LENGTH_LONG).show();


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (listLikes.size() != 0 ) listLikes.clear();

               // Toast.makeText(context,response.length()+"" , Toast.LENGTH_LONG).show();

                try {
                    for (int i = 0; i < response.length(); i++) {
                        String userEmail = response.get(i).toString();
                        //Toast.makeText(,userEmail,Toast.LENGTH_LONG).show();
                        listLikes.add(userEmail);
                    }

                    //Toast.makeText()


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
            if (listLikes.get(i).equals(admin.getEmail()))
                return true;
        }
        return false;

    }



    public String getTxtpost() {
        return txtpost;
    }

    public void setTxtpost(String txtpost) {
        this.txtpost = txtpost;
    }

    public String getEmailusr() {
        return emailusr;
    }

    public void setEmailusr(String emailusr) {
        this.emailusr = emailusr;
    }

    public String getImgpost() {
        return imgpost;
    }

    public void setImgpost(String imgpost) {
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
}
