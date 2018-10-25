package com.example.sadokmm.student.Objects;

import android.graphics.Bitmap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Post {

    private String id;
    private String txtpost;
    private String emailusr;
    private String imgpost;
    private String datepost;


    public Post(String txtpost, String emailusr, String imgpost , String id) {
        this.id = id;
        this.txtpost = txtpost;
        this.emailusr = emailusr;
        this.imgpost = imgpost;
        this.datepost = getStringDate(Calendar.getInstance().getTime());
    }

    private String getStringDate(Date dateNow) {
        DateFormat df = new SimpleDateFormat("d MMM yyyy");
        String date = df.format(dateNow);
        return date;
    }


    public String getTxtpost() {
        return txtpost;
    }

    public void setTxtpost(String txtpost) {
        this.txtpost = txtpost;
    }

    public String getEmailusr() {
        return emailusr;
    }

    public void setEmailusr(String emailusr) {
        this.emailusr = emailusr;
    }

    public String getImgpost() {
        return imgpost;
    }

    public void setImgpost(String imgpost) {
        this.imgpost = imgpost;
    }

    public String getDatepost() {
        return datepost;
    }

    public void setDatepost(String datepost) {
        this.datepost = datepost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
