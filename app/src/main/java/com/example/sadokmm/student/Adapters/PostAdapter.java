package com.example.sadokmm.student.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.example.sadokmm.student.Activities.PostActivity;
import com.example.sadokmm.student.Activities.ProfileActivity;
import com.example.sadokmm.student.Objects.Post;
import com.example.sadokmm.student.Objects.User;
import com.example.sadokmm.student.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> implements Filterable  {


    private List<Post> myListPost;
    private List<Post> listPost;

    private LayoutInflater layoutInflater;

    private AQuery aq;
    private RequestQueue requestQueueUserPost,requestQueueLikePost,requestQueueUnlikePost;

    private Boolean clickFromProfile;






    public PostAdapter(Context context) {
        layoutInflater=LayoutInflater.from(context);
        myListPost=new ArrayList<>();
        listPost=new ArrayList<>();
        aq=new AQuery(context);
        requestQueueUserPost = Volley.newRequestQueue(layoutInflater.getContext());
        requestQueueLikePost = Volley.newRequestQueue(layoutInflater.getContext());
        requestQueueUnlikePost = Volley.newRequestQueue(layoutInflater.getContext());
        clickFromProfile = false;





    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.post_custom,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);


        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        chercherUserByEmailAndCreatePost(i,viewHolder);


    }




    @Override
    public int getItemCount() {
        return myListPost.size();
    }



    public void setMyList(List<Post> list){
        listPost = list ;
        myListPost.addAll(listPost);
        notifyItemRangeChanged(0,list.size());
    }



    public List<Post> getMyListPost() {
        return myListPost;
    }

    public Boolean getClickFromProfile() {
        return clickFromProfile;
    }

    public void setClickFromProfile(Boolean clickFromProfile) {
        this.clickFromProfile = clickFromProfile;
    }

    @Override
    public Filter getFilter() {
        return postFilter;
    }

    private Filter postFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Post> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(listPost);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Post post : listPost){
                    if (post.getTxtpost().toLowerCase().contains(filterPattern)){
                        filteredList.add(post);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            myListPost.clear();
            myListPost.addAll((List<Post>)filterResults.values);
            notifyDataSetChanged();
        }
    };

    //

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView profilName,filiere,textPost,datePost,nbLikesView,nbCommView;
        private ImageView imgPost,likePost,commentPost;
        private CircleImageView imgUsr;
        private ViewPager imgpostViewer;
        private EditText textComm;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilName=(TextView)itemView.findViewById(R.id.profileName);
            filiere=(TextView)itemView.findViewById(R.id.filiereUser);
            datePost = (TextView)itemView.findViewById(R.id.datePost);
            textPost=(TextView)itemView.findViewById(R.id.textPost);
            //imgPost=(ImageView) itemView.findViewById(R.id.imagePost);
            imgpostViewer = (ViewPager) itemView.findViewById(R.id.view_pager);
            commentPost = (ImageView) itemView.findViewById(R.id.commentPostButton);
            likePost=(ImageView) itemView.findViewById(R.id.likePostButton);
             imgUsr=(CircleImageView) itemView.findViewById(R.id.profileImg);
            textComm = (EditText) itemView.findViewById(R.id.commentPostText);
            nbLikesView = (TextView) itemView.findViewById(R.id.nbLikes);
            nbCommView = (TextView) itemView.findViewById(R.id.nbComms);
            //salleLayout=(LinearLayout)itemView.findViewById(R.id.salleLayout);

        }
    }




    public void chercherUserByEmailAndCreatePost(final int i , final ViewHolder viewHolder) {


        final Post post=myListPost.get(i);
        String url = publicUrl + "student/getuser/"+post.getIdusr();
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
                   // aq.id(viewHolder.imgPost).image(publicUrl + post.getImgpost()).progress(dialog);

                    viewHolder.commentPost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {

                            Intent intent = new Intent(layoutInflater.getContext(), PostActivity.class);
                            intent.putExtra("idpost",post.getId());
                            intent.putExtra("idusr",post.getIdusr());
                            layoutInflater.getContext().startActivity(intent);

                            }
                            catch (Exception e) {
                                Toast.makeText(layoutInflater.getContext(),e.toString(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });



                    viewHolder.nbCommView.setText(String.valueOf(myListPost.get(i).getCommentList().size()));


                    viewHolder.nbLikesView.setText(String.valueOf(myListPost.get(i).getListLikes().size()));


                    viewHolder.likePost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String textNbLikes;
                            if (!myListPost.get(i).userlike()) {
                                likePost(myListPost.get(i).getId() , i);
                                viewHolder.likePost.setImageResource(R.drawable.ic_like_color);

                                if (myListPost.get(i).getListLikes().size() > 0 )
                                textNbLikes = String.valueOf(myListPost.get(i).getListLikes().size()+1);
                                else textNbLikes = String.valueOf(myListPost.get(i).getListLikes().size()+1) ;

                                viewHolder.nbLikesView.setText(textNbLikes);


                            }
                            else {
                                unlikePost(myListPost.get(i).getId() , i);
                                viewHolder.likePost.setImageResource(R.drawable.ic_like_not_clicked);

                                //if (myListPost.get(i).)
                                viewHolder.nbLikesView.setText( String.valueOf(myListPost.get(i).getListLikes().size() - 1));
                            }

                        }
                    });

                } catch (JSONException e) {
                    Toast.makeText(layoutInflater.getContext(),e.toString(),Toast.LENGTH_LONG).show();
                }


                if (myListPost.get(i).userlike()) {
                    viewHolder.likePost.setImageResource(R.drawable.ic_like_color);
                }
                else {
                    viewHolder.likePost.setImageResource(R.drawable.ic_like_not_clicked);
                }

                //test imageslider

                ImageAdapter adapter = new ImageAdapter(layoutInflater.getContext(),post.getImgpost());
                //adapter.setImgUrls(post.getImgpost());
                //adapter.notifyDataSetChanged();
                viewHolder.imgpostViewer.setAdapter(adapter);


                //interdire d'ouvrir le profil de nouveau s'il est dans la page profil
                if (!clickFromProfile) {
                    viewHolder.imgUsr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(layoutInflater.getContext(),ProfileActivity.class);
                            intent.putExtra("id",myListPost.get(i).getIdusr());
                            layoutInflater.getContext().startActivity(intent);
                        }
                    });

                    viewHolder.profilName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(layoutInflater.getContext(),ProfileActivity.class);
                            intent.putExtra("id",myListPost.get(i).getIdusr());
                            layoutInflater.getContext().startActivity(intent);
                        }
                    });
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(layoutInflater.getContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });


       /* if (isNetworkAvailable())
            requestQueueUserPost.getCache().clear();*/

        requestQueueUserPost.add(jsonObjectRequest);

        //return user;

    }









    public void likePost(String idpost, final int position) {

        String url = publicUrl + "student/like/" + idpost + "/" + admin.getId();


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

        String url = publicUrl + "student/likeremove/" + idpost + "/" + admin.getId();

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