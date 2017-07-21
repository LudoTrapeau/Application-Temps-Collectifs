package com.g_concept.tempscollectifs.ClassesMetiers;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.g_concept.tempscollectifs.R;

public class Documentation extends AppCompatActivity {

    Button btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentation);

        btnDownload = (Button)findViewById(R.id.btnDownload);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToUrl ( "https://www.web-familles.fr/AppliTempsCollectifs/Documentation/Temps%20collectifs%20-%20Notice%20Utilisateur.pdf");
            }
        });
    }

    private void goToUrl (String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}
