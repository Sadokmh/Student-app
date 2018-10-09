package com.example.sadokmm.student.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sadokmm.student.Objects.Seance;
import com.example.sadokmm.student.R;

import java.util.ArrayList;
import java.util.List;

//import static com.example.sadokmm.student.Activities.MainActivity.currentSession;

public class MainRvAdapter extends RecyclerView.Adapter<MainRvAdapter.ViewHolder>  {


    private List<Seance> myListSeance;
    private LayoutInflater layoutInflater;
    Context con;


    /*public static ArrayList<Plan> getMyListPlanFood() {
        return myListPlanFood;
    }*/



    public MainRvAdapter(Context context) {
        layoutInflater=LayoutInflater.from(context);
        myListSeance=new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.custom_main_rv_item,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);


        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Seance seance=myListSeance.get(i);
        viewHolder.matiere.setText(seance.getMatiere());
        viewHolder.enseignant.setText(seance.getEnseignant());
        viewHolder.type.setText(seance.getType());
        viewHolder.salle.setText(seance.getSalle());


        if ( seance.getNumSeance() == 1 ) {
            viewHolder.numSeance.setText("1ère Séance : ");
            viewHolder.sTime.setText("8:00 - 9:30");
        }
        else if ( seance.getNumSeance() == 2 ) {
            viewHolder.numSeance.setText("2ème Séance : ");
            viewHolder.sTime.setText("9:35 - 11:05");
        }
        else if ( seance.getNumSeance() == 3 ) {
            viewHolder.numSeance.setText("3ème Séance : ");
            viewHolder.sTime.setText("11:10 - 12:40");
        }
        else if ( seance.getNumSeance() == 4 ) {
            viewHolder.numSeance.setText("4ème Séance : ");
            viewHolder.sTime.setText("13:15 - 14:45");
        }
        else if ( seance.getNumSeance() == 5 ) {
            viewHolder.numSeance.setText("5ème Séance : ");
            viewHolder.sTime.setText("14:50 - 16:20");
        }
        else if ( seance.getNumSeance() == 6 ){
            viewHolder.numSeance.setText("6ème Séance : ");
            viewHolder.sTime.setText("16:25 - 17:55");
        }
        else {
            viewHolder.numSeance.setText("");
            viewHolder.sTime.setText("");
            viewHolder.coursLayout.setVisibility(View.INVISIBLE);
            viewHolder.profLayout.setVisibility(View.INVISIBLE);
            viewHolder.salleLayout.setVisibility(View.INVISIBLE);
        }





        if (seance.getParQuanzaine()){
            viewHolder.parQuinzaineLayout.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.parQuinzaineLayout.setVisibility(View.INVISIBLE);
        }





    }

    @Override
    public int getItemCount() {
        return myListSeance.size();
    }

    public void setMyList(List<Seance> list){
        myListSeance=list;
        notifyItemRangeChanged(0,list.size());
    }





    //

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView numSeance,matiere,enseignant,salle,parQuinzaine,type,sTime;
        private LinearLayout parQuinzaineLayout,actuelle,coursLayout,profLayout,salleLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            numSeance=(TextView)itemView.findViewById(R.id.numSeance);
            matiere=(TextView)itemView.findViewById(R.id.matiere);
            enseignant = (TextView)itemView.findViewById(R.id.enseignant);
            salle=(TextView)itemView.findViewById(R.id.salle);
            type=(TextView)itemView.findViewById(R.id.type);
            parQuinzaine = (TextView)itemView.findViewById(R.id.parQuinziane);
            parQuinzaineLayout=(LinearLayout)itemView.findViewById(R.id.parQuinzaineLayout);
            actuelle=(LinearLayout)itemView.findViewById(R.id.actuelle);
            sTime = (TextView)itemView.findViewById(R.id.sTime);
            salleLayout=(LinearLayout)itemView.findViewById(R.id.salleLayout);
            profLayout=(LinearLayout)itemView.findViewById(R.id.profLayout);
            coursLayout=(LinearLayout)itemView.findViewById(R.id.coursLayout);

        }
    }




}
