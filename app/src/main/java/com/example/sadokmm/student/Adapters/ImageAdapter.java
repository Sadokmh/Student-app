package com.example.sadokmm.student.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.example.sadokmm.student.R;

import java.util.ArrayList;

import static com.example.sadokmm.student.Activities.firstActivity.publicUrl;

public class ImageAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<String> imgUrls;
    private LayoutInflater inflater;
    private ImageView imgPost;
    private TextView numImgPost;
    private TextView file_name;

    public ImageAdapter(Context context , ArrayList<String> imgUrls) {
        this.context = context;
        this.imgUrls = imgUrls;
    }

    @Override
    public int getCount() {
        return imgUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        AQuery aq = new AQuery(context);

        /*imageView.setTag("1");
        TextView imgNum = new TextView(context);
        imgNum.setText((position+1)+ "/" + (imgUrls.size()+1) );*/


            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.imgpost_custom_item, container,false);
            imgPost = (ImageView) itemView.findViewById(R.id.imgPost);
            numImgPost = (TextView) itemView.findViewById(R.id.numImgPost);
            file_name = (TextView) itemView.findViewById(R.id.file_name);


            if (imgUrls.get(position).toLowerCase().contains("fileisgstudent")) {
                file_name.setText(imgUrls.get(position));
                file_name.setVisibility(View.VISIBLE);
                imgPost.setImageDrawable(context.getDrawable(R.drawable.file));
            }

            else {
                aq.id(imgPost).image(publicUrl + imgUrls.get(position));
                numImgPost.setVisibility(View.VISIBLE);
                numImgPost.setText((position+1)+ "/" + imgUrls.size());
            }






        ((ViewPager) container).addView(itemView);


        return itemView;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        notifyDataSetChanged();
    }

    public ArrayList<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(ArrayList<String> imgUrls) {
        this.imgUrls = imgUrls;
        notifyDataSetChanged();
    }
}
