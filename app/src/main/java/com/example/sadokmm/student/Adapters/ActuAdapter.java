package com.example.sadokmm.student.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sadokmm.student.Activities.Site;
import com.example.sadokmm.student.Objects.Actualite;
import com.example.sadokmm.student.R;

import java.util.ArrayList;

public class ActuAdapter extends RecyclerView.Adapter<ActuAdapter.ViewHolder> {


    private ArrayList<Actualite> listActu ;
    private Context context;
    private LayoutInflater layoutInflater;

    public ActuAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        listActu = new ArrayList<>();
    }


    public ArrayList<Actualite> getListActu() {
        return listActu;
    }

    public void setListActu(ArrayList<Actualite> listActu) {
        this.listActu = listActu;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.custom_feed_item,viewGroup,false);
        ActuAdapter.ViewHolder viewHolder = new ActuAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {

        final Actualite actualite = listActu.get(i);

        viewHolder.titreActu.setText(actualite.getTitre());
        viewHolder.txtActu.setText(actualite.getText());
        viewHolder.dateActu.setText(actualite.getDate());

        viewHolder.titreActu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Site.class);
                intent.putExtra("url",actualite.getLien());
                intent.putExtra("titre",actualite.getTitre());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listActu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titreActu,txtActu,dateActu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titreActu = (TextView) itemView.findViewById(R.id.titreActu);
            txtActu = (TextView) itemView.findViewById(R.id.txtActu);
            dateActu = (TextView) itemView.findViewById(R.id.dateActu);
        }
    }
}
