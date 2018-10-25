package com.example.sadokmm.student.Fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.ImageRequest;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.util.Progress;
import com.example.sadokmm.student.Adapters.PostAdapter;
import com.example.sadokmm.student.Objects.Post;
import com.example.sadokmm.student.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.sadokmm.student.Activities.firstActivity.getResizedBitmap;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class PostFragment extends Fragment {


    private RecyclerView postRv;
    private PostAdapter postAdapter;
    ArrayList<Post> ll;
    ProgressDialog prgDialog;

    public PostFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post, container, false);

        postRv = (RecyclerView) view.findViewById(R.id.postRV);
        postAdapter = new PostAdapter(getActivity());

        ll = new ArrayList<>();
        charger(0, 0);

        //postAdapter.setMyList(ll);
        postRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRv.setAdapter(postAdapter);


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public void charger(int deb, int fin) {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());


        String url = publicUrl + "student/postg/";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject postJson = response.getJSONObject(i);
                                String id , txtpost, datepost, emailusr, imgpost;
                                id = postJson.getString("_id");
                                txtpost = postJson.getString("txtpost");
                                datepost = postJson.getString("datepost");
                                emailusr = postJson.getString("emailusr");
                                imgpost = postJson.getString("imgpost");

                                //getImageByUrl(publicUrl + imgpost, i);
                                Post p = new Post(txtpost, emailusr, imgpost , id);
                                p.setDatepost(datepost);
                                ll.add(p);
                                postAdapter.getMyListPost().add(p);
                                postAdapter.notifyDataSetChanged();

                                //Toast.makeText(getContext(), "gggoeg", Toast.LENGTH_LONG).show();
                                //prgDialog.done();

                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonArrayRequest);

    }


    //download img using volley
    public void getImageByUrl(String url, final int position) {
        // Initialize a new RequestQueue instance
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        final Bitmap[] im = new Bitmap[1];
        im[0] = null;

        prgDialog = new ProgressDialog(getContext());
        prgDialog.setMessage("Téléchargementt en cours ...");
        prgDialog.setIndeterminate(false);
        //prgDialog.setMax(100);
        prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        prgDialog.setCancelable(true);
        prgDialog.show();

        // Initialize a new ImageRequest
        ImageRequest imageRequest = new ImageRequest(url, getContext().getResources(), getContext().getContentResolver(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                im[0] = response;
                prgDialog.dismiss();
                //postAdapter.getMyListPost().get(position).setImgpost(response);
                postAdapter.notifyDataSetChanged();

            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();

            }
        });


        // Add ImageRequest to the RequestQueue
        requestQueue.add(imageRequest);


    }
}
