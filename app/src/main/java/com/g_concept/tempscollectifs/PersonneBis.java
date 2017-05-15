package com.g_concept.tempscollectifs;

/**
 * Created by G-CONCEPT on 06/03/2017.
 */

public class PersonneBis {

    String code = null;
    String typePersonne = null;
    String nom = null;
    String dateNaissance = null;
    String prenom = null;
    boolean selected = false;
    String idTempsColl = null;

    public PersonneBis(String code, String nom, String prenom, String dateNaissance, boolean selected, String typePersonne, String idTempsColl) {
        super();
        this.code = code;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.selected = selected;
        this.typePersonne = typePersonne;
        this.idTempsColl = idTempsColl;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean getSelected(){
        return selected;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getIdTempsColl() {
        return idTempsColl;
    }

    public void setIdTempsColl(String idTempsColl) {
        this.idTempsColl = idTempsColl;
    }

    public String getTypePersonne() {
        return typePersonne;
    }

    public void setTypePersonne(String typePersonne) {
        this.typePersonne = typePersonne;
    }
}
