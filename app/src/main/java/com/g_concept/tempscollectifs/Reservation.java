package com.g_concept.tempscollectifs;

/**
 * Created by G-CONCEPT on 19/12/2016.
 */

public class Reservation {

    String id, idTC;
    String date, lieu;
    String nom, categorie, horaire;
    String nbPlaces, nbPlacesEnfant, nbPlacesAdulte;

    public Reservation(String monId, String maDate, String monNom, String monHoraire, String maCategorie, String monNbPlaces, String monLieu, String monNbPlacesEnfant, String monNbPlacesAdulte) {
        this.id = monId;
        this.date = maDate;
        this.nom = monNom;
        this.horaire = monHoraire;
        this.categorie = maCategorie;
        this.nbPlaces = monNbPlaces;
        this.nbPlacesEnfant = monNbPlacesEnfant;
        this.nbPlacesAdulte = monNbPlacesAdulte;
        this.lieu = monLieu;
    }

    public Reservation(String monId, String maDate, String monNom, String monHoraire, String maCategorie, String monNbPlaces, String monLieu, String monNbPlacesEnfant, String monNbPlacesAdulte, String idTC) {
        this.id = monId;
        this.date = maDate;
        this.nom = monNom;
        this.horaire = monHoraire;
        this.categorie = maCategorie;
        this.nbPlaces = monNbPlaces;
        this.nbPlacesEnfant = monNbPlacesEnfant;
        this.nbPlacesAdulte = monNbPlacesAdulte;
        this.lieu = monLieu;
        this.idTC = idTC;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getNom() {
        return nom;
    }

    public String getLieu(){ return lieu; }

    public String getCategorie() {
        return categorie;
    }

    public String getNbPlaces() {
        return nbPlaces;
    }

    public String getHoraire() {
        return horaire;
    }

    public void setId(String monId) {
        this.id = monId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setNbPlaces(String nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public void setHoraire(String horaire) {
        this.horaire = horaire;
    }

    public void setLieu(String monLieu){
        this.lieu = monLieu;
    }

    public String getNbPlacesEnfant() {
        return nbPlacesEnfant;
    }

    public void setNbPlacesEnfant(String nbPlacesEnfant) {
        this.nbPlacesEnfant = nbPlacesEnfant;
    }

    public String getNbPlacesAdulte() {
        return nbPlacesAdulte;
    }

    public void setNbPlacesAdulte(String nbPlacesAdulte) {
        this.nbPlacesAdulte = nbPlacesAdulte;
    }
}