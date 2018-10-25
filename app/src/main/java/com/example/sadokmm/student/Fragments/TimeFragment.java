package com.example.sadokmm.student.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sadokmm.student.Activities.AfficheJournee;
import com.example.sadokmm.student.R;

import static com.example.sadokmm.student.Activities.MainActivity.currentSession;
import static com.example.sadokmm.student.Activities.MainActivity.jourNum;
import static com.example.sadokmm.student.Activities.MainActivity.seanceActuelle;
import static com.example.sadokmm.student.Activities.MainActivity.seanceVide;
import static com.example.sadokmm.student.Activities.firstActivity.monEmploi;

public class TimeFragment extends Fragment {


    public static TextView afficheBtn;

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
            } /*else if (jourNum == 5) {

                for (int i = 0; i < monEmploi.getJours().get(5).getListSeance().size(); i++) {
                    if (monEmploi.getJours().get(5).getListSeance().get(i).getNumSeance() == currentSession) {
                        seanceActuelle = (monEmploi.getJours().get(5).getListSeance().get(i));
                    }
                }
            }*/
        }

        else {
            //matiere.setText("Pas de cours maintenant");
            seanceActuelle = seanceVide;


        }




        matiere.setText(seanceActuelle.getMatiere());
        enseignant.setText(seanceActuelle.getEnseignant());
        type.setText(seanceActuelle.getType());
        salle.setText(seanceActuelle.getSalle());


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



}
