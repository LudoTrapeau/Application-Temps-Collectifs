package com.g_concept.tempscollectifs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Login extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;

    private static final String[] DUMMY_CREDENTIALS = new String[]{};
    String url_verif_authent = "https://www.web-familles.fr/AppliTempsCollectifs/toLogIn.php";
    private UserLoginTask mAuthTask = null;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    Intent intent;
    int res = 0;
    JSONObject ladherent;
    TextView msgErreur;
    ArrayList<String> liste = new ArrayList<>();
    JSONArray jsonArray, adherents;
    JSONObject jsonObject, json;
    String idUser, part1, part2,
            url = "https://www.web-familles.fr/AppliTempsCollectifs/getAllDB.php", db2, email, password, initBDD = "Choisir votre structure", db, choixDB;
    StringRequest requete_adhrent;
    Button btnConnexion;
    String[] pieces, parts2;
    int shortAnimTime;
    JsonObjectRequest jsonObjectRequest;
    RequestQueue requestQueue, requestQueue3;
    ArrayAdapter<String> dataAdapter, adapter;
    boolean cancel;
    View focusView;
    Spinner spinnerDB;
    List exempleList;
    Map<String, String> params = new HashMap<String, String>();
    ConnectivityManager cm;
    NetworkInfo netInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setLogo(R.drawable.icone);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.hide();

        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1d87bb")));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        msgErreur = (TextView) findViewById(R.id.msgErreur);
        msgErreur.setText("");

        requestQueue3 = Volley.newRequestQueue(getApplicationContext());
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);

        /*** Pour l'enregistrement des identifiants ***/
        //ReadSettings(getBaseContext());
        //cbMemory.setChecked(true);

        if (isOnline()) {
            getAllDb();
        } else {
            Toast.makeText(getApplicationContext(), "Veuillez vérifier votre connexion internet et relancer l'application", Toast.LENGTH_SHORT).show();
        }

        btnConnexion = (Button) findViewById(R.id.btnConnexion);

        // Lors du clic sur le bouton Connexion
        btnConnexion.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin(mEmailView.getText().toString(), mPasswordView.getText().toString());
            }
        });

        mProgressView = findViewById(R.id.login_progress);

        res = 0;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    /*********
     * Vérification si l'appareil est connecté au réseau internet
     ********/
    public boolean isOnline() {
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /******
     * Méthode permettant de vérifier les identifiants saisis par l'utilisateur et de les confirmer ou pas.
     *******/
    private void attemptLogin(final String mail, final String pass) {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        cancel = false;
        focusView = null;

        // Vérification si le champs MDP est rempli
        if (TextUtils.isEmpty(pass)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Vérification si le champs Login est rempli
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }

        requete_adhrent = new StringRequest(Request.Method.POST, url_verif_authent, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    json = new JSONObject(response);
                    adherents = json.getJSONArray("adherent");

                    for (int i = 0; i < adherents.length(); i++) {
                        ladherent = adherents.getJSONObject(i);
                        idUser = ladherent.getString("id");
                    }

                    //Toast.makeText(Login.this, "L'id est : " + idUser, Toast.LENGTH_SHORT).show();

                    // Si on reçoit '[1]' en JSON, l'utilisateur a rentré les bons identifiants et est connecté
                    if (!choixDB.equals(initBDD)) {
                        if (response.contains("[1]")) {
                            System.out.println("TEST1 : " + choixDB);
                            intent = new Intent(getApplicationContext(), Accueil.class);
                            intent.putExtra("donnees", choixDB);
                            intent.putExtra("idUser", idUser);
                            res = 1;
                            startActivity(intent);
                            }
                    }

                    if(choixDB.equals(initBDD)){
                        msgErreur.setText("Veuillez selectionner une base de données !");
                        System.out.println("Equals : " + choixDB + " et " + initBDD);
                    }else{
                        System.out.println("Pas Equals : " + choixDB + " et " + initBDD);
                    }

                    if (response.contains("[0]")) {
                        res = 0;
                        msgErreur.setText("Login et/ou mot de passe incorrect !");
                        msgErreur.setTextColor(Color.RED);
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
                params.put("username", mEmailView.getText().toString());
                params.put("password", mPasswordView.getText().toString());
                params.put("donnees", choixDB);

                Log.e("Prise en compte paramêtres :", params.toString());
                return params;
            }
        };
        requestQueue3.add(requete_adhrent);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                //returnLogin();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    /******
     * Ecriture sur un fichier des données à mémoriser
     *******/
    public void WriteSettings(Context context, String data) {
        FileOutputStream fOut = null;
        OutputStreamWriter osw = null;

        try {
            fOut = context.openFileOutput("memoire.dat", MODE_PRIVATE);
            System.out.println("FOUT " + fOut);
            osw = new OutputStreamWriter(fOut);
            osw.write(data);
            osw.flush();
            //popup surgissant pour le résultat
            //Toast.makeText(context, "Settings saved",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //Toast.makeText(context, "Settings not saved",Toast.LENGTH_SHORT).show();
            Log.e("Erreur", String.valueOf(e));
        } finally {
            try {
                osw.close();
                fOut.close();
            } catch (IOException e) {
                //Toast.makeText(context, "Settings not saved",Toast.LENGTH_SHORT).show();
                Log.e("Erreur", String.valueOf(e));
            }
        }
    }

    /*****
     * Lecture sur le fichier des données à récupérer
     ******/
    public String ReadSettings(Context context) {
        FileInputStream fIn = null;
        InputStreamReader isr = null;

        char[] inputBuffer = new char[255];
        String data = null;

        try {
            fIn = context.openFileInput("memoire.dat");
            isr = new InputStreamReader(fIn);
            isr.read(inputBuffer);
            data = new String(inputBuffer);
            //affiche le contenu de mon fichier dans un popup surgissant
            //Toast.makeText(context, " "+data,Toast.LENGTH_SHORT).show();
            System.out.println("DATA :" + data);
            //parts2 = data.split("-");
            //part1 = parts2[0].replaceAll("_", " ");
            //part2 = parts2[1];
            //liste.add(part1);
            //liste.remove(initBDD);
            System.out.println("part1 :" + part1);
            System.out.println("part2 :" + part2);
            mEmailView.setText(data);
        } catch (Exception e) {
            Log.e("Erreur", String.valueOf(e));
        }
        return data;
    }

    /************************** Récupération des bdd dans une liste ****************************/
    public void getAllDb(){

        //Menu déroulant contenant les bdd
        spinnerDB = (Spinner) findViewById(R.id.spBDD);

        //Création d'une liste d'éléments à mettre dans le Spinner(pour l'exemple)
        exempleList = new ArrayList();

        liste.add(initBDD);

        //Le Spinner a besoin d'un adapter pour sa presentation
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exempleList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Enfin on passe l'adapter au Spinner et c'est tout
        spinnerDB.setAdapter(adapter);

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, liste);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDB.setAdapter(dataAdapter);

        //-- gestion du Click sur la liste Région
        spinnerDB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                choixDB = String.valueOf(spinnerDB.getSelectedItem());
                if (!choixDB.equals(initBDD)) {
                    choixDB = choixDB.replaceAll(" ", "_");
                }

                System.out.println("NOUS ESSAYONS : " + choixDB);

                System.out.println(choixDB);
                Log.i("Page Emprunt", "Vous avez appuyé sur :" + choixDB);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        /*********************************************************/

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    jsonArray = response.getJSONArray("db");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        db = jsonObject.getString("Database");
                        if (!db.equals("information_schema")) {
                            //if (db.contains("_")) {
                                //db2 = db.replaceAll("_", " ");
                                if (!liste.contains(db2)) {
                                    //liste.add(db2);
                                }
                            //} else {
                              //  if (!liste.contains(db)) {
                                    liste.add(db);
                                //}
                           // }
                        }
                    }
                } catch (
                        JSONException e
                        ) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Erreur", String.valueOf(error));
            }
        });
        requestQueue.add(jsonObjectRequest);
}
}