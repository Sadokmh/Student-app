package com.example.sadokmm.student.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.ImageRequest;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.JsonRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.sadokmm.student.Activities.CommentaireActivity;
import com.example.sadokmm.student.Activities.MainActivity;
import com.example.sadokmm.student.Objects.Post;
import com.example.sadokmm.student.Objects.Seance;
import com.example.sadokmm.student.Objects.User;
import com.example.sadokmm.student.R;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.getResizedBitmap;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>  {


    private List<Post> myListPost;
    private LayoutInflater layoutInflater;

    AQuery aq;
    RequestQueue requestQueueUserPost,requestQueueLikePost,requestQueueUnlikePost;

    ProgressDialog prgDialog;




    public PostAdapter(Context context) {
        layoutInflater=LayoutInflater.from(context);
        myListPost=new ArrayList<>();
        aq=new AQuery(context);
        requestQueueUserPost = Volley.newRequestQueue(layoutInflater.getContext());
        requestQueueLikePost = Volley.newRequestQueue(layoutInflater.getContext());
        requestQueueUnlikePost = Volley.newRequestQueue(layoutInflater.getContext());




    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.custom_post,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);


        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        chercherUserByEmailAndCreatePost(i,viewHolder);

        /*if (userlike(i)) {
            viewHolder.likePost.setImageResource(R.drawable.ic_like_color);
        }*/


    }




    @Override
    public int getItemCount() {
        return myListPost.size();
    }



    public void setMyList(List<Post> list){
        myListPost=list;
        notifyItemRangeChanged(0,list.size());
    }



    public List<Post> getMyListPost() {
        return myListPost;
    }




    //

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView profilName,filiere,textPost,datePost,nbLikesView;
        private ImageView imgPost,likePost,commentPost;
        private CircleImageView imgUsr;
        private EditText textComm;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilName=(TextView)itemView.findViewById(R.id.profileName);
            filiere=(TextView)itemView.findViewById(R.id.filiereUser);
            datePost = (TextView)itemView.findViewById(R.id.datePost);
            textPost=(TextView)itemView.findViewById(R.id.textPost);
            imgPost=(ImageView) itemView.findViewById(R.id.imagePost);
            commentPost = (ImageView) itemView.findViewById(R.id.commentPostButton);
            likePost=(ImageView) itemView.findViewById(R.id.likePostButton);
             imgUsr=(CircleImageView) itemView.findViewById(R.id.profileImg);
            textComm = (EditText) itemView.findViewById(R.id.commentPostText);
            nbLikesView = (TextView) itemView.findViewById(R.id.nbLikes);
            //salleLayout=(LinearLayout)itemView.findViewById(R.id.salleLayout);
            //profLayout=(LinearLayout)itemView.findViewById(R.id.profLayout);
            //coursLayout=(LinearLayout)itemView.findViewById(R.id.coursLayout);

        }
    }




    public void chercherUserByEmailAndCreatePost(final int i , final ViewHolder viewHolder) {


        final Post post=myListPost.get(i);
        String url = publicUrl + "student/getuser/"+post.getEmailusr();
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                String id , email , nom , prenom , filiere , mdp , imgUrl;
                int niveau , groupe;


                try {
                    id = jsonObject.getString("_id");
                    nom = jsonObject.getString("nom");
                    prenom = jsonObject.getString("prenom");
                    filiere = jsonObject.getString("filiere");
                    imgUrl = publicUrl + jsonObject.getString("img");
                    email = jsonObject.getString("email");
                    niveau = Integer.parseInt(jsonObject.getString("niveau"));
                    groupe = Integer.parseInt(jsonObject.getString("groupe"));


                    User u = new User(id, nom, prenom, email, imgUrl, filiere, groupe, niveau);

                    Dialog dialog = new Dialog(layoutInflater.getContext());
                    dialog.setTitle("Chargement");
                    dialog.setCancelable(true);

                    viewHolder.profilName.setText(prenom + " " + nom);
                    viewHolder.filiere.setText(niveau + filiere + " -g" + groupe);
                    viewHolder.datePost.setText(post.getDatepost());
                    viewHolder.textPost.setText(post.getTxtpost());
                    aq.id(viewHolder.imgUsr).image(imgUrl);
                    aq.id(viewHolder.imgPost).image(publicUrl + post.getImgpost()).progress(dialog);
                    viewHolder.commentPost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {

                            Intent intent = new Intent(layoutInflater.getContext(), CommentaireActivity.class);
                            intent.putExtra("idpost",post.getId());
                            layoutInflater.getContext().startActivity(intent);

                            }
                            catch (Exception e) {
                                Toast.makeText(layoutInflater.getContext(),e.toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });



                    viewHolder.nbLikesView.setText(myListPost.get(i).getListLikes().size()+" personnes");


                    viewHolder.likePost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String textNbLikes;
                            if (!myListPost.get(i).userlike()) {
                                likePost(myListPost.get(i).getId() , i);
                                viewHolder.likePost.setImageResource(R.drawable.ic_like_color);

                                if (myListPost.get(i).getListLikes().size() > 0 )
                                textNbLikes = "Vous et " + myListPost.get(i).getListLikes().size()+" autres personnes";
                                else textNbLikes = "1 Vous aimez ça" ;

                                viewHolder.nbLikesView.setText(textNbLikes);


                            }
                            else {
                                unlikePost(myListPost.get(i).getId() , i);
                                viewHolder.likePost.setImageResource(R.drawable.ic_like_not_clicked);

                                //if (myListPost.get(i).)
                                viewHolder.nbLikesView.setText( (myListPost.get(i).getListLikes().size() - 1)+"  personnes");
                            }

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (myListPost.get(i).userlike()) {
                    viewHolder.likePost.setImageResource(R.drawable.ic_like_color);
                }
                else {
                    viewHolder.likePost.setImageResource(R.drawable.ic_like_not_clicked);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


       /* if (isNetworkAvailable())
            requestQueueUserPost.getCache().clear();*/

        requestQueueUserPost.add(jsonObjectRequest);

        //return user;

    }









    public void likePost(String idpost, final int position) {

        String url = publicUrl + "student/like/" + idpost + "/" + admin.getEmail();


        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(layoutInflater.getContext(),"like ajouté",Toast.LENGTH_LONG).show();
                myListPost.get(position).chargerLikes();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(layoutInflater.getContext(),"erreur like ",Toast.LENGTH_LONG).show();
            }
        });

        /*if (isNetworkAvailable())
            requestQueueLikePost.getCache().clear();*/

        requestQueueLikePost.add(stringRequest);

    }



    public void unlikePost(String idpost , final int position){

        String url = publicUrl + "student/likeremove/" + idpost + "/" + admin.getEmail();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(layoutInflater.getContext(),"like supprimé",Toast.LENGTH_LONG).show();
                myListPost.get(position).chargerLikes();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(layoutInflater.getContext(),"erreur supp like",Toast.LENGTH_LONG).show();
            }
        });


        /*if (isNetworkAvailable())
            requestQueueUnlikePost.getCache().clear();*/

        requestQueueUnlikePost.add(stringRequest);

    }










    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) layoutInflater.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }


}