package com.example.sadokmm.student.Fragments;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sadokmm.student.Adapters.PostAdapter;
import com.example.sadokmm.student.Objects.Post;
import com.example.sadokmm.student.R;

import java.util.ArrayList;

public class PostFragment extends Fragment {


    private RecyclerView postRv;
    private PostAdapter postAdapter;

    public PostFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post,container,false);

        postRv = (RecyclerView)view.findViewById(R.id.postRV);
        postAdapter=new PostAdapter(getActivity());
        ArrayList<Post> ll = new ArrayList<>();
        ll.add(new Post("Mhiri" , "Sadok Mourad", "Lfig" , 3, BitmapFactory.decodeResource(getResources(),R.drawable.sadok),BitmapFactory.decodeResource(getResources(),R.drawable.cc),"sahihffieffhiehzeihzeifhzeifhzfhzfezhfizfezofhzeofezhfezfhzefhzeofhezfhzepfhzofhzeofhezophfezhfoezfopzehfezhfozhefoezfhzepfzofhezf"));
        ll.add(new Post("Mhiri" , "Sadok Mourad", "Lfig" , 3, BitmapFactory.decodeResource(getResources(),R.drawable.sadok),BitmapFactory.decodeResource(getResources(),R.drawable.cc),"sahihffieffhiehzeihzeifhzeifhzfhzfezhfizfezofhzeofezhfezfhzefhzeofhezfhzepfhzofhzeofhezophfezhfoezfopzehfezhfozhefoezfhzepfzofhezf"));

        postAdapter.setMyList(ll);
        postRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRv.setAdapter(postAdapter);



        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
