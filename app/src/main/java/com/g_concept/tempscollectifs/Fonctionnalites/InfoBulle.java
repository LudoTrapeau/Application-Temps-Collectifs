package com.g_concept.tempscollectifs.Fonctionnalites;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.g_concept.tempscollectifs.R;

/**
 * Created by G-CONCEPT on 27/12/2016.
 */
public class InfoBulle extends Activity{

    String EXTRA_ID="id", choix;
    Intent intent;
    DisplayMetrics dm;
    int width, height;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        intent = getIntent();
        choix = intent.getStringExtra(EXTRA_ID);

        if(choix.equals("1")){
            setContentView(R.layout.popwindows);
        }else if(choix.equals("2")){
            setContentView(R.layout.popcreationtempscoll);
        }

        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        width = dm.widthPixels;
        height = dm.widthPixels;

        getWindow().setLayout((int)(width*.8), (int) (height*.6));



    }


}
