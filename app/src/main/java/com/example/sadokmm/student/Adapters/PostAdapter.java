package com.example.sadokmm.student.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sadokmm.student.Objects.Post;
import com.example.sadokmm.student.Objects.Seance;
import com.example.sadokmm.student.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>  {


    private List<Post> myListPost;
    private LayoutInflater layoutInflater;
    Context con;


    /*public static ArrayList<Plan> getMyListPlanFood() {
        return myListPlanFood;
    }*/



    public PostAdapter(Context context) {
        layoutInflater=LayoutInflater.from(context);
        myListPost=new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=layoutInflater.inflate(R.layout.custom_post,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);


        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Post post=myListPost.get(i);
        viewHolder.profilName.setText(post.getNomComplet());
        viewHolder.filiere.setText(post.getFiliereEtNiveau());
        viewHolder.datePost.setText(post.getDate());
        viewHolder.textPost.setText(post.getTextPost());
        viewHolder.imgUsr.setImageBitmap(post.getImgUsr());
        viewHolder.imgPost.setImageBitmap(post.getImgPost());







    }

    @Override
    public int getItemCount() {
        return myListPost.size();
    }

    public void setMyList(List<Post> list){
        myListPost=list;
        notifyItemRangeChanged(0,list.size());
    }





    //

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView profilName,filiere,textPost,datePost;
        private ImageView imgPost,likePost,commentPost;
        private CircleImageView imgUsr;
        private EditText textComm;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilName=(TextView)itemView.findViewById(R.id.profileName);
            filiere=(TextView)itemView.findViewById(R.id.filiereUser);
            datePost = (TextView)itemView.findViewById(R.id.datePost);
            textPost=(TextView)itemView.findViewById(R.id.textPost);
            imgPost=(ImageView) itemView.findViewById(R.id.imagePost);
            commentPost = (ImageView) itemView.findViewById(R.id.commentPostButton);
            likePost=(ImageView) itemView.findViewById(R.id.likePostButton);
             imgUsr=(CircleImageView) itemView.findViewById(R.id.profileImg);
            textComm = (EditText) itemView.findViewById(R.id.commentPostText);
            //salleLayout=(LinearLayout)itemView.findViewById(R.id.salleLayout);
            //profLayout=(LinearLayout)itemView.findViewById(R.id.profLayout);
            //coursLayout=(LinearLayout)itemView.findViewById(R.id.coursLayout);

        }
    }




}