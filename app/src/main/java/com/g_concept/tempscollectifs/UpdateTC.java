package com.g_concept.tempscollectifs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by G-CONCEPT on 27/12/2016.
 */
public class UpdateTC extends Activity {

    Context context;
    JSONObject leTempsColl, objectNbPlacesReservees, objectNbPlacesTotal, parent, json, leLieuTempsColl, lactiviteTempsColl, leRAM;
    JSONArray lesTempsColl, arrayNbPlacesReservees, arrayNbPlaceTotal,lesLieuxTempsColl, lesActivitesTempsColl, lesRAM;
    ImageButton ibDate, ibHeureDebut, ibHeureFin;
    DatePickerDialog.OnDateSetListener dpicker;
    TimePickerDialog tpicker;
    ImageView ibDelete;
    Calendar mcurrentTime;
    TextView tvRAM, tvActivite, tvNomTC, tvLieu;
    EditText edDate, edHeureDebut, edHeureFin, edDetailsPublic, edDetailsRAM, dateDeb;
    String time;
    Button btnCreateTempsColl, btnRefresh, btnRefresh2, btnRefresh3;
    boolean cancel;
    View focusView;
    Spinner spCategorie;
    static final int DIALOG_ID = 0;
    int annee, mois, jour, heure, minute;
    Spinner spLieux, spNom, spActivite, spRAM;
    ArrayList<String> listeNomsTempsColl = new ArrayList<>(),
                      listeActivitesTempsColl = new ArrayList<>(),
                      listeRAM = new ArrayList<>(),
                      numEtId = new ArrayList<>(),
                      AllLieuxTempsColl = new ArrayList<String>();
    Button floatingActionButton, btnInfos;
    SeekBar seekBar, seekBar2;
    Switch swIndefini, swIndefini2;
    TextView tvSeek, tvSeek2, tvNbLimitChar, tvNbLimitChar2;
    Intent intent;
    String EXTRA_ID_TC = "idTC",
            url_obtenir_datas_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/Preinscription/getDatasTempsCollectifsById.php",
            dateNaissance, nbPlacesReservees, nbPlacesTotal,
            horaire, idTempsColl, choix, nbPlaces, dateTempsColl,
            url_update_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/updateTC.php", EXTRA_DB = "donnees",
            titleRAM, nbPlacesEnfant, nbPlacesAdulte, activiteTempsColl, descriptif, date, heureDebut, heureFin, nomTempsColl, m, j, lieuTempsColl, categorie,
            initNom = "Modifiez le nom", initActivite = "Modifiez l'activité", initRAM = "Modifiez le RAM", initLieu = "Modifiez le lieu", db2, email,
            password, initBDD = "Choisir votre structure", db, choixDB,
            url_obtenir_lieu_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/getLieuxTempsCollectifs.php",
            url_obtenir_nom_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/CreationTempsColl/getNomsTempsColl.php",
            url_obtenir_ram = "https://www.web-familles.fr/AppliTempsCollectifs/CreationTempsColl/getRAM.php",
            url_obtenir_activite_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/CreationTempsColl/getActivitesTempsColl.php";
    StringRequest requete_update_temps_coll, requete_datas_temps_coll, requete_temps_coll, requete_nom_temps_coll, requete_activite_temps_coll, requete_ram;
    Map<String, String> params = new HashMap<String, String>();
    RequestQueue requestQueue;
    String[] listeCategories = {"ACTIONS COLLECTIVES", "REUNIONS A THEMES", "SORTIES - VISITES - AUTRES"};

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popupdate);

        spCategorie = (Spinner) findViewById(R.id.spCategorie);
        edDate = (EditText) findViewById(R.id.edDate);
        spNom = (Spinner) findViewById(R.id.spNom);
        spLieux = (Spinner) findViewById(R.id.spinnerLieu);
        spActivite = (Spinner) findViewById(R.id.spActivite);
        spRAM = (Spinner) findViewById(R.id.spinnerRAM);
        spCategorie = (Spinner) findViewById(R.id.spCategorie);
        edDetailsPublic = (EditText) findViewById(R.id.edDetailsPublic);
        edDetailsRAM = (EditText) findViewById(R.id.edDetailsRAM);
        edHeureDebut = (EditText) findViewById(R.id.edHeureDebut);
        edHeureFin = (EditText) findViewById(R.id.edHeureFin);
        btnCreateTempsColl = (Button) findViewById(R.id.btnCreationTempsColl);
        btnInfos = (Button) findViewById(R.id.btnInfo);
        ibDelete = (ImageView) findViewById(R.id.ibDelete);
        seekBar = (SeekBar) findViewById(R.id.simpleSeekBar);
        seekBar2 = (SeekBar) findViewById(R.id.simpleSeekBar2);
        tvSeek = (TextView) findViewById(R.id.tvNbEnf);
        tvSeek2 = (TextView) findViewById(R.id.tvNbAdultes);
        swIndefini = (Switch) findViewById(R.id.swIndefini);
        swIndefini2 = (Switch) findViewById(R.id.swIndefini2);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        btnRefresh2 = (Button) findViewById(R.id.btnRefresh2);
        btnRefresh3 = (Button) findViewById(R.id.btnRefresh3);
        tvNbLimitChar = (TextView) findViewById(R.id.nbLimitChar);
        tvNbLimitChar2 = (TextView) findViewById(R.id.nbLimitChar2);
        floatingActionButton = (Button) findViewById(R.id.fabButton);

        tvRAM = (TextView) findViewById(R.id.tvRAM);
        tvActivite = (TextView) findViewById(R.id.tvActivite);
        tvNomTC = (TextView) findViewById(R.id.tvNom);
        tvLieu = (TextView) findViewById(R.id.tvLieu);

        intent = getIntent();
        choix = intent.getStringExtra(EXTRA_ID_TC);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        intent = getIntent();
        choixDB = "";
        choixDB = intent.getStringExtra(EXTRA_DB);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.widthPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .9));

        // Récupération des informations liées au temps collectif choisi
        getDatasTempsCollById(choix);

        // Obtention des noms de temps collectifs
        getNomTempsCollectifs();

        // Obtention des RAM
        getRAMCollectifs();

        // Obtention des activités de temps collectif
        getActivitesTempsCollectifs();

        // Obtention des lieux
        getLieuxTempsCollectifs();

        showDialog();

        // On récupère la date
        getDate();

        getHorlogeHeureDebut();

        getHorlogeHeureFin();

        // Ajout des initialisations
        listeRAM.add(initRAM);
        AllLieuxTempsColl.add(initLieu);
        listeActivitesTempsColl.add(initActivite);
        listeNomsTempsColl.add(initNom);

        final ArrayAdapter spinnerArrayAdapterCategorie = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listeCategories);
        spCategorie.setAdapter(spinnerArrayAdapterCategorie);

        spCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categorie = String.valueOf(spCategorie.getSelectedItem());
                System.out.println("Vous venez d'appuyer sur la catégorie : " + categorie);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categorie = "categorie";
            }
        });

        ArrayAdapter spinnerArrayAdapterLieux = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, AllLieuxTempsColl);
        spLieux.setAdapter(spinnerArrayAdapterLieux);

        spLieux.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lieuTempsColl = String.valueOf(spLieux.getSelectedItem());
                System.out.println("Vous venez d'appuyer sur l'activité : " + lieuTempsColl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categorie = "categorie";
            }
        });

        ArrayAdapter spinnerArrayAdapterActivites = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listeActivitesTempsColl);
        spActivite.setAdapter(spinnerArrayAdapterActivites);

        spActivite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activiteTempsColl = String.valueOf(spActivite.getSelectedItem());
                System.out.println("Vous venez d'appuyer sur l'activité : " + activiteTempsColl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categorie = "categorie";
            }
        });

        ArrayAdapter spinnerArrayAdapterNom = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listeNomsTempsColl);
        spNom.setAdapter(spinnerArrayAdapterNom);

        spNom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nomTempsColl = String.valueOf(spNom.getSelectedItem());
                System.out.println("Vous venez d'appuyer sur le nom : " + nomTempsColl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categorie = "categorie";
            }
        });

        ArrayAdapter spinnerArrayAdapterRAM = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listeRAM);
        spRAM.setAdapter(spinnerArrayAdapterRAM);

        spRAM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                titleRAM = String.valueOf(spRAM.getSelectedItem());
                System.out.println("Vous venez d'appuyer sur le nom : " + titleRAM);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categorie = "categorie";
            }
        });

        // Lorsque l'on clique sur le bouton de validation
        btnCreateTempsColl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ELEMENTS : " + edDate.getText().toString());

                // Si la date est vide
                if (edDate.getText().toString().equals("")) {
                    edDate.setError(getString(R.string.error_field_required));
                    focusView = edDate;
                    cancel = true;
                    edDate.setFocusableInTouchMode(true);
                    edDate.requestFocus();
                } else {
                    cancel = false;
                    edDate.setFocusableInTouchMode(false);
                }

                // Si l'heure de fin est vide
                if (edHeureFin.getText().toString().equals("")) {
                    edHeureFin.setError(getString(R.string.error_field_required));
                    focusView = edHeureFin;
                    cancel = true;
                    edHeureFin.setFocusableInTouchMode(true);
                    edHeureFin.requestFocus();
                }

                // Si l'heure de début est vide
                if (edHeureDebut.getText().toString().equals("")) {
                    edHeureDebut.setError(getString(R.string.error_field_required));
                    focusView = edHeureDebut;
                    cancel = true;
                    edHeureDebut.setFocusableInTouchMode(true);
                    edHeureDebut.requestFocus();
                }
                updateTempsCollectifs();
                finish();
                /*Intent intent = new Intent(getApplicationContext(), Accueil.class);
                intent.putExtra("donnees", choixDB);
                startActivity(intent);*/
            }
        });
    }

    /*****
     * On charge les datas correspondants au temps collectif choisi
     *******/
    public void getDatasTempsCollById(final String id) {
        requete_datas_temps_coll = new StringRequest(Request.Method.POST, url_obtenir_datas_temps_coll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("Réponse requète getDatasTempsColl : " + response);
                    json = new JSONObject(response);
                    lesTempsColl = json.getJSONArray("temps_collectifs");
                    arrayNbPlacesReservees = json.getJSONArray("nb_places_reservees");
                    arrayNbPlaceTotal = json.getJSONArray("nb_places_total");

                    for (int i = 0; i < lesTempsColl.length(); i++) {
                        leTempsColl = lesTempsColl.getJSONObject(i);
                        idTempsColl = leTempsColl.getString("id");
                        nomTempsColl = leTempsColl.getString("nom");
                        lieuTempsColl = leTempsColl.getString("lieu");
                        dateTempsColl = leTempsColl.getString("dateTempsColl");
                        categorie = leTempsColl.getString("categorie");
                        nbPlaces = leTempsColl.getString("nb_place");
                        horaire = leTempsColl.getString("heureDeb") + " - " + leTempsColl.getString("heureFin");
                        String heureDebut = leTempsColl.getString("heureDeb");
                        String heureFin = leTempsColl.getString("heureFin");
                        String nbPlacesEnfants = leTempsColl.getString("nb_place_enfant");
                        String nbPlacesAdultes = leTempsColl.getString("nb_place_adulte");

                        //listeTempsColl.add(idTempsColl + " | " + nomTempsColl + " | " + dateTempsColl);
                        System.out.println(numEtId + " Les infos : " + nomTempsColl + " " + "date " + dateTempsColl + " et n° :" + i);

                        //listeNomsTempsColl.add(nomTempsColl);
                        //listeActivitesTempsColl.add(activiteTempsColl);
                        //AllLieuxTempsColl.add(lieuTempsColl);
                        edDate.setText(dateTempsColl);
                        edHeureDebut.setText(heureDebut);
                        edHeureFin.setText(heureFin);
                        tvSeek.setText(nbPlacesEnfants);
                        tvSeek2.setText(nbPlacesAdultes);
                        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress,
                                                          boolean fromUser) {
                                // TODO Auto-generated method stub
                                tvSeek.setText(String.valueOf(progress));
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                // TODO Auto-generated method stub
                            }
                        });

                        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress,
                                                          boolean fromUser) {
                                // TODO Auto-generated method stub
                                tvSeek2.setText(String.valueOf(progress));
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                // TODO Auto-generated method stub
                            }
                        });
                    }

                    for (int i = 0; i < arrayNbPlacesReservees.length(); i++) {
                        objectNbPlacesReservees = arrayNbPlacesReservees.getJSONObject(i);
                        nbPlacesReservees = objectNbPlacesReservees.getString("nb_places_reservees");
                    }

                    for (int i = 0; i < arrayNbPlaceTotal.length(); i++) {
                        objectNbPlacesTotal = arrayNbPlaceTotal.getJSONObject(i);
                        nbPlacesTotal = objectNbPlacesTotal.getString("nb_places_total");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", String.valueOf(error));
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put("id", id);
                params.put("donnees", choixDB);
                Log.e("Envoi params Reserv", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_datas_temps_coll);
    }

    /******
     * Récupération des temps collectifs
     * *********/
    public void getNomTempsCollectifs() {
        requete_nom_temps_coll = new StringRequest(Request.Method.POST, url_obtenir_nom_temps_coll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("Voici la réponse à la requète pour les lieux : " + response);
                    json = new JSONObject(response);
                    lesLieuxTempsColl = json.getJSONArray("noms");
                    for (int i = 0; i < lesLieuxTempsColl.length(); i++) {
                        leLieuTempsColl = lesLieuxTempsColl.getJSONObject(i);
                        nomTempsColl = leLieuTempsColl.getString("tcNom");

                        listeNomsTempsColl.add(nomTempsColl);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", String.valueOf(error));
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put("donnees", choixDB);
                Log.e("Envoi params Reserv", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_nom_temps_coll);
    }

    /*******
     * Pour l'affichage du calendrier
     * ********/
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            final Calendar cal = Calendar.getInstance();
            annee = cal.get(Calendar.YEAR);
            mois = cal.get(Calendar.MONTH);
            jour = cal.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this, dpicker, annee, mois, jour);
        }
        return null;
    }

    /*******
     * Au click sur le bouton calendrier
     * *******/
    public void showDialog() {
        ibDate = (ImageButton) findViewById(R.id.ibDate);
        ibDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    public void getDate() {
        dpicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                annee = year;
                mois = monthOfYear + 1;

                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(mois);
                m = sb.toString();

                for (int i = 0; i < 10; i++) {
                    if (mois == i) {
                        m = "0" + mois;
                        System.out.println(m);
                    }
                }

                jour = dayOfMonth;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("");
                sb2.append(jour);
                j = sb2.toString();

                for (int i = 0; i < 10; i++) {
                    if (jour == i) {
                        j = "0" + jour;
                        System.out.println(j);
                    }
                }

                dateDeb = edDate;

                dateDeb.setText(j + "-" + m + "-" + annee);

            }
        };
    }

    public void updateTempsCollectifs() {
        requete_update_temps_coll = new StringRequest(Request.Method.POST, url_update_temps_coll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la réponse à la requète pour les lieux : " + response);

                if (response.contains("[1]")) {
                    Toast.makeText(UpdateTC.this, "La modification du temps collectif a été effectuée !" + tvSeek.getText().toString() + " enfants pour " + tvSeek2.getText().toString() + " adultes à ce temps collectif.", Toast.LENGTH_LONG).show();

                } else if (response.contains("[0]")) {
                    Toast.makeText(UpdateTC.this, "Erreur ! La modification du temps collectif n'a pas pu être faite !", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", String.valueOf(error));
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put("donnees", choixDB);
                params.put("nom", nomTempsColl);
                params.put("idTC", choix);

                params.put("heureDeb", "2017-05-10" + " " + edHeureDebut.getText().toString());
                params.put("heureFin", "2017-05-10" + " " + edHeureFin.getText().toString());

                params.put("detailsPublic", edDetailsPublic.getText().toString());
                params.put("detailsRAM", edDetailsRAM.getText().toString());
                params.put("date", edDate.getText().toString());
                params.put("categorie", categorie);
                params.put("activite", activiteTempsColl);
                params.put("lieu", lieuTempsColl);
                params.put("nbPlacesEnfant", tvSeek.getText().toString());
                params.put("nbPlacesAdulte", tvSeek2.getText().toString());
                params.put("ram", titleRAM);
                Log.e("Params création TC", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_update_temps_coll);
    }

    public void getRAMCollectifs() {
        requete_ram = new StringRequest(Request.Method.POST, url_obtenir_ram, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("Voici la réponse à la requète pour les ram : " + response);
                    json = new JSONObject(response);
                    lesRAM = json.getJSONArray("ram");
                    for (int i = 0; i < lesRAM.length(); i++) {
                        leRAM = lesRAM.getJSONObject(i);
                        titleRAM = leRAM.getString("title");

                        listeRAM.add(titleRAM);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", String.valueOf(error));
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put("donnees", choixDB);
                Log.e("Envoi params Reserv", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_ram);
    }

    public void getActivitesTempsCollectifs() {
        requete_activite_temps_coll = new StringRequest(Request.Method.POST, url_obtenir_activite_temps_coll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("Voici la réponse à la requète pour les activités : " + response);
                    json = new JSONObject(response);
                    lesActivitesTempsColl = json.getJSONArray("activites");
                    for (int i = 0; i < lesActivitesTempsColl.length(); i++) {
                        lactiviteTempsColl = lesActivitesTempsColl.getJSONObject(i);
                        activiteTempsColl = lactiviteTempsColl.getString("tcActivite");

                        listeActivitesTempsColl.add(activiteTempsColl);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", String.valueOf(error));
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put("donnees", choixDB);
                Log.e("Envoi params Reserv", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_activite_temps_coll);
    }

    public void getLieuxTempsCollectifs() {
        requete_temps_coll = new StringRequest(Request.Method.POST, url_obtenir_lieu_temps_coll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("Voici la réponse à la requète pour les lieux : " + response);
                    json = new JSONObject(response);
                    lesLieuxTempsColl = json.getJSONArray("lieux");

                    for (int i = 0; i < lesLieuxTempsColl.length(); i++) {
                        leLieuTempsColl = lesLieuxTempsColl.getJSONObject(i);
                        //System.out.println("ENFANT AVEC RESERVATION " + enfant);
                        lieuTempsColl = leLieuTempsColl.getString("tcLieu");

                        AllLieuxTempsColl.add(lieuTempsColl);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", String.valueOf(error));
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put("donnees", choixDB);
                Log.e("Envoi params Reserv", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_temps_coll);
    }

    public void getHorlogeHeureDebut() {
        ibHeureDebut = (ImageButton) findViewById(R.id.ibHeureDeb);
        ibHeureDebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcurrentTime = Calendar.getInstance();
                heure = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
                tpicker = new TimePickerDialog(UpdateTC.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (Integer.toString(selectedHour).length() == 1 && Integer.toString(selectedMinute).length() == 1) {
                            time = "0" + selectedHour + ":0" + selectedMinute + ":00";
                        } else if (Integer.toString(selectedHour).length() == 1) {
                            time = "0" + selectedHour + ":" + selectedMinute + ":00";
                        } else if (Integer.toString(selectedMinute).length() == 1) {
                            time = selectedHour + ":0" + selectedMinute + ":00";
                        } else {
                            time = selectedHour + ":" + selectedMinute + ":00";
                        }
                        edHeureDebut.setText(time);
                    }
                }, heure, minute, true);//Yes 24 hour time

                tpicker.setTitle("Choisissez l'heure de début");
                tpicker.show();
            }
        });
    }

    public void getHorlogeHeureFin() {
        ibHeureFin = (ImageButton) findViewById(R.id.ibHeureFin);
        ibHeureFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcurrentTime = Calendar.getInstance();
                heure = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
                tpicker = new TimePickerDialog(UpdateTC.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (Integer.toString(selectedHour).length() == 1 && Integer.toString(selectedMinute).length() == 1) {
                            time = "0" + selectedHour + ":0" + selectedMinute + ":00";
                        } else if (Integer.toString(selectedHour).length() == 1) {
                            time = "0" + selectedHour + ":" + selectedMinute + ":00";
                        } else if (Integer.toString(selectedMinute).length() == 1) {
                            time = selectedHour + ":0" + selectedMinute + ":00";
                        } else {
                            time = selectedHour + ":" + selectedMinute + ":00";
                        }
                        edHeureFin.setText(time);
                    }
                }, heure, minute, true);//Yes 24 hour time
                tpicker.setTitle("Choisissez l'heure de fin");
                tpicker.show();
            }
        });
    }
}