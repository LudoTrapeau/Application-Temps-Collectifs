package com.g_concept.tempscollectifs.VuesPrincipales;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.ListView;
import android.widget.ScrollView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Preinscription extends AppCompatActivity {

    String id_enfant;
    int nb, newNumber, myNb = 0, myNb2 = 0, myNb3 = 0, myNb4 = 0, myNb5 = 0;
    Spinner spListeTempsColl;
    ArrayAdapter<String> adapter;
    ConnectivityManager cm;
    MyCustomAdapterPartenaires.ViewHolder finalHolder;
    Network myNetwork;
    int nbPlacesDispo;
    MyCustomAdapterPartenaires.ViewHolder holder = null;
    EditText inputSearch, inputSearchParents, inputSearchEnfants, inputSearchPartenaires;
    Button btnResValidAM, btnResValidEnfantsByAM, btnResValidEnfants, btnParentPreinscrireEtValider, btnEnfantPreinscrireEtValider, btnAddToListView, btnAddAm, btnAddPartenaire, btnReservationEnfant, btnReservationAsmat, btnReservationPartenaire, btnReservationEnfant2, btnReservationEnfantParent, btnReservationParent;
    ArrayList<String> listeTempsColl = new ArrayList<String>(), listeAM = new ArrayList<String>(), listePartenaires = new ArrayList<String>(), numEtId = new ArrayList<>(), listeAsmatsString = new ArrayList<>();
    ArrayList<Reservation> tableauReservations = new ArrayList<Reservation>();
    ArrayList<Enfant> listEnfByAM = new ArrayList<Enfant>(), listEnfByParent = new ArrayList<Enfant>(), enfantsByAMList = new ArrayList<Enfant>(), enfantsByParentList = new ArrayList<Enfant>(), enfantsList = new ArrayList<Enfant>();
    ArrayList<Parent> parentsList = new ArrayList<Parent>();
    ArrayList<Asmat> asmatsList = new ArrayList<Asmat>();
    ArrayList<Partenaire> partenaireList = new ArrayList<Partenaire>();
    Reservation myReservation = new Reservation("1", "1", "1", "1", "1", "1", "1", "1", "1");
    MyCustomAdapter dataAdapter1 = null;
    MyCustomAdapterEnfByAM dataAdapter8 = null;
    MyCustomAdapterAsmats dataAdapter2 = null;
    MyCustomAdapterPartenaires dataAdapter3 = null;
    MyCustomAdapterForParents dataAdapter4 = null;
    TextView tvDate, tvLieu, tvCategorie, tvNbPlaces, tvNbPlacesTotal, tvEnfByAm, tvEnfByParent, tnNbAM, tvNbParents, tvNbEnf, tvNbPart;
    JSONObject json, enfant;
    JSONArray adherents, enfants;
    Button btnDeconnexion;
    TextView nbCheck;
    StringRequest requete_enfants, requete_partenaires, requete_parents, requete_enf_by_parent;
    Map<String, String> params = new HashMap<String, String>(), params6 = new HashMap<String, String>();
    HashMap<Integer, Integer> myHash = new HashMap<Integer, Integer>();
    Context context;
    RequestQueue requestQueue;
    String[] tabChoix;
    int action;
    ListView listViewEnfants, listviewAsmats, listviewPartenaires, listviewEnfByAM, listViewParents, listviewEnfByParent;
    Intent intent;
    String id_parent, nom_parent, prenom_parent, nom_parent2, prenom_parent2, tcChoisi, dateNaissance, nbPlacesReservees, nbPlacesTotal, EXTRA_ID_USER = "idUser", idUser, EXTRA_DB = "donnees", initTC = "Choisir le temps collectif",
            prenom_assmat, id_part, nom_part, prenom_part, id_assmat, nom_assmat, myValue, lieuTempsColl, nom_enfant, prenom_enfant,
            url_get_enfants = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/getAllEnfants.php", horaire, idTempsColl, choix, nbPlaces, categorie, nomTempsColl, dateTempsColl,
            url_get_enfants_by_filter = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/getAllEnfantsByFilter.php",
            url_get_parents = "https://www.web-familles.fr/AppliTempsCollectifs/Preinscription/getAllParents.php",
            url_get_parents_by_filter = "https://www.web-familles.fr/AppliTempsCollectifs/Preinscription/getAllParentsByFilter.php",
            url_obtenir_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/getTempsCollectifs.php",
            url_do_reservation = "https://www.web-familles.fr/AppliTempsCollectifs/Preinscription/doReservation.php",
            url_get_assmats = "https://www.web-familles.fr/AppliTempsCollectifs/Preinscription/getAllAsmats.php",
            url_get_assmats_by_filter = "https://www.web-familles.fr/AppliTempsCollectifs/Preinscription/getAllAsmatsByFilter.php",
            url_enf_by_am = "https://www.web-familles.fr/AppliTempsCollectifs/Preinscription/getEnfByAM.php",
            url_enf_by_parent = "https://www.web-familles.fr/AppliTempsCollectifs/Preinscription/getEnfByParent.php",
            url_get_partenaire = "https://www.web-familles.fr/AppliTempsCollectifs/Preinscription/getAllPartenaires.php",
            url_get_partenaire_by_filter = "https://www.web-familles.fr/AppliTempsCollectifs/Preinscription/getAllPartenairesByFilter.php",
            url_obtenir_datas_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/Preinscription/getDatasTempsCollectifsById.php", choixDB;
    StringRequest requete_enfants_by_filter, requete_partenaires_by_filter, requete_parents_by_filter, requete_temps_coll, requete_datas_temps_coll, requete_do_reservation, requete_assmats, requete_enf_by_am, requete_assmats_by_filter;
    JSONObject leTempsColl, objectNbPlacesReservees, objectNbPlacesTotal, parent;
    JSONArray lesTempsColl, arrayNbPlacesReservees, arrayNbPlaceTotal, parents;
    Reservation maReservation;
    TabActivity tabs;
    ScrollView mainScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preinscription);

        /************************** Initialisation et instantiation des paramêtres **************************/
        spListeTempsColl = (Spinner) findViewById(R.id.spListeTempsColl);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvLieu = (TextView) findViewById(R.id.tvLieu);
        tvCategorie = (TextView) findViewById(R.id.tvCategorie);
        tvNbPlaces = (TextView) findViewById(R.id.tvNbPlaces);
        tvNbPlacesTotal = (TextView) findViewById(R.id.tvNbPlacesTotal);
        btnDeconnexion = (Button) findViewById(R.id.fabButton);
        btnReservationEnfant = (Button) findViewById(R.id.btnResEnf);
        btnReservationEnfant2 = (Button) findViewById(R.id.btnResEnfants);
        btnReservationEnfantParent = (Button) findViewById(R.id.btnResEnfantsParents);
        btnReservationParent = (Button) findViewById(R.id.btnResParents);
        btnReservationAsmat = (Button) findViewById(R.id.btnResAsmat);
        btnReservationPartenaire = (Button) findViewById(R.id.btnResPart);
        btnResValidEnfants = (Button) findViewById(R.id.btnResValidEnfants);
        mainScrollView = (ScrollView) findViewById(R.id.mainScrollView);
        btnEnfantPreinscrireEtValider = (Button) findViewById(R.id.btnResValidEnfantsParents);
        btnParentPreinscrireEtValider = (Button) findViewById(R.id.btnResValidParents);
        btnResValidEnfantsByAM = (Button) findViewById(R.id.btnResEtValidEnfByAM);
        btnResValidAM = (Button) findViewById(R.id.btnResEtValidAsmat);
        tvEnfByAm = (TextView) findViewById(R.id.tvEnfByAm);
        tvEnfByParent = (TextView) findViewById(R.id.tvEnfByParent);
        listViewEnfants = (ListView) findViewById(R.id.listView1);
        listViewParents = (ListView) findViewById(R.id.lvParents);
        listviewAsmats = (ListView) findViewById(R.id.lvAsmat);
        listviewPartenaires = (ListView) findViewById(R.id.lvPart);
        listviewEnfByAM = (ListView) findViewById(R.id.lvEnfByAM);
        listviewEnfByParent = (ListView) findViewById(R.id.lvEnfByParent);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearchParents = (EditText) findViewById(R.id.inputSearchParents);
        inputSearchEnfants = (EditText) findViewById(R.id.inputSearchEnfants);
        inputSearchPartenaires = (EditText) findViewById(R.id.inputSearchPartenaires);
        tnNbAM = (TextView) findViewById(R.id.tvNbPers);
        tvNbEnf = (TextView) findViewById(R.id.tvNbEnfants);
        tvNbParents = (TextView) findViewById(R.id.tvNbParents);
        tvNbPart = (TextView) findViewById(R.id.tvNbPartenaire);
        /**************************************************************************************/

        /************** Init ************/
        myFunction();
        myValue = ((Accueil) getParent()).getValue();

        myReservation = ((Accueil) getParent()).getReservation();

        tableauReservations.add(myReservation);

        mainScrollView.post(new Runnable() {

            @Override
            public void run() {
                mainScrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        intent = getIntent();
        choixDB = "";
        choixDB = intent.getStringExtra(EXTRA_DB);
        System.out.println("MA BDD : " + choixDB);

        idUser = "";
        idUser = intent.getStringExtra(EXTRA_ID_USER);
        System.out.println("MA BDD : " + idUser);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        initialiseListView();

        // Initialisation du nombre d'enfant coché à 0
        nb = 0;

        if (myReservation != null) {
            listeTempsColl.add(myReservation.getNom() + " • " + myReservation.getDate() + " • de " + myReservation.getHoraire());
            tableauReservations.add(myReservation);
            myHash.put(2, Integer.parseInt(myReservation.getId()));
            System.out.println("TEST DE LA RESA : " + myReservation.getNom());
            getDatasTempsCollById(Integer.toString(myHash.get(2)), myReservation.getId());
        } else {
            listeTempsColl.add(initTC);
        }

        /*
        tableauReservations.add(myReservation);
        myHash.put(0, Integer.parseInt(myReservation.getId()));
        spListeTempsColl.setSelection(0,true);*/

        spListeTempsColl.setSelection(1, true);
        /****************************/

        View current = getCurrentFocus();
        if (current != null) current.clearFocus();

        // Récupération du réseau
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        myNetwork = new Network(cm);

        if (myNetwork.isOnline()) {
            getListeAllEnfants();
            System.out.println("SIZE country : " + enfantsList.size());

            asmatsList.clear();
            getListeAllAssmats();
            System.out.println(" Nombre d'asmats : " + asmatsList.size());
            displayListViewAsmats();

            getListeAllPartenaires();
            displayListViewPartenaires();

            getListeAllParents();
            displayListViewParents();

            TabActivity tabs = (TabActivity) getParent();
            System.out.println("ON RECUPERE LA VALEUR SUIVANTE " + tabs);

            getTempsCollectifs("1", "", "date ASC");
        } else {
            Toast.makeText(context, "Veuillez vérifier votre connexion internet", Toast.LENGTH_SHORT).show();
        }

        //Le Spinner a besoin d'un adapter pour sa presentation
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listeTempsColl);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Enfin on passe l'adapter au Spinner et c'est tout
        spListeTempsColl.setAdapter(adapter);

        // Pour se déconnecter
        btnDeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabs = (TabActivity) getParent();
                tabs.getTabHost().setCurrentTab(0);
            }
        });
    }

    /*******
     * Permet d'initialiser les listviews
     * ******/
    public void initialiseListView() {
        listViewParents.setOnTouchListener(new ListView.OnTouchListener() {
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

        listViewEnfants.setOnTouchListener(new ListView.OnTouchListener() {
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

        listviewPartenaires.setOnTouchListener(new ListView.OnTouchListener() {
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

        listviewAsmats.setOnTouchListener(new ListView.OnTouchListener() {
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

        listviewEnfByAM.setOnTouchListener(new ListView.OnTouchListener() {
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

        listviewEnfByParent.setOnTouchListener(new ListView.OnTouchListener() {
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

    }

    /*****
     * Permet de modifier la valeur par défaut pour la transmettre dans d'autres onglets
     ****/
    public void myFunction() {
        ((Accueil) getParent()).setValue(myValue);
    }

    /*******
     * Récupération liste des assmats
     ********/
    private void getListeAllAssmats() {
        // Récupération des enfants dans une listview
        requete_assmats = new StringRequest(Request.Method.POST, url_get_assmats, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    json = new JSONObject(response);
                    adherents = json.getJSONArray("am");

                    asmatsList.clear();

                    for (int i = 0; i < adherents.length(); i++) {
                        enfant = adherents.getJSONObject(i);
                        id_assmat = enfant.getString("id");
                        nom_assmat = enfant.getString("nom_naissance");
                        prenom_assmat = enfant.getString("prenom_naissance");

                        Asmat asmat = new Asmat(id_assmat, nom_assmat, prenom_assmat, false);
                        asmatsList.add(asmat);
                        listeAsmatsString.add(nom_assmat + " " + prenom_assmat);

                        listeAM.add(nom_assmat + " " + prenom_assmat + " |" + id_assmat + "| ");

                        System.out.println(" Liste des asmats : " + i);
                    }

                    displayListViewAsmats();

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
                Log.e("Envoi params Reserv", params6.toString());
                return params6;
            }
        };
        requestQueue.add(requete_assmats);
    }

    /*******
     * Récupération liste des assmats par filtre
     ********/
    private void getListeAllAssmatsByFilter(final String charSequence) {
        // Récupération des enfants dans une listview
        requete_assmats_by_filter = new StringRequest(Request.Method.POST, url_get_assmats_by_filter, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    json = new JSONObject(response);
                    adherents = json.getJSONArray("am");

                    asmatsList.clear();

                    for (int i = 0; i < adherents.length(); i++) {
                        enfant = adherents.getJSONObject(i);
                        id_assmat = enfant.getString("id");
                        nom_assmat = enfant.getString("nom_naissance");
                        prenom_assmat = enfant.getString("prenom_naissance");

                        Asmat asmat = new Asmat(id_assmat, nom_assmat, prenom_assmat, false);
                        asmatsList.add(asmat);
                        listeAsmatsString.add(nom_assmat + " " + prenom_assmat);

                        listeAM.add(nom_assmat + " " + prenom_assmat + " |" + id_assmat + "| ");

                        System.out.println(" Liste des asmats : " + i);
                    }

                    displayListViewAsmats();

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
                params6.put("charSequence", charSequence);
                Log.e("Envoi params Reserv", params6.toString());
                return params6;
            }
        };
        requestQueue.add(requete_assmats_by_filter);
    }

    /*******
     * Récupération liste des enfants
     ********/
    private void getListeAllEnfants() {
        // Récupération des enfants dans une listview
        requete_enfants = new StringRequest(Request.Method.POST, url_get_enfants, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    json = new JSONObject(response);
                    adherents = json.getJSONArray("enfants");

                    for (int i = 0; i < adherents.length(); i++) {
                        enfant = adherents.getJSONObject(i);
                        id_enfant = enfant.getString("id");
                        nom_enfant = enfant.getString("nom");
                        prenom_enfant = enfant.getString("prenom");
                        dateNaissance = enfant.getString("date_naissance");

                        Enfant enfant = new Enfant(id_enfant, nom_enfant, prenom_enfant, dateNaissance, false);
                        enfantsList.add(enfant);
                    }

                    System.out.println("COUNTRY LIST : " + enfantsList.size());
                    displayListView();

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
                Log.e("Recup des enfants", params6.toString());
                return params6;
            }
        };
        requestQueue.add(requete_enfants);
    }

    /*******
     * Récupération liste des enfants par filtre
     ********/
    private void getListeAllEnfantsByFilter(final String charSequence) {
        // Récupération des enfants dans une listview
        requete_enfants_by_filter = new StringRequest(Request.Method.POST, url_get_enfants_by_filter, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    json = new JSONObject(response);
                    adherents = json.getJSONArray("enfants");

                    enfantsList.clear();

                    for (int i = 0; i < adherents.length(); i++) {
                        enfant = adherents.getJSONObject(i);
                        id_enfant = enfant.getString("id");
                        nom_enfant = enfant.getString("nom");
                        prenom_enfant = enfant.getString("prenom");
                        dateNaissance = enfant.getString("date_naissance");

                        Enfant enfant = new Enfant(id_enfant, nom_enfant, prenom_enfant, dateNaissance, false);
                        enfantsList.add(enfant);
                    }

                    System.out.println("COUNTRY LIST : " + enfantsList.size());
                    displayListView();

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
                params6.put("charSequence", charSequence);
                Log.e("Recup des enfants", params6.toString());
                return params6;
            }
        };
        requestQueue.add(requete_enfants_by_filter);
    }

    /*******
     * Récupération liste des parents
     ********/
    private void getListeAllParents() {
        // Récupération des enfants dans une listview
        requete_parents = new StringRequest(Request.Method.POST, url_get_parents, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    json = new JSONObject(response);
                    parents = json.getJSONArray("parents");

                    parentsList.clear();

                    for (int i = 0; i < parents.length(); i++) {
                        parent = parents.getJSONObject(i);
                        id_parent = parent.getString("id");
                        nom_parent = parent.getString("resp1Nom");
                        prenom_parent = parent.getString("resp1Prenom");
                        nom_parent2 = parent.getString("resp2Nom");
                        prenom_parent2 = parent.getString("resp2Prenom");

                        if (nom_parent.isEmpty()) {
                            Parent parent = new Parent(id_parent, nom_parent, prenom_parent, false);
                            parentsList.add(parent);
                        } else {
                            Parent parent = new Parent(id_parent, nom_parent2, prenom_parent2, false);
                            parentsList.add(parent);
                        }

                    }

                    displayListViewParents();

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
                Log.e("Recup des parents", params6.toString());
                return params6;
            }
        };
        requestQueue.add(requete_parents);
    }

    /*******
     * Récupération liste des enfants d'un parent sélectionné
     ********/
    private void getEnfByParent(final String idParent) {
        tvEnfByParent.setText("");
        // Récupération des enfants dans une listview
        requete_enf_by_parent = new StringRequest(Request.Method.POST, url_enf_by_parent, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    json = new JSONObject(response);
                    enfants = json.getJSONArray("enf");

                    System.out.println("test = " + enfants.length());

                    if (enfants.length() == 0) {
                        tvEnfByParent.setText("Pas d'enfant rattaché à ces parents");
                    } else {
                        for (int i = 0; i < enfants.length(); i++) {
                            enfant = enfants.getJSONObject(i);
                            id_enfant = enfant.getString("id");
                            nom_enfant = enfant.getString("nom");
                            prenom_enfant = enfant.getString("prenom");

                            Enfant enf = new Enfant(id_enfant, nom_enfant, prenom_enfant, "", false);
                            listEnfByParent.add(enf);
                            enfantsByParentList.add(enf);

                            System.out.println(" Liste des enfants by parent : " + enf.getNom() + " " + enf.getPrenom());
                        }
                        tvEnfByParent.setText("Liste des enfants");

                        displayListViewEnfByParent();
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
                params6.put("idParent", idParent);
                params6.put("donnees", choixDB);
                Log.e("Envoi params Reserv", params6.toString());
                return params6;
            }
        };
        requestQueue.add(requete_enf_by_parent);
    }

    /*******
     * Récupération liste des parents
     ********/
    private void getListeAllParentsByFilter(final String charSequence) {
        // Récupération des enfants dans une listview
        requete_parents_by_filter = new StringRequest(Request.Method.POST, url_get_parents_by_filter, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    json = new JSONObject(response);
                    parents = json.getJSONArray("parents");

                    parentsList.clear();

                    for (int i = 0; i < parents.length(); i++) {
                        parent = parents.getJSONObject(i);
                        id_parent = parent.getString("id");
                        nom_parent = parent.getString("resp1Nom");
                        prenom_parent = parent.getString("resp1Prenom");
                        nom_parent2 = parent.getString("resp2Nom");
                        prenom_parent2 = parent.getString("resp2Prenom");

                        if (nom_parent.isEmpty()) {
                            Parent parent = new Parent(id_parent, nom_parent, prenom_parent, false);
                            parentsList.add(parent);
                        } else {
                            Parent parent = new Parent(id_parent, nom_parent2, prenom_parent2, false);
                            parentsList.add(parent);
                        }
                    }

                    displayListViewParents();

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
                params6.put("charSequence", charSequence);
                Log.e("Recup des parents", params6.toString());
                return params6;
            }
        };
        requestQueue.add(requete_parents_by_filter);
    }

    /*******
     * Récupération liste des partenaires
     ********/
    private void getListeAllPartenaires() {
        // Récupération des enfants dans une listview
        requete_partenaires = new StringRequest(Request.Method.POST, url_get_partenaire, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    json = new JSONObject(response);
                    adherents = json.getJSONArray("part");

                    for (int i = 0; i < adherents.length(); i++) {
                        enfant = adherents.getJSONObject(i);
                        id_part = enfant.getString("id");
                        nom_part = enfant.getString("nom");
                        prenom_part = enfant.getString("prenom");

                        Partenaire partenaire = new Partenaire(id_part, nom_part, prenom_part, false);
                        partenaireList.add(partenaire);

                        listePartenaires.add(nom_part + " " + prenom_part + "|" + id_part + "|");

                        System.out.println(" PARTENAIRE : " + i);
                    }

                    System.out.println(" Nombre de partenaire : " + partenaireList.size());
                    displayListViewPartenaires();

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
                Log.e("Envoi params Reserv", params6.toString());
                return params6;
            }
        };
        requestQueue.add(requete_partenaires);
    }

    /*******
     * Récupération liste des partenaires
     ********/
    private void getListeAllPartenairesByFilter(final String charSequence) {
        // Récupération des enfants dans une listview
        requete_partenaires_by_filter = new StringRequest(Request.Method.POST, url_get_partenaire_by_filter, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    json = new JSONObject(response);
                    adherents = json.getJSONArray("part");

                    partenaireList.clear();

                    for (int i = 0; i < adherents.length(); i++) {
                        enfant = adherents.getJSONObject(i);
                        id_part = enfant.getString("id");
                        nom_part = enfant.getString("nom");
                        prenom_part = enfant.getString("prenom");

                        Partenaire partenaire = new Partenaire(id_part, nom_part, prenom_part, false);
                        partenaireList.add(partenaire);

                        listePartenaires.add(nom_part + " " + prenom_part + "|" + id_part + "|");

                        System.out.println(" PARTENAIRE : " + i);
                    }

                    System.out.println(" Nombre de partenaire : " + partenaireList.size());
                    displayListViewPartenaires();

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
                params6.put("charSequence", charSequence);
                Log.e("Envoi params Reserv", params6.toString());
                return params6;
            }
        };
        requestQueue.add(requete_partenaires_by_filter);
    }

    /*****
     * Chargement des temps collectifs
     ****/
    public void getTempsCollectifs(final String lieuOuRAM, final String id, final String choixOrderBy) {
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

                        listeTempsColl.add(nomTempsColl + " • " + dateTempsColl + " • de " + horaire);

                        System.out.println(numEtId + " Les infos : " + nomTempsColl + " " + "date " + dateTempsColl + " et n° :" + i);

                        myHash.put(i, Integer.parseInt(idTempsColl));

                    }

                    //-- gestion du Click sur la liste de temps collectifs
                    spListeTempsColl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long id) {
                            tabChoix = String.valueOf(spListeTempsColl.getSelectedItem()).replaceAll(" ", "").split("\\|");
                            String temp = String.valueOf(spListeTempsColl.getSelectedItem());
                            choix = tabChoix[0];
                            tcChoisi = String.valueOf(spListeTempsColl.getSelectedItem());
                            Toast.makeText(Preinscription.this, tcChoisi, Toast.LENGTH_SHORT).show();
                            if (temp.equals(initTC)) {
                                // On initilise les différents textviews
                                tvDate.setText("");
                                tvLieu.setText("");
                                tvCategorie.setText("");
                                tvNbPlaces.setText("");
                                tvNbPlacesTotal.setText("");
                            } else {
                                System.out.println("MY HASH IS EQUALS TO " + myHash);
                                if (position != 0) {
                                    getDatasTempsCollById(Integer.toString(myHash.get(position - 1)), choix);
                                }
                            }

                            System.out.println(" TEST DU HASHMAP " + myHash.get(position - 1));
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
                Log.e("Envoi params Reserv", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_temps_coll);
    }

    /*****
     * On charge les datas correspondants au temps collectif choisi
     *******/
    public void getDatasTempsCollById(final String id, final String choix) {
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

                    int nbPlacesRestantes = Integer.parseInt(nbPlacesTotal) - Integer.parseInt(nbPlacesReservees);

                    System.out.println("Nombre de places dispos : " + nbPlacesRestantes);


                    getInfosTempsColl(nbPlacesRestantes, idTempsColl);

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
                params.put("id", id);
                params.put("donnees", choixDB);
                Log.e("Envoi params Reserv", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_datas_temps_coll);
    }

    /******
     * Chargement des données relatives à un temps collectif
     * *******/
    public void getInfosTempsColl(int nbPlacesRestantes, final String choix) {
        // Si aucun lieu n'est choisi, les textviews sont vides
        if (choix == null) {
            tvDate.setText(convertDate(dateTempsColl));
            tvCategorie.setText(categorie);
            tvNbPlaces.setText(Integer.toString(nbPlacesRestantes));
            tvNbPlacesTotal.setText(nbPlaces);
            tvLieu.setText(lieuTempsColl);
        } else {
            if (choix.equals("Choisirletempscollectif")) {
                tvDate.setText("");
                tvCategorie.setText("");
                tvNbPlaces.setText("");
                tvNbPlacesTotal.setText("");
                tvLieu.setText("");
            } else {
                tvDate.setText(convertDate(dateTempsColl));
                tvCategorie.setText(categorie);
                tvNbPlaces.setText(Integer.toString(nbPlacesRestantes));
                tvNbPlacesTotal.setText(nbPlaces);
                tvLieu.setText(lieuTempsColl);
            }
        }
    }

    /******
     * Procédure pour faire la réservation à ce temps collectif
     *****/
    public void doReservation(final String choix, final String typePersonne, final String maVariable, final String inscriptionOuValidation) {
        requete_do_reservation = new StringRequest(Request.Method.POST, url_do_reservation, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Réponse requète do réservation : " + response);

                // Si c'est un enfant
                if (typePersonne == "1") {
                    if (response.contains("[1]")) {
                        Toast.makeText(getApplicationContext(), "La réservation de l'enfant a été prise en compte !", Toast.LENGTH_SHORT).show();
                    } else if (response.contains("[2]")) {
                        Toast.makeText(getApplicationContext(), "Cet enfant est déjà inscrit à ce temps collectif !", Toast.LENGTH_SHORT).show();
                    }

                    // Si c'est une assmat
                } else if (typePersonne == "2") {
                    if (response.contains("[1]")) {
                        Toast.makeText(getApplicationContext(), "La réservation de cette Assistante Maternelle a été prise en compte !", Toast.LENGTH_SHORT).show();
                    } else if (response.contains("[2]")) {
                        Toast.makeText(getApplicationContext(), "Cette Assistante Maternelle est déjà inscrite à ce temps collectif ! ", Toast.LENGTH_SHORT).show();
                    }

                    // Si c'est un partenaire
                } else if (typePersonne == "3") {
                    if (response.contains("[1]")) {
                        Toast.makeText(getApplicationContext(), "La réservation de ce partenaire a été prise en compte !", Toast.LENGTH_SHORT).show();
                    } else if (response.contains("[2]")) {
                        Toast.makeText(getApplicationContext(), "Ce Partenaire est déjà inscrit à ce temps collectif", Toast.LENGTH_SHORT).show();
                    }
                } else if (typePersonne == "4") {
                    if (response.contains("[1]")) {
                        Toast.makeText(getApplicationContext(), "La réservation de ce parent a été prise en compte !", Toast.LENGTH_SHORT).show();
                    } else if (response.contains("[2]")) {
                        Toast.makeText(getApplicationContext(), "Ce parent est déjà inscrit à ce temps collectif", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", String.valueOf(error));
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put("idTC", idTempsColl);
                if (typePersonne == "1") {
                    params.put("idEnf", maVariable);
                } else if (typePersonne == "2") {
                    params.put("idAssmat", maVariable);
                } else if (typePersonne == "3") {
                    params.put("idPart", maVariable);
                } else if (typePersonne == "4") {
                    params.put("idParent", maVariable);
                }
                params.put("typePersonne", typePersonne);
                params.put("donnees", choixDB);
                params.put("idUser", idUser);
                params.put("inscriptionOuValidation", inscriptionOuValidation);
                Log.e("Envoi params Réserv TC", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_do_reservation);
    }

    public String convertDate(String date) {
        String[] dateTab = date.split("-");
        String moisLettre = "00";
        if (dateTab[1].equals("01")) {
            moisLettre = "Janvier";
        } else if (dateTab[1].equals("02")) {
            moisLettre = "Février";
        } else if (dateTab[1].equals("03")) {
            moisLettre = "Mars";
        } else if (dateTab[1].equals("04")) {
            moisLettre = "Avril";
        } else if (dateTab[1].equals("05")) {
            moisLettre = "Mai";
        } else if (dateTab[1].equals("06")) {
            moisLettre = "Juin";
        } else if (dateTab[1].equals("07")) {
            moisLettre = "Juillet";
        } else if (dateTab[1].equals("08")) {
            moisLettre = "Août";
        } else if (dateTab[1].equals("09")) {
            moisLettre = "Septembre";
        } else if (dateTab[1].equals("10")) {
            moisLettre = "Octobre";
        } else if (dateTab[1].equals("11")) {
            moisLettre = "Novembre";
        } else if (dateTab[1].equals("12")) {
            moisLettre = "Décembre";
        }

        String dateFr = dateTab[0] + " " + moisLettre + " " + dateTab[2];

        return dateFr;
    }

    /*******
     * Affichage des enfants dans la listeView
     * *********/
    private void displayListView() {

        dataAdapter1 = new MyCustomAdapter(this, R.layout.listviewcheckbox, enfantsList);
        listViewEnfants.setAdapter(dataAdapter1);

        inputSearchEnfants.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                enfantsList.clear();
                getListeAllEnfantsByFilter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    /*******
     * Affichage des parents dans la listeView
     * *********/
    private void displayListViewParents() {

        //create an ArrayAdaptar from the String Array
        dataAdapter4 = new MyCustomAdapterForParents(this, R.layout.listviewcheckbox, parentsList);
        // Assign adapter to ListView
        listViewParents.setAdapter(dataAdapter4);

        listViewParents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Parent par = (Parent) parent.getItemAtPosition(position);
                /*Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + enf.getNom(),
                        Toast.LENGTH_LONG).show();*/
            }
        });


        inputSearchParents.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //Preinscription.this.dataAdapter2.getFilter().filter(cs);
                System.out.println("cs " + cs + " arg1 " + arg1 + " arg2 " + arg2 + " arg3 " + arg3);

                parentsList.clear();
                getListeAllParentsByFilter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    /*******
     * Affichage des assmats dans la listeView
     * *********/
    private void displayListViewAsmats() {

        System.out.println(" Asmats liste : " + asmatsList.size());
        //create an ArrayAdaptar from the String Array
        dataAdapter2 = new MyCustomAdapterAsmats(this, R.layout.listviewcheckbox, asmatsList, enfantsList);
        adapter = new ArrayAdapter<String>(this, R.layout.listviewcheckbox, listeAM);
        // Assign adapter to ListView
        listviewAsmats.setAdapter(dataAdapter2);

        adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.lblListItem, listeAsmatsString);
        //listviewAsmats.setAdapter(adapter);

        listviewAsmats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Asmat asmat = (Asmat) parent.getItemAtPosition(position);
                /*Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + asmat.getNom(),
                        Toast.LENGTH_LONG).show();*/
            }
        });


        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //Preinscription.this.dataAdapter2.getFilter().filter(cs);
                System.out.println("cs " + cs + " arg1 " + arg1 + " arg2 " + arg2 + " arg3 " + arg3);

                asmatsList.clear();
                getListeAllAssmatsByFilter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

    }

    /*******
     * Affichage des enfants appartenant aux assmats dans la listeView
     * *********/
    private void displayListViewEnfByAM() {

        //create an ArrayAdaptar from the String Array
        dataAdapter8 = new MyCustomAdapterEnfByAM(this, R.layout.listviewcheckbox, enfantsByAMList);
        // Assign adapter to ListView
        listviewEnfByAM.setAdapter(dataAdapter8);

        listviewEnfByAM.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Enfant enf = (Enfant) parent.getItemAtPosition(position);
                /*Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + enf.getNom(),
                        Toast.LENGTH_LONG).show();*/
            }
        });
    }

    /*******
     * Affichage des enfants appartenant aux parents dans la listeView
     * *********/
    private void displayListViewEnfByParent() {

        //create an ArrayAdaptar from the String Array
        dataAdapter1 = new MyCustomAdapter(this, R.layout.listviewcheckbox, enfantsByParentList);
        // Assign adapter to ListView
        listviewEnfByParent.setAdapter(dataAdapter1);

        listviewEnfByParent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Enfant enf = (Enfant) parent.getItemAtPosition(position);
                /*Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + enf.getNom(),
                        Toast.LENGTH_LONG).show();*/
            }
        });
    }

    /*******
     * Affichage des partenaires dans la listeView
     * *********/
    private void displayListViewPartenaires() {

        System.out.println(" Partenaires liste : " + partenaireList.size());
        //create an ArrayAdaptar from the String Array
        dataAdapter3 = new MyCustomAdapterPartenaires(this, R.layout.listviewcheckbox, partenaireList);
        // Assign adapter to ListView
        listviewPartenaires.setAdapter(dataAdapter3);

        listviewPartenaires.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Partenaire partenaire = (Partenaire) parent.getItemAtPosition(position);
                /*Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + partenaire.getNom(),
                        Toast.LENGTH_LONG).show();*/
            }
        });

        inputSearchPartenaires.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                //Preinscription.this.dataAdapter2.getFilter().filter(cs);
                System.out.println("cs " + cs + " arg1 " + arg1 + " arg2 " + arg2 + " arg3 " + arg3);

                partenaireList.clear();
                getListeAllPartenairesByFilter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    /********
     * Adapter pour afficher la liste des enfants checkbox
     * *********/
    private class MyCustomAdapter extends ArrayAdapter<Enfant> {

        private ArrayList<Enfant> enfantsListe;
        final ArrayList<Enfant> maListeEnfants = new ArrayList<Enfant>();
        String text;
        Enfant enf;
        Toast toast;
        CheckBox cb;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Enfant> enfantsListe) {
            super(context, textViewResourceId, enfantsListe);
            this.enfantsListe = new ArrayList<Enfant>();
            this.enfantsListe.addAll(enfantsListe);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));
            maListeEnfants.clear();

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.listviewcheckbox, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                final ViewHolder finalHolder = holder;
                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        cb = (CheckBox) v;
                        enf = (Enfant) cb.getTag();
                        enf.setSelected(cb.isChecked());
                        maListeEnfants.add(enf);
                        for (int i = 0; i < maListeEnfants.size(); i++) {
                            System.out.println("Le contenu de ma liste : " + maListeEnfants.get(i).getNom());
                        }

                        text = spListeTempsColl.getSelectedItem().toString();
                        System.out.println("TC TEST " + text);

                        btnReservationEnfant2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (text.equals("Choisir le temps collectif")) {
                                    toast.makeText(Preinscription.this, "Veuillez sélectionner un temps collectif !", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < maListeEnfants.size(); i++) {
                                        System.out.println(" mon enfant " + maListeEnfants.get(i).getCode());
                                        doReservation(choix, "1", maListeEnfants.get(i).getCode(), "1");
                                        maListeEnfants.get(i).setSelected(false);
                                    }
                                    enfantsList.clear();
                                    getListeAllEnfants();
                                    displayListView();
                                    toast.makeText(getApplicationContext(), "Réservation pour cet enfant " + enf.getNom(), Toast.LENGTH_SHORT).show();

                                    myNb3 = 0;
                                    tvNbEnf.setText("Nb enfants selectionnée(s) : " + myNb3);

                                }
                            }
                        });

                        btnResValidEnfantsByAM.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (text.equals("Choisir le temps collectif")) {
                                    toast.makeText(Preinscription.this, "Veuillez sélectionner un temps collectif !", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < maListeEnfants.size(); i++) {
                                        System.out.println(" mon enfant " + maListeEnfants.get(i).getCode());
                                        doReservation(choix, "1", maListeEnfants.get(i).getCode(), "2");
                                        maListeEnfants.get(i).setSelected(false);
                                    }
                                    enfantsList.clear();
                                    getListeAllEnfants();
                                    displayListView();
                                    toast.makeText(getApplicationContext(), "Réservation pour cet enfant " + enf.getNom(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        // Pour les pré-inscriptions avec validation en même temps
                        btnResValidEnfants.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (text.equals("Choisir le temps collectif")) {
                                    toast.makeText(Preinscription.this, "Veuillez sélectionner un temps collectif !", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < maListeEnfants.size(); i++) {
                                        System.out.println(" mon enfant " + maListeEnfants.get(i).getCode());
                                        doReservation(choix, "1", maListeEnfants.get(i).getCode(), "2");
                                        maListeEnfants.get(i).setSelected(false);
                                    }
                                    enfantsList.clear();
                                    getListeAllEnfants();
                                    displayListView();
                                    toast.makeText(getApplicationContext(), "Réservation pour cet enfant " + enf.getNom(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        btnReservationEnfantParent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (text.equals("Choisir le temps collectif")) {
                                    toast.makeText(Preinscription.this, "Veuillez sélectionner un temps collectif !", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < maListeEnfants.size(); i++) {
                                        System.out.println(" mon enfant " + maListeEnfants.get(i).getCode());
                                        doReservation(choix, "1", maListeEnfants.get(i).getCode(), "1");
                                        maListeEnfants.get(i).setSelected(false);
                                    }
                                    enfantsList.clear();
                                    getListeAllEnfants();
                                    displayListView();
                                    toast.makeText(getApplicationContext(), "Réservation pour cet enfant " + enf.getNom(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        btnEnfantPreinscrireEtValider.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (text.equals("Choisir le temps collectif")) {
                                    toast.makeText(Preinscription.this, "Veuillez sélectionner un temps collectif !", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < maListeEnfants.size(); i++) {
                                        System.out.println(" mon enfant " + maListeEnfants.get(i).getCode());
                                        doReservation(choix, "1", maListeEnfants.get(i).getCode(), "2");
                                        maListeEnfants.get(i).setSelected(false);
                                    }
                                    enfantsList.clear();
                                    getListeAllEnfants();
                                    displayListView();
                                    toast.makeText(getApplicationContext(), "Réservation pour cet enfant " + enf.getNom(), Toast.LENGTH_SHORT).show();

                                    myNb3 = 0;
                                    tvNbEnf.setText("Nb enfants selectionnée(s) : " + myNb3);
                                }
                            }
                        });


                        int nbPlacesDispo = 0;

                        if (!tvNbPlaces.getText().toString().equals("")) {
                            nbPlacesDispo = Integer.parseInt(tvNbPlaces.getText().toString());
                        }

                        if (!text.equals("Choisir le temps collectif")) {
                            if (enf.isSelected()) {
                                myNb3 = myNb3 + 1;
                            } else {
                                if (myNb3 > 0) {
                                    myNb3 = myNb3 - 1;
                                } else {
                                    myNb3 = 0;
                                }
                            }
                        }

                        if (nbPlacesDispo > 0) {
                            tvNbEnf.setText("Nb enfants selectionnée(s) : " + myNb3);
                        }

                        nb = 0;
                        if (enf.isSelected()) {
                            if (!text.equals("Choisir le temps collectif")) {
                                newNumber = nbPlacesDispo - nb;
                                tvNbPlaces.setText(String.valueOf(newNumber));

                                if (nbPlacesDispo <= 0) {
                                    finalHolder.name.setChecked(false);
                                    toast.makeText(Preinscription.this, "Plus de places !", Toast.LENGTH_SHORT).show();
                                } else {
                                    nb = nb + 1;
                                    newNumber = nbPlacesDispo - nb;
                                    tvNbPlaces.setText(String.valueOf(newNumber));
                                }
                            } else {
                                finalHolder.name.setChecked(false);
                                toast.makeText(Preinscription.this, "Veuillez choisir un temps collectif !", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            nb = nb + 1;
                            newNumber = nbPlacesDispo + nb;
                            tvNbPlaces.setText(String.valueOf(newNumber));
                        }
                    }
                });

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            enf = enfantsListe.get(position);
            holder.name.setText(enf.getNom() + " " + enf.getPrenom());
            holder.name.setChecked(enf.isSelected());
            holder.name.setTag(enf);

            return convertView;
        }
    }

    /********
     * Adapter pour afficher la liste des enfants des assmats sélectionnées checkbox
     * *********/
    private class MyCustomAdapterEnfByAM extends ArrayAdapter<Enfant> {

        private ArrayList<Enfant> enfantsListe;
        final ArrayList<Enfant> maListeEnfants = new ArrayList<Enfant>();
        String text;
        Enfant enf;
        Toast toast;
        CheckBox cb;

        public MyCustomAdapterEnfByAM(Context context, int textViewResourceId,
                                      ArrayList<Enfant> enfantsListe) {
            super(context, textViewResourceId, enfantsListe);
            this.enfantsListe = new ArrayList<Enfant>();
            this.enfantsListe.addAll(enfantsListe);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));
            maListeEnfants.clear();

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.listviewcheckbox, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                final ViewHolder finalHolder = holder;
                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        cb = (CheckBox) v;
                        enf = (Enfant) cb.getTag();
                        enf.setSelected(cb.isChecked());
                        maListeEnfants.add(enf);
                        for (int i = 0; i < maListeEnfants.size(); i++) {
                            System.out.println("Le contenu de ma liste : " + maListeEnfants.get(i).getNom());
                        }

                        text = spListeTempsColl.getSelectedItem().toString();
                        System.out.println("TC TEST " + text);

                        btnReservationEnfant.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (text.equals("Choisir le temps collectif")) {
                                    toast.makeText(Preinscription.this, "Veuillez sélectionner un temps collectif !", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < maListeEnfants.size(); i++) {
                                        System.out.println(" mon enfant " + maListeEnfants.get(i).getCode());
                                        doReservation(choix, "1", maListeEnfants.get(i).getCode(), "1");
                                        maListeEnfants.get(i).setSelected(false);
                                    }
                                    enfantsByAMList.clear();
                                    displayListViewEnfByAM();
                                    tvEnfByAm.setText("Veuillez sélectionner une Assmat pour afficher ses enfants");
                                    toast.makeText(getApplicationContext(), "Réservation pour cet enfant " + enf.getNom(), Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(getApplicationContext(), "Nombre d'enfant(s) : " + maListeEnfants.size(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        int nbPlacesDispo = 0;

                        if (!tvNbPlaces.getText().toString().equals("")) {
                            nbPlacesDispo = Integer.parseInt(tvNbPlaces.getText().toString());
                        }

                        if (!text.equals("Choisir le temps collectif")) {
                            if (enf.isSelected()) {
                                myNb5 = myNb5 + 1;
                            } else {
                                if (myNb5 > 0) {
                                    myNb5 = myNb5 - 1;
                                } else {
                                    myNb5 = 0;
                                }
                            }
                        }

                        nb = 0;
                        if (enf.isSelected()) {
                            if (!text.equals("Choisir le temps collectif")) {
                                newNumber = nbPlacesDispo - nb;
                                tvNbPlaces.setText(String.valueOf(newNumber));

                                if (nbPlacesDispo <= 0) {
                                    finalHolder.name.setChecked(false);
                                    toast.makeText(Preinscription.this, "Plus de places !", Toast.LENGTH_SHORT).show();
                                } else {
                                    nb = nb + 1;
                                    newNumber = nbPlacesDispo - nb;
                                    tvNbPlaces.setText(String.valueOf(newNumber));
                                }
                            } else {
                                finalHolder.name.setChecked(false);
                                toast.makeText(Preinscription.this, "Veuillez choisir un temps collectif !", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            nb = nb + 1;
                            newNumber = nbPlacesDispo + nb;
                            tvNbPlaces.setText(String.valueOf(newNumber));
                        }
                    }
                });

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            enf = enfantsListe.get(position);
            holder.name.setText(enf.getNom() + " " + enf.getPrenom());
            holder.name.setChecked(enf.isSelected());
            holder.name.setTag(enf);

            return convertView;
        }
    }

    /********
     * Adapter pour afficher la liste des parents checkbox
     * *********/
    private class MyCustomAdapterForParents extends ArrayAdapter<Parent> {

        private ArrayList<Parent> parentsListe;
        final ArrayList<Parent> maListeParents = new ArrayList<Parent>();
        String text;
        CheckBox cb;
        Parent par;
        LayoutInflater vi;
        Toast toast;
        ViewHolder holder = null;
        ViewHolder finalHolder;
        int nbPlacesDispo = 0;

        public MyCustomAdapterForParents(Context context, int textViewResourceId,
                                         ArrayList<Parent> parentsListe) {
            super(context, textViewResourceId, parentsListe);
            this.parentsListe = new ArrayList<Parent>();
            this.parentsListe.addAll(parentsListe);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {

            Log.v("ConvertView", String.valueOf(position));
            maListeParents.clear();

            if (convertView == null) {
                vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.listviewcheckbox, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                finalHolder = holder;
                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        cb = (CheckBox) v;
                        par = (Parent) cb.getTag();
                        par.setSelected(cb.isChecked());
                        maListeParents.add(par);

                        text = spListeTempsColl.getSelectedItem().toString();
                        System.out.println("TC TEST " + text);

                        if (!tvNbPlaces.getText().toString().equals("")) {
                            nbPlacesDispo = Integer.parseInt(tvNbPlaces.getText().toString());
                        }

                        if (!text.equals("Choisir le temps collectif")) {
                            if (par.isSelected()) {
                                myNb4 = myNb4 + 1;
                            } else {
                                if (myNb4 > 0) {
                                    myNb4 = myNb4 - 1;
                                } else {
                                    myNb4 = 0;
                                }
                            }
                        }

                        if (nbPlacesDispo > 0) {
                            tvNbParents.setText("Nb parent(s) selectionnée(s) : " + myNb4);
                        }

                        btnReservationParent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (text.equals("Choisir le temps collectif")) {
                                    toast.makeText(Preinscription.this, "Veuillez sélectionner un temps collectif !", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < maListeParents.size(); i++) {
                                        System.out.println(" mon enfant " + maListeParents.get(i).getCode());
                                        doReservation(choix, "4", maListeParents.get(i).getCode(), "1");
                                        maListeParents.get(i).setSelected(false);
                                    }
                                    parentsList.clear();
                                    getListeAllParents();
                                    displayListViewParents();
                                    toast.makeText(getApplicationContext(), "Réservation pour cette famille " + par.getNom(), Toast.LENGTH_SHORT).show();

                                    myNb4 = 0;
                                    tvNbParents.setText("Nb parent(s) selectionnée(s) : " + myNb4);
                                }
                            }
                        });

                        btnParentPreinscrireEtValider.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (text.equals("Choisir le temps collectif")) {
                                    toast.makeText(Preinscription.this, "Veuillez sélectionner un temps collectif !", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < maListeParents.size(); i++) {
                                        System.out.println(" mon enfant " + maListeParents.get(i).getCode());
                                        doReservation(choix, "4", maListeParents.get(i).getCode(), "2");
                                        maListeParents.get(i).setSelected(false);
                                    }
                                    parentsList.clear();
                                    getListeAllParents();
                                    displayListViewParents();
                                    toast.makeText(getApplicationContext(), "Réservation pour cette famille " + par.getNom(), Toast.LENGTH_SHORT).show();

                                    myNb4 = 0;
                                    tvNbParents.setText("Nb parent(s) selectionnée(s) : " + myNb4);
                                }
                            }
                        });


                        nb = 0;
                        if (par.isSelected()) {
                            if (!text.equals("Choisir le temps collectif")) {
                                getEnfByParent(par.getCode());
                                newNumber = nbPlacesDispo - nb;
                                tvNbPlaces.setText(String.valueOf(newNumber));

                                if (nbPlacesDispo <= 0) {
                                    finalHolder.name.setChecked(false);
                                    toast.makeText(Preinscription.this, "Plus de places !", Toast.LENGTH_SHORT).show();
                                } else {
                                    nb = nb + 1;
                                    newNumber = nbPlacesDispo - nb;
                                    tvNbPlaces.setText(String.valueOf(newNumber));
                                }
                            } else {
                                finalHolder.name.setChecked(false);
                                toast.makeText(Preinscription.this, "Veuillez choisir un temps collectif !", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            nb = nb + 1;
                            newNumber = nbPlacesDispo + nb;
                            tvNbPlaces.setText(String.valueOf(newNumber));
                        }
                    }
                });

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            par = parentsListe.get(position);
            holder.name.setText(par.getNom() + " " + par.getPrenom());
            holder.name.setChecked(par.isSelected());
            holder.name.setTag(par);

            return convertView;
        }
    }

    /********
     * Adapter pour afficher la liste des Assmats checkbox
     * *********/
    private class MyCustomAdapterAsmats extends ArrayAdapter<Asmat> {

        private ArrayList<Asmat> asmatsListe;
        private ArrayList<Enfant> enfantsListe;
        final ArrayList<Asmat> maListeAsmats = new ArrayList<Asmat>();
        final ArrayList<Enfant> maListeEnfants = new ArrayList<Enfant>();
        String text;
        CheckBox cb;
        Asmat asmat;
        String[] temp;
        Toast toast;
        LayoutInflater vi;
        ViewHolder finalHolder;
        int nbPlacesDispo = 0;

        public MyCustomAdapterAsmats(Context context, int textViewResourceId,
                                     ArrayList<Asmat> asmatsList, ArrayList<Enfant> enfantsList) {
            super(context, textViewResourceId, asmatsList);
            this.asmatsListe = new ArrayList<Asmat>();
            this.asmatsListe.addAll(asmatsList);
            this.enfantsListe = new ArrayList<Enfant>();
            this.enfantsListe.addAll(enfantsList);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.listviewcheckbox, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                nb = 0;

                finalHolder = holder;
                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        cb = (CheckBox) v;
                        asmat = (Asmat) cb.getTag();
                        temp = cb.getText().toString().split("|");
                        asmat.setSelected(cb.isChecked());
                        maListeAsmats.add(asmat);

                        text = spListeTempsColl.getSelectedItem().toString();
                        System.out.println("TC TEST " + text);

                        nb = 0;

                        if (!tvNbPlaces.getText().toString().equals("")) {
                            nbPlacesDispo = Integer.parseInt(tvNbPlaces.getText().toString());
                        }

                        if (asmat.isSelected()) {
                            if (!text.equals("Choisir le temps collectif")) {
                                getEnfByAM(asmat.getId());
                                System.out.println("Ma liste enfant by am ");
                                tvNbPlaces.setText(String.valueOf(newNumber));

                                if (nbPlacesDispo <= 0) {
                                    finalHolder.name.setChecked(false);
                                    toast.makeText(Preinscription.this, "Plus de places !", Toast.LENGTH_SHORT).show();
                                } else {
                                    nb = nb + 1;
                                    newNumber = nbPlacesDispo - nb;
                                    tvNbPlaces.setText(String.valueOf(newNumber));
                                }

                            } else {
                                finalHolder.name.setChecked(false);
                                toast.makeText(Preinscription.this, "Veuillez choisir un temps collectif !", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            listEnfByAM.clear();
                            enfantsByAMList.clear();

                            nb = nb + 1;
                            newNumber = nbPlacesDispo + nb;
                            tvNbPlaces.setText(String.valueOf(newNumber));

                        }

                        if (!text.equals("Choisir le temps collectif")) {
                            if (asmat.isSelected()) {
                                myNb = myNb + 1;
                            } else {
                                if (myNb > 0) {
                                    myNb = myNb - 1;
                                } else {
                                    myNb = 0;
                                }
                            }
                        }
                        if (nbPlacesDispo > 0) {
                            tnNbAM.setText("Nb am selectionnée(s) : " + myNb);
                        }


                        btnReservationAsmat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (text.equals("Choisir le temps collectif")) {
                                    toast.makeText(Preinscription.this, "Veuillez sélectionner un temps collectif !", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < maListeAsmats.size(); i++) {
                                        doReservation(choix, "2", maListeAsmats.get(i).getId(), "1");
                                        toast.makeText(getApplicationContext(), "Réservation pour cette Asmat " + maListeAsmats.get(i).getNom(), Toast.LENGTH_SHORT).show();
                                    }

                                    myNb = 0;

                                    tnNbAM.setText("Nb am selectionnée(s) : " + myNb);

                                    asmatsList.clear();
                                    getListeAllAssmats();
                                    displayListViewAsmats();
                                }
                            }
                        });

                        btnResValidAM.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (text.equals("Choisir le temps collectif")) {
                                    toast.makeText(Preinscription.this, "Veuillez sélectionner un temps collectif !", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < maListeAsmats.size(); i++) {
                                        doReservation(choix, "2", maListeAsmats.get(i).getId(), "2");
                                        toast.makeText(getApplicationContext(), "Réservation pour cette Asmat " + maListeAsmats.get(i).getNom(), Toast.LENGTH_SHORT).show();
                                    }

                                    asmatsList.clear();
                                    getListeAllAssmats();
                                    displayListViewAsmats();

                                    myNb = 0;

                                    tnNbAM.setText("Nb am selectionnée(s) : " + myNb);
                                }
                            }
                        });

                        btnReservationEnfant.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (text.equals("Choisir le temps collectif")) {
                                    toast.makeText(Preinscription.this, "Veuillez sélectionner un temps collectif !", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < maListeEnfants.size(); i++) {
                                        System.out.println(" mon enfant " + maListeEnfants.get(i).getCode());
                                        doReservation(choix, "1", maListeEnfants.get(i).getCode(), "1");
                                        maListeEnfants.get(i).setSelected(false);
                                    }
                                    enfantsList.clear();
                                    getListeAllEnfants();
                                    displayListView();
                                }
                            }
                        });
                    }
                });

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            asmat = asmatsListe.get(position);
            holder.name.setText(asmat.getNom() + " " + asmat.getPrenom());
            holder.name.setChecked(asmat.isSelected());
            holder.name.setTag(asmat);

            return convertView;
        }
    }

    /********** Classe permettant de récupérer les partenaires en checkbox ************/
    private class MyCustomAdapterPartenaires extends ArrayAdapter<Partenaire> {

        private ArrayList<Partenaire> partenairesListe = new ArrayList<Partenaire>(0);
        final ArrayList<Partenaire> maListePartenaire = new ArrayList<Partenaire>();
        String text;
        Toast toast;
        Partenaire partenaire;
        String[] temp;
        CheckBox cb;
        LayoutInflater vi;

        public MyCustomAdapterPartenaires(Context context, int textViewResourceId,
                                          ArrayList<Partenaire> partenaireList) {
            super(context, textViewResourceId, partenaireList);
            this.partenairesListe = new ArrayList<Partenaire>();
            this.partenairesListe.addAll(partenaireList);
        }

        private class ViewHolder {
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {

            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.listviewcheckbox, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                nb = 0;

                finalHolder = holder;
                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        cb = (CheckBox) v;
                        partenaire = (Partenaire) cb.getTag();
                        temp = cb.getText().toString().split("|");
                        partenaire.setSelected(cb.isChecked());

                        maListePartenaire.add(partenaire);

                        text = spListeTempsColl.getSelectedItem().toString();
                        System.out.println("TC TEST " + text);

                        nbPlacesDispo = 0;
                        nb = 0;

                        if (!tvNbPlaces.getText().equals("")) {
                            nbPlacesDispo = Integer.parseInt(tvNbPlaces.getText().toString());
                        }

                        if (!text.equals("Choisir le temps collectif")) {
                            if (partenaire.isSelected()) {
                                myNb2 = myNb2 + 1;
                            } else {
                                if (myNb2 > 0) {
                                    myNb2 = myNb2 - 1;
                                } else {
                                    myNb2 = 0;
                                }
                            }
                        }

                        if (nbPlacesDispo > 0) {
                            tvNbPart.setText("Nb partenaire(s) selectionnée(s) : " + myNb2);
                        }

                        if (partenaire.isSelected()) {
                            if (!text.equals("Choisir le temps collectif")) {
                                newNumber = nbPlacesDispo - nb;
                                tvNbPlaces.setText(String.valueOf(newNumber));

                                if (nbPlacesDispo <= 0) {
                                    finalHolder.name.setChecked(false);
                                    toast.makeText(Preinscription.this, "Plus de places !", Toast.LENGTH_SHORT).show();
                                } else {
                                    nb = nb + 1;
                                    newNumber = nbPlacesDispo - nb;
                                    tvNbPlaces.setText(String.valueOf(newNumber));
                                }
                            } else {
                                finalHolder.name.setChecked(false);
                                toast.makeText(Preinscription.this, "Veuillez choisir un temps collectif !", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            nb = nb + 1;
                            newNumber = nbPlacesDispo + nb;
                            tvNbPlaces.setText(String.valueOf(newNumber));
                        }

                        btnReservationPartenaire.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (text.equals("Choisir le temps collectif")) {
                                    toast.makeText(Preinscription.this, "Veuillez choisir un temps collectif !", Toast.LENGTH_SHORT).show();
                                } else {
                                    for (int i = 0; i < maListePartenaire.size(); i++) {
                                        doReservation(choix, "3", maListePartenaire.get(i).getId(), "1");
                                    }
                                    partenaireList.clear();
                                    getListeAllPartenaires();
                                    displayListViewPartenaires();

                                    myNb2 = 0;
                                    tvNbPart.setText("Nb partenaire(s) selectionnée(s) : " + myNb2);

                                    toast.makeText(getApplicationContext(), "Réservation pour ce Partenaire", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            partenaire = partenairesListe.get(position);
            holder.name.setText(partenaire.getNom() + " " + partenaire.getPrenom());
            holder.name.setChecked(partenaire.isSelected());
            holder.name.setTag(partenaire);

            return convertView;
        }
    }

    /*******
     * Récupération liste des assmats
     ********/
    private void getEnfByAM(final String idAM) {
        tvEnfByAm.setText("");
        // Récupération des enfants dans une listview
        requete_enf_by_am = new StringRequest(Request.Method.POST, url_enf_by_am, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    json = new JSONObject(response);
                    adherents = json.getJSONArray("enf");

                    System.out.println("test = " + adherents.length());

                    if (adherents.length() == 0) {
                        tvEnfByAm.setText("Pas d'enfant rattaché à cette assistante maternelle");
                    } else {
                        for (int i = 0; i < adherents.length(); i++) {
                            enfant = adherents.getJSONObject(i);
                            id_enfant = enfant.getString("id");
                            nom_enfant = enfant.getString("nom");
                            prenom_enfant = enfant.getString("prenom");

                            Enfant enf = new Enfant(id_enfant, nom_enfant, prenom_enfant, "", false);
                            listEnfByAM.add(enf);
                            enfantsByAMList.add(enf);

                            System.out.println(" Liste des enfants by am : " + enf.getNom() + " " + enf.getPrenom());
                        }
                        tvEnfByAm.setText("Liste des enfants");

                        displayListViewEnfByAM();
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
                params6.put("idAM", idAM);
                params6.put("donnees", choixDB);
                Log.e("Envoi params Reserv", params6.toString());
                return params6;
            }
        };
        requestQueue.add(requete_enf_by_am);
    }
}