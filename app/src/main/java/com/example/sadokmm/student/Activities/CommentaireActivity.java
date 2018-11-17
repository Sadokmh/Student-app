package com.example.sadokmm.student.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.example.sadokmm.student.Adapters.AdapterComm;
import com.example.sadokmm.student.Objects.Commentaire;
import com.example.sadokmm.student.Objects.Post;
import com.example.sadokmm.student.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class CommentaireActivity extends AppCompatActivity {


    private EditText commText;
    private ImageView ajoutComm;
    private RecyclerView commRV;
    private AdapterComm adapterComm;
    private ArrayList<Commentaire> listComm;
    private String id_post;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentaire);

        try {

            id_post = getIntent().getExtras().getString("idpost");
            requestQueue = Volley.newRequestQueue(this);

            commText = (EditText) findViewById(R.id.commentPostText);
            ajoutComm = (ImageView) findViewById(R.id.insertComment);
            commRV = (RecyclerView) findViewById(R.id.commRV);
            adapterComm = new AdapterComm(this);
            charger();
            listComm = new ArrayList<>();



            commRV.setLayoutManager(new LinearLayoutManager(this));
            commRV.setAdapter(adapterComm);

            ajoutComm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (commText.getText().toString().isEmpty()) {
                        commText.setHint("Veiller écrire quelque chose");
                    } else {
                        postCommentaire();

                    }
                }
            });

        }
        catch (Exception e) {
            commText.setText(e.toString());
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }




   public void charger () {
       AQuery aq=new AQuery(this);
       String url = publicUrl + "student/getallcom/" + id_post;

       aq.ajax(url,JSONArray.class,this,"chargercallback");

   }

   public void chargercallback(String url , JSONArray response , AjaxStatus status){

       if (response != null) {
           try {
               if (adapterComm.getMyListComm().size() != 0) {
                   adapterComm.getMyListComm().clear();
                   adapterComm.notifyDataSetChanged();
               }
               Toast.makeText(getApplicationContext(),response.length() + " ",Toast.LENGTH_LONG).show();
               commText.setText(id_post);
               for (int i = 0; i < response.length(); i++) {
                   JSONObject CommJson = response.getJSONObject(i);
                   String idComm  , txtComm, dateComm, id_usr;
                   idComm = CommJson.getString("_id");
                   txtComm = CommJson.getString("txtcom");
                   dateComm = CommJson.getString("datecom");
                   id_usr = CommJson.getString("idusr");

                   Commentaire commentaire = new Commentaire(idComm ,txtComm, id_usr, id_post );
                   commentaire.setDateComm(dateComm);
                   listComm.add(commentaire);
                   adapterComm.getMyListComm().add(commentaire);
                   adapterComm.notifyDataSetChanged();



               }
           } catch (JSONException e) {
               Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
               Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
               Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
           }
       }

       else {
           Toast.makeText(this,"NULL",Toast.LENGTH_LONG).show();
       }



   }



    public void postCommentaire(){


        String url = publicUrl + "student/postc" ;
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                commText.getText().clear();
                //charger(0,0);
                Toast.makeText(getApplicationContext(),"commentaire ajouté" , Toast.LENGTH_LONG).show();
                charger();
                adapterComm.notifyDataSetChanged();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"erreur insertion commentaire ", Toast.LENGTH_LONG).show();
            }
        });
        Commentaire commentaire = new Commentaire("0",commText.getText().toString(),admin.getId(),id_post);
        smr.addStringParam("txtcom",commentaire.getTxtComm());
        smr.addStringParam("datecom",commentaire.getDateComm());
        smr.addStringParam("idusr",commentaire.getIdusr());
        smr.addStringParam("idpost",commentaire.getIdPost());



        requestQueue.add(smr);


    }






}
