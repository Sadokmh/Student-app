package com.example.sadokmm.student.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sadokmm.student.Activities.MainActivity;
import com.example.sadokmm.student.Activities.ParametreActivity;
import com.example.sadokmm.student.Activities.ProfileActivity;
import com.example.sadokmm.student.Activities.SalleActivity;
import com.example.sadokmm.student.Adapters.AdapterDrawer;
import com.example.sadokmm.student.Listeners.RecyclerTouchListener;
import com.example.sadokmm.student.Objects.Information;
import com.example.sadokmm.student.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.sadokmm.student.Activities.firstActivity.SESSION;
import static com.example.sadokmm.student.Activities.firstActivity.admin;

public class NavigationDrawerFragment extends Fragment  {

    private View containerView;
    private RecyclerView recyclerView;

    public static final String PREF_FILE_Name="testpref";
    public static final String KEY_USER_LEARNED_DRAWER="user_learned_drawer";

    public static AdapterDrawer mAdap;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;


    //for setting the DL for the first run of app
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer=Boolean.valueOf(readFromPreferences(getContext(),KEY_USER_LEARNED_DRAWER,"false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState=true;
        }
    }

    public NavigationDrawerFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_naivgation_drawer,container,false);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView)view.findViewById(R.id.drawerList);

        mAdap = new AdapterDrawer(getActivity(),getData());

        recyclerView.setAdapter(mAdap);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),recyclerView,new RecyclerTouchListener.ClickListener(){

            @Override
            public void onClick(View view, int position) {

                switch (position) {

                    case 1 : {
                        break;
                    }
                    case 2 : {
                        Intent intent = new Intent(getContext(),ProfileActivity.class);
                        intent.putExtra("id",admin.getId());
                        getContext().startActivity(intent);
                        break;
                    }
                    case 3 : {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("A propos");
                        alert.setMessage("ISG STUDENT est une application blablbablaaa .......\n blablabla ..........");
                        alert.setPositiveButton("OK", null);
                        alert.show();
                        break;
                    }
                    case 4 : {
                        Intent intent = new Intent(getContext(), SalleActivity.class);
                        intent.putExtra("nom_salle","Plan de l'ISGs");
                        intent.putExtra("nom_salle_correcte","Plan de l'ISGs");
                        startActivity(intent);
                        break;
                    }
                    case 5 : {
                        Intent intent = new Intent(getContext(), ParametreActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 6 : {
                        SharedPreferences.Editor editor=getActivity().getSharedPreferences(SESSION,Context.MODE_PRIVATE).edit();
                        editor.clear();
                        editor.commit();
                        getActivity().finish();
                        ((MainActivity) getActivity()).deconnexion();
                        break;
                    }

                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }





    public static List<Information> getData(){
        List<Information> data=new ArrayList<>();
        int[] icons = {R.drawable.ic_home , R.drawable.ic_person , R.drawable.ic_about , R.drawable.ic_map , R.drawable.ic_build_black_24dp ,R.drawable.ic_logout };
        String[] titles= {"Acceuil" , "Profile" , "Info" , "Plan de l'ISG" , "Paramètres" , "Déconnexion" };

        for (int i=0 ; i<icons.length && i<titles.length ; i++)
        {
            Information current = new Information();
            current.icon=icons[i];
            current.title=titles[i];
            data.add(current);
        }
        return data;
    }






    public static void saveToPreferences(Context context, String preferenceName , String preferenceValue){

        SharedPreferences sharedPreferences=context.getSharedPreferences(PREF_FILE_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.commit();
    }

    public static String readFromPreferences(Context context, String preferenceName , String defaultValue){

        SharedPreferences sharedPreferences=context.getSharedPreferences(PREF_FILE_Name, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName,defaultValue);

    }




    public void settUp(int fragmentId, DrawerLayout drawerLayout , final android.support.v7.widget.Toolbar toolbar) {

        containerView=getActivity().findViewById(fragmentId);
        mDrawerLayout=drawerLayout;
        mDrawerToggle= new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.Open,R.string.Close){

            @Override
            public void onDrawerOpened(View drawerView) {

                if (!mUserLearnedDrawer){
                    mUserLearnedDrawer=true;
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,mUserLearnedDrawer+""); // we add "" because mUserLearnedDrawer is Boolean and the param needs String

                }
                getActivity().invalidateOptionsMenu();

            }


            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                /*((MainActivity) getActivity()).onDrawerSlide(slideOffset);
                toolbar.setAlpha(1 - slideOffset/2);*/
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState){

            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

}
