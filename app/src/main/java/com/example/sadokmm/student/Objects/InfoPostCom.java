package com.example.sadokmm.student.Objects;

public class InfoPostCom {

    private String idPoste ;
    private String idCom;

    public InfoPostCom(String idPoste, String idCom) {
        this.idPoste = idPoste;
        this.idCom = idCom;
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
}
