package com.gouandiaka.market.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.gouandiaka.market.entity.ApplicationConfig;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.entity.Paiement;

import java.util.Calendar;

public class PrefUtils {


    private static SharedPreferences preferences;


    public static void init(Context context) {
        preferences = context.getSharedPreferences("bamako_express_pref", 0);
    }


    public static int getPrefPosition(Spinner spinner, String value){
        if( spinner == null || Utils.isSelectOrEmpty(value)) return 0;
        Adapter adapter = spinner.getAdapter();
        if(adapter instanceof  ArrayAdapter){
            ArrayAdapter arrayAdapter = (ArrayAdapter)adapter;
            return arrayAdapter.getPosition(value);
        }
        return 0;

    }
}
