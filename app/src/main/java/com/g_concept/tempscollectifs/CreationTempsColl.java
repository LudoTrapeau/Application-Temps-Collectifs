package com.g_concept.tempscollectifs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreationTempsColl extends AppCompatActivity {

    ImageButton ibDate, ibHeureDebut, ibHeureFin;
    ImageView ibAddNomTempsColl, ibAddActivite, ibAddLieu;
    DatePickerDialog.OnDateSetListener dpicker;
    TimePickerDialog tpicker;
    ImageView ibDelete;
    Calendar mcurrentTime;
    TextView tvRAM, tvActivite, tvNomTC, tvLieu;
    EditText edDate, edHeureDebut, edHeureFin, edDetailsPublic, edDetailsRAM, dateDeb;
    String time;
    Button btnCreateTempsColl, btnRefresh, btnRefresh2, btnRefresh3, btnDeleteLieu, btnDeleteNom, btnDeleteActivite;
    boolean cancel;
    View focusView;
    Spinner spCategorie;
    String[] tabNbPlaces = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25"
            , "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
            "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75",
            "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100"};
    static final int DIALOG_ID = 0;
    int annee, mois, jour, heure, minute;
    Spinner spLieux, spNom, spActivite, spRAM;
    ArrayList<String> listeNomsTempsColl = new ArrayList<>();
    ArrayList<String> listeActivitesTempsColl = new ArrayList<>();
    ArrayList<String> listeRAM = new ArrayList<>();
    JSONObject json;
    Button floatingActionButton, btnInfos;
    SeekBar seekBar, seekBar2;
    Switch swIndefini, swIndefini2;
    TextView tvSeek, tvSeek2, tvNbLimitChar, tvNbLimitChar2;
    Intent intent;
    String  heureDebParams, heureFinParams, EXTRA_DB="donnees", titleRAM, nbPlacesEnfant, nbPlacesAdulte, activiteTempsColl, descriptif, date, heureDebut, heureFin, nomTempsColl, m, j, lieuTempsColl, categorie,
            initNom = "Choisissez le nom",initActivite = "Choisissez l'activité",initRAM = "Choisissez le RAM", initLieu = "Choisissez le lieu",
            url_creation_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/CreationTempsColl/createTempsColl.php", db2, email, password, initBDD = "Choisir votre structure", db, choixDB,
            url_obtenir_lieu_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/Informations/getLieuxTempsCollectifs.php",
            url_obtenir_nom_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/CreationTempsColl/getNomsTempsColl.php",
            url_delete_lieu_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/CreationTempsColl/deleteLieuTempsColl.php",
            url_delete_nom_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/CreationTempsColl/deleteLieuTempsColl.php",
            url_delete_activite_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/CreationTempsColl/deleteLieuTempsColl.php",
            url_obtenir_ram = "https://www.web-familles.fr/AppliTempsCollectifs/CreationTempsColl/getRAM.php",
            url_heures_params = "https://www.web-familles.fr/AppliTempsCollectifs/CreationTempsColl/getHeuresParams.php",
            url_obtenir_activite_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/CreationTempsColl/getActivitesTempsColl.php";
    StringRequest requete_heures_params,requete_temps_coll, requete_create_temps_coll, requete_nom_temps_coll, requete_activite_temps_coll, requete_ram,
                  requete_delete_temps_coll, requete_delete_nom_temps_coll, requete_delete_activite_temps_coll;
    Map<String, String> params = new HashMap<String, String>();
    JSONObject leLieuTempsColl, lactiviteTempsColl, leRAM, objectHeureParams;
    JSONArray lesLieuxTempsColl, lesActivitesTempsColl, lesRAM, lesHeuresParams;
    RequestQueue requestQueue;
    ArrayList<String> AllLieuxTempsColl = new ArrayList<String>();
    String[] listeCategories = {"ACTIONS COLLECTIVES", "REUNIONS A THEMES", "SORTIES - VISITES - AUTRES"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_temps_coll);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        //Menu déroulant contenant les bdd
        spLieux = (Spinner) findViewById(R.id.spinnerLieu);
        spRAM = (Spinner) findViewById(R.id.spinnerRAM);
        spCategorie = (Spinner) findViewById(R.id.spCategorie);
        edDate = (EditText) findViewById(R.id.edDate);
        spNom = (Spinner) findViewById(R.id.spNom);
        spActivite = (Spinner) findViewById(R.id.spActivite);
        edDetailsPublic = (EditText) findViewById(R.id.edDetailsPublic);
        edDetailsRAM = (EditText) findViewById(R.id.edDetailsRAM);
        edHeureDebut = (EditText) findViewById(R.id.edHeureDebut);
        edHeureFin = (EditText) findViewById(R.id.edHeureFin);
        btnCreateTempsColl = (Button) findViewById(R.id.btnCreationTempsColl);
        btnInfos = (Button)findViewById(R.id.btnInfo);
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
        //btnDeleteLieu = (Button)findViewById(R.id.btnDelete);
        //btnDeleteNom = (Button)findViewById(R.id.btnDeleteNom);
        //btnDeleteActivite = (Button)findViewById(R.id.btnDeleteActivite);

        tvRAM = (TextView)findViewById(R.id.tvRAM);
        tvActivite = (TextView)findViewById(R.id.tvActivite);
        tvNomTC = (TextView)findViewById(R.id.tvNom);
        tvLieu = (TextView)findViewById(R.id.tvLieu);

        swIndefini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swIndefini.isChecked()) {
                    seekBar.setEnabled(false);
                    tvSeek.setText("indéfini");
                } else {
                    seekBar.setEnabled(true);
                }
            }
        });

        intent = getIntent();
        choixDB = "";
        choixDB = intent.getStringExtra(EXTRA_DB);
        System.out.println("MA BDD : " + choixDB);

        final TextWatcher mTextEditorWatcher = new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                tvNbLimitChar.setText(String.valueOf(s.length()));
            }

            public void afterTextChanged(Editable s) {
            }
        };
        edDetailsPublic.addTextChangedListener(mTextEditorWatcher);

        final TextWatcher mTextEditorWatcher2 = new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                tvNbLimitChar2.setText(String.valueOf(s.length()));
            }

            public void afterTextChanged(Editable s) {
            }
        };
        edDetailsRAM.addTextChangedListener(mTextEditorWatcher2);


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

        swIndefini2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swIndefini2.isChecked()) {
                    seekBar2.setEnabled(false);
                    tvSeek2.setText("indéfini");
                } else {
                    seekBar2.setEnabled(true);
                }
            }
        });

        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                tvSeek2.setText(String.valueOf(progress) + " ");
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

        date = edDate.getText().toString();
        heureDebut = edDate.getText().toString() + " " + edHeureDebut.getText().toString();
        heureFin = edDate.getText().toString() + " " + edHeureFin.getText().toString();
        descriptif = edDetailsPublic.getText().toString();

        showDialog();

        // Si l'appareil a une connexion internet
        if (isOnline()) {
            getHeuresParams();
            getDate();
            getHorlogeHeureDebut();
            getHorlogeHeureFin();
            getLieuxTempsCollectifs();
            getNomTempsCollectifs();
            getRAMCollectifs();
            getActivitesTempsCollectifs();
        }

        // Quand on clique sur le bouton Ajout d'activité
        ibAddActivite = (ImageView) findViewById(R.id.ivAddActivite);
        ibAddActivite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On ajoute une nouvelle entité de nom pour les temps collectifs
                AddActiviteTempsColl addActivite = new AddActiviteTempsColl(choixDB);
                addActivite.show(getFragmentManager(), "dialog");
            }
        });

        // Quand on clique sur le bouton Ajout de nom
        ibAddNomTempsColl = (ImageView) findViewById(R.id.ibAdd);
        ibAddNomTempsColl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On ajoute une nouvelle entité de nom pour les temps collectifs
                AddNomTempsColl addNom = new AddNomTempsColl(choixDB);
                addNom.show(getFragmentManager(), "dialog");
                // Récupération des nom de temps collectifs
                getNomTempsCollectifs();
            }
        });

        // Quand on clique sur le bouton Ajout de lieu
        ibAddLieu = (ImageView) findViewById(R.id.ivAddLieu);
        ibAddLieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // On ajoute une nouvelle entité de nom pour les temps collectifs
                AddLieuTempsColl addLieuTempsColl = new AddLieuTempsColl(choixDB);
                addLieuTempsColl.show(getFragmentManager(), "dialog");
                updateSpinnerLieux();
                AllLieuxTempsColl.add(initLieu);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
                    btnRefresh.startAnimation(an);

                    getNomTempsCollectifs();
                    // Mise à jour de la liste des enfants
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getNomTempsCollectifs();
                            listeNomsTempsColl.clear();
                        }
                    }, 1000);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Network error",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRefresh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
                    btnRefresh2.startAnimation(an);

                    getLieuxTempsCollectifs();
                    // Mise à jour de la liste des enfants
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getLieuxTempsCollectifs();
                            AllLieuxTempsColl.clear();
                        }
                    }, 1000);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Network error",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnInfos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), InfoBulle.class);
                intent.putExtra("id","2");
                startActivity(intent);
            }
        });

        btnRefresh3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
                    btnRefresh3.startAnimation(an);

                    getActivitesTempsCollectifs();
                    // Mise à jour de la liste des enfants
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivitesTempsCollectifs();
                            listeActivitesTempsColl.clear();
                        }
                    }, 1000);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Network error",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        listeRAM.add(initRAM);
        AllLieuxTempsColl.add(initLieu);
        listeNomsTempsColl.add(initNom);
        listeActivitesTempsColl.add(initActivite);

        ArrayAdapter spinnerArrayAdapterLieux = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, AllLieuxTempsColl);
        spLieux.setAdapter(spinnerArrayAdapterLieux);

        spLieux.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lieuTempsColl = String.valueOf(spLieux.getSelectedItem());
                System.out.println("Vous venez d'appuyer sur le lieu : " + lieuTempsColl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categorie = "categorie";
            }
        });

        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("indéfini");
        for (int i = 1; i <= tabNbPlaces.length; i++) {
            String j = Integer.toString(i);
            spinnerArray.add(j);
        }

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
                titleRAM = "titleRAM";
            }
        });


        final ArrayAdapter spinnerArrayAdapterActivites = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listeActivitesTempsColl);
        spActivite.setAdapter(spinnerArrayAdapterActivites);

        spActivite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                activiteTempsColl = String.valueOf(spActivite.getSelectedItem());
                System.out.println("Vous venez d'appuyer sur le nom : " + activiteTempsColl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categorie = "categorie";
            }
        });

        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edDate.getText().toString().equals("")) {
                    edDate.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Ce champs est déjà vide !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Lorsque l'on clique sur le bouton de validation
        btnCreateTempsColl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ELEMENTS : " + edDate.getText().toString());
                if (isOnline()) {

                    // Si la date est vide
                    if (edDate.getText().toString().equals("")) {
                        edDate.setError(getString(R.string.error_field_required));
                        focusView = edDate;
                        cancel = true;
                        edDate.setFocusableInTouchMode(true);
                        edDate.requestFocus();
                    }else{
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

                    String valeurActivite = spActivite.getItemAtPosition(spActivite.getSelectedItemPosition()).toString();
                    String valeurNom = spNom.getItemAtPosition(spNom.getSelectedItemPosition()).toString();
                    String valeurRAM = spRAM.getItemAtPosition(spRAM.getSelectedItemPosition()).toString();
                    String valeurLieu = spLieux.getItemAtPosition(spLieux.getSelectedItemPosition()).toString();

                    System.out.println(valeurActivite + " " + initActivite);
                    System.out.println(valeurLieu + " " + initLieu);
                    System.out.println(valeurNom + " " + initNom);
                    System.out.println(valeurRAM + " " + initRAM);

                    if (!edHeureDebut.getText().toString().equals("")
                            && !edHeureFin.getText().toString().equals("")
                            && !edDate.getText().toString().equals("")) {

                        if(!valeurNom.equals(initNom)){
                            tvNomTC.setTextColor(Color.BLACK);
                            tvRAM.setTextColor(Color.BLACK);
                            tvLieu.setTextColor(Color.BLACK);
                            tvActivite.setTextColor(Color.BLACK);

                            if(!valeurActivite.equals(initActivite)){
                                tvNomTC.setTextColor(Color.BLACK);
                                tvRAM.setTextColor(Color.BLACK);
                                tvLieu.setTextColor(Color.BLACK);
                                tvActivite.setTextColor(Color.BLACK);

                                if(!valeurRAM.equals(initRAM)){
                                    tvNomTC.setTextColor(Color.BLACK);
                                    tvRAM.setTextColor(Color.BLACK);
                                    tvLieu.setTextColor(Color.BLACK);
                                    tvActivite.setTextColor(Color.BLACK);

                                    if(!valeurLieu.equals(initLieu)){
                                        tvNomTC.setTextColor(Color.BLACK);
                                        tvRAM.setTextColor(Color.BLACK);
                                        tvLieu.setTextColor(Color.BLACK);
                                        tvActivite.setTextColor(Color.BLACK);

                                        createTempsCollectifs();
                                    }else{
                                        tvLieu.setTextColor(Color.RED);
                                        Toast.makeText(CreationTempsColl.this, "Veuillez saisir tous les champs !", Toast.LENGTH_SHORT).show();
                                    }

                                }else{
                                    tvRAM.setTextColor(Color.RED);
                                    Toast.makeText(CreationTempsColl.this, "Veuillez saisir tous les champs !", Toast.LENGTH_SHORT).show();
                                }


                            }else{
                                tvActivite.setTextColor(Color.RED);
                                Toast.makeText(CreationTempsColl.this, "Veuillez saisir tous les champs !", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            tvNomTC.setTextColor(Color.RED);
                            Toast.makeText(CreationTempsColl.this, "Veuillez saisir tous les champs !", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(CreationTempsColl.this, "Veuillez vérifier votre connexion internet", Toast.LENGTH_SHORT).show();
                }
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edDate.getText().toString().equals("")){
                    TabActivity tabs = (TabActivity) getParent();
                    tabs.getTabHost().setCurrentTab(0);
                }else{
                    Toast.makeText(CreationTempsColl.this, "Infos non enregistrées ! Veuillez finir la création ou supprimer les données saisies.", Toast.LENGTH_LONG).show();
                }
            }
        });

        /*btnDeleteLieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLieuTempsCollectifs();
            }
        });

        btnDeleteNom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNomTempsCollectifs();
            }
        });

        btnDeleteActivite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteActiviteTempsCollectifs();
            }
        });
        */
    }

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
                if (dateDeb.getText().toString().isEmpty()) {
                    dateDeb.setText(annee + "-" + m + "-" + j);
                } else {
                    dateDeb.append(" . " + annee + "-" + m + "-" + j);
                }
            }
        };
    }

    public void getHorlogeHeureDebut() {
        ibHeureDebut = (ImageButton) findViewById(R.id.ibHeureDeb);
        ibHeureDebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcurrentTime = Calendar.getInstance();
                heure = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                minute = mcurrentTime.get(Calendar.MINUTE);
                tpicker = new TimePickerDialog(CreationTempsColl.this, new TimePickerDialog.OnTimeSetListener() {
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
                tpicker = new TimePickerDialog(CreationTempsColl.this, new TimePickerDialog.OnTimeSetListener() {
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

    public void deleteLieuTempsCollectifs() {
        requete_delete_temps_coll = new StringRequest(Request.Method.POST, url_delete_lieu_temps_coll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la réponse à la requète pour les lieux : " + response);

                if (response.contains("[1]")) {
                    Toast.makeText(CreationTempsColl.this, "Ce lieu a bien été supprimé !", Toast.LENGTH_LONG).show();
                } else if (response.contains("[2]")) {
                    Toast.makeText(CreationTempsColl.this, "Ce lieu n'a pas pu être supprimé !", Toast.LENGTH_SHORT).show();
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
                params.put("lieu", lieuTempsColl);
                Log.e("Params suppression temps collectif", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_delete_temps_coll);
    }

    public void deleteActiviteTempsCollectifs() {
        requete_delete_activite_temps_coll = new StringRequest(Request.Method.POST, url_delete_activite_temps_coll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la réponse à la requète pour les lieux : " + response);

                if (response.contains("[1]")) {
                    Toast.makeText(CreationTempsColl.this, "Ce lieu a bien été supprimé !", Toast.LENGTH_LONG).show();
                } else if (response.contains("[2]")) {
                    Toast.makeText(CreationTempsColl.this, "Ce lieu n'a pas pu être supprimé !", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", String.valueOf(error));
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put("activite", activiteTempsColl);
                params.put("lieu", lieuTempsColl);
                Log.e("Params suppression temps collectif", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_delete_activite_temps_coll);
    }

    public void deleteNomTempsCollectifs() {
        requete_delete_nom_temps_coll = new StringRequest(Request.Method.POST, url_delete_nom_temps_coll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la réponse à la requète pour les lieux : " + response);

                if (response.contains("[1]")) {
                    Toast.makeText(CreationTempsColl.this, "Ce lieu a bien été supprimé !", Toast.LENGTH_LONG).show();
                } else if (response.contains("[2]")) {
                    Toast.makeText(CreationTempsColl.this, "Ce lieu n'a pas pu être supprimé !", Toast.LENGTH_SHORT).show();
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
                Log.e("Params suppression temps collectif", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_delete_nom_temps_coll);
    }

    public void createTempsCollectifs() {
        requete_create_temps_coll = new StringRequest(Request.Method.POST, url_creation_temps_coll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la réponse à la requète pour les lieux : " + response);

                if (response.contains("[1]")) {
                    Toast.makeText(CreationTempsColl.this, "Ce temps collectif a été ajouté avec succès ! Il y aura " + tvSeek.getText().toString() + " enfants pour " + tvSeek2.getText().toString() + " adultes à ce temps collectif.", Toast.LENGTH_LONG).show();

                    edDetailsPublic.setText("");
                    edDetailsRAM.setText("");
                    edDate.setText("");
                    spNom.setSelection(0);
                    spLieux.setSelection(0);
                    spActivite.setSelection(0);
                    spRAM.setSelection(0);
                    seekBar.setMax(0);
                    seekBar2.setMax(0);

                } else if (response.contains("[0]")){
                    Toast.makeText(CreationTempsColl.this, "Ces temps collectifs ont été ajouté avec succès ! Il y aura " + tvSeek.getText().toString() + " enfants pour " + tvSeek2.getText().toString() + " adultes à ces temps collectifs.", Toast.LENGTH_LONG).show();

                    edDetailsPublic.setText("");
                    edDetailsRAM.setText("");
                    edDate.setText("");
                    spNom.setSelection(0);
                    spLieux.setSelection(0);
                    spActivite.setSelection(0);
                    spRAM.setSelection(0);
                    seekBar.setMax(0);
                    seekBar2.setMax(0);
                }
                else if (response.contains("[2]")) {
                    Toast.makeText(CreationTempsColl.this, "Ce temps collectif n'a pu être ajouté !", Toast.LENGTH_SHORT).show();
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
                params.put("ram", titleRAM);
                if (edDate.getText().toString().contains(".")) {
                    String[] tab = edDate.getText().toString().split("\\.");
                    System.out.println("Le premiere case " + tab[0]);
                    params.put("heureDeb", tab[0] + " " + edHeureDebut.getText().toString());
                    params.put("heureFin", tab[0] + " " + edHeureFin.getText().toString());
                } else {
                    params.put("heureDeb", edDate.getText().toString() + " " + edHeureDebut.getText().toString());
                    params.put("heureFin", edDate.getText().toString() + " " + edHeureFin.getText().toString());
                }

                params.put("detailsPublic", edDetailsPublic.getText().toString());
                params.put("detailsRAM", edDetailsRAM.getText().toString());
                params.put("date", edDate.getText().toString());
                params.put("categorie", categorie);
                params.put("activite", activiteTempsColl);
                params.put("lieu", lieuTempsColl);
                params.put("nbPlacesEnfant", tvSeek.getText().toString());
                params.put("nbPlacesAdulte", tvSeek2.getText().toString());
                Log.e("Params création TC", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_create_temps_coll);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void updateSpinnerLieux() {
        AllLieuxTempsColl.clear();
        spLieux.invalidate();
        System.out.println("1/ VOICI " + AllLieuxTempsColl);
        getLieuxTempsCollectifs();
        System.out.println("2/ VOICI " + AllLieuxTempsColl);
    }

    public void getHeuresParams() {
        requete_heures_params = new StringRequest(Request.Method.POST, url_heures_params, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("Voici la réponse à la requète pour les ram : " + response);
                    json = new JSONObject(response);
                    lesHeuresParams = json.getJSONArray("heuresParams");
                    for (int i = 0; i < lesHeuresParams.length(); i++) {
                        objectHeureParams = lesHeuresParams.getJSONObject(i);

                        if(i == 0){
                            heureDebParams = objectHeureParams.getString("valeur");
                            edHeureDebut.setText(heureDebParams + ":00");
                        }else if(i == 1){
                            heureFinParams = objectHeureParams.getString("valeur");
                            edHeureFin.setText(heureFinParams + ":00");
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
                Log.e("Envoi params Reserv", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_heures_params);
    }
}