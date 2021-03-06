package com.g_concept.tempscollectifs.VuesPrincipales;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.g_concept.tempscollectifs.ClassesMetiers.AsmatBis;
import com.g_concept.tempscollectifs.ClassesMetiers.EnfantBis;
import com.g_concept.tempscollectifs.ClassesMetiers.Network;
import com.g_concept.tempscollectifs.ClassesMetiers.ParentBis;
import com.g_concept.tempscollectifs.ClassesMetiers.PartenaireBis;
import com.g_concept.tempscollectifs.ClassesMetiers.PersonneBis;
import com.g_concept.tempscollectifs.ClassesMetiers.Reservation;
import com.g_concept.tempscollectifs.Fonctionnalites.InfoBulleValidation;
import com.g_concept.tempscollectifs.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ValidationPresence extends AppCompatActivity {
    Spinner spListeTempsColl;
    ConnectivityManager cm;
    Network myNetwork;
    PartenaireBis part;
    Reservation myReservation;
    ParentBis parentBis;
    TabActivity tabs;
    LayoutInflater vi;
    JSONObject json;
    ImageView imgPers;
    int amQuantite, enfant0a3Quant, enfant3a6Quant, enfant6plusQuant, parQuantite, total, action, nb = 0, autreQuantite, gardeDomQuant;
    PersonneBis pers;
    EditText etAM, etEnf0a3, etEnf3a6, etEnfplus6, etParents, etAutre, etGardeDomicile;
    TextView tvNbPersonnesCochees, tvPersCochees;
    Button btnValidationPersonne, btnValidationSaisieQuantitative, btnInfos, btnNonPrevu;
    CheckBox cb;
    MyCustomAdapterPersonne dataAdapter4 = null;
    Map<String, String> params = new HashMap<String, String>(), params6 = new HashMap<String, String>();
    HashMap<Integer, Integer> myHash = new HashMap<Integer, Integer>();
    ListView lvPers;
    RequestQueue requestQueue;
    ArrayList<String> numEtId = new ArrayList<>(), listeTempsColl = new ArrayList<String>();
    ArrayList<EnfantBis> listeEnfants = new ArrayList<>();
    ArrayList<AsmatBis> listeAM = new ArrayList<>();
    ArrayList<PartenaireBis> listePart = new ArrayList<>();
    ArrayList<PersonneBis> listePers = new ArrayList<>();
    ArrayList<ParentBis> parentsList = new ArrayList<>();
    ArrayList<Reservation> tableauReservations = new ArrayList<Reservation>();
    ArrayAdapter<String> adapter;
    final ArrayList<Integer> tabNb = new ArrayList<Integer>();
    Intent intent;
    Reservation maResa;
    String nomTC, text, amQuant, enf0a3Quant, enf3a6Quant, enfPlus6, parentQuant, autreQuant, gardeDomicileQuant, idEnf, nomEnf, prenomEnf,
            id_parent, nom_parent, prenom_parent, idPart, nomPart, prenomPart, temp, idAM, nomAM, prenomAM, idPreinscrit,
            nomPreinscrit, prenomPreinscrit, EXTRA_ID_USER = "idUser", idUser, EXTRA_DB = "donnees", initTC = "Choisir le temps collectif",
            myValue, lieuTempsColl, horaire, idTempsColl, choix, nbPlaces, categorie, nomTempsColl, dateTempsColl,
            url_obtenir_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/getTempsCollectifs.php",
            url_enf_by_tc = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/getEnfantsByIdTC.php",
            url_get_parents_by_tc = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/getAllParentsByIdTC.php",
            url_asmats_by_tc = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/getAsmatsByIdTC.php",
            url_personnes_by_tc = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/getPersonnesByTC.php",
            url_partenaires_by_tc = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/getPartenairesByIdTC.php",
            url_obtenir_datas_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/Preinscription/getDatasTempsCollectifsById.php",
            url_valider_presences_saisie_quantitative = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/doValidationQuantitative.php",
            url_valider_presences = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/validerPresenceEnfants.php",
            url_delete_by_tc = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/deleteReservation.php",
            url_places_by_tc = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/getNbPlacesRestantes.php",
            choixDB;
    StringRequest requete_datas_temps_coll,requete_parents, requete_delete_by_tc, requete_places_by_tc, requete_temps_coll, requete_enfants_by_tc, requete_validation_presence,
            requete_asmats_by_tc, requete_personnes_by_tc, requete_partenaires_by_tc, requete_validation_saisie_quantitative;
    JSONObject leTempsColl, leTempsColl1, leTempsColl2, leTempsColl3, leTempsColl4, parent;
    JSONArray lesTempsColl, lesTempsColl1, lesTempsColl2, lesTempsColl3, lesTempsColl4, parents;
    Reservation maReservation;
    Button fabbuton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation_presence);

        // Initialisation des variables
        spListeTempsColl = (Spinner) findViewById(R.id.spListeTempsColl);
        fabbuton = (Button) findViewById(R.id.fabButton);
        btnInfos = (Button) findViewById(R.id.btnInfos);
        btnValidationSaisieQuantitative = (Button) findViewById(R.id.btnValidationSaisieQuantitative);
        etAM = (EditText) findViewById(R.id.nbAsmat);
        etEnf0a3 = (EditText) findViewById(R.id.nbEnf0a3);
        etEnf3a6 = (EditText) findViewById(R.id.nbEnf3a6);
        etEnfplus6 = (EditText) findViewById(R.id.nbEnfplus6);
        etParents = (EditText) findViewById(R.id.nbParent);
        etAutre = (EditText) findViewById(R.id.nbAutre);
        etGardeDomicile = (EditText) findViewById(R.id.nbGardeDomicile);
        btnValidationPersonne = (Button) findViewById(R.id.btnValidationPersonne);
        btnNonPrevu = (Button) findViewById(R.id.btnNonPrevu);
        lvPers = (ListView) findViewById(R.id.lvPers);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        imgPers = (ImageView) findViewById(R.id.imgPers);
        //tvNbPersonnesCochees = (TextView) findViewById(R.id.tvNbPersonnesCochees);
        tvPersCochees = (TextView) findViewById(R.id.tvPersCochees);

        etAM.setText("0");
        etEnf0a3.setText("0");
        etEnf3a6.setText("0");
        etEnfplus6.setText("0");
        etParents.setText("0");
        etAutre.setText("0");
        etGardeDomicile.setText("0");

        lvPers.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);
                return true;
            }
        });

        // On charge les parameters de la tabhost
        myFunction();
        myValue = ((Accueil) getParent()).getValue();
        System.out.println("MYVALUE" + myValue);

        intent = getIntent();
        choixDB = "";
        choixDB = intent.getStringExtra(EXTRA_DB);
        System.out.println("MA BDD : " + choixDB);

        idUser = "";
        idUser = intent.getStringExtra(EXTRA_ID_USER);
        System.out.println("MA BDD : " + idUser);

        String myPosition = ((Accueil) getParent()).getPosition();

        if(myPosition != null){
            myReservation = ((Accueil) getParent()).getReservation();
        }else{
            ((Accueil) getParent()).setReservation(null);
        }

        // Si l'on a une réservation qui a été envoyée d'un autre onglet
        if (myReservation != null) {
            listeTempsColl.add(myReservation.getNom() + " • " + myReservation.getDate() + " • de " + myReservation.getHoraire());
            tableauReservations.add(myReservation);
            myHash.put(2, Integer.parseInt(myReservation.getId()));
            getPersonnesByTC(myReservation.getId());
            System.out.println("TEST DE LA RESA : " + myReservation.getNom());
        } else {
            listeTempsColl.add(initTC);
        }

        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        myNetwork = new Network(cm);

        // Si on a une Connexion Internet
        if (myNetwork.isOnline()) {
            getTempsCollectifs("1", "date ASC");
        } else {
            Toast.makeText(ValidationPresence.this, "Veuillez vérifier votre connexion internet", Toast.LENGTH_SHORT).show();
        }

        //Le Spinner a besoin d'un adapter pour sa presentation
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listeTempsColl);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Enfin on passe l'adapter au Spinner et c'est tout
        spListeTempsColl.setAdapter(adapter);

        /*** Lors de l'appui  ***/
        fabbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabs = (TabActivity) getParent();
                tabs.getTabHost().setCurrentTab(0);
            }
        });

        /*** Lors du clic sur le bouton Infos ***/
        btnInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), InfoBulleValidation.class);
                intent.putExtra("id", "1");
                startActivity(intent);
            }
        });
    }

    /**********
    ** Chargement des temps collectifs
    **********/
    public void getTempsCollectifs(final String lieuOuRAM, final String choixOrderBy) {
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
                        categorie = leTempsColl.getString("categorie");
                        nbPlaces = leTempsColl.getString("nb_place");
                        horaire = leTempsColl.getString("heureDeb") + " à " + leTempsColl.getString("heureFin");

                        maReservation = new Reservation(idTempsColl, dateTempsColl, nomTempsColl, horaire, categorie, nbPlaces, lieuTempsColl, "", "");
                        tableauReservations.add(maReservation);

                        listeTempsColl.add(nomTempsColl + " • le " + dateTempsColl + " • de " + horaire);
                        System.out.println(numEtId + " Les infos : " + nomTempsColl + " " + "date " + dateTempsColl + " et n° :" + i);

                        myHash.put(i, Integer.parseInt(idTempsColl));
                    }

                    //-- gestion du Click sur la liste Région
                    spListeTempsColl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   final int position, long id) {
                            temp = String.valueOf(spListeTempsColl.getSelectedItem());

                            //System.out.println(" TEST DU HASHMAP " + myHash.get(position - 1));
                            //System.out.println(" Mon temp " + temp);

                            nb = 0;

                            if (temp == initTC) {
                                listePers.clear();
                                listeEnfants.clear();
                                listeAM.clear();
                                listePart.clear();
                                parentsList.clear();
                            } else {

                                getDatasTempsCollById(myHash.get(position - 1).toString());
                                //System.out.println("On affiche ma resa : " + maResa.getNom());

                                btnNonPrevu.setVisibility(View.VISIBLE);
                                tvPersCochees.setVisibility(View.VISIBLE);
                                btnNonPrevu.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        sendTCtoPreinscriptions(maResa);

                                        tabs = (TabActivity) getParent();
                                        tabs.getTabHost().setCurrentTab(2);
                                    }
                                });

                                getNbPlacesDispo(myHash.get(position - 1).toString());
                                btnValidationSaisieQuantitative.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        doSaisieQuantitative(myHash.get(position - 1).toString());
                                    }
                                });
                                listeEnfants.clear();
                                listeAM.clear();
                                listePart.clear();
                                listePers.clear();
                                parentsList.clear();
                                System.out.println("Liste Parents : " + parentsList.size());

                                getPersonnesByTC((myHash.get(position - 1)).toString());
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                    System.out.println("ARRAYLIST " + numEtId.size());

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
                params.put("lieu", "Choisir un lieu");
                params.put("donnees", choixDB);
                params.put("lieuOuRAM", lieuOuRAM);
                params.put("choixOrderBy", choixOrderBy);
                Log.e("Get all temps coll", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_temps_coll);
    }

    /*******
     * Récupération liste des enfants
     ********/
    private void getListeAllParentsByTC(final String idTC) {
        // Récupération des enfants dans une listview
        requete_parents = new StringRequest(Request.Method.POST, url_get_parents_by_tc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("Voici la réponse à la permettant de récupérer les parents qui ont été pré inscrits" + response);
                    json = new JSONObject(response);
                    parents = json.getJSONArray("parents");

                    for (int i = 0; i < parents.length(); i++) {
                        parent = parents.getJSONObject(i);
                        id_parent = parent.getString("id");
                        nom_parent = parent.getString("nom");
                        prenom_parent = parent.getString("prenom");

                        parentBis = new ParentBis(id_parent, nom_parent, prenom_parent, false, idTC);
                        parentsList.add(parentBis);

                        pers = new PersonneBis(id_parent, nom_parent, prenom_parent, "", false, "4", idTempsColl);
                        listePers.add(pers);

                        System.out.println("Liste Personnes with enf and am and part and parents : " + listePers.size());

                        System.out.println("Mon parent : " + parentBis.getNom());
                    }

                    displayListView();
                    Toast.makeText(ValidationPresence.this, "Liste pers " + listePers.size(), Toast.LENGTH_SHORT).show();

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
                params6.put("donnees", choixDB);
                params6.put("idTempsColl", idTC);
                Log.e("Recup des parents", params6.toString());
                return params6;
            }
        };
        requestQueue.add(requete_parents);
    }

    /***********
     * Permet la saisie quantitative des personnes qui sont présentes mais pas en nominatif
     * ************/
    public void doSaisieQuantitative(final String idTempsColl) {
        amQuant = etAM.getText().toString();
        enf0a3Quant = etEnf0a3.getText().toString();
        enf3a6Quant = etEnf3a6.getText().toString();
        enfPlus6 = etEnfplus6.getText().toString();
        parentQuant = etParents.getText().toString();
        autreQuant = etAutre.getText().toString();
        gardeDomicileQuant = etGardeDomicile.getText().toString();

        amQuantite = 0;
        enfant0a3Quant = 0;
        enfant3a6Quant = 0;
        enfant6plusQuant = 0;
        parQuantite = 0;
        autreQuantite = 0;
        gardeDomQuant = 0;

        amQuantite = Integer.parseInt(amQuant);
        enfant0a3Quant = Integer.parseInt(enf0a3Quant);
        enfant3a6Quant = Integer.parseInt(enf3a6Quant);
        enfant6plusQuant = Integer.parseInt(enfPlus6);
        parQuantite = Integer.parseInt(parentQuant);

        etAM.setText("0");
        etEnf0a3.setText("0");
        etEnf3a6.setText("0");
        etEnfplus6.setText("0");
        etParents.setText("0");
        etAutre.setText("0");
        etGardeDomicile.setText("0");

        if (amQuant.equals("")
                && enf0a3Quant.equals("")
                && enf3a6Quant.equals("")
                && enfPlus6.equals("")
                && parentQuant.equals("")
                && autreQuant.equals("")
                && gardeDomicileQuant.equals("")) {
            Toast.makeText(ValidationPresence.this, "Veuillez remplir au moins un champs !", Toast.LENGTH_SHORT).show();
        } else {
            total = Integer.parseInt(amQuant) + Integer.parseInt(enf0a3Quant) + Integer.parseInt(enf3a6Quant) + Integer.parseInt(enfPlus6) + Integer.parseInt(parentQuant) + Integer.parseInt(autreQuant) + Integer.parseInt(gardeDomicileQuant);

            tabNb.add(Integer.parseInt(amQuant));
            tabNb.add(Integer.parseInt(enf0a3Quant));
            tabNb.add(Integer.parseInt(enf3a6Quant));
            tabNb.add(Integer.parseInt(enfPlus6));
            tabNb.add(Integer.parseInt(parentQuant));
            tabNb.add(Integer.parseInt(autreQuant));
            tabNb.add(Integer.parseInt(gardeDomicileQuant));

            if (amQuantite >= 1) {
                System.out.println(" Parent am ");
                //Toast.makeText(ValidationPresence.this, String.valueOf(total), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < amQuantite; i++) {
                    requete_validation_saisie_quantitative = new StringRequest(Request.Method.POST, url_valider_presences_saisie_quantitative, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("Voici la réponse à la validation saisie quantitative : " + response);
                            if (response.contains("[3]")) {
                                Toast.makeText(getApplicationContext(), "Il ne reste pas assez de places pour ce temps collectif", Toast.LENGTH_SHORT).show();
                            } else if (response.contains("[1]")) {
                                Toast.makeText(getApplicationContext(), "Validation prise en compte", Toast.LENGTH_SHORT).show();
                            } else if (response.contains("[2]")) {
                                Toast.makeText(getApplicationContext(), "Validation impossible ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error :", String.valueOf(error));
                        }
                    }) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            params.put("idTempsColl", idTempsColl);
                            params.put("tabNb", String.valueOf(tabNb));
                            params.put("amQuantite", "amQuantite");
                            Log.e("Do saisie quantitative", params.toString());
                            return params;
                        }
                    };
                    requestQueue.add(requete_validation_saisie_quantitative);
                }
            }

            if (enfant0a3Quant >= 1) {
                System.out.println(" Parent enf 0 a 3 ");
                //Toast.makeText(ValidationPresence.this, String.valueOf(total), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < enfant0a3Quant; i++) {
                    requete_validation_saisie_quantitative = new StringRequest(Request.Method.POST, url_valider_presences_saisie_quantitative, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("Voici la réponse à la validation saisie quantitative : " + response);
                            if (response.contains("[3]")) {
                                Toast.makeText(getApplicationContext(), "Il ne reste pas assez de places pour ce temps collectif", Toast.LENGTH_SHORT).show();
                            } else if (response.contains("[1]")) {
                                Toast.makeText(getApplicationContext(), "Validation prise en compte", Toast.LENGTH_SHORT).show();
                            } else if (response.contains("[2]")) {
                                Toast.makeText(getApplicationContext(), "Validation impossible ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error :", String.valueOf(error));
                        }
                    }) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            params.put("idTempsColl", idTempsColl);
                            params.put("tabNb", String.valueOf(tabNb));
                            params.put("enfant0a3Quant", "enfant0a3Quant");
                            Log.e("Do saisie quantitative", params.toString());
                            return params;
                        }
                    };
                    requestQueue.add(requete_validation_saisie_quantitative);
                }
            }

            if (enfant3a6Quant >= 1) {
                System.out.println(" Parent enf 3 a 6 ");
                //Toast.makeText(ValidationPresence.this, String.valueOf(total), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < enfant3a6Quant; i++) {
                    requete_validation_saisie_quantitative = new StringRequest(Request.Method.POST, url_valider_presences_saisie_quantitative, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("Voici la réponse à la validation saisie quantitative : " + response);
                            if (response.contains("[3]")) {
                                Toast.makeText(getApplicationContext(), "Il ne reste pas assez de places pour ce temps collectif", Toast.LENGTH_SHORT).show();
                            } else if (response.contains("[1]")) {
                                Toast.makeText(getApplicationContext(), "Validation prise en compte", Toast.LENGTH_SHORT).show();
                            } else if (response.contains("[2]")) {
                                Toast.makeText(getApplicationContext(), "Validation impossible ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error :", String.valueOf(error));
                        }
                    }) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            params.put("idTempsColl", idTempsColl);
                            params.put("tabNb", String.valueOf(tabNb));
                            params.put("enfant3a6Quant", "enfant3a6Quant");
                            Log.e("Do saisie quantitative", params.toString());
                            return params;
                        }
                    };
                    requestQueue.add(requete_validation_saisie_quantitative);
                }
            }

            if (enfant6plusQuant >= 1) {
                System.out.println(" Parent enf 6 plus  ");
                //Toast.makeText(ValidationPresence.this, String.valueOf(total), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < enfant6plusQuant; i++) {
                    requete_validation_saisie_quantitative = new StringRequest(Request.Method.POST, url_valider_presences_saisie_quantitative, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("Voici la réponse à la validation saisie quantitative : " + response);
                            if (response.contains("[3]")) {
                                Toast.makeText(getApplicationContext(), "Il ne reste pas assez de places pour ce temps collectif", Toast.LENGTH_SHORT).show();
                            } else if (response.contains("[1]")) {
                                Toast.makeText(getApplicationContext(), "Validation prise en compte", Toast.LENGTH_SHORT).show();
                            } else if (response.contains("[2]")) {
                                Toast.makeText(getApplicationContext(), "Validation impossible ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error :", String.valueOf(error));
                        }
                    }) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            params.put("idTempsColl", idTempsColl);
                            params.put("tabNb", String.valueOf(tabNb));
                            params.put("enfant6plusQuant", "enfant6plusQuant");
                            Log.e("Do saisie quantitative", params.toString());
                            return params;
                        }
                    };
                    requestQueue.add(requete_validation_saisie_quantitative);
                }
            }

            if (parQuantite >= 1) {
                //Toast.makeText(ValidationPresence.this, String.valueOf(total), Toast.LENGTH_SHORT).show();
                System.out.println(" Parent quantite ");
                for (int i = 0; i < parQuantite; i++) {
                    requete_validation_saisie_quantitative = new StringRequest(Request.Method.POST, url_valider_presences_saisie_quantitative, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("Voici la réponse à la validation saisie quantitative : " + response);
                            if (response.contains("[3]")) {
                                Toast.makeText(getApplicationContext(), "Il ne reste pas assez de places pour ce temps collectif", Toast.LENGTH_SHORT).show();
                            } else if (response.contains("[1]")) {
                                Toast.makeText(getApplicationContext(), "Validation prise en compte", Toast.LENGTH_SHORT).show();
                            } else if (response.contains("[2]")) {
                                Toast.makeText(getApplicationContext(), "Validation impossible ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error :", String.valueOf(error));
                        }
                    }) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            params.put("idTempsColl", idTempsColl);
                            params.put("tabNb", String.valueOf(tabNb));
                            params.put("parQuantite", "parQuantite");
                            Log.e("Do saisie quantitative", params.toString());
                            return params;
                        }
                    };
                    requestQueue.add(requete_validation_saisie_quantitative);
                }
            }

            /*if (autreQuantite > 0) {
                System.out.println(" Parent autre ");
                //Toast.makeText(ValidationPresence.this, String.valueOf(total), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < autreQuantite; i++) {
                    requete_validation_saisie_quantitative = new StringRequest(Request.Method.POST, url_valider_presences_saisie_quantitative, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("Voici la réponse à la validation saisie quantitative : " + response);
                            if (response.contains("[3]")) {
                                Toast.makeText(getApplicationContext(), "Il ne reste pas assez de places pour ce temps collectif", Toast.LENGTH_SHORT).show();
                            } else if (response.contains("[1]")) {
                                Toast.makeText(getApplicationContext(), "Validation prise en compte", Toast.LENGTH_SHORT).show();
                            } else if (response.contains("[2]")) {
                                Toast.makeText(getApplicationContext(), "Validation impossible ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error :", String.valueOf(error));
                        }
                    }) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            params.put("idTempsColl", idTempsColl);
                            params.put("tabNb", String.valueOf(tabNb));
                            params.put("autreQuantite", "autreQuantite");
                            Log.e("Do saisie quantitative", params.toString());
                            return params;
                        }
                    };
                    requestQueue.add(requete_validation_saisie_quantitative);
                }
            }

            if (gardeDomQuant > 0) {
                System.out.println(" Parent am ");
                //Toast.makeText(ValidationPresence.this, String.valueOf(total), Toast.LENGTH_SHORT).show();
                for (int i = 0; i < gardeDomQuant; i++) {
                    requete_validation_saisie_quantitative = new StringRequest(Request.Method.POST, url_valider_presences_saisie_quantitative, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("Voici la réponse à la validation saisie quantitative : " + response);
                            if (response.contains("[3]")) {
                                Toast.makeText(getApplicationContext(), "Il ne reste pas assez de places pour ce temps collectif", Toast.LENGTH_SHORT).show();
                            } else if (response.contains("[1]")) {
                                Toast.makeText(getApplicationContext(), "Validation prise en compte", Toast.LENGTH_SHORT).show();
                            } else if (response.contains("[2]")) {
                                Toast.makeText(getApplicationContext(), "Validation impossible ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error :", String.valueOf(error));
                        }
                    }) {
                        protected Map<String, String> getParams() throws AuthFailureError {
                            params.put("idTempsColl", idTempsColl);
                            params.put("tabNb", String.valueOf(tabNb));
                            params.put("gardeDomQuant", "gardeDomQuant");
                            Log.e("Do saisie quantitative", params.toString());
                            return params;
                        }
                    };
                    requestQueue.add(requete_validation_saisie_quantitative);
                }
            }*/
        }
    }

    /****************
     * Récupération des places disponibles pour ce temps collectif
     * **************/
    private void getNbPlacesDispo(final String idTempsColl) {
        requete_places_by_tc = new StringRequest(Request.Method.POST, url_places_by_tc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la réponse à la permettant de récupérer le nombre de places restantes : " + response);
                //tvPlacesRestantes.setText("Il reste " + response + " place(s) disponible(s) pour ce temps collectif");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", String.valueOf(error));
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put("idTempsColl", idTempsColl);
                Log.e("getNbPlacesDispo", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_places_by_tc);
    }

    /************
     * Affichage du listview
     * **********/
    private void displayListView() {
        System.out.println(" On est dans la vague 4");
        System.out.println(" Avant l'appel ");
        //create an ArrayAdaptar from the String Array
        dataAdapter4 = new ValidationPresence.MyCustomAdapterPersonne(this, R.layout.listviewcheckbox, listePers);

        // Assign adapter to ListView
        lvPers.setAdapter(dataAdapter4);

        System.out.println(" Après l'appel ");
    }

    /*********
     * Récupération des personnes du temps collectif choisi
     * *******/
    public void getPersonnesByTC(final String idTempsColl) {
        requete_personnes_by_tc = new StringRequest(Request.Method.POST, url_personnes_by_tc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la réponse à la permettant de récupérer les personnes qui ont été pré inscrits : " + response);
                try {
                    json = new JSONObject(response);
                    lesTempsColl1 = json.getJSONArray("enf");
                    lesTempsColl2 = json.getJSONArray("am");
                    lesTempsColl3 = json.getJSONArray("part");
                    lesTempsColl4 = json.getJSONArray("par");

                    for (int i = 0; i < lesTempsColl1.length(); i++) {
                        leTempsColl1 = lesTempsColl1.getJSONObject(i);
                        idEnf = leTempsColl1.getString("id");
                        nomEnf = leTempsColl1.getString("nom");
                        prenomEnf = leTempsColl1.getString("prenom");
                        int typeValidationInString = leTempsColl1.getInt("type_validation");
                        Boolean typeValidation;
                        if (typeValidationInString == 1) {
                            typeValidation = true;
                        } else {
                            typeValidation = false;
                        }
                        System.out.println("valid " + typeValidationInString);

                        // Chargement de l'enfant en tant que personne
                        pers = new PersonneBis(idEnf, nomEnf, prenomEnf, "", typeValidation, "1", idTempsColl);
                        listePers.add(pers);

                        System.out.println("Taille de la liste asmats " + listeAM.size());
                    }

                    for (int i = 0; i < lesTempsColl2.length(); i++) {
                        leTempsColl2 = lesTempsColl2.getJSONObject(i);
                        idAM = leTempsColl2.getString("id");
                        nomAM = leTempsColl2.getString("nom_naissance");
                        prenomAM = leTempsColl2.getString("prenom_naissance");
                        int typeValidationInString = leTempsColl2.getInt("type_validation");
                        Boolean typeValidation;
                        if (typeValidationInString == 1) {
                            typeValidation = true;
                        } else {
                            typeValidation = false;
                        }

                        System.out.println(" Pour " + nomAM + " " + prenomAM + " -> " + typeValidation + " avec " + idTempsColl);

                        // Chargement de l'am en tant que personne
                        pers = new PersonneBis(idAM, nomAM, prenomAM, "", typeValidation, "2", idTempsColl);
                        listePers.add(pers);

                        System.out.println("Taille de la liste asmats " + listeAM.size());
                    }

                    for (int i = 0; i < lesTempsColl3.length(); i++) {
                        leTempsColl3 = lesTempsColl3.getJSONObject(i);
                        idPart = leTempsColl3.getString("id");
                        nomPart = leTempsColl3.getString("nom");
                        prenomPart = leTempsColl3.getString("prenom");
                        int typeValidationInString = leTempsColl3.getInt("type_validation");
                        Boolean typeValidation;
                        if (typeValidationInString == 1) {
                            typeValidation = true;
                        } else {
                            typeValidation = false;
                        }

                        System.out.println(" Pour " + nomPart + " " + prenomPart + " -> " + typeValidation + " avec " + idTempsColl);

                        // Chargement du partenaire en tant que personne
                        pers = new PersonneBis(idPart, nomPart, prenomPart, "", typeValidation, "3", idTempsColl);
                        listePers.add(pers);

                        System.out.println("Taille de la liste asmats " + listeAM.size());
                    }

                    for (int i = 0; i < lesTempsColl4.length(); i++) {
                        leTempsColl4 = lesTempsColl4.getJSONObject(i);
                        id_parent = leTempsColl4.getString("id");
                        nom_parent = leTempsColl4.getString("nom");
                        prenom_parent = leTempsColl4.getString("prenom");
                        int typeValidationInString = leTempsColl4.getInt("type_validation");
                        Boolean typeValidation;
                        if (typeValidationInString == 1) {
                            typeValidation = true;
                        } else {
                            typeValidation = false;
                        }

                        System.out.println(" Pour " + nom_parent + " " + prenom_parent + " -> " + typeValidation + " avec " + idTempsColl);

                        // Chargement du parent en tant que personne
                        pers = new PersonneBis(id_parent, nom_parent, prenom_parent, "", typeValidation, "4", idTempsColl);
                        listePers.add(pers);

                        System.out.println("Mon parent " + nom_parent);

                    }

                    displayListView();
                    System.out.println("Liste des assmats : " + listeAM.toString());

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
                params.put("idTempsColl", idTempsColl);
                Log.e("Envoi params Reserv", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_personnes_by_tc);
    }

    /***********
     * Validation des présences de pré inscrits à un temps collectif choisi
     ***********/
    public void validerPresence(final String personneReservation, final String idTempsColl, final String typePersonne) {
        requete_validation_presence = new StringRequest(Request.Method.POST, url_valider_presences, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la réponse à la validation : " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", String.valueOf(error));
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put("idTempsColl", idTempsColl);
                params.put("idPersonneReservation", personneReservation);
                params.put("typePersonne", typePersonne);
                Log.e("Valider presence", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_validation_presence);
    }

    /*****
     * Permet de modifier la valeur par défaut pour la transmettre dans d'autres onglets
     ****/
    public void myFunction() {
        ((Accueil) getParent()).setValue(myValue);
    }

    /*********
     * Adapter pour afficher les personnes pré-inscrites à un temps collectif
     * *******/
    private class MyCustomAdapterPersonne extends ArrayAdapter<PersonneBis> {

        private ArrayList<PersonneBis> personnesListe;
        final ArrayList<PersonneBis> maListePersonnes = new ArrayList<PersonneBis>();
        int i;
        PartenaireBis part;
        PersonneBis pers;

        public MyCustomAdapterPersonne(Context context, int textViewResourceId,
                                       ArrayList<PersonneBis> personnesListe) {
            super(context, textViewResourceId, personnesListe);
            this.personnesListe = new ArrayList<PersonneBis>();
            this.personnesListe.addAll(personnesListe);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
            ImageView img;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ValidationPresence.MyCustomAdapterPersonne.ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.listviewcheckbox, null);

                holder = new ValidationPresence.MyCustomAdapterPersonne.ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                holder.img = (ImageView) convertView.findViewById(R.id.imgPers);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        PersonneBis pers = (PersonneBis) cb.getTag();

                        pers.setSelected(cb.isChecked());
                        maListePersonnes.add(pers);

                        System.out.println(" PERSONNE LISTE1 " + personnesListe);
                        System.out.println(" PERSONNE LISTE2 " + maListePersonnes);

                        btnValidationPersonne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                /******* Correspond au contenu du spinner *******/
                                text = spListeTempsColl.getSelectedItem().toString();
                                System.out.println("TC TEST " + text);
                                Toast.makeText(ValidationPresence.this, text, Toast.LENGTH_SHORT).show();

                                // Si la sélection est égale à l'initTC
                                if (text.equals(initTC)) {
                                    Toast.makeText(ValidationPresence.this, "Veuillez sélectionner un temps collectif !", Toast.LENGTH_SHORT).show();
                                    // Sinon on procède à la validation des présences
                                } else {
                                    for (i = 0; i < maListePersonnes.size(); i++) {
                                        System.out.println(" ma personne " + maListePersonnes.get(i).getCode() + " " + maListePersonnes.get(i).getIdTempsColl());
                                        validerPresence(maListePersonnes.get(i).getCode(), maListePersonnes.get(i).getIdTempsColl(), maListePersonnes.get(i).getTypePersonne());
                                        maListePersonnes.get(i).setSelected(false);
                                        doSaisieQuantitative(maListePersonnes.get(i).getIdTempsColl());
                                    }

                                    // Permet de switcher de tab quand on clique sur le bouton de validation de présence
                                    tabs = (TabActivity) getParent();
                                    tabs.getTabHost().setCurrentTab(4);
                                }
                            }
                        });

                        if (pers.isSelected()) {
                            nb = nb + 1;
                        } else {
                            if (nb > 0) {
                                nb = nb - 1;
                            } else {
                                nb = 0;
                            }
                        }
                        System.out.println("Nombre d'éléments sélectionnés : " + nb);

                        //tvNbPersonnesCochees.setText("Nombre de personne(s) sélectionnée(s) : " + nb);
                    }

                });

            } else {
                holder = (ValidationPresence.MyCustomAdapterPersonne.ViewHolder) convertView.getTag();
                System.out.println("Nous sommes dans le holder");
            }

            // On récupère la personne à cette position
            pers = personnesListe.get(position);

            if (pers.getSelected()) {
                holder.name.setChecked(pers.getSelected());
                nb = nb + 1;
            } else {
                if (nb > 0) {
                    nb = nb - 1;
                } else {
                    nb = 0;
                }
            }

            //tvNbPersonnesCochees.setText("Nombre de personne(s) sélectionnée(s) : " + nb);

            holder.name.setTag(pers);

            // On charge les images correspondantes aux personnes
            if (pers.getTypePersonne().equals("1")) {
                holder.name.setText(pers.getNom() + " " + pers.getPrenom());
                holder.img.setImageResource(R.drawable.enf);
            } else if (pers.getTypePersonne().equals("2")) {
                holder.name.setText(pers.getNom() + " " + pers.getPrenom());
                holder.img.setImageResource(R.drawable.am);
            } else if (pers.getTypePersonne().equals("3")) {
                holder.name.setText(pers.getNom() + " " + pers.getPrenom());
                holder.img.setImageResource(R.drawable.parte);
            } else if (pers.getTypePersonne().equals("4")) {
                holder.name.setText(pers.getNom() + " " + pers.getPrenom());
                holder.img.setImageResource(R.drawable.famille1);
            }

            return convertView;
        }
    }

    public void getPartenairesByTC(final String idTempsColl) {
        requete_partenaires_by_tc = new StringRequest(Request.Method.POST, url_partenaires_by_tc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la réponse à la permettant de récupérer les partenaires qui ont été pré inscrits : " + response);
                try {
                    json = new JSONObject(response);
                    lesTempsColl = json.getJSONArray("part");
                    final int number = lesTempsColl.length();

                    for (int i = 0; i < number; i++) {
                        leTempsColl = lesTempsColl.getJSONObject(i);
                        idPart = leTempsColl.getString("id");
                        nomPart = leTempsColl.getString("nom");
                        prenomPart = leTempsColl.getString("prenom");

                        part = new PartenaireBis(idPart, nomPart, prenomPart, false, idTempsColl);
                        listePart.add(part);

                        pers = new PersonneBis(idPart, nomPart, prenomPart, "", false, "3", idTempsColl);
                        listePers.add(pers);

                        System.out.println("Liste Personnes with enf and am and part : " + listePers.size());

                        System.out.println("Taille de la liste partenaires " + listePart.size());
                    }

                    System.out.println("Liste des assmats : " + listeAM.toString());

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
                params.put("idTempsColl", idTempsColl);
                Log.e("Get partenaire by TC", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_partenaires_by_tc);
    }

    public void getAsmatsByTC(final String idTempsColl) {
        requete_asmats_by_tc = new StringRequest(Request.Method.POST, url_asmats_by_tc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la réponse à la permettant de récupérer les asmats qui ont été pré inscrits : " + response);
                try {
                    json = new JSONObject(response);
                    lesTempsColl = json.getJSONArray("am");

                    for (int i = 0; i < lesTempsColl.length(); i++) {
                        leTempsColl = lesTempsColl.getJSONObject(i);
                        idAM = leTempsColl.getString("id");
                        nomAM = leTempsColl.getString("nom_naissance");
                        prenomAM = leTempsColl.getString("prenom_naissance");
                        String dateNaissanceAM = leTempsColl.getString("date_naissance");

                        AsmatBis am = new AsmatBis(idAM, nomAM, prenomAM, false, idTempsColl);
                        listeAM.add(am);

                        PersonneBis pers = new PersonneBis(idAM, nomAM, prenomAM, dateNaissanceAM, false, "2", idTempsColl);
                        listePers.add(pers);


                        System.out.println("Liste Personnes with enf and am : " + listePers.size());

                        System.out.println("Taille de la liste asmats " + listeAM.size());
                    }

                    System.out.println("Liste des assmats : " + listeAM.toString());

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
                params.put("idTempsColl", idTempsColl);
                Log.e("Get assmat by TC", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_asmats_by_tc);
    }

    private void getEnfantsByTC(final String idTempsColl) {
        requete_enfants_by_tc = new StringRequest(Request.Method.POST, url_enf_by_tc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la réponse à la permettant de récupérer les enfants qui ont été pré inscrits : " + response);
                try {
                    json = new JSONObject(response);
                    lesTempsColl = json.getJSONArray("enf");

                    System.out.println("TEST liste personnes " + listePers.size());
                    listePers.clear();

                    for (int i = 0; i < lesTempsColl.length(); i++) {
                        leTempsColl = lesTempsColl.getJSONObject(i);
                        idPreinscrit = leTempsColl.getString("id");
                        nomPreinscrit = leTempsColl.getString("nom");
                        prenomPreinscrit = leTempsColl.getString("prenom");
                        String dateNaissancePreinscrit = leTempsColl.getString("date_naissance");

                        EnfantBis enfant = new EnfantBis(idPreinscrit, nomPreinscrit, prenomPreinscrit, dateNaissancePreinscrit, false, idTempsColl);
                        listeEnfants.add(enfant);

                        PersonneBis pers = new PersonneBis(idPreinscrit, nomPreinscrit, prenomPreinscrit, "", false, "1", idTempsColl);
                        listePers.add(pers);


                        System.out.println("Liste Personnes with enf : " + listePers.size());

                        System.out.println("Taille de la liste enfants " + listeEnfants.size());

                        System.out.println(listeEnfants.toString());
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
                params.put("idTempsColl", idTempsColl);
                Log.e("Get enfant by TC", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_enfants_by_tc);
    }

    private void deleteReservation(final String idTempsColl, final String idPersonne, final String typePersonne) {
        requete_delete_by_tc = new StringRequest(Request.Method.POST, url_delete_by_tc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la réponse à la requete delete : " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", String.valueOf(error));
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put("idTempsColl", idTempsColl);
                params.put("idPersonneReservation", idPersonne);
                params.put("typePersonne", typePersonne);
                Log.e("delete reservation", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_delete_by_tc);
    }

    /***********
    *** Passage de l'objet Reservation vers un autre onglet
    ***********/
    public void sendTCtoPreinscriptions(Reservation maResa) {
        ((Accueil) getParent()).setReservation(maResa);
        //System.out.println("On envoie cette réservation : " + maResa.getNom());
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

                    for (int i = 0; i < lesTempsColl.length(); i++) {
                        leTempsColl = lesTempsColl.getJSONObject(i);
                        idTempsColl = leTempsColl.getString("id");
                        nomTC = leTempsColl.getString("nom");
                        lieuTempsColl = leTempsColl.getString("lieu");
                        dateTempsColl = leTempsColl.getString("dateTempsColl");
                        categorie = leTempsColl.getString("categorie");
                        nbPlaces = leTempsColl.getString("nb_place");
                        horaire = leTempsColl.getString("heureDeb") + " - " + leTempsColl.getString("heureFin");

                        //listeTempsColl.add(idTempsColl + " | " + nomTempsColl + " | " + dateTempsColl);
                        System.out.println(numEtId + " Les infos : " + nomTempsColl + " " + "date " + dateTempsColl + " et n° :" + i);
                    }

                    maResa = new Reservation(id, dateTempsColl, nomTC, horaire, categorie, nbPlaces, lieuTempsColl, "", "");

                    System.out.println(" Ma resa : " + maResa.getNom());


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
}
