package com.g_concept.tempscollectifs.ClassesMetiers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by G CONCEPT on 30/05/2017.
 */

public class Network extends AppCompatActivity {

    Context context;
    ConnectivityManager connect;
    NetworkInfo netInfo;

    public Network(ConnectivityManager cm) {
        this.connect = cm;
    }

    /********
     * Permet de vérifier si notre appareil est connecté à un réseau internet
     *********/
    public boolean isOnline() {
        netInfo = connect.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
