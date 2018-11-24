package com.example.sadokmm.student.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.example.sadokmm.student.Activities.ParametreActivity;
import com.example.sadokmm.student.Adapters.PostAdapter;
import com.example.sadokmm.student.Objects.Post;
import com.example.sadokmm.student.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class ProfileFragment extends Fragment {


    public static Boolean IS_ACTIVE = false;
    private TextView profile_name,profileFiliere,profileGroupe,profileEmail;
    private ImageView profilePhoto;
    private RecyclerView profileRv;
    private String IDUSR ;
    private PostAdapter postAdapter;
    //private User user;
    private AQuery aq;
    private ArrayList<Post> listPost ;
    private LinearLayout editProfile;

    private Toolbar toolbar;


    public ProfileFragment(){

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment,container,false);

        profileFiliere = (TextView) view.findViewById(R.id.profileFiliere);
        profileGroupe = (TextView) view.findViewById(R.id.profileGroupe) ;
        profileEmail = (TextView) view.findViewById(R.id.profileEmail) ;
        profilePhoto = (ImageView) view.findViewById(R.id.profilePhoto);
        profileRv = (RecyclerView) view.findViewById(R.id.profileRv);
        editProfile = (LinearLayout) view.findViewById(R.id.editProfileLayout);


        postAdapter = new PostAdapter(getContext());
        postAdapter.setClickFromProfile(true);
        aq = new AQuery(getContext());
        listPost = new ArrayList<>();
        IS_ACTIVE = true;


        IDUSR = admin.getId();

        postAdapter.setMyList(listPost);
        profileRv.setLayoutManager(new LinearLayoutManager(getContext()));
        profileRv.setAdapter(postAdapter);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),ParametreActivity.class);
                startActivity(intent);
            }
        });

        chercherUser(IDUSR);

        return view;

    }




    private void chercherUser(final String idUsr) {



        profileEmail.setText(admin.getEmail());
        profileGroupe.setText("Groupe " + admin.getGroupe());
        profileFiliere.setText(admin.getNiveau() + " " + admin.getFiliere().toUpperCase());
        //Drawable d = new BitmapDrawable(getResources(), aq.getCachedImage(publicUrl+admin.getImg()));
        aq.id(profilePhoto).image(publicUrl + admin.getImg());

        if (isNetworkAvailable())
            chargerPost();


    }





    private void chargerPost(){

        String url = publicUrl + "student/getpost/" + admin.getId();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {
                    if (listPost.size() != 0 ) listPost.clear();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject postJson = response.getJSONObject(i);
                        String id , txtpost, datepost, id_user;
                        id = postJson.getString("_id");
                        txtpost = postJson.getString("txtpost");
                        datepost = postJson.getString("datepost");
                        id_user = postJson.getString("idusr");
                        JSONArray imgpost = postJson.getJSONArray("imgpost");
                        ArrayList<String> imgListPost = new ArrayList<>();
                        for (int j=0 ; j<imgpost.length() ; j++) {
                            imgListPost.add(imgpost.getString(j));
                        }

                        Post p = new Post(txtpost, id_user, imgListPost , id );
                        p.setDatepost(datepost);
                        listPost.add(0,p);



                    }
                    postAdapter.setMyList(listPost);
                    postAdapter.notifyDataSetChanged();
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




    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

}
