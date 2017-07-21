package com.g_concept.tempscollectifs.ClassesMetiers;

/**
 * Created by G-CONCEPT on 10/01/2017.
 */
public class Enfant {

    String code = null;
    String nomAM = null;
    String nom = null;
    String dateNaissance = null;
    String prenom = null;
    boolean selected = false;

    public Enfant(String code, String nom, String prenom, String dateNaissance, boolean selected) {
        super();
        this.code = code;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.selected = selected;
    }

    public Enfant(String code, String nom, String prenom, String dateNaissance, boolean selected, String nomAM) {
        super();
        this.code = code;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.selected = selected;
        this.nomAM = nomAM;
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

    public boolean getSelected() {
        return selected;
    }


    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNomAM() {
        return nomAM;
    }

    public void setNomAM(String nomAM) {
        this.nomAM = nomAM;
    }
}