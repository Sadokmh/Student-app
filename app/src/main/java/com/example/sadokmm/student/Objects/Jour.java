package com.example.sadokmm.student.Objects;

import java.util.ArrayList;
import java.util.List;

public class Jour  {

    private String nom;
    private List<Seance> listSeance;


    public Jour(String nom) {
        this.nom = nom;
        listSeance=new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public List<Seance> getListSeance() {
        return listSeance;
    }

    public void setListSeance(List<Seance> listSeance) {
        this.listSeance = listSeance;
    }
}
