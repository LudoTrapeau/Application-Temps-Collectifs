package com.g_concept.tempscollectifs.Fonctionnalites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.g_concept.tempscollectifs.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by G-CONCEPT on 09/11/2016.
 */

public class AddActiviteTempsColl extends DialogFragment {

    LayoutInflater inflater;
    View v;
    Button btn;
    Intent intent;
    AlertDialog.Builder builder;
    RequestQueue requestQueue;
    Map<String, String> params = new HashMap<String, String>();
    StringRequest requete_add_activite_temps_coll;
    String url_add_activite_temps_coll = "https://www.web-familles.fr/AppliTempsCollectifs/CreationTempsColl/addActiviteTempsColl.php";
    EditText input;
    String baseDonnees;
    Toast toast;

    public AddActiviteTempsColl(String choixDB) {
        this.baseDonnees = choixDB;
    }

    public AddActiviteTempsColl() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.alertdialogactivite, null);
        builder = new AlertDialog.Builder(getActivity());

        requestQueue = Volley.newRequestQueue(this.getActivity());

        input = new EditText(getActivity());
        builder.setView(input);

        builder.setTitle("Paramétrage 41 sur Gramweb");

        builder.setIcon(R.drawable.param_activite);

        builder.setPositiveButton("Ajouter cette valeur au paramétrage 41", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!input.getText().toString().equals("")) {
                    addActivite(input.getText().toString(), getActivity(), baseDonnees);
                } else {
                    toast.makeText(getActivity(), "Champs vide !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        AddActiviteTempsColl.super.onDismiss(dialog);
                    }
                });
            }
        });

        return builder.create();
    }

    public void addActivite(final String activite, final Context context, final String bdd) {
        requete_add_activite_temps_coll = new StringRequest(Request.Method.POST, url_add_activite_temps_coll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Voici la réponse à la requète pour l'ajout d'activité : " + response);

                if (response.contains("[1]")) {
                    toast.makeText(context, "Votre valeur a bien été ajoutée ! Appuyez sur le bouton actualiser pour récupérer la nouvelle valeur.", Toast.LENGTH_LONG).show();
                } else if (response.contains("[2]")) {
                    toast.makeText(context, "Votre valeur n'a pas pu être ajoutée !", Toast.LENGTH_LONG).show();
                } else if (response.contains("[3]")) {
                    toast.makeText(context, "La valeur que vous avez saisi existe déjà !", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :", String.valueOf(error));
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                params.put("activite", activite);
                params.put("donnees", bdd);
                Log.e("Params ajout nom TC", params.toString());
                return params;
            }
        };
        requestQueue.add(requete_add_activite_temps_coll);
    }
}