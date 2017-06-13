package com.g_concept.tempscollectifs.VuesPrincipales;

import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.g_concept.tempscollectifs.ClassesMetiers.Network;
import com.g_concept.tempscollectifs.Fonctionnalites.InfoBulle;
import com.g_concept.tempscollectifs.R;
import com.g_concept.tempscollectifs.ClassesMetiers.Reservation;
import com.g_concept.tempscollectifs.Fonctionnalites.UpdateTC;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Infos extends AppCompatActivity {

    ArrayList<String> numEtId = new ArrayList<>();
    ConnectivityManager cm;
    Network myNetwork;
    NetworkInfo netInfo;
    Reservation myReservation;
    Map<Integer, Integer> myMap = new HashMap<Integer, Integer>();
    Spinner spLieux, spRAM;
    TextView tv;
    int number;
    Toast toast;
    Reservation maReservation;
    AlertDialog.Builder adb;
    TableLayout.LayoutParams tableRowParams;
    Button btnInfos, floatingActionButton;
    ArrayAdapter<String> adapter;
    int tab, res, j, i = 0;
    // Strings to Show In Dialog with Radio Buttons
    final CharSequence[] items = {"Modifier le Temps Collectif", " Supprimer le Temps Collectif"};
    String var, horaire, initLieu = "Choisir un lieu", initRAM = "Choisir un RAM", idTempsColl, choix, nbPlaces, dateTempsColl,
            url_delete_tc = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/deleteTC.php",
            url_obtenir_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/getTempsCollectifs.php",
            url_places_by_tc = "https://www.web-familles.fr/AppliTempsCollectifs/ValidationPresence/getNbPlacesRestantes.php";
    ArrayAdapter adapter2;
    JSONObject leTempsColl;
    JSONArray lesTempsColl;
    TableLayout TLtempscoll;
    LinearLayout llContentLieu, llContentRAM;
    ImageView ivDetails;
    TextView tvNom, tvDate;
    Date myDate;
    TabActivity tabs;
    ArrayList<String> liste = new ArrayList<String>(), listeRAM = new ArrayList<String>();
    int temp, nbPlacesDispo;
    TableRow row;
    JSONObject json;
    Intent intent;
    TextView tvIsEmpty;
    String EXTRA_DB = "donnees", titleRAM, nbPlacesEnfant, nbPlacesAdulte, nomTempsColl, choixDB, lieuTempsColl, categorie,
            url_obtenir_lieu_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/getLieuxTempsCollectifs.php",
            url_obtenir_ram = "https://www.web-familles.fr/AppliTempsCollectifs/CreationTempsColl/getRAM.php", choixRAM;
    StringRequest requete_temps_coll, requete_ram, requete_places_by_tc, requete_delete_tc;
    Map<String, String> params = new HashMap<String, String>();
    JSONObject leLieuTempsColl, leRAM;
    JSONArray lesLieuxTempsColl, lesRAM;
    RadioGroup radioGroup;
    RequestQueue requestQueue;
    RadioButton rbLieu, rbRAM;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos);

        tvIsEmpty = (TextView) findViewById(R.id.tvIsEmpty);
        tvNom = (TextView) findViewById(R.id.tvNom);
        tvDate = (TextView) findViewById(R.id.tvDate);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        TLtempscoll = (TableLayout) findViewById(R.id.TLtempsColl);
        btnInfos = (Button) findViewById(R.id.btnInfo);
        rbLieu = (RadioButton) findViewById(R.id.rbLieu);
        rbRAM = (RadioButton) findViewById(R.id.rbRAM);
        radioGroup = (RadioGroup) findViewById(R.id.radioSex);
        llContentLieu = (LinearLayout) findViewById(R.id.llContentLieu);
        llContentRAM = (LinearLayout) findViewById(R.id.llContentRAM);
        floatingActionButton = (Button) findViewById(R.id.fabButton);

        myDate = new Date();
        System.out.println(myDate);
        System.out.println(new SimpleDateFormat("MM-dd-yyyy").format(myDate));
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(myDate));
        System.out.println("La date " + myDate);

        /********
         * On récupère les informations de la page de login
         * ********/
        intent = getIntent();
        choixDB = "";
        choixDB = intent.getStringExtra(EXTRA_DB);
        System.out.println("MA BDD : " + choixDB);

        // On vide la liste
        liste.clear();

        // Si notre appareil est bien connecté à un réseau internet on récupère les lieux et les RAM sinon on affiche un message
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        myNetwork = new Network(cm);

        if (myNetwork.isOnline()) {
            getLieuxTempsCollectifs();
            getRAMCollectifs();
        } else {
            toast.makeText(Infos.this, "Veuillez vérifier votre connexion internet", toast.LENGTH_SHORT).show();
        }

        llContentRAM.setVisibility(View.GONE);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                toast.makeText(Infos.this, "Déconnexion !", toast.LENGTH_SHORT).show();
            }
        });

        rbRAM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llContentRAM.setVisibility(View.VISIBLE);
                llContentLieu.setVisibility(View.GONE);
                spRAM.setSelection(0);
                toast.makeText(getApplicationContext(), "Lieu check", toast.LENGTH_SHORT).show();
            }
        });

        rbLieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llContentRAM.setVisibility(View.GONE);
                llContentLieu.setVisibility(View.VISIBLE);
                spLieux.setSelection(0);
                toast.makeText(getApplicationContext(), "Lieu check", toast.LENGTH_SHORT).show();
            }
        });

        System.out.println("TEST de la liste : " + liste.size());

        //Menu déroulant contenant les lieux
        spLieux = (Spinner) findViewById(R.id.spLieux);

        liste.add(initLieu);

        //Menu déroulant contenant les RAM
        spRAM = (Spinner) findViewById(R.id.spRAM);

        listeRAM.add(initRAM);

        btnInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), InfoBulle.class);
                intent.putExtra("id", "1");
                startActivity(intent);
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
                choixRAM = String.valueOf(spRAM.getSelectedItem());
                TLtempscoll.removeAllViews();
                Log.i("Page Emprunt", "Vous avez appuyé sur :" + choixRAM);
                System.out.println("LINE " + TLtempscoll.getChildCount());
                spLieux.setSelection(0);
                cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                myNetwork = new Network(cm);

                if (myNetwork.isOnline()) {
                    try {
                        liste.clear();
                        i++;
                        if (i % 2 == 0) {
                            getTempsCollectifs("2", "", choixRAM, "nom ASC");
                        } else {
                            getTempsCollectifs("2", "", choixRAM, "nom DESC");
                        }
                    } catch (Exception e) {
                        toast.makeText(getApplicationContext(), "Network error",
                                toast.LENGTH_SHORT).show();
                    }
                } else {
                    toast.makeText(Infos.this, "Veuillez vérifier votre connexion internet", toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

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
                spRAM.setSelection(0);
                cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                myNetwork = new Network(cm);

                if (myNetwork.isOnline()) {
                    getTempsCollectifs("1", choix, "", "date ASC");
                } else {
                    toast.makeText(Infos.this, "Veuillez vérifier votre connexion internet", toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        tvNom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TLtempscoll.removeAllViews();
                i++;
                if (i % 2 == 0) {
                    getTempsCollectifs("1", choix, "", "nom ASC");
                } else {
                    getTempsCollectifs("1", choix, "", "nom DESC");
                }
            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TLtempscoll.removeAllViews();
                i++;
                if (i % 2 == 0) {
                    getTempsCollectifs("1", choix, "", "date ASC");
                } else {
                    getTempsCollectifs("1", choix, "", "date DESC");
                }
            }
        });
    }

    public void deleteTC(final String idTC) {
        requete_delete_tc = new StringRequest(Request.Method.POST, url_delete_tc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la requête delete to modify : " + response);
                if (response.contains("[1]")) {
                    toast.makeText(Infos.this, "Vous venez de supprimer ce temps collectif ! Vous pouvez dès à présent en créer de nouveaux.", toast.LENGTH_LONG).show();
                    // Permet de switcher de tab quand on clique sur le bouton de validation de présence
                    tabs = (TabActivity) getParent();
                    tabs.getTabHost().setCurrentTab(1);
                } else if (response.contains("[2]")) {
                    toast.makeText(Infos.this, "Erreur !", toast.LENGTH_LONG).show();
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
        requestQueue.add(requete_delete_tc);
    }

    /*********
     * Chargement des lieux de temps collectif
     * *********/
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

    /*******
     * Chargement des temps collectifs dans une liste
     * *********/
    public void getTempsCollectifs(final String lieuOuRAM, final String monChoix, final String monRAM, final String choixOrderBy) {
        requete_temps_coll = new StringRequest(Request.Method.POST, url_obtenir_temps_coll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("Voici la réponse à la requète : " + response);
                    json = new JSONObject(response);
                    lesTempsColl = json.getJSONArray("temps_collectifs");

                    if (lesTempsColl.length() == 0) {
                        tvIsEmpty.setText("Il n'y a pas de temps collectif pour ce critère.");
                    } else {
                        tvIsEmpty.setText("");
                    }

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

                        var = idTempsColl + " \\| " + nomTempsColl + " " + dateTempsColl;

                        // Permettra ensuite de distinguer deux lignes en mettant des couleurs différentes
                        number = 0;
                        if (i % 2 == 0) {
                            number = 1;
                        } else {
                            number = 2;
                        }

                        numEtId.add(idTempsColl);
                        BuildTable(TLtempscoll, i, number, idTempsColl);
                        temp++;
                        System.out.println(numEtId + " Les infos : " + nomTempsColl + " " + "date " + dateTempsColl + " et n° :" + i);
                    }

                    System.out.println("HASHMAP " + myMap.size());


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
                Log.e("Envoi params Reserv", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_temps_coll);
    }

    /********
     * Chargement des RAM
     * ********/
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

    /********
     * Construit le tableau contenant les données
     ********/
    private void BuildTable(TableLayout tb, final int variable, int number, final String idTC) {

        row = new TableRow(this);

        tableRowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

        // inner for loop
        tableRowParams.setMargins(10, 10, 10, 10);

        row.setLayoutParams(tableRowParams);

        if (number != 1) {
            row.setBackgroundColor(Color.parseColor("#6d6c6c"));
        }

        myMap.put(variable, Integer.parseInt(idTempsColl));

        for (int j = 1; j <= 1; j++) {
            tv = new TextView(this);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toast.makeText(Infos.this, "Filtrage", toast.LENGTH_SHORT).show();
                }
            });

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                tv.setLayoutParams(new TableRow.LayoutParams(53,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                tv.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.FILL_PARENT));
            }

            tv.setPadding(5, 5, 5, 5);
            tv.setText(dateTempsColl);
            tv.setTextColor(Color.WHITE);
            if (number != 1) {
                tv.setBackgroundColor(Color.parseColor("#6d6c6c"));
            }
            row.addView(tv);
        }

        // inner for loop
        for (int j = 2; j <= 2; j++) {
            tv = new TextView(this);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                tv.setLayoutParams(new TableRow.LayoutParams(80,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                tv.setLayoutParams(new TableRow.LayoutParams(80,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            }
            tv.setPadding(5, 5, 5, 5);
            tv.setText(nomTempsColl);
            tv.setTextColor(Color.WHITE);
            if (number != 1) {
                tv.setBackgroundColor(Color.parseColor("#6d6c6c"));
            }
            row.addView(tv);

        }

        // inner for loop
        for (int j = 3; j <= 3; j++) {
            tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setPadding(5, 5, 5, 5);
            tv.setText(horaire);
            tv.setTextColor(Color.WHITE);
            if (number != 1) {
                tv.setBackgroundColor(Color.parseColor("#6d6c6c"));
            }
            row.addView(tv);
        }

        // inner for loop
        for (int j = 4; j <= 4; j++) {
            tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setPadding(5, 5, 5, 5);
            tv.setText(categorie);
            tv.setTextColor(Color.WHITE);
            if (number != 1) {
                tv.setBackgroundColor(Color.parseColor("#6d6c6c"));
            }
            row.addView(tv);
        }

        // inner for loop
        for (int j = 5; j <= 5; j++) {
            tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv.setPadding(5, 5, 5, 5);
            getNbPlacesDispo(idTempsColl, tv);
            tv.setTextColor(Color.WHITE);
            if (number != 1) {
                tv.setBackgroundColor(Color.parseColor("#6d6c6c"));
            }
            row.addView(tv);
        }

        final Reservation maReservation = new Reservation(idTempsColl, dateTempsColl, nomTempsColl, horaire, categorie, nbPlaces, lieuTempsColl, nbPlacesEnfant, nbPlacesAdulte);

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toast.makeText(Infos.this, "Id : " + maReservation.getNom() + "\n" +
                                "Date : " + maReservation.getDate() + "\n" +
                                "Lieu : " + maReservation.getLieu() + "\n" +
                                "Catégorie : " + maReservation.getCategorie() + "\n" +
                                "Nb enfants : " + maReservation.getNbPlacesEnfant() + "\n" +
                                "Nb adultes : " + maReservation.getNbPlacesAdulte()
                        , toast.LENGTH_LONG).show();
                System.out.println("GET CATEGORIE " + maReservation.getCategorie());
                myFunction2(maReservation);
                tabs = (TabActivity) getParent();
                tabs.getTabHost().setCurrentTab(2);
            }
        });

        row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // TODO Auto-generated method stub
                res = 0;

                //Création de l'AlertDialog
                adb = new AlertDialog.Builder(Infos.this);

                adb.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        switch (item) {
                            case 0:
                                res = 0;
                                break;
                            case 1:
                                res = 1;
                                break;
                            default:
                                res = 0;
                        }
                    }
                });

                //On donne un titre à l'AlertDialog
                adb.setTitle("Vous souhaitez :");

                adb.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("RES " + res);
                        if (res == 1) {
                            try {
                                handler = new Handler();
                                j = 0;
                                runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            deleteTC(idTC);
                                            liste.clear();
                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                        System.out.println("J EST EGAL A " + j);
                                        j++;
                                    }
                                };
                                handler.postDelayed(runnable, 20);

                            } catch (Exception e) {
                                toast.makeText(getApplicationContext(), "Network error" + e,
                                        toast.LENGTH_SHORT).show();
                                System.out.println("error " + e);
                            }
                            res = 0;

                        } else if ((res == 0)) {
                            intent = new Intent(getApplicationContext(), UpdateTC.class);
                            intent.putExtra("idTC", idTC);
                            intent.putExtra("donnees", choixDB);
                            startActivity(intent);
                        } else {
                            toast.makeText(getApplicationContext(), "Veuillez faire un choix !", toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //On crée un bouton "Annuler" à notre AlertDialog et on lui affecte un évènement
                adb.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Lorsque l'on cliquera sur annuler on quittera l'application
                        adb.setCancelable(true);
                    }
                });

                adb.show();
                return false;
            }
        });
        tb.addView(row);
    }

    /***********
     * Passage de données vers les autres onglets
     ********/
    public void myFunction() {
        ((Accueil) getParent()).setValue(Integer.toString(tab));
    }

    /***********
     * Passage de l'objet Reservation vers un autre onglet
     ********/
    public void myFunction2(Reservation maResa) {
        ((Accueil) getParent()).setReservation(maResa);
    }

    /*****
     * Chargement des places disponibles de ce temps collectif
     * *******/
    private int getNbPlacesDispo(final String idTempsColl, final TextView tv) {
        nbPlacesDispo = 0;
        requete_places_by_tc = new StringRequest(Request.Method.POST, url_places_by_tc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la réponse à la permettant de récupérer le nombre de places restantes : " + response);
                //tvPlacesRestantes.setText("Il reste " + response + " place(s) disponible(s) pour ce temps collectif");
                nbPlacesDispo = Integer.parseInt(response);

                tv.setText(response);

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

        return nbPlacesDispo;
    }
}