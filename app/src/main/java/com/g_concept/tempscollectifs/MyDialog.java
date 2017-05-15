package com.g_concept.tempscollectifs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by G-CONCEPT on 09/11/2016.
 */

public class MyDialog extends DialogFragment{

    LayoutInflater inflater;
    View v;
    Button btn;
    Intent intent;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        inflater = getActivity().getLayoutInflater();
        v = inflater.inflate(R.layout.alertdialogdetails, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        builder.setView(v).setNeutralButton("Valider la présence", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }

    public void displayDatas(){

    }
}