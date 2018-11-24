package com.example.sadokmm.student.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
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
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class SearchPostAdapter extends RecyclerView.Adapter<SearchPostAdapter.ViewHolder> {


    private ArrayList<Post> listPost ;
    private Context context;
    private LayoutInflater layoutInflater;
    private AQuery aq;
    private RequestQueue requestQueueUserPost;


    public SearchPostAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        aq = new AQuery(context);
        requestQueueUserPost = Volley.newRequestQueue(context);
        listPost = new ArrayList<>();
    }


    public ArrayList<Post> getListPost() {
        return listPost;
    }

    public void setListPost(ArrayList<Post> listPost) {
        this.listPost = listPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.custom_search_post_adapter,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        chercherUserByEmailAndCreatePost(i,viewHolder);

    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }







    public void chercherUserByEmailAndCreatePost(final int i , final SearchPostAdapter.ViewHolder viewHolder) {


        final Post post=listPost.get(i);
        String url = publicUrl + "student/getuser/"+post.getIdusr();
        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {


                String id  , nom , prenom  , imgUrl;



                try {
                    id = jsonObject.getString("_id");
                    nom = jsonObject.getString("nom");
                    prenom = jsonObject.getString("prenom");

                    imgUrl = publicUrl + jsonObject.getString("img");




                    Dialog dialog = new Dialog(layoutInflater.getContext());
                    dialog.setTitle("Chargement");
                    dialog.setCancelable(true);

                    viewHolder.profileName.setText(prenom + " " + nom);
                    viewHolder.txtPost.setText(post.getTxtpost());
                    aq.id(viewHolder.profileImg).image(imgUrl);


                } catch (JSONException e) {
                    Toast.makeText(layoutInflater.getContext(),e.toString(),Toast.LENGTH_LONG).show();
                }





                ImageAdapter adapter = new ImageAdapter(layoutInflater.getContext(),post.getImgpost());

                viewHolder.viewPager.setAdapter(adapter);

                viewHolder.postSerachCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, PostActivity.class);
                        intent.putExtra("idpost",post.getId());
                        intent.putExtra("idusr",post.getIdusr());
                        context.startActivity(intent);
                    }
                });





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






    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtPost , profileName ;
        ViewPager viewPager;
        CircleImageView profileImg;
        CardView postSerachCardView;


        public ViewHolder(View view) {
            super(view);

            txtPost = (TextView) view.findViewById(R.id.searchPostText);
            profileName = (TextView) view.findViewById(R.id.searchPostProfileText);
            viewPager = (ViewPager) view.findViewById(R.id.view_pager);
            profileImg = (CircleImageView) view.findViewById(R.id.searchPostProfileImg);
            postSerachCardView = (CardView) view.findViewById(R.id.postSearchCardView);



        }
    }
}
