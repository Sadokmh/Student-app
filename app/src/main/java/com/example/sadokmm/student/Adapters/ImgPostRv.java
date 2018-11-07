package com.example.sadokmm.student.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sadokmm.student.R;

import java.util.ArrayList;

public class ImgPostRv extends RecyclerView.Adapter<ImgPostRv.ViewHolder> {

    private ArrayList<Bitmap> listImg;
    private Context context;
    private LayoutInflater layoutInflater;
    private String fileName;

    public ImgPostRv(Context context) {
        this.context = context;
        layoutInflater=LayoutInflater.from(context);
        listImg = new ArrayList<>();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = layoutInflater.inflate(R.layout.img_post_rv_custom_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.imgShow.setImageBitmap(listImg.get(i));
       // viewHolder.fileName.setText(fileName);

    }

    @Override
    public int getItemCount() {
        return listImg.size();
    }

    public ArrayList<Bitmap> getListImg() {
        return listImg;
    }

    public void setListImg(ArrayList<Bitmap> listImg) {
        this.listImg = listImg;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgShow;
        private TextView fileName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgShow = (ImageView) itemView.findViewById(R.id.imgShow);
            //fileName = (TextView) itemView.findViewById(R.id.filename);

        }
    }
}
