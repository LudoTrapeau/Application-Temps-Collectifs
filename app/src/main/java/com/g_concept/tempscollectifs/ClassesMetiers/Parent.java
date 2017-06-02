package com.g_concept.tempscollectifs.ClassesMetiers;

/**
 * Created by G-CONCEPT on 10/01/2017.
 */
public class Parent {

    String code = null;
    String nom = null;
    String dateNaissance = null;
    String prenom = null;
    boolean selected = false;

    public Parent(String code, String nom, String prenom, boolean selected) {
        super();
        this.code = code;
        this.nom = nom;
        this.prenom = prenom;
        this.selected = selected;
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
}