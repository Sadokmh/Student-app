package com.example.sadokmm.student.Objects;

import android.graphics.Bitmap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Post {

    private String nom;
    private String prenom;
    private String filiere;
    private int niveau;
    private Bitmap imgUsr;
    private Bitmap imgPost;
    private String textPost;
    private String date;


    public Post(String nom, String prenom, String filiere, int niveau, Bitmap imgUsr, Bitmap imgPost, String textPost) {
        this.nom = nom;
        this.prenom = prenom;
        this.filiere = filiere.toUpperCase();
        this.niveau = niveau;
        this.imgUsr = imgUsr;
        this.imgPost = imgPost;
        this.textPost = textPost;
        this.date = getStringDate(Calendar.getInstance().getTime());
    }



    public String getNomComplet() {
        return this.prenom + " " + this.nom;
    }

    public String getFiliereEtNiveau(){
        return this.niveau + this.filiere;
    }

    private String getStringDate(Date dateNow) {
        DateFormat df = new SimpleDateFormat("d MMM yyyy");
        String date = df.format(dateNow);
        return date;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public Bitmap getImgUsr() {
        return imgUsr;
    }

    public void setImgUsr(Bitmap imgUsr) {
        this.imgUsr = imgUsr;
    }

    public Bitmap getImgPost() {
        return imgPost;
    }

    public void setImgPost(Bitmap imgPost) {
        this.imgPost = imgPost;
    }

    public String getTextPost() {
        return textPost;
    }

    public void setTextPost(String textPost) {
        this.textPost = textPost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }





}
