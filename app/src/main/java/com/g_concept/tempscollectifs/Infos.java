package com.g_concept.tempscollectifs;

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
    NetworkInfo netInfo;
    Map<Integer, Integer> myMap = new HashMap<Integer, Integer>();
    Spinner spLieux, spRAM;
    Button btnInfos, floatingActionButton;
    ArrayAdapter<String> adapter;
    int tab, res, j;
    String  var, horaire, initLieu = "Choisir un lieu", initRAM = "Choisir un RAM", idTempsColl, choix, nbPlaces, dateTempsColl,
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
    ArrayList<String> liste = new ArrayList<String>();
    ArrayList<String> listeRAM = new ArrayList<String>();
    int temp, nbPlacesDispo;
    TableRow row;
    JSONObject json;
    Intent intent;
    TextView tvIsEmpty;
    String EXTRA_DB = "donnees", titleRAM, nbPlacesEnfant, nbPlacesAdulte, nomTempsColl, choixDB, lieuTempsColl, categorie,
            url_obtenir_lieu_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/getLieuxTempsCollectifs.php",
            url_obtenir_ram = "https://www.web-familles.fr/AppliTempsCollectifs/CreationTempsColl/getRAM.php";
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
        if (isOnline()) {
            getLieuxTempsCollectifs();
            getRAMCollectifs();
        } else {
            Toast.makeText(Infos.this, "Veuillez vérifier votre connexion internet", Toast.LENGTH_SHORT).show();
        }

        // TEST

        llContentRAM.setVisibility(View.GONE);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(Infos.this, "Déconnexion !", Toast.LENGTH_SHORT).show();
            }
        });

        rbRAM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llContentRAM.setVisibility(View.VISIBLE);
                llContentLieu.setVisibility(View.GONE);
                spRAM.setSelection(0);
                Toast.makeText(getApplicationContext(), "Lieu check", Toast.LENGTH_SHORT).show();
            }
        });

        rbLieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llContentRAM.setVisibility(View.GONE);
                llContentLieu.setVisibility(View.VISIBLE);
                spLieux.setSelection(0);
                Toast.makeText(getApplicationContext(), "Lieu check", Toast.LENGTH_SHORT).show();
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
                choix = String.valueOf(spRAM.getSelectedItem());
                TLtempscoll.removeAllViews();
                Log.i("Page Emprunt", "Vous avez appuyé sur :" + choix);
                System.out.println("LINE " + TLtempscoll.getChildCount());
                spLieux.setSelection(0);
                if (isOnline()) {
                    try {
                        liste.clear();
                        getTempsCollectifs("2", "", choix, "date ASC");
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                liste.clear();
                                getTempsCollectifs("2", "", choix, "date ASC");
                            }
                        }, 1000);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Network error",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(Infos.this, "Veuillez vérifier votre connexion internet", Toast.LENGTH_SHORT).show();
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
                if (isOnline()) {
                    getTempsCollectifs("1", choix, "", "date ASC");
                } else {
                    Toast.makeText(Infos.this, "Veuillez vérifier votre connexion internet", Toast.LENGTH_SHORT).show();
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
                getTempsCollectifs("1", "", choix, "nom");
            }
        });

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TLtempscoll.removeAllViews();
                getTempsCollectifs("1", choix, "", "date DESC");
            }
        });

        System.out.println(" LIGNES TOTALES : " + TLtempscoll.getChildCount());

    }

    public void deleteTC(final String idTC) {
        requete_delete_tc = new StringRequest(Request.Method.POST, url_delete_tc, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la requête delete to modify : " + response);
                if(response.contains("[1]")){
                    Toast.makeText(Infos.this, "Vous venez de supprimer ce temps collectif ! Vous pouvez dès à présent en créer de nouveaux.", Toast.LENGTH_LONG).show();
                    // Permet de switcher de tab quand on clique sur le bouton de validation de présence
                    TabActivity tabs = (TabActivity) getParent();
                    tabs.getTabHost().setCurrentTab(1);
                }
                else if (response.contains("[2]")){
                    Toast.makeText(Infos.this, "Erreur !", Toast.LENGTH_LONG).show();
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
                        int number = 0;
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
        /*row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                100));*/

        TableLayout.LayoutParams tableRowParams =
                new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

        // inner for loop
        tableRowParams.setMargins(10, 10, 10, 10);

        row.setLayoutParams(tableRowParams);

        if (number != 1) {
            row.setBackgroundColor(Color.parseColor("#6d6c6c"));
        }

        myMap.put(variable, Integer.parseInt(idTempsColl));

        for (int j = 1; j <= 1; j++) {
            TextView tv = new TextView(this);

            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Infos.this, "Filtrage", Toast.LENGTH_SHORT).show();
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
            ImageView tv = new ImageView(this);

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                tv.setLayoutParams(new TableRow.LayoutParams(80,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                tv.setLayoutParams(new TableRow.LayoutParams(80,
                        80));
            }

            tv.getLayoutParams().height = 40;
            tv.setPadding(5, 5, 5, 5);
            tv.setImageResource(R.drawable.un);
            if (number != 1) {
                tv.setBackgroundColor(Color.parseColor("#6d6c6c"));
            }

            row.addView(tv);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (Map.Entry<Integer, Integer> entry : myMap.entrySet()) {
                        int key = entry.getKey();
                        if (key == variable) {
                            tab = entry.getValue();
                        }

                        Integer.toString(tab);
                        if (Integer.toString(tab) != "0") {
                            System.out.println(" La valeur au click est de " + tab);
                            myFunction();
                            TabActivity tabs = (TabActivity) getParent();
                            tabs.getTabHost().setCurrentTab(2);
                            System.out.println("On a cliqué");
                        }
                    }
                }
            });
            System.out.println("Nous affichons le résultat de la requète" + tab);
        }

        // inner for loop
        for (int j = 3; j <= 3; j++) {
            TextView tv = new TextView(this);
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
        for (int j = 4; j <= 4; j++) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setPadding(5, 5, 5, 5);
            tv.setText(horaire);
            tv.setTextColor(Color.WHITE);
            if (number != 1) {
                tv.setBackgroundColor(Color.parseColor("#6d6c6c"));
            }
            row.addView(tv);
        }

        // inner for loop
        for (int j = 5; j <= 5; j++) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setPadding(5, 5, 5, 5);
            tv.setText(categorie);
            tv.setTextColor(Color.WHITE);
            if (number != 1) {
                tv.setBackgroundColor(Color.parseColor("#6d6c6c"));
            }
            row.addView(tv);
        }

        // inner for loop
        for (int j = 6; j <= 6; j++) {
            TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
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

                Toast.makeText(Infos.this, "Id : " + maReservation.getNom() + "\n" +
                                "Date : " + maReservation.getDate() + "\n" +
                                "Lieu : " + maReservation.getLieu() + "\n" +
                                "Catégorie : " + maReservation.getCategorie() + "\n" +
                                "Nb enfants : " + maReservation.getNbPlacesEnfant() + "\n" +
                                "Nb adultes : " + maReservation.getNbPlacesAdulte()
                        , Toast.LENGTH_LONG).show();
                System.out.println("GET CATEGORIE " + maReservation.getCategorie());
            }
        });

        row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                // TODO Auto-generated method stub
                res = 0;

                //On instancie notre layout en tant que View
                LayoutInflater factory = LayoutInflater.from(Infos.this);
                final View alertDialogView = factory.inflate(R.layout.alertdialogpresent, null);

                //Création de l'AlertDialog
                final AlertDialog.Builder adb = new AlertDialog.Builder(Infos.this);

                // Strings to Show In Dialog with Radio Buttons
                final CharSequence[] items = {"Modifier le Temps Collectif" , " Supprimer le Temps Collectif"};

                adb.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        switch (item) {
                            case 0:
                                // Your code when first option seletced
                                res = 0;
                                break;
                            case 1:
                                // Your code when 2nd  option seletced
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
                                final Handler handler = new Handler();
                                j = 0;
                                runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if(j==0 || j==1 || j==2 || j==3 || j==4 || j==5 || j==6 || j==7 || j==8 || j==9){
                                                deleteTC(idTC);
                                            }
                                            else
                                            {
                                                handler.removeCallbacks(this);
                                            }
                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                        System.out.println("J EST EGAL A "+j);
                                        j++;
                                    }
                                };
                                handler.postDelayed(runnable,1000);

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Network error"+e,
                                        Toast.LENGTH_SHORT).show();
                                System.out.println("error " + e);
                            }
                            res = 0;

                        }
                        else if((res==0)){
                            intent = new Intent(getApplicationContext(), UpdateTC.class);
                            intent.putExtra("idTC",idTC);
                            intent.putExtra("donnees",choixDB);
                            startActivity(intent);
                        } else{
                            Toast.makeText(getApplicationContext(), "Veuillez faire un choix !",Toast.LENGTH_SHORT).show();
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

    public boolean isOnline() {
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /***********
     * Passage de données vers les autres onglets
     ********/
    public void myFunction() {
        ((Accueil) getParent()).setValue(Integer.toString(tab));
    }

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