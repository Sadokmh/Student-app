package com.example.sadokmm.student.Objects;

public class InfoPostCom {

    private String idPoste ;
    private String idCom;
    private String like;

    public InfoPostCom(String idPoste, String idCom , String like) {
        this.idPoste = idPoste;
        this.idCom = idCom;
        this.like = like;
    }


    public InfoPostCom(){

    }


    public String getIdPoste() {
        return idPoste;
    }

    public void setIdPoste(String idPoste) {
        this.idPoste = idPoste;
    }

    public String getIdCom() {
        return idCom;
    }

    public void setIdCom(String idCom) {
        this.idCom = idCom;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }
}
