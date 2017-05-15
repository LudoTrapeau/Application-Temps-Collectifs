package com.g_concept.tempscollectifs;

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.widget.Toast;

/**
 * Created by G-CONCEPT on 27/12/2016.
 */
public class InfoBulle extends Activity{

    String EXTRA_ID="id", choix;
    Intent intent;

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

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.widthPixels;

        getWindow().setLayout((int)(width*.8), (int) (height*.6));



    }


}
