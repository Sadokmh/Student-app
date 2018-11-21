package com.example.sadokmm.student.Activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.example.sadokmm.student.Adapters.AdapterComm;
import com.example.sadokmm.student.Adapters.ImageAdapter;
import com.example.sadokmm.student.Objects.Commentaire;
import com.example.sadokmm.student.Objects.Post;
import com.example.sadokmm.student.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.contextFirst;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class PostActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private EditText commText;
    private ImageView ajoutComm;
    private RecyclerView commRV;
    private AdapterComm adapterComm;
    private ArrayList<Commentaire> listComm;
    private String id_post;
    //private String id_usr;
    private RequestQueue requestQueue;

    private TextView profilNameView,filiereView,textPostView,datePostView,nbLikesView,nbCommView;
    private ImageView imgPost,likePost,commentPost;
    private CircleImageView imgUsr;
    private ViewPager imgpostViewer;
    private EditText textComm;

    private Post p;



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commentaire);

        try {

            toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            id_post = getIntent().getExtras().getString("idpost");
            //id_usr = getIntent().getExtras().getString("idusr");
            requestQueue = Volley.newRequestQueue(this);

            commText = (EditText) findViewById(R.id.commentPostText);
            ajoutComm = (ImageView) findViewById(R.id.insertComment);
            commRV = (RecyclerView) findViewById(R.id.commRV);
            adapterComm = new AdapterComm(this);

            listComm = new ArrayList<>();

            //
            profilNameView=(TextView)findViewById(R.id.profileName);
            filiereView=(TextView)findViewById(R.id.filiereUser);
            datePostView = (TextView)findViewById(R.id.datePost);
            textPostView=(TextView)findViewById(R.id.textPost);
            imgpostViewer = (ViewPager) findViewById(R.id.view_pager);
            commentPost = (ImageView) findViewById(R.id.commentPostButton);
            likePost=(ImageView) findViewById(R.id.likePostButton);
            imgUsr=(CircleImageView) findViewById(R.id.profileImg);
            textComm = (EditText) findViewById(R.id.commentPostText);
            nbLikesView = (TextView) findViewById(R.id.nbLikes);
            nbCommView = (TextView) findViewById(R.id.nbComms);
            chargerPost(id_post);




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
               nbLikesView.setText(p.getListLikes().size()+"");
               nbCommView.setText(p.getCommentList().size()+ "");
               if (p.userlike()) {
                   likePost.setImageResource(R.drawable.ic_like_color);
               }
               else {
                   likePost.setImageResource(R.drawable.ic_like_not_clicked);
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




    public void chargerPost(final String idPost) {

        String url = publicUrl + "student/getpostbyid/" + idPost;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    Toast.makeText(getApplicationContext(),"here",Toast.LENGTH_LONG).show();
                    String txtPost, datePost, idUsr;

                    txtPost = response.getString("txtpost");
                    datePost = response.getString("datepost");
                    idUsr = response.getString("idusr");
                    JSONArray imgpost = response.getJSONArray("imgpost");
                    ArrayList<String> imgListPost = new ArrayList<>();
                    for (int j = 0; j < imgpost.length(); j++) {
                        imgListPost.add(imgpost.getString(j));
                    }
                    p = new Post(txtPost, idUsr, imgListPost, idPost);
                    p.setDatepost(datePost);

                    textPostView.setText(p.getTxtpost());
                    datePostView.setText(p.getDatepost());





                    likePost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String textNbLikes;
                            if (!p.userlike()) {
                                like_Post(id_post);
                               likePost.setImageResource(R.drawable.ic_like_color);

                                if (p.getListLikes().size() > 0 )
                                    textNbLikes = String.valueOf(p.getListLikes().size()+1);
                                else textNbLikes = String.valueOf(p.getListLikes().size()+1) ;

                                nbLikesView.setText(textNbLikes);


                            }
                            else {
                                unlike_Post(id_post);
                                likePost.setImageResource(R.drawable.ic_like_not_clicked);

                                //if (myListPost.get(i).)
                                nbLikesView.setText( String.valueOf(p.getListLikes().size() - 1));
                            }

                        }
                    });



                    ImageAdapter adapter = new ImageAdapter(getApplicationContext(),p.getImgpost());
                    imgpostViewer.setAdapter(adapter);

                    chargerUsr(p.getIdusr());



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

        requestQueue.add(request);

    }


    public void chargerUsr(String idUsr) {

        String url = publicUrl + "student/getuser/"+idUsr;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final AQuery aq = new AQuery(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String nom,prenom,filiere,imgUrl,email;
                    int niveau,groupe;
                    nom = response.getString("nom");
                    prenom = response.getString("prenom");
                    filiere = response.getString("filiere");
                    imgUrl = publicUrl + response.getString("img");
                    email = response.getString("email");
                    niveau = Integer.parseInt(response.getString("niveau"));
                    groupe = Integer.parseInt(response.getString("groupe"));

                    profilNameView.setText(prenom + " " + nom);
                    filiereView.setText(niveau + " " + filiere);
                    aq.id(imgUsr).image(imgUrl);

                    getSupportActionBar().setTitle("Publication de " + prenom + " " + nom);

                    imgUsr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                            intent.putExtra("id",p.getIdusr());
                            startActivity(intent);
                        }
                    });

                    profilNameView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                            intent.putExtra("id",p.getIdusr());
                            startActivity(intent);
                        }
                    });

                    charger();


                    imgUsr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                            intent.putExtra("id",p.getIdusr());
                            getApplicationContext().startActivity(intent);
                        }
                    });

                    profilNameView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                            intent.putExtra("id",p.getIdusr());
                            getApplicationContext().startActivity(intent);
                        }
                    });



                }
                catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(request);

    }






    public void like_Post(String idpost) {

        String url = publicUrl + "student/like/" + idpost + "/" + admin.getId();
        RequestQueue requestQueueLikePost = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"like ajouté",Toast.LENGTH_LONG).show();
                p.chargerLikes();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"erreur like ",Toast.LENGTH_LONG).show();
            }
        });

        /*if (isNetworkAvailable())
            requestQueueLikePost.getCache().clear();*/

        requestQueueLikePost.add(stringRequest);

    }



    public void unlike_Post(String idpost){

        String url = publicUrl + "student/likeremove/" + idpost + "/" + admin.getId();
        RequestQueue requestQueueUnlikePost = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"like supprimé",Toast.LENGTH_LONG).show();
                p.chargerLikes();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"erreur supp like",Toast.LENGTH_LONG).show();
            }
        });


        /*if (isNetworkAvailable())
            requestQueueUnlikePost.getCache().clear();*/

        requestQueueUnlikePost.add(stringRequest);

    }



}
