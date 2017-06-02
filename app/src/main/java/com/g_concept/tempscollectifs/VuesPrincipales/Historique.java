package com.g_concept.tempscollectifs.VuesPrincipales;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.g_concept.tempscollectifs.ClassesMetiers.Asmat;
import com.g_concept.tempscollectifs.ClassesMetiers.Enfant;
import com.g_concept.tempscollectifs.ClassesMetiers.Network;
import com.g_concept.tempscollectifs.ClassesMetiers.Parent;
import com.g_concept.tempscollectifs.ClassesMetiers.Partenaire;
import com.g_concept.tempscollectifs.R;
import com.g_concept.tempscollectifs.ClassesMetiers.Reservation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Historique extends AppCompatActivity {

    Context context;
    View post;
    ConnectivityManager cm;
    Network myNetwork;
    ArrayAdapter<String> adapter, adapter2;
    NetworkInfo netInfo;
    TextView tv;
    TabActivity tabs;
    Reservation maReservation;
    String prenomParentPres, nbPlacesReservees, nbPlacesTotal,
            url_obtenir_datas_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/Preinscription/getDatasTempsCollectifsById.php";
    StringRequest requete_datas_temps_coll;
    JSONObject objectNbPlacesReservees, objectNbPlacesTotal, monObjetParentsPresents;
    JSONArray arrayNbPlacesReservees, arrayNbPlaceTotal, monArrayParentsPresents;
    ArrayList<String> numEtId = new ArrayList<>();
    LayoutInflater inflater;
    Date myDate;
    JSONObject json;
    String[] countries = {"Lyon", "Marseille", "Paris"};
    AutoCompleteTextView autoCompleteNom;
    Intent intent;
    String idParentPres, nomParentPres, idAMPres, nomAMPres, prenomAMPres, idPartPres, nomPartPres, prenomPartPres, idEnfPres, nomEnfPres, prenomEnfPres, EXTRA_DB = "donnees", titleRAM, nbPlacesEnfant, nbPlacesAdulte,
            horaire, initLieu = "Choisir un lieu", initRAM = "Choisir un RAM", idTempsColl, choix, nbPlaces, lieuTempsColl, categorie, nomTempsColl, dateTempsColl,
            url_obtenir_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/getTempsCollectifs.php", choixDB,
            url_places_by_tc = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/getNbParticipants",
            url_obtenir_lieu_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/getLieuxTempsCollectifs.php",
            url_delete_tc_to_modify = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/deleteTCtoModify.php",
            url_obtenir_pers_presentes = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/getPersonnesPresentesHistorique.php",
            url_obtenir_ram = "https://www.web-familles.fr/AppliTempsCollectifs/CreationTempsColl/getRAM.php";
    StringRequest requete_temps_coll, requete_ram, requete_pers_presentes, requete_delete_tc_to_modify, requete_places_by_tc;
    Map<String, String> params = new HashMap<String, String>();
    JSONObject leTempsColl, leLieuTempsColl, leRAM, monObjetPersPresente, monObjetEnfPresente, monObjetAMPresente, monObjetPartPresente;
    JSONArray lesTempsColl, lesLieuxTempsColl, lesDetails, lesRAM, monArrayPersPresentes, monArrayENFPresents, monArrayAMPresentes, monArrayPARTPresents;
    TableLayout TLtempscoll;
    int i = 0, nbPlacesDispo, nbPlacesRestantes, number = 0, nbEnf = 0, nbAM = 0, nbPart = 0, nbParents = 0, temp;
    Button btnBackHome, btnModifTC;
    ImageView ivDetails;
    RequestQueue requestQueue;
    ArrayList<String> liste = new ArrayList<String>(),listeRAM = new ArrayList<String>(), listeEnfPresents = new ArrayList<String>(), listeAMPresents = new ArrayList<String>(),
    listePartPresents = new ArrayList<String>(), listeParentsPresents = new ArrayList<String>();
    LinearLayout llContentLieu, llContentRAM;
    TableRow row;
    Spinner spLieux, spRAM;
    TextView tvEnfPresents, tvAmPresentes, tvPartPresents, tvTitle, tvENF, tvAM, tvPART, tvParents, tvParentsPresents, tvAvertissement;
    RadioGroup radioGroup;
    RadioButton rbLieu, rbRAM;
    Enfant monEnfant;
    Partenaire monPartenaire;
    Asmat monAsmat;
    Parent monParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);

        /*************************       On initialise toutes les variables et on les instancie       ***********************/
        intent = getIntent();
        choixDB = "";
        choixDB = intent.getStringExtra(EXTRA_DB);
        System.out.println("MA BDD : " + choixDB);
        btnBackHome = (Button) findViewById(R.id.fabButton);
        rbLieu = (RadioButton) findViewById(R.id.rbLieu);
        rbRAM = (RadioButton) findViewById(R.id.rbRAM);
        radioGroup = (RadioGroup) findViewById(R.id.radioSex);
        llContentLieu = (LinearLayout) findViewById(R.id.llContentLieu);
        llContentRAM = (LinearLayout) findViewById(R.id.llContentRAM);
        //Menu déroulant contenant les RAM
        spRAM = (Spinner) findViewById(R.id.spRAM);
        tvEnfPresents = (TextView) findViewById(R.id.tvEnfPresents);
        tvAmPresentes = (TextView) findViewById(R.id.tvAmPresentes);
        tvPartPresents = (TextView) findViewById(R.id.tvPartPresents);
        tvParentsPresents = (TextView) findViewById(R.id.tvParentsPresents);
        tvParents = (TextView) findViewById(R.id.tvParent);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvENF = (TextView) findViewById(R.id.tvENF);
        tvAM = (TextView) findViewById(R.id.tvAM);
        tvPART = (TextView) findViewById(R.id.tvPART);
        tvAvertissement = (TextView) findViewById(R.id.tvAvertissement);
        btnModifTC = (Button) findViewById(R.id.btnModifTC);
        /*******************************************************************************************************/

        btnModifTC.setVisibility(View.INVISIBLE);

        //Menu déroulant contenant les bdd
        spLieux = (Spinner) findViewById(R.id.spLieux);

        listeRAM.add(initRAM);

        llContentRAM.setVisibility(View.GONE);

        rbRAM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llContentRAM.setVisibility(View.VISIBLE);
                llContentLieu.setVisibility(View.GONE);
                spRAM.setSelection(0);
                //Toast.makeText(getApplicationContext(), "RAM check", Toast.LENGTH_SHORT).show();
            }
        });

        rbLieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llContentRAM.setVisibility(View.GONE);
                llContentLieu.setVisibility(View.VISIBLE);
                spLieux.setSelection(0);
                //Toast.makeText(getApplicationContext(), "Lieu check", Toast.LENGTH_SHORT).show();
            }
        });

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabs = (TabActivity) getParent();
                tabs.getTabHost().setCurrentTab(0);
            }
        });

        myDate = new Date();
        System.out.println(myDate);
        System.out.println(new SimpleDateFormat("MM-dd-yyyy").format(myDate));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(myDate));
        System.out.println("La date " + myDate);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        TLtempscoll = (TableLayout) findViewById(R.id.TLtempsColl);

        liste.clear();

        // Initialisation des éléments de connexion au réseau
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        myNetwork = new Network(cm);

        if (myNetwork.isOnline()) {
            getLieuxTempsCollectifs();
            getRAMCollectifs();
        } else {
            Toast.makeText(this, "Veuillez vérifier votre connexion internet", Toast.LENGTH_SHORT).show();
        }

        System.out.println("TEST de la liste : " + liste.size());

        liste.add(initLieu);

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        post = inflater.inflate(R.layout.alertdialogdetails, null);

        autoCompleteNom = (AutoCompleteTextView) post.findViewById(R.id.autoCompleteNom);

        adapter2 = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, countries);
        autoCompleteNom.setAdapter(adapter2);

        ivDetails = new ImageView(this);
        ivDetails.setImageResource(R.drawable.details);

        //Le Spinner a besoin d'un adapter pour sa presentation
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, liste);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Enfin on passe l'adapter au Spinner et c'est tout
        spLieux.setAdapter(adapter);

        //-- gestion du Click sur la liste Région
        spLieux.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                choix = String.valueOf(spLieux.getSelectedItem());
                TLtempscoll.removeAllViews();
                System.out.println(choixDB);
                Log.i("Page Emprunt", "Vous avez appuyé sur :" + choix);
                System.out.println("LINE " + TLtempscoll.getChildCount());

                cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                myNetwork = new Network(cm);

                if (myNetwork.isOnline()) {
                    getTempsCollectifs("1", choix, "", "date desc", "hist");
                } else {
                    Toast.makeText(Historique.this, "Veuillez vérifier votre connexion internet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        //Le Spinner a besoin d'un adapter pour sa presentation
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listeRAM);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Enfin on passe l'adapter au Spinner et c'est tout
        spRAM.setAdapter(adapter2);

        //-- gestion du Click sur la liste Région
        spRAM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                choix = String.valueOf(spRAM.getSelectedItem());
                TLtempscoll.removeAllViews();
                Log.i("Page Emprunt", "Vous avez appuyé sur :" + choix);
                System.out.println("LINE " + TLtempscoll.getChildCount());
                spLieux.setSelection(0);
                cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                myNetwork = new Network(cm);

                if (myNetwork.isOnline()) {
                    getTempsCollectifs("2", "", choix, "date desc", "hist");
                } else {
                    Toast.makeText(Historique.this, "Veuillez vérifier votre connexion internet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
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

    public void deleteTCtoMofify(final String idTC) {
        requete_delete_tc_to_modify = new StringRequest(Request.Method.POST, url_delete_tc_to_modify, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la requête delete to modify : " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", String.valueOf(error));
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put("donnees", choixDB);
                params.put("idTC", idTC);
                Log.e("Envoi params Reserv", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_delete_tc_to_modify);
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
                        lieuTempsColl = leLieuTempsColl.getString("tcLieu");

                        liste.add(lieuTempsColl);
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

                        //listeTempsColl.add(idTempsColl + " | " + nomTempsColl + " | " + dateTempsColl);
                        System.out.println(numEtId + " Les infos : " + nomTempsColl + " " + "date " + dateTempsColl + " et n° :" + i);
                    }

                    for (int i = 0; i < arrayNbPlacesReservees.length(); i++) {
                        objectNbPlacesReservees = arrayNbPlacesReservees.getJSONObject(i);
                        nbPlacesReservees = objectNbPlacesReservees.getString("nb_places_reservees");
                    }

                    for (int i = 0; i < arrayNbPlaceTotal.length(); i++) {
                        objectNbPlacesTotal = arrayNbPlaceTotal.getJSONObject(i);
                        nbPlacesTotal = objectNbPlacesTotal.getString("nb_places_total");
                    }

                    nbPlacesRestantes = Integer.parseInt(nbPlacesTotal) - Integer.parseInt(nbPlacesReservees);

                    System.out.println("Nombre de places dispos : " + nbPlacesRestantes);

                    System.out.println("ARRAYLIST " + numEtId.size());

                    btnModifTC.setVisibility(View.VISIBLE);
                    btnModifTC.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            deleteTCtoMofify(id);
                            // Permet de switcher de tab quand on clique sur le bouton de validation de présence
                            tabs = (TabActivity) getParent();
                            tabs.getTabHost().setCurrentTab(3);
                        }
                    });

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

    public void getTempsCollectifs(final String lieuOuRAM, final String monChoix, final String monRAM, final String choixOrderBy, final String choice) {
        requete_temps_coll = new StringRequest(Request.Method.POST, url_obtenir_temps_coll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("Voici la réponse à la requète : " + response);
                    json = new JSONObject(response);
                    lesTempsColl = json.getJSONArray("temps_collectifs");

                    for (int i = 0; i < lesTempsColl.length(); i++) {
                        leTempsColl = lesTempsColl.getJSONObject(i);
                        idTempsColl = leTempsColl.getString("id");
                        nomTempsColl = leTempsColl.getString("nom");
                        dateTempsColl = leTempsColl.getString("dateTempsColl");
                        lieuTempsColl = leTempsColl.getString("lieu");
                        nbPlacesEnfant = leTempsColl.getString("nb_place_enfant");
                        nbPlacesAdulte = leTempsColl.getString("nb_place_adulte");
                        categorie = leTempsColl.getString("categorie");
                        nbPlaces = leTempsColl.getString("nb_place");
                        horaire = leTempsColl.getString("heureDeb") + " - " + leTempsColl.getString("heureFin");

                        // Permettra ensuite de distinguer deux lignes en mettant des couleurs différentes
                        if (i % 2 == 0) {
                            number = 1;
                        } else {
                            number = 2;
                        }

                        numEtId.add(idTempsColl);
                        BuildTable(TLtempscoll, i, number);
                        temp++;
                        System.out.println(numEtId + " Les infos : " + nomTempsColl + " " + "date " + dateTempsColl + " et n° :" + i);
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
                params.put("lieu", monChoix);
                params.put("ram", monRAM);
                params.put("lieuOuRAM", lieuOuRAM);
                params.put("choixOrderBy", choixOrderBy);
                params.put("donnees", choixDB);
                params.put("choice", choice);
                Log.e("Envoi params Reserv", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_temps_coll);
    }

    /********
     * Construit le tableau contenant les données
     ********/
    private void BuildTable(TableLayout tb, final int variable, final int number) {

        row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                50));

        for (int j = 1; j <= 1; j++) {
            tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setPadding(5, 5, 5, 5);
            tv.setText(dateTempsColl);
            tv.setTextColor(Color.WHITE);
            if (number != 1) {
                tv.setBackgroundColor(Color.parseColor("#6d6c6c"));
            }
            tv.setBackgroundResource(R.drawable.cell_shape);
            row.addView(tv);
        }

        // inner for loop
        for (int j = 2; j <= 2; j++) {
            tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(60,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setPadding(5, 5, 5, 5);
            tv.setText(nomTempsColl);
            tv.setTextColor(Color.WHITE);
            if (number != 1) {
                tv.setBackgroundColor(Color.parseColor("#6d6c6c"));
            }
            tv.setBackgroundResource(R.drawable.cell_shape);
            row.addView(tv);
        }

        // inner for loop
        for (int j = 3; j <= 3; j++) {
            tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setPadding(5, 5, 5, 5);
            tv.setText(horaire);
            tv.setTextColor(Color.WHITE);
            if (number != 1) {
                tv.setBackgroundColor(Color.parseColor("#6d6c6c"));
            }
            tv.setBackgroundResource(R.drawable.cell_shape);
            row.addView(tv);
        }

        // inner for loop
        for (int j = 4; j <= 4; j++) {
            tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setPadding(5, 5, 5, 5);
            tv.setText(categorie);
            tv.setTextColor(Color.WHITE);
            if (number != 1) {
                tv.setBackgroundColor(Color.parseColor("#6d6c6c"));
            }
            tv.setBackgroundResource(R.drawable.cell_shape);
            row.addView(tv);
        }

        // inner for loop
        for (int j = 5; j <= 5; j++) {
            tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            tv.setPadding(5, 5, 5, 5);
            getNbParticipants(idTempsColl, tv);
            tv.setTextColor(Color.WHITE);
            if (number != 1) {
                tv.setBackgroundColor(Color.parseColor("#6d6c6c"));
            }
            tv.setBackgroundResource(R.drawable.cell_shape);
            row.addView(tv);
        }

        maReservation = new Reservation(idTempsColl, dateTempsColl, nomTempsColl, horaire, categorie, nbPlaces, lieuTempsColl, nbPlacesEnfant, nbPlacesAdulte);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Mon id2 : " + maReservation.getId());
                getPersPresentes(maReservation.getId(), maReservation.getNom());
            }
        });

        tb.addView(row);
    }

    /************
     * Obtenir les personnes ayant participées à un temps collectif
     * ***************/
    private void getPersPresentes(final String idTC, final String nomTC) {
        requete_pers_presentes = new StringRequest(Request.Method.POST, url_obtenir_pers_presentes, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la réponse à la requète pour les personnes présentes à un tc : " + response);


                try {
                    json = new JSONObject(response);
                    monArrayENFPresents = json.getJSONArray("enf");
                    monArrayAMPresentes = json.getJSONArray("am");
                    monArrayPARTPresents = json.getJSONArray("part");
                    monArrayParentsPresents = json.getJSONArray("parents");

                    listeEnfPresents.clear();
                    listeAMPresents.clear();
                    listePartPresents.clear();
                    listePartPresents.clear();
                    listeParentsPresents.clear();

                    getDatasTempsCollById(idTC);
                    System.out.println("L'id est : " + idTC);

                    for (int i = 0; i < monArrayENFPresents.length(); i++) {
                        monObjetEnfPresente = monArrayENFPresents.getJSONObject(i);
                        idEnfPres = monObjetEnfPresente.getString("id");
                        nomEnfPres = monObjetEnfPresente.getString("nom");
                        prenomEnfPres = monObjetEnfPresente.getString("prenom");

                        monEnfant = new Enfant(idEnfPres, nomEnfPres, prenomEnfPres, "", true);
                        listeEnfPresents.add(monEnfant.getNom() + " " + monEnfant.getPrenom());
                        System.out.println("Pour i = " + i + " on a " + listeEnfPresents);
                        nbEnf++;
                    }

                    for (int i = 0; i < monArrayAMPresentes.length(); i++) {
                        monObjetAMPresente = monArrayAMPresentes.getJSONObject(i);
                        idAMPres = monObjetAMPresente.getString("id");
                        nomAMPres = monObjetAMPresente.getString("nom_naissance");
                        prenomAMPres = monObjetAMPresente.getString("prenom_naissance");

                        monAsmat = new Asmat(idAMPres, nomAMPres, prenomAMPres, true);
                        listeAMPresents.add(monAsmat.getNom() + " " + monAsmat.getPrenom());
                        System.out.println("Pour i = " + i + " on a " + listeAMPresents);
                        nbAM++;
                    }

                    for (int i = 0; i < monArrayPARTPresents.length(); i++) {
                        monObjetPartPresente = monArrayPARTPresents.getJSONObject(i);
                        idPartPres = monObjetPartPresente.getString("id");
                        nomPartPres = monObjetPartPresente.getString("nom");
                        prenomPartPres = monObjetPartPresente.getString("prenom");

                        monPartenaire = new Partenaire(idPartPres, nomPartPres, prenomPartPres, true);
                        listePartPresents.add(monPartenaire.getNom() + " " + monPartenaire.getPrenom());
                        System.out.println("Pour i = " + i + " on a " + listePartPresents);
                        nbPart++;
                    }

                    for (int i = 0; i < monArrayParentsPresents.length(); i++) {
                        monObjetParentsPresents = monArrayParentsPresents.getJSONObject(i);
                        idParentPres = monObjetParentsPresents.getString("id");
                        nomParentPres = monObjetParentsPresents.getString("nom");
                        prenomParentPres = monObjetParentsPresents.getString("prenom");

                        monParent = new Parent(idParentPres, nomParentPres, prenomParentPres, true);
                        listeParentsPresents.add(monParent.getNom() + " " + prenomParentPres);
                        System.out.println("Pour i = " + i + " on a " + listeParentsPresents);
                        nbParents++;
                    }

                    if (listeEnfPresents.size() == 0 && listeAMPresents.size() == 0 && listePartPresents.size() == 0 && listeParentsPresents.size() == 0) {
                        tvTitle.setText("Pas de participants pour ce temps collectif " + nomTC);
                        tvENF.setText("");
                        tvAM.setText("");
                        tvPART.setText("");
                        tvParents.setText("");
                        tvEnfPresents.setText("");
                        tvPartPresents.setText("");
                        tvAmPresentes.setText("");
                        tvParentsPresents.setText("");
                    } else {
                        // On affiche les sous titres et le titre
                        tvENF.setText("Enfants (" + nbEnf + ")");
                        tvAM.setText("Assistantes maternelles (" + nbAM + ")");
                        tvPART.setText("Partenaires (" + nbPart + ")");
                        tvTitle.setText("Voici les personnes ayant participé au temps collectif : " + nomTC);
                        tvParents.setText("Parents (" + nbParents + ")");
                        tvAvertissement.setVisibility(View.VISIBLE);

                        if (listeEnfPresents.size() == 0) {
                            tvEnfPresents.setText("Pas d'enfant");
                            //Toast.makeText(Historique.this, "Liste vide", Toast.LENGTH_SHORT).show();
                        } else {
                            tvEnfPresents.setText("");

                            for (int i = 0; i < listeEnfPresents.size(); i++) {
                                tvEnfPresents.append(" • " + listeEnfPresents.get(i) + "\n");
                            }
                        }

                        if (listeAMPresents.size() == 0) {
                            tvAmPresentes.setText("Pas d'AM");
                            //Toast.makeText(Historique.this, "Liste vide", Toast.LENGTH_SHORT).show();
                        } else {
                            tvAmPresentes.setText("");

                            for (int i = 0; i < listeAMPresents.size(); i++) {
                                tvAmPresentes.append(" • " + listeAMPresents.get(i) + "\n");
                            }
                        }

                        if (listePartPresents.size() == 0) {
                            tvPartPresents.setText("Pas de partenaire");
                        } else {
                            tvPartPresents.setText("");

                            for (int i = 0; i < listePartPresents.size(); i++) {
                                tvPartPresents.append(" • " + listePartPresents.get(i) + "\n");
                            }
                        }

                        if (listeParentsPresents.size() == 0) {
                            tvParentsPresents.setText("Pas de parents");
                            //Toast.makeText(Historique.this, "Liste vide", Toast.LENGTH_SHORT).show();
                        } else {
                            tvParentsPresents.setText("");

                            for (int i = 0; i < listeParentsPresents.size(); i++) {
                                tvParentsPresents.append(" • " + listeParentsPresents.get(i) + "\n");
                            }
                        }
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
                params.put("idTC", idTC);
                Log.e("Envoi params Reserv", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_pers_presentes);
    }


    private int getNbParticipants(final String idTempsColl, final TextView tv) {
        nbPlacesDispo = 0;
        requete_places_by_tc = new StringRequest(Request.Method.POST, url_places_by_tc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la réponse à la permettant de récupérer le nombre de participants : " + response);
                //nbPlacesDispo = Integer.parseInt(response);

                tv.setText(response.replaceAll("\"", ""));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", String.valueOf(error));
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put("idTempsColl", idTempsColl);
                Log.e("getNbParticipants", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_places_by_tc);

        return nbPlacesDispo;
    }
}