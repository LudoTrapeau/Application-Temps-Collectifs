package com.g_concept.tempscollectifs.Fonctionnalites;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.g_concept.tempscollectifs.R;

public class Bundle extends Service {
    private WindowManager windowManager;
    private ImageView globalView;
    WindowManager.LayoutParams params;

    public Bundle() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        globalView = new ImageView(this);
        globalView.setImageResource(R.drawable.bulle); //Ajout d'une image à  l'image view
        //Creation du layout avec une taille de 180 sur 180
        params = new WindowManager.LayoutParams(180, 180, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        //On place la bulle en haut de l'écran
        params.gravity = Gravity.TOP;
        //On ajoute le layout et l'imageview au windowsmanager
        windowManager.addView(globalView, params);
    }

}
