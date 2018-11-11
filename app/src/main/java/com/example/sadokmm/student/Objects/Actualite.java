package com.example.sadokmm.student.Objects;

public class Actualite {

    private String titre ;
    private String text ;
    private String date ;
    private String lien ;


    public Actualite(String titre, String text, String date, String lien) {
        this.titre = titre;
        this.text = text;
        this.date = date;
        this.lien = lien;
    }


    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }
}
