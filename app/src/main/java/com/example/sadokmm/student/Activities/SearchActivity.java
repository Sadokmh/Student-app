package com.example.sadokmm.student.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sadokmm.student.Adapters.SearchProfileAdapter;
import com.example.sadokmm.student.Objects.User;
import com.example.sadokmm.student.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;


public class SearchActivity extends AppCompatActivity {


    private RecyclerView rvProfile ;
    private SearchProfileAdapter searchProfileAdapter;
    private Toolbar toolbar;
    private SearchView searchView ;

    private String url = publicUrl + "student/searchuser/";
    private RequestQueue requestQueue;

    private List<User> myList;





    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        requestQueue = Volley.newRequestQueue(this);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Rechercher...");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myList = new ArrayList<>();

        rvProfile = (RecyclerView) findViewById(R.id.profileSearchRv);
        searchProfileAdapter = new SearchProfileAdapter(this);
        rvProfile.setLayoutManager(new LinearLayoutManager(this));
        rvProfile.setAdapter(searchProfileAdapter);
        //searchProfileAdapter.setMyListUser(null);

    }







    public void searchProfile(String s) {

        String myUrl = url + s;
        searchProfileAdapter.getListUser().clear();
        searchProfileAdapter.notifyDataSetChanged();



        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {



                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String id, nom, prenom, img, filiere="", email, groupe="0", niveau="0";
                        id = jsonObject.getString("id");
                        nom = jsonObject.getString("nom");
                        prenom = jsonObject.getString("prenom");
                        img = publicUrl + jsonObject.getString("img");
                        email = jsonObject.getString("email");

                        searchProfileAdapter.getListUser().add(new User(id, nom, prenom, email, img, filiere, Integer.parseInt(groupe), Integer.parseInt(niveau)));
                        searchProfileAdapter.notifyDataSetChanged();
                    }


                }
                catch (JSONException e) {
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        requestQueue.getCache().clear();
        requestQueue.add(jsonArrayRequest);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);

        final MenuItem search=menu.findItem(R.id.search_action);
        searchView=(android.support.v7.widget.SearchView)search.getActionView();
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                searchProfile(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {



                    //requestQueue.getCache().clear();
                   /*myList.clear();
                    //searchProfileAdapter.getListUser().clear();
                    searchProfile(s);*/
                   searchProfile(s);




                return false;
            }


        });
        return true;
    }

}
