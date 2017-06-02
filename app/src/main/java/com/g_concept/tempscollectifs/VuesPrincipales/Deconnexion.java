package com.g_concept.tempscollectifs.VuesPrincipales;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.g_concept.tempscollectifs.R;

public class Deconnexion extends AppCompatActivity {

    Button btnDeconnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deconnexion);

        btnDeconnexion = (Button) findViewById(R.id.btnDeconnexion);

        btnDeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Toast.makeText(Deconnexion.this, "Déconnexion !", Toast.LENGTH_SHORT).show();
            }
        });

        finish();

    }
}