package com.example.sadokmm.student.Objects;

import java.util.ArrayList;
import java.util.List;

public class Groupe {


    private String nom;
    private List<Jour> jourListe ;


    public Groupe(String nom) {
        this.nom = nom;
        jourListe=new ArrayList<>() ;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Jour> getJourListe() {
        return jourListe;
    }

    public void setJourListe(List<Jour> jourListe) {
        this.jourListe = jourListe;
    }
}
