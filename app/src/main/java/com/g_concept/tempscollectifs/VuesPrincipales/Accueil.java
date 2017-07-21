package com.g_concept.tempscollectifs.VuesPrincipales;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TabHost;

import com.g_concept.tempscollectifs.ClassesMetiers.Documentation;
import com.g_concept.tempscollectifs.R;
import com.g_concept.tempscollectifs.ClassesMetiers.Reservation;

public class Accueil extends TabActivity {

    Intent intent;
    Resources res;
    TabHost tabHost;
    public String EXTRA_DB = "donnees", EXTRA_ID_USER = "idUser", idUser, value, choixDB;
    Reservation reservation;
    CheckBox checkBox;
    String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (isTablet(getApplicationContext())) {
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        }

        res = getResources();
        /** Création de la table contenant les onglets */
        tabHost = getTabHost();
        TabHost.TabSpec spec;

        intent = getIntent();

        choixDB = "";
        choixDB = intent.getStringExtra(EXTRA_DB);
        System.out.println("MA BDD : " + choixDB);

        idUser = "";
        idUser = intent.getStringExtra(EXTRA_ID_USER);
        System.out.println("MA BDD : " + idUser);

        intent = new Intent().setClass(this, Infos.class).putExtra("donnees", choixDB).putExtra("idUser", idUser).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /** Ajout du premier onglet à la table */
        spec = tabHost.newTabSpec("Infos").setIndicator("", getResources().getDrawable(R.drawable.inf)).setContent(intent);

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, CreationTempsColl.class).putExtra("donnees", choixDB).putExtra("idUser", idUser).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /** Ajout du premier onglet à la table */
        spec = tabHost.newTabSpec("Creation").setIndicator("", getResources().getDrawable(R.drawable.creat)).setContent(intent);

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        tabHost.addTab(spec);

        //intent = new Intent().setClass(this, CreationGroupe.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /** Ajout du premier onglet à la table */
        spec = tabHost.newTabSpec("Réservé").setIndicator("Création de groupes").setContent(intent);

        intent = new Intent().setClass(this, Preinscription.class).putExtra("donnees", choixDB).putExtra("idUser", idUser).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /** Ajout du premier onglet à la table */
        spec = tabHost.newTabSpec("Pré-inscriptions").setIndicator("", getResources().getDrawable(R.drawable.inscr)).setContent(intent);

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ValidationPresence.class).putExtra("donnees", choixDB).putExtra("idUser", idUser).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /** Ajout du premier onglet à la table */
        spec = tabHost.newTabSpec("Réservé").setIndicator("", getResources().getDrawable(R.drawable.valide)).setContent(intent);

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Historique.class).putExtra("donnees", choixDB).putExtra("idUser", idUser).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /** Ajout du premier onglet à la table */
        spec = tabHost.newTabSpec("Historique").setIndicator("", getResources().getDrawable(R.drawable.history)).setContent(intent);

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Documentation.class).putExtra("donnees", choixDB).putExtra("idUser", idUser).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /** Ajout du premier onglet à la table */
        spec = tabHost.newTabSpec("Documentation").setIndicator("", getResources().getDrawable(R.drawable.file)).setContent(intent);

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, Deconnexion.class).putExtra("donnees", choixDB).putExtra("idUser", idUser).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        /** Ajout du premier onglet à la table */
        spec = tabHost.newTabSpec("Deconnexion").setIndicator("", getResources().getDrawable(R.drawable.exit2)).setContent(intent);

        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        tabHost.addTab(spec);

        /** On se place sur le premier onglet lors de l'ouverture de la page */
        tabHost.setCurrentTab(0);
        setValue("0");
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Reservation getReservation() {
        return this.reservation;
    }

    public void setReservation(Reservation maReservation) {
        this.reservation = maReservation;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    /*public CheckBox getCheck(CheckBox maCheckbox) {
        return maCheckbox;
    }

    public void setCheck() {
        this.checkBox.setChecked(true);
    }*/

}