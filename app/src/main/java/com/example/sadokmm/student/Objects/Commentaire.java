package com.example.sadokmm.student.Objects;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Commentaire {

    private String id ;
    private String txtComm;
    private String dateComm;
    private String emailUsr;
    private String idPost;


    public Commentaire(String id, String txtComm, String emailUsr , String idPost) {
        this.id = id;
        this.txtComm = txtComm;
        this.emailUsr = emailUsr;
        this.idPost = idPost;
        this.dateComm = getStringDate(Calendar.getInstance().getTime());
    }


    private String getStringDate(Date dateNow) {
        DateFormat df = new SimpleDateFormat("d MMM yyyy");
        String date = df.format(dateNow);
        return date;
    }

    public String getTxtComm() {
        return txtComm;
    }

    public void setTxtComm(String txtComm) {
        this.txtComm = txtComm;
    }

    public String getDateComm() {
        return dateComm;
    }

    public void setDateComm(String dateComm) {
        this.dateComm = dateComm;
    }

    public String getEmailUsr() {
        return emailUsr;
    }

    public void setEmailUsr(String emailUsr) {
        this.emailUsr = emailUsr;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
