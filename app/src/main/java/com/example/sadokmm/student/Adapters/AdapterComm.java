package com.example.sadokmm.student.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestTickle;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.VolleyTickle;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.example.sadokmm.student.Activities.MainActivity;
import com.example.sadokmm.student.Activities.ProfileActivity;
import com.example.sadokmm.student.Objects.Commentaire;
import com.example.sadokmm.student.Objects.Post;
import com.example.sadokmm.student.Objects.User;
import com.example.sadokmm.student.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.getResizedBitmap;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class AdapterComm extends RecyclerView.Adapter<AdapterComm.ViewHolder> {

    private ArrayList<Commentaire> myListComm;
    private LayoutInflater layoutInflater;
    private int niveau;

    private Bitmap imgUsr;
    private String nomUsr;


    ProgressDialog prgDialog;

    AQuery aq;
    RequestQueue requestQueue;





    public AdapterComm(Context context){

        layoutInflater=LayoutInflater.from(context);
        myListComm=new ArrayList<>();

    }

    @NonNull
    @Override
    public AdapterComm.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view=layoutInflater.inflate(R.layout.custom_comm,viewGroup,false);
        AdapterComm.ViewHolder viewHolder=new AdapterComm.ViewHolder(view);


        aq=new AQuery(layoutInflater.getContext());
        requestQueue = Volley.newRequestQueue(layoutInflater.getContext());

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Commentaire commentaire=myListComm.get(i);


        // requette pour chercher l'utilisateur qui a écrit le commentaire par son email (commentaire.getEmailUsr() )

        chercherUserByEmailAndCreateComment(i,viewHolder);




        //Afficher / masquer le bouton supprimer
        if (commentaire.getIdusr().equals(admin.getId()))
        {
            viewHolder.deleteComm.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.deleteComm.setVisibility(View.INVISIBLE);
        }




        viewHolder.userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent=new Intent(layoutInflater.getContext(), Profile.class);
                intent.putExtra("email",commentaire.getEmailUser());
                intent.putExtra("type","profile");
                layoutInflater.getContext().startActivity(intent);*/
            }
        });

        final int position=i;
        viewHolder.deleteComm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeComment(position);
                //myListComm.remove(position);
                //
            }
        });





    }







    @Override
    public int getItemCount() {
        return myListComm.size();
    }


    public void setMyList(ArrayList<Commentaire> list){
        myListComm=list;
        notifyItemRangeChanged(0,list.size());
    }

    public ArrayList<Commentaire> getMyListComm() {
        return myListComm;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView userName,comment,deleteComm,date;
        //private ImageView nbstars;
        private CircleImageView userPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName=(TextView)itemView.findViewById(R.id.profileNameComm);
            comment=(TextView)itemView.findViewById(R.id.textComm);
            userPhoto=(CircleImageView) itemView.findViewById(R.id.profileImgComm);
            deleteComm=(TextView)itemView.findViewById(R.id.suppComm);
            date=(TextView)itemView.findViewById(R.id.dateComm);

        }
    }






    public void chercherUserByEmailAndCreateComment(final int i , final AdapterComm.ViewHolder viewHolder) {


        final Commentaire commentaire=myListComm.get(i);
        String url = publicUrl + "student/getuser/"+commentaire.getIdusr();
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                final String id , email , nom , prenom , filiere , mdp , imgUrl;
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


                    final User u = new User(id, nom, prenom, email, imgUrl, filiere, groupe, niveau);

                    viewHolder.comment.setText(commentaire.getTxtComm());
                    viewHolder.date.setText(commentaire.getDateComm());
                    viewHolder.userName.setText(prenom + " " + nom);
                    aq.id(viewHolder.userPhoto).image(imgUrl);



                    viewHolder.userPhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(layoutInflater.getContext(),ProfileActivity.class);
                            intent.putExtra("id",id);
                            layoutInflater.getContext().startActivity(intent);
                        }
                    });

                    viewHolder.userName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(layoutInflater.getContext(),ProfileActivity.class);
                            intent.putExtra("id",id);
                            layoutInflater.getContext().startActivity(intent);
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



    public void removeComment(final int i) {

        final String url = publicUrl + "student/removec/" + myListComm.get(i).getId();


        RequestQueue requestQueue = Volley.newRequestQueue(layoutInflater.getContext());

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(layoutInflater.getContext(),"Commentaire supprimé",Toast.LENGTH_LONG).show();
                myListComm.remove(i);
                notifyDataSetChanged(); // hereeeeeeeeee !!!!
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(layoutInflater.getContext(),"Commentaire n'a pas été supprimé " + url,Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }



}
