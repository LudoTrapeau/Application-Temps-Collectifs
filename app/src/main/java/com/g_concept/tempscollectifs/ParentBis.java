package com.g_concept.tempscollectifs;

/**
 * Created by G-CONCEPT on 23/01/2017.
 */

public class ParentBis {

    String id, nom, prenom, dateNaissance, idTempsColl;
    boolean selected;

    public ParentBis(String id, String nom, String prenom, boolean selected, String idTempsColl){
        super();
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.selected = selected;
        this.idTempsColl = idTempsColl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getIdTempsColl() {
        return idTempsColl;
    }

    public void setIdTempsColl(String idTempsColl) {
        this.idTempsColl = idTempsColl;
    }
}
