package com.gouandiaka.market.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.gouandiaka.market.entity.Entity;

public class PrefUtils {

    public static final String KEY_LAST_LOCATION_UPDATE = "last_location_update";
    private static SharedPreferences preferences;


    public static void init(Context context) {

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

    public static long getLong(String key) {
        return preferences.getLong(key, 0);
    }


    public static Entity getEntity() {

        Entity model = new Entity(getString("commune"), PrefUtils.getString("ville"),
                PrefUtils.getString("place"), PrefUtils.getString("espace"), PrefUtils.getInt("user_id"));

        return model;
    }
}
