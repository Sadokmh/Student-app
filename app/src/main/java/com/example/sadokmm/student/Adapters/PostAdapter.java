package com.example.sadokmm.student.Adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


import static com.example.sadokmm.student.Activities.firstActivity.getResizedBitmap;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>  {


    private List<Post> myListPost;
    private LayoutInflater layoutInflater;

    Context con;
    AQuery aq;
    RequestQueue requestQueue;

    ProgressDialog prgDialog;

    private User user;
    //private String nom,prenom,email,mdp,filiere,id,imgUrl;
    //private int niveau,groupe;
    //private Bitmap imgusr,imgpost;


    public PostAdapter(Context context) {
        layoutInflater=LayoutInflater.from(context);
        myListPost=new ArrayList<>();
        aq=new AQuery(context);
        requestQueue = Volley.newRequestQueue(layoutInflater.getContext());




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






        /*viewHolder.profilName.setText(postUser.getPrenom() + " " + postUser.getNom());
        viewHolder.filiere.setText(postUser.getNiveau() + postUser.getFiliere());
        viewHolder.datePost.setText(post.getDatepost());
        viewHolder.textPost.setText(post.getTxtpost());
        viewHolder.imgUsr.setImageBitmap(postUser.getImg());
        viewHolder.imgPost.setImageBitmap(post.getImgpost());*/







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

        private TextView profilName,filiere,textPost,datePost;
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonObjectRequest);

        //return user;

    }






    //download img using volley
    public Bitmap getImageByUrl(String url) {
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(layoutInflater.getContext());
        final Bitmap[] im = new Bitmap[1];

        // Initialize a new ImageRequest
        ImageRequest imageRequest = new ImageRequest(url,layoutInflater.getContext().getResources(),layoutInflater.getContext().getContentResolver(),new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                im[0] = response;
            }
        },0,0, ImageView.ScaleType.CENTER_CROP,Bitmap.Config.RGB_565,new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(layoutInflater.getContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });


        // Add ImageRequest to the RequestQueue
        requestQueue.add(imageRequest);
        return im[0];
    }



}