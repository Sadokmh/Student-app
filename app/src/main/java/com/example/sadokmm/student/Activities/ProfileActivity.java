package com.example.sadokmm.student.Activities;


import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.example.sadokmm.student.Adapters.PostAdapter;
import com.example.sadokmm.student.Objects.Post;
import com.example.sadokmm.student.Objects.User;
import com.example.sadokmm.student.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class ProfileActivity extends AppCompatActivity {

    private TextView profile_name,profileFiliere,profileGroupe,profileEmail;
    private ImageView profilePhoto;
    private RecyclerView profileRv;
    private String email ;
    private PostAdapter postAdapter;
    private User user;
    private AQuery aq;
    private ArrayList<Post> listPost ;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        profile_name = (TextView) findViewById(R.id.profile_name);
        profileFiliere = (TextView) findViewById(R.id.profileFiliere);
        profileGroupe = (TextView) findViewById(R.id.profileGroupe) ;
        profileEmail = (TextView) findViewById(R.id.profileEmail) ;
        profilePhoto = (ImageView) findViewById(R.id.profilePhoto);
        profileRv = (RecyclerView) findViewById(R.id.profileRv);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbarid);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);







        postAdapter = new PostAdapter(this);
        postAdapter.setClickFromProfile(true);
        aq = new AQuery(this);
        listPost = new ArrayList<>();


        email = getIntent().getExtras().getString("email");

        postAdapter.setMyList(listPost);
        profileRv.setLayoutManager(new LinearLayoutManager(this));
        profileRv.setAdapter(postAdapter);

        chercherUser(email);


    }



    private void chercherUser(String email) {

        String url = publicUrl + "student/getuser/"+email;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest profileRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    final String id, email, nom, prenom, filiere, imgUrl;
                    int niveau, groupe;

                    id = jsonObject.getString("_id");
                    nom = jsonObject.getString("nom");
                    prenom = jsonObject.getString("prenom");
                    filiere = jsonObject.getString("filiere");
                    imgUrl = publicUrl + jsonObject.getString("img");
                    email = jsonObject.getString("email");
                    niveau = Integer.parseInt(jsonObject.getString("niveau"));
                    groupe = Integer.parseInt(jsonObject.getString("groupe"));

                    user = new User(id, nom, prenom, email, imgUrl, filiere, groupe, niveau);


                    collapsingToolbarLayout.setTitle(prenom + " " + nom);
                    profileEmail.setText(email);
                    profileGroupe.setText("Groupe " + groupe);
                    profileFiliere.setText(niveau + " " + filiere.toUpperCase());
                    Drawable d = new BitmapDrawable(getResources(), aq.getCachedImage(imgUrl));
                    collapsingToolbarLayout.setBackground(d);


                    chargerPost();

                }
                catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(profileRequest);

    }





    private void chargerPost(){

        String url = publicUrl + "student/getpost/" + user.getEmail();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    if (listPost.size() != 0 ) listPost.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject postJson = response.getJSONObject(i);
                        String id , txtpost, datepost, emailusr;
                        id = postJson.getString("_id");
                        txtpost = postJson.getString("txtpost");
                        datepost = postJson.getString("datepost");
                        emailusr = postJson.getString("emailusr");
                        JSONArray imgpost = postJson.getJSONArray("imgpost");
                        ArrayList<String> imgListPost = new ArrayList<>();
                        for (int j=0 ; j<imgpost.length() ; j++) {
                            imgListPost.add(imgpost.getString(j));
                        }

                        //getImageByUrl(publicUrl + imgpost, i);
                        Post p = new Post(txtpost, emailusr, imgListPost , id , getApplicationContext());
                        p.setDatepost(datepost);
                        listPost.add(0,p);



                    }
                    postAdapter.setMyList(listPost);
                    postAdapter.notifyDataSetChanged();
                    //Toast.makeText(getContext(),,Toast.LENGTH_LONG).show();
                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonArrayRequest);

    }


}
