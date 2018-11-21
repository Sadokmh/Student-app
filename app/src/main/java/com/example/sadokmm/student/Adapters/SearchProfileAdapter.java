package com.example.sadokmm.student.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.androidquery.AQuery;
import com.example.sadokmm.student.Activities.ProfileActivity;
import com.example.sadokmm.student.Objects.User;
import com.example.sadokmm.student.R;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class SearchProfileAdapter extends RecyclerView.Adapter<SearchProfileAdapter.ViewHolder> {


    private Context context;
    private LayoutInflater layoutInflater;
    private List<User> listUser ;

    private RequestQueue requestQueue ;
    private String url = publicUrl + "student/searchuser/";

    private AQuery aq;

    public SearchProfileAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        listUser = new ArrayList<>();
        aq = new AQuery(context);
        requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public SearchProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.custom_rech_profil,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchProfileAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.profileName.setText(listUser.get(i).getPrenom() + " " + listUser.get(i).getNom());

        aq.id(viewHolder.profileImg).image(listUser.get(i).getImg());

        viewHolder.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ProfileActivity.class);
                intent.putExtra("id",listUser.get(i).getId());
                context.startActivity(intent);
            }
        });
        viewHolder.profileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ProfileActivity.class);
                intent.putExtra("id",listUser.get(i).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }


    public List<User> getListUser() {
        return listUser;
    }



    public void setListUser(List<User> myListUser) {
        listUser.addAll(myListUser);
        notifyDataSetChanged();
    }











    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profileImg;
        private TextView profileName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImg = (CircleImageView) itemView.findViewById(R.id.profileImg);
            profileName = (TextView) itemView.findViewById(R.id.profileName);
        }
    }


}
