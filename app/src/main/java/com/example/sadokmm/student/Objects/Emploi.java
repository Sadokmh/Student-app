package com.example.sadokmm.student.Objects;

import java.util.ArrayList;
import java.util.List;

public class Emploi {

    private String id;
    private String filiere ;
    private int niveau ;
    private int groupe ;
    private List<Jour> jours;


    public Emploi(String id, String filiere, int niveau, int groupe) {
        this.id = id;
        this.filiere = filiere;
        this.niveau = niveau;
        this.groupe = groupe;
        this.jours = new ArrayList<>();
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

    public int getGroupe() {
        return groupe;
    }

    public void setGroupe(int groupe) {
        this.groupe = groupe;
    }

    public List<Jour> getJours() {
        return jours;
    }

    public void setJours(List<Jour> jours) {
        this.jours = jours;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
