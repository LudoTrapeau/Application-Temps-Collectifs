package com.g_concept.tempscollectifs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

/**
 * Created by G-CONCEPT on 09/05/2016.
 */
class CustomAdapter extends ArrayAdapter {

    private Context mContext;
    private int id;
    private ArrayList<String> items;
    RequestQueue requestQueue1;

    public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> list) {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list;
        requestQueue1 = Volley.newRequestQueue(getContext());
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        View mView = v;
        if (mView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }

        TextView text = (TextView) mView.findViewById(R.id.tv);

        text.setText("Emile");

        //for(int i = 0; i<items.length; i++){
//            System.out.println("ESSAI " + items[1]);
        //}
        return mView;
    }
}