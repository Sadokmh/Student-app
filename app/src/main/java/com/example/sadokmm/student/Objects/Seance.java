package com.example.sadokmm.student.Objects;

import java.util.Date;

public class Seance  {
    private int id_mat;
    private String matiere;
    private String enseignant;
    private String salle;
    private int numSeance;
    private Boolean parQuanzaine;
    private String type;



    public Seance(String matiere, String enseignant, String salle, String type, int numSeance, Boolean parQuanzaine) {
        this.matiere = matiere;
        this.enseignant = enseignant;
        this.salle = salle;
        this.numSeance = numSeance;
        this.parQuanzaine = parQuanzaine;
        this.type = type;
    }


    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public String getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(String enseignant) {
        this.enseignant = enseignant;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public int getNumSeance() {
        return numSeance;
    }

    public void setNumSeance(int numSeance) {
        this.numSeance = numSeance;
    }

    public Boolean getParQuanzaine() {
        return parQuanzaine;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setParQuanzaine(Boolean parQuanzaine) {
        this.parQuanzaine = parQuanzaine;


    }
}
