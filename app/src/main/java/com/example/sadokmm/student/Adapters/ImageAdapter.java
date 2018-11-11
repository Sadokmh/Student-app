package com.example.sadokmm.student.Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.example.sadokmm.student.Activities.FullScreenImage;
import com.example.sadokmm.student.R;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.util.ArrayList;

import static android.content.Context.DOWNLOAD_SERVICE;
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
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        AQuery aq = new AQuery(context);

        /*imageView.setTag("1");
        TextView imgNum = new TextView(context);
        imgNum.setText((position+1)+ "/" + (imgUrls.size()+1) );*/


            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.imgpost_custom_item, container,false);
            imgPost = (ImageView) itemView.findViewById(R.id.imgPost);
            numImgPost = (TextView) itemView.findViewById(R.id.numImgPost);
            file_name = (TextView) itemView.findViewById(R.id.file_name);

            //Fresco.initialize(this);


            if (imgUrls.get(position).toLowerCase().contains("fileisgstudent")) {
                file_name.setText(imgUrls.get(position));
                file_name.setVisibility(View.VISIBLE);
                imgPost.setImageDrawable(context.getDrawable(R.drawable.file));

                imgPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DownloadManager.Request request = new DownloadManager.Request(
                                Uri.parse(publicUrl + imgUrls.get(position)));

                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, imgUrls.get(position).substring(26,imgUrls.get(position).length()));
                        DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(request);
                        Toast.makeText(context, "Téléchargement de fichier ...", //To notify the Client that the file is being downloaded
                                Toast.LENGTH_LONG).show();
                    }
                });

            }

            else {
                aq.id(imgPost).image(publicUrl + imgUrls.get(position));
                numImgPost.setVisibility(View.VISIBLE);
                numImgPost.setText((position+1)+ "/" + imgUrls.size());

                imgPost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context,FullScreenImage.class);
                        intent.putExtra("imgurl",publicUrl+imgUrls.get(position));
                        context.startActivity(intent);
                    }
                });
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
