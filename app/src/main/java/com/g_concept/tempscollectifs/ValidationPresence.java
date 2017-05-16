package com.g_concept.tempscollectifs;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.RelativeLayout;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ValidationPresence extends AppCompatActivity implements View.OnClickListener {
    int action, nb;
    Spinner spListeTempsColl;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    ArrayList<String> listeTempsColl = new ArrayList<String>();
    ArrayList<Reservation> tableauReservations = new ArrayList<Reservation>();
    JSONObject json;
    ImageView imgPers;
    EditText etAM, etEnf0a3, etEnf3a6, etEnfplus6, etParents, etAutre, etGardeDomicile;
    TextView tvNbPersonnesCochees;
    Button btnValidationPersonne, btnValidationSaisieQuantitative, btnCocher;
    MyCustomAdapterPersonne dataAdapter4 = null;
    Map<String, String> params = new HashMap<String, String>(), params6 = new HashMap<String, String>();
    HashMap<Integer, Integer> myHash = new HashMap<Integer, Integer>();
    Context context;
    ListView lvPers;
    RequestQueue requestQueue;
    ArrayList<String> numEtId = new ArrayList<>();
    ArrayList<EnfantBis> listeEnfants = new ArrayList<>();
    ArrayList<AsmatBis> listeAM = new ArrayList<>();
    ArrayList<PartenaireBis> listePart = new ArrayList<>();
    ArrayList<PersonneBis> listePers = new ArrayList<>();
    ArrayList<ParentBis> parentsList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Intent intent;
    String amQuant, enf0a3Quant, enf3a6Quant, enfPlus6, parentQuant, autreQuant, gardeDomicileQuant, idEnf, nomEnf, prenomEnf,
            id_parent, nom_parent, prenom_parent, idPart, nomPart, prenomPart, temp, idAM, nomAM, prenomAM, idPreinscrit,
            nomPreinscrit, prenomPreinscrit, EXTRA_ID_USER = "idUser", idUser, EXTRA_DB = "donnees", initTC = "Choisir le temps collectif",
            myValue, lieuTempsColl, horaire, idTempsColl, choix, nbPlaces, categorie, nomTempsColl, dateTempsColl,
            url_obtenir_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/getTempsCollectifs.php",
            url_enf_by_tc = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/getEnfantsByIdTC.php",
            url_get_parents_by_tc = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/getAllParentsByIdTC.php",
            url_asmats_by_tc = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/getAsmatsByIdTC.php",
            url_personnes_by_tc = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/getPersonnesByTC.php",
            url_partenaires_by_tc = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/getPartenairesByIdTC.php",
            url_valider_presences_saisie_quantitative = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/doValidationQuantitative.php",
            url_valider_presences = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/validerPresenceEnfants.php",
            url_delete_by_tc = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/deleteReservation.php",
            url_places_by_tc = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/getNbPlacesRestantes.php",
            choixDB;
    StringRequest requete_parents, requete_delete_by_tc, requete_places_by_tc, requete_temps_coll, requete_enfants_by_tc, requete_validation_presence, requete_asmats_by_tc, requete_personnes_by_tc, requete_partenaires_by_tc, requete_validation_saisie_quantitative;
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
        btnValidationSaisieQuantitative = (Button) findViewById(R.id.btnValidationSaisieQuantitative);
        etAM = (EditText) findViewById(R.id.nbAsmat);
        etEnf0a3 = (EditText) findViewById(R.id.nbEnf0a3);
        etEnf3a6 = (EditText) findViewById(R.id.nbEnf3a6);
        etEnfplus6 = (EditText) findViewById(R.id.nbEnfplus6);
        etParents = (EditText) findViewById(R.id.nbParent);
        etAutre = (EditText) findViewById(R.id.nbAutre);
        etGardeDomicile = (EditText) findViewById(R.id.nbGardeDomicile);
        btnValidationPersonne = (Button) findViewById(R.id.btnValidationPersonne);
        lvPers = (ListView) findViewById(R.id.lvPers);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        imgPers = (ImageView) findViewById(R.id.imgPers);
        btnCocher = (Button) findViewById(R.id.btnCocher);

        tvNbPersonnesCochees = (TextView)findViewById(R.id.tvNbPersonnesCochees);

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

        listeTempsColl.add(initTC);

        if (isOnline()) {
            getTempsCollectifs("1", "", "date ASC");
        }

        //Le Spinner a besoin d'un adapter pour sa presentation
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listeTempsColl);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Enfin on passe l'adapter au Spinner et c'est tout
        spListeTempsColl.setAdapter(adapter);

        fabbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabActivity tabs = (TabActivity) getParent();
                tabs.getTabHost().setCurrentTab(0);
            }
        });
    }

    @Override
    public void onClick(final View v) {

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

                        listeTempsColl.add(idTempsColl + " " + nomTempsColl + " • le " + dateTempsColl + " • de " + horaire);
                        System.out.println(numEtId + " Les infos : " + nomTempsColl + " " + "date " + dateTempsColl + " et n° :" + i);

                        myHash.put(i, Integer.parseInt(idTempsColl));
                    }

                    //-- gestion du Click sur la liste Région
                    spListeTempsColl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   final int position, long id) {
                            temp = String.valueOf(spListeTempsColl.getSelectedItem());

                            System.out.println(" TEST DU HASHMAP " + myHash.get(position - 1));
                            System.out.println(" Mon temp " + temp);

                            if (temp == initTC) {
                                listePers.clear();
                                listeEnfants.clear();
                                listeAM.clear();
                                listePart.clear();
                                parentsList.clear();
                            } else {
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

                        ParentBis parent = new ParentBis(id_parent, nom_parent, prenom_parent, false, idTC);
                        parentsList.add(parent);

                        PersonneBis pers = new PersonneBis(id_parent, nom_parent, prenom_parent, "", false, "4", idTempsColl);
                        listePers.add(pers);


                        System.out.println("Liste Personnes with enf and am and part and parents : " + listePers.size());

                        System.out.println("Mon parent : " + parent.getNom());
                    }

                    displayListView("", "");
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

    public void doSaisieQuantitative(final String idTempsColl) {
        amQuant = etAM.getText().toString();
        enf0a3Quant = etEnf0a3.getText().toString();
        enf3a6Quant = etEnf3a6.getText().toString();
        enfPlus6 = etEnfplus6.getText().toString();
        parentQuant = etParents.getText().toString();
        autreQuant = etAutre.getText().toString();
        gardeDomicileQuant = etGardeDomicile.getText().toString();

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
            final int total = Integer.parseInt(amQuant) + Integer.parseInt(enf0a3Quant) + Integer.parseInt(enf3a6Quant) + Integer.parseInt(enfPlus6) + Integer.parseInt(parentQuant) + Integer.parseInt(autreQuant) + Integer.parseInt(gardeDomicileQuant);
            final ArrayList<Integer> tabNb = new ArrayList<Integer>();
            tabNb.add(Integer.parseInt(amQuant));
            tabNb.add(Integer.parseInt(enf0a3Quant));
            tabNb.add(Integer.parseInt(enf3a6Quant));
            tabNb.add(Integer.parseInt(enfPlus6));
            tabNb.add(Integer.parseInt(parentQuant));
            tabNb.add(Integer.parseInt(autreQuant));
            tabNb.add(Integer.parseInt(gardeDomicileQuant));

            //Toast.makeText(ValidationPresence.this, String.valueOf(total), Toast.LENGTH_SHORT).show();
            for (int i = 0; i < total; i++) {
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
                        params.put("total", String.valueOf(total));
                        Log.e("Do saisie quantitative", params.toString());
                        return params;
                    }
                };
                requestQueue.add(requete_validation_saisie_quantitative);
            }
        }
    }

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

    private void displayListView(String choice, final String idTempsColl) {

        System.out.println(" On est dans la vague 4");
        System.out.println(" Avant l'appel ");
        //create an ArrayAdaptar from the String Array
        dataAdapter4 = new ValidationPresence.MyCustomAdapterPersonne(this, R.layout.listviewcheckbox, listePers);

        // Assign adapter to ListView
        lvPers.setAdapter(dataAdapter4);

        System.out.println(" Après l'appel ");

        lvPers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                PersonneBis pers = (PersonneBis) parent.getItemAtPosition(position);
            }
        });
    }

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

                        PersonneBis pers = new PersonneBis(idEnf, nomEnf, prenomEnf, "", false, "1", idTempsColl);
                        listePers.add(pers);

                        System.out.println("Taille de la liste asmats " + listeAM.size());
                    }

                    for (int i = 0; i < lesTempsColl2.length(); i++) {
                        leTempsColl2 = lesTempsColl2.getJSONObject(i);
                        idAM = leTempsColl2.getString("id");
                        nomAM = leTempsColl2.getString("nom_naissance");
                        prenomAM = leTempsColl2.getString("prenom_naissance");

                        PersonneBis pers = new PersonneBis(idAM, nomAM, prenomAM, "", false, "2", idTempsColl);
                        listePers.add(pers);

                        System.out.println("Taille de la liste asmats " + listeAM.size());
                    }

                    for (int i = 0; i < lesTempsColl3.length(); i++) {
                        leTempsColl3 = lesTempsColl3.getJSONObject(i);
                        idPart = leTempsColl3.getString("id");
                        nomPart = leTempsColl3.getString("nom");
                        prenomPart = leTempsColl3.getString("prenom");

                        PersonneBis pers = new PersonneBis(idPart, nomPart, prenomPart, "", false, "3", idTempsColl);
                        listePers.add(pers);

                        System.out.println("Taille de la liste asmats " + listeAM.size());
                    }

                    for (int i = 0; i < lesTempsColl4.length(); i++) {
                        leTempsColl4 = lesTempsColl4.getJSONObject(i);
                        id_parent = leTempsColl4.getString("id");
                        nom_parent = leTempsColl4.getString("nom");
                        prenom_parent = leTempsColl4.getString("prenom");

                        PersonneBis pers = new PersonneBis(id_parent, nom_parent, prenom_parent, "", false, "4", idTempsColl);
                        listePers.add(pers);

                        System.out.println("Mon parent " + nom_parent);

                    }

                    displayListView("", "");
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

    /*********
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

    private class MyCustomAdapterPersonne extends ArrayAdapter<PersonneBis> {

        private ArrayList<PersonneBis> personnesListe;
        final ArrayList<PersonneBis> maListePersonnes = new ArrayList<PersonneBis>();

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

            System.out.println(" La liste de personnes " + personnesListe);

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.listviewcheckbox, null);

                holder = new ValidationPresence.MyCustomAdapterPersonne.ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                holder.img = (ImageView) convertView.findViewById(R.id.imgPers);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        final PersonneBis pers = (PersonneBis) cb.getTag();

                        pers.setSelected(cb.isChecked());
                        maListePersonnes.add(pers);

                        System.out.println(" PERSONNE LISTE1 " + personnesListe);
                        System.out.println(" PERSONNE LISTE2 " + maListePersonnes);

                        for (int i = 0; i < maListePersonnes.size(); i++) {
                            System.out.println("Ma personne " + i + " est " + maListePersonnes.get(i).getNom() + " " + maListePersonnes.get(i).getPrenom());
                        }

                        btnValidationPersonne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                String text = spListeTempsColl.getSelectedItem().toString();
                                System.out.println("TC TEST " + text);
                                Toast.makeText(ValidationPresence.this, text, Toast.LENGTH_SHORT).show();

                                if (text.equals(initTC)) {
                                    Toast.makeText(ValidationPresence.this, "Veuillez sélectionner un temps collectif !", Toast.LENGTH_SHORT).show();
                                } else {
                                    int i;
                                    for (i = 0; i < maListePersonnes.size(); i++) {
                                        System.out.println(" ma personne " + maListePersonnes.get(i).getCode() + " " + maListePersonnes.get(i).getIdTempsColl());
                                        validerPresence(maListePersonnes.get(i).getCode(), maListePersonnes.get(i).getIdTempsColl(), maListePersonnes.get(i).getTypePersonne());
                                        maListePersonnes.get(i).setSelected(false);
                                    }

                                    //doSaisieQuantitative(maListePersonnes.get(i).getIdTempsColl());

                                    // Permet de switcher de tab quand on clique sur le bouton de validation de présence
                                    TabActivity tabs = (TabActivity) getParent();
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

                        tvNbPersonnesCochees.setText("Nombre de personne(s) sélectionnée(s) : "+ nb);
                    }

                });

            } else {
                holder = (ValidationPresence.MyCustomAdapterPersonne.ViewHolder) convertView.getTag();
            }

            final PersonneBis pers = personnesListe.get(position);
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

            holder.name.setChecked(pers.isSelected());
            holder.name.setTag(pers);

            return convertView;
        }
    }

    /********
     * Permet de vérifier si notre appareil est connecté à un réseau internet
     *********/
    public boolean isOnline() {
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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

                        PartenaireBis part = new PartenaireBis(idPart, nomPart, prenomPart, false, idTempsColl);
                        listePart.add(part);

                        PersonneBis pers = new PersonneBis(idPart, nomPart, prenomPart, "", false, "3", idTempsColl);
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

}