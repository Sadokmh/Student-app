package com.example.sadokmm.student.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sadokmm.student.Adapters.MainRvAdapter;
import com.example.sadokmm.student.Objects.Seance;
import com.example.sadokmm.student.R;

import java.util.ArrayList;

import static com.example.sadokmm.student.Activities.MainActivity.currentHour;
import static com.example.sadokmm.student.Activities.MainActivity.jourNum;
import static com.example.sadokmm.student.Activities.firstActivity.monEmploi;

public class AfficheJournee extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rv;
    private MainRvAdapter mAdapter;
    private ImageView flechePrec,fleshSuiv;
    private TextView jour;

    private int positionJour;

    final static String LUNDI = "lundi/monday/lun./mon.";
    final static String MARDI = "mardi/tuesday/mar./tue.";
    final static String MERCREDI = "mercredi/wednesday/mer./wed.";
    final static String JEUDI = "jeudi/thursday/jeu./thu.";
    final static String VENDREDI = "vendredi/friday/ven./fri.";
    final static String SAMEDI = "samedi/saturday/sam./sat.";
    final static String DIMANCHE = "dimanche/sunday/dim./sun.";

    private String jourAffiche;



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiche_journee);

        try {

            Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show();

            toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Votre journée");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            flechePrec = (ImageView) findViewById(R.id.prec);
            fleshSuiv = (ImageView) findViewById(R.id.suiv);
            jour = (TextView) findViewById(R.id.jourAffiche);


            rv = (RecyclerView) findViewById(R.id.mainRV);
            mAdapter = new MainRvAdapter(this);

            //positionJour = jourNum;
            positionJour = jourNum;


            //mAdapter.setMyList((monGroupe.getJourListe().get(jourNum).getListSeance()));
            if (jourNum == 6 || (jourNum == 5 && currentHour > 12)) {
                mAdapter.setMyList(monEmploi.getJours().get(0).getListSeance());
                mAdapter.notifyDataSetChanged();
                positionJour = 0 ;
                jour.setText(monEmploi.getJours().get(0).getNom());
            }
            else {
                mAdapter.setMyList(monEmploi.getJours().get(jourNum).getListSeance());
                jour.setText(monEmploi.getJours().get(jourNum).getNom());
            }

            //passer à la journée suivante
            fleshSuiv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (positionJour == 5) positionJour = -1;
                    mAdapter.setMyList(monEmploi.getJours().get(++positionJour).getListSeance());
                    jour.setText(monEmploi.getJours().get(positionJour).getNom());

                    mAdapter.notifyDataSetChanged();
                }
            });


            //passer à la journée précédente
            flechePrec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (positionJour == 0) positionJour = 6;
                    mAdapter.setMyList(monEmploi.getJours().get(--positionJour).getListSeance());
                    jour.setText(monEmploi.getJours().get(positionJour).getNom());
                    mAdapter.notifyDataSetChanged();
                }
            });


            rv.setLayoutManager(new LinearLayoutManager(this));
            rv.setAdapter(mAdapter);

        }
        catch (Exception e) {
            jour.setText(e.toString());
        }



    }
}