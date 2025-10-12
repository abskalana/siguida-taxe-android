package com.gouandiaka.market.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.gouandiaka.market.R;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.entity.Paiement;

import java.util.Calendar;

public class PrefUtils {

    public static final String KEY_LAST_LOCATION_UPDATE = "last_location_update";
    private static SharedPreferences preferences;


    public static void init(Context context) {
        Resources res = context.getResources();
        String[] months = res.getStringArray(R.array.ticket_months);

        preferences = context.getSharedPreferences("bamako_express_pref", 0);
    }


    public static void save(String key, String token) {
        preferences.edit().putString(key, token).apply();
    }

    public static void save(String key, long token) {
        preferences.edit().putLong(key, token).apply();
    }

    public static String getString(String key) {
        return preferences.getString(key, null);
    }

    public static String getString(String key, String fallback) {
        return preferences.getString(key, fallback);
    }

    public static void setInt(String key, int num) {
        preferences.edit().putInt(key, num).apply();
    }

    public static int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public static int getInt(String key, int fallback) {
        return preferences.getInt(key, fallback);
    }

    public static long getLong(String key) {
        return preferences.getLong(key, 0);
    }


    public static Entity getEntity() {

        Entity model = new Entity(getString("commune"), PrefUtils.getString("ville"),
                PrefUtils.getString("place"), PrefUtils.getString("espace"), PrefUtils.getInt("user_id"));

        if(Validator.isValid(model))return model;
        return null;
    }

    public static Paiement getPaiement(String entity){
        Paiement paiement = new Paiement(PrefUtils.getInt("user_id"), entity);
        paiement.setAnnee(getAnnee());
        paiement.setPeriod(getMois());
        return paiement;
    }

    public static int getAnnee(){
        return PrefUtils.getInt("annee", Calendar.getInstance().get(Calendar.YEAR));
    }
    public static String getMois(){
        String mois = PrefUtils.getString("mois",null);
        if(Utils.isEmpty(mois)) mois = Constant.MOIS_MAP.get(Calendar.getInstance().get(Calendar.MONTH));
        return mois;
    }

    public static int getPrefPositionSpinner(Spinner spinner, String nom){
        if( spinner == null || Utils.isSelectOrEmpty(nom)) return 0;
        Adapter adapter = spinner.getAdapter();
        if(adapter instanceof  ArrayAdapter){
            ArrayAdapter arrayAdapter = (ArrayAdapter)adapter;
            return arrayAdapter.getPosition(nom);
        }
        return 0;

    }

    public static int getPrefPosition(Spinner spinner, String key){
        String nom  = PrefUtils.getString(key);
        if( spinner == null || Utils.isSelectOrEmpty(nom)) return 0;
        Adapter adapter = spinner.getAdapter();
        if(adapter instanceof  ArrayAdapter){
            ArrayAdapter arrayAdapter = (ArrayAdapter)adapter;
            return arrayAdapter.getPosition(nom);
        }
        return 0;

    }
}
