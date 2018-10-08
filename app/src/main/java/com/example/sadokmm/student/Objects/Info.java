package com.example.sadokmm.student.Objects;

import java.util.ArrayList;
import java.util.List;

public class Info  {

    private String filiere;
    private int niveau;
    private int nbGroupe;
    private List<Groupe> emploi;


    public Info(String filiere, int niveau, int nbGroupe) {
        this.filiere = filiere;
        this.niveau = niveau;
        this.nbGroupe = nbGroupe;
        emploi=new ArrayList<>();
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

    public int getNbGroupe() {
        return nbGroupe;
    }

    public void setNbGroupe(int nbGroupe) {
        this.nbGroupe = nbGroupe;
    }

    public List<Groupe> getEmploi() {
        return emploi;
    }

    public void setEmploi(List<Groupe> emploi) {
        this.emploi = emploi;
    }
}
