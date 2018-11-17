package com.example.sadokmm.student.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sadokmm.student.Activities.AfficheJournee;
import com.example.sadokmm.student.Activities.SalleActivity;
import com.example.sadokmm.student.Adapters.ActuAdapter;
import com.example.sadokmm.student.Adapters.ImageAdapter;
import com.example.sadokmm.student.Objects.Actualite;
import com.example.sadokmm.student.R;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static com.example.sadokmm.student.Activities.MainActivity.currentHour;
import static com.example.sadokmm.student.Activities.MainActivity.currentSession;
import static com.example.sadokmm.student.Activities.MainActivity.jourNum;
import static com.example.sadokmm.student.Activities.MainActivity.seanceActuelle;
import static com.example.sadokmm.student.Activities.MainActivity.seanceVide;
import static com.example.sadokmm.student.Activities.MainActivity.weekend;
import static com.example.sadokmm.student.Activities.firstActivity.admin;
import static com.example.sadokmm.student.Activities.firstActivity.monEmploi;
import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class TimeFragment extends Fragment {


    public static TextView afficheBtn;
    private RecyclerView actuRv;
    private ActuAdapter actuAdapter ;
    private ArrayList<Actualite> listeAct ;

    public TimeFragment() {

    }




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_time,container,false);
        actuelle(view);

        actuRv = (RecyclerView) view.findViewById(R.id.actuRv);
        actuAdapter = new ActuAdapter(getContext());
        actuRv.setLayoutManager(new LinearLayoutManager(getContext()));


        listeAct = new ArrayList<>();
        chargerActu();



        return view ;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }




    void actuelle(View view) {

        TextView numSeance, matiere, enseignant, salle, parQuinzaine, type, sTime;
        LinearLayout parQuinzaineLayout, actuelle, coursLayout, profLayout, salleLayout;


        numSeance = (TextView) view.findViewById(R.id.numSeance);
        matiere = (TextView) view.findViewById(R.id.matiere);
        enseignant = (TextView) view.findViewById(R.id.enseignant);
        salle = (TextView) view.findViewById(R.id.salle);
        type = (TextView) view.findViewById(R.id.type);
        parQuinzaine = (TextView) view.findViewById(R.id.parQuinziane);
        parQuinzaineLayout = (LinearLayout) view.findViewById(R.id.parQuinzaineLayout);
        //actuelle = (LinearLayout) view.findViewById(R.id.actuelle);
        sTime = (TextView) view.findViewById(R.id.sTime);
        salleLayout = (LinearLayout) view.findViewById(R.id.salleLayout);
        profLayout = (LinearLayout) view.findViewById(R.id.profLayout);
        coursLayout = (LinearLayout) view.findViewById(R.id.coursLayout);
        afficheBtn = (TextView) view.findViewById(R.id.afficheJournee);

        afficheBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getActivity(), AfficheJournee.class);
                    startActivity(intent);
                }
                catch (Exception e) {
                    afficheBtn.setText(e.toString());
                }
            }
        });




        if (jourNum == 6) {
            seanceActuelle = weekend;
            afficheBtn.setText("Afficher votre emploi");
            weekend.setNumSeance(0);
        }
        else if (jourNum == 5) {
            if (currentHour < 12) {
                for (int i = 0; i < monEmploi.getJours().get(5).getListSeance().size(); i++) {
                    if (monEmploi.getJours().get(5).getListSeance().get(i).getNumSeance() == currentSession) {
                        seanceActuelle = (monEmploi.getJours().get(5).getListSeance().get(i));
                    }
                }
            }
            else {
                seanceActuelle = weekend;
                afficheBtn.setText("Afficher votre emploi");
                weekend.setNumSeance(0);
            }
        }

        else {
            if (currentSession != 0) {

                if (jourNum == 0) {

                    for (int i = 0; i < monEmploi.getJours().get(0).getListSeance().size(); i++) {

                        if (monEmploi.getJours().get(0).getListSeance().get(i).getNumSeance() == currentSession) {
                            //Toast.makeText(this,"hani lenaAA",Toast.LENGTH_LONG).show();
                            seanceActuelle = monEmploi.getJours().get(0).getListSeance().get(i);
                            //Toast.makeText(this,"hani lena" + monEmploi.getJours().get(0).getListSeance().get(i).getMatiere(),Toast.LENGTH_LONG).show();

                            break;
                        }
                    }


                } else if (jourNum == 1) {

                    for (int i = 0; i < monEmploi.getJours().get(1).getListSeance().size(); i++) {
                        if (monEmploi.getJours().get(1).getListSeance().get(i).getNumSeance() == currentSession) {
                            seanceActuelle = monEmploi.getJours().get(1).getListSeance().get(i);
                            break;
                        }


                    }
                } else if (jourNum == 2) {

                    for (int i = 0; i < monEmploi.getJours().get(2).getListSeance().size(); i++) {
                        if (monEmploi.getJours().get(2).getListSeance().get(i).getNumSeance() == currentSession) {
                            seanceActuelle = (monEmploi.getJours().get(2).getListSeance().get(i));
                        }
                    }
                } else if (jourNum == 3) {

                    for (int i = 0; i < monEmploi.getJours().get(3).getListSeance().size(); i++) {
                        if (monEmploi.getJours().get(3).getListSeance().get(i).getNumSeance() == currentSession) {
                            seanceActuelle = (monEmploi.getJours().get(3).getListSeance().get(i));
                        }
                    }
                } else if (jourNum == 4) {

                    for (int i = 0; i < monEmploi.getJours().get(4).getListSeance().size(); i++) {
                        if (monEmploi.getJours().get(4).getListSeance().get(i).getNumSeance() == currentSession) {
                            seanceActuelle = (monEmploi.getJours().get(4).getListSeance().get(i));

                        }
                    }
                }
            }
        }





        matiere.setText(seanceActuelle.getMatiere());
        enseignant.setText(seanceActuelle.getEnseignant());
        type.setText(seanceActuelle.getType());
        salle.setText(seanceActuelle.getSalle());

        salle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SalleActivity.class);
                String nom_salle = seanceActuelle.getSalle().toLowerCase();


                if (nom_salle.toCharArray()[0] == 'a') {
                    if (nom_salle.equals("amphi1") || nom_salle.equals("amphi 1"))
                        nom_salle = "amphi1" ;
                    else if (nom_salle.equals("amphi2") || nom_salle.equals("amphi 2"))
                        nom_salle = "amphi2";
                    else nom_salle = "a" ;
                }
                if (nom_salle.toCharArray()[0] == 'b')
                    nom_salle = "b";
                if (nom_salle.toCharArray()[0] == 'c')
                    nom_salle = "c";
                if (nom_salle.toCharArray()[0] == 'd')
                    nom_salle = "d";


                intent.putExtra("nom_salle",nom_salle);
                intent.putExtra("nom_salle_correcte",seanceActuelle.getSalle());
                getContext().startActivity(intent);
            }
        });


        if (seanceActuelle.getNumSeance() == 1) {
            numSeance.setText("1ère Séance : ");
            sTime.setText("8:00 - 9:30");
        } else if (seanceActuelle.getNumSeance() == 2) {
            numSeance.setText("2ème Séance : ");
            sTime.setText("9:35 - 11:05");
        } else if (seanceActuelle.getNumSeance() == 3) {
            numSeance.setText("3ème Séance : ");
            sTime.setText("11:10 - 12:40");
        } else if (seanceActuelle.getNumSeance() == 4) {
            numSeance.setText("4ème Séance : ");
            sTime.setText("13:15 - 14:45");
        } else if (seanceActuelle.getNumSeance() == 5) {
            numSeance.setText("5ème Séance : ");
            sTime.setText("14:50 - 16:20");
        } else if (seanceActuelle.getNumSeance() == 6) {
            numSeance.setText("6ème Séance : ");
            sTime.setText("16:25 - 17:55");
        } else {
            numSeance.setText("");
            sTime.setText("");
            coursLayout.setVisibility(View.INVISIBLE);
            profLayout.setVisibility(View.INVISIBLE);
            salleLayout.setVisibility(View.INVISIBLE);
        }






        if (seanceActuelle == seanceVide ) {
            coursLayout.setVisibility(View.INVISIBLE);
            //parQuinzaineLayout.setVisibility(View.INVISIBLE);
            profLayout.setVisibility(View.INVISIBLE);
            salleLayout.setVisibility(View.INVISIBLE);
        }

        //Toast.makeText(this,"hggg" + jourNum,Toast.LENGTH_LONG).show();

    }




    private void chargerActu(){



        String url = publicUrl + "student/getisgnews";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String text , titre , date , lien ;
                try {


                for (int i=0 ; i<response.length() ; i++) {
                    JSONObject act = response.getJSONObject(i);
                    titre = act.getString("titre");
                    text = act.getString("txt");
                    date = act.getString("date");
                    lien = act.getString("lien");


                    listeAct.add(new Actualite(titre,text,date,lien));

                }

                actuAdapter.setListActu(listeAct);
                actuRv.setAdapter(actuAdapter);

                }
                catch (JSONException e) {
                    Toast.makeText(getContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }



}
