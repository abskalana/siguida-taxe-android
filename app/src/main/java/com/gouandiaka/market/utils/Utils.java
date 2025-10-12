package com.gouandiaka.market.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gouandiaka.market.activity.ChoiceActivity;
import com.gouandiaka.market.activity.ConfigActivity;
import com.gouandiaka.market.activity.EnregistrementActivity;
import com.gouandiaka.market.activity.MainActivity;
import com.gouandiaka.market.activity.PayConfirmActivity;
import com.gouandiaka.market.activity.PayRechercheActivity;
import com.gouandiaka.market.data.LocalDatabase;
import com.gouandiaka.market.entity.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

    public static Map<String, String> users = new HashMap<>();


    public static void launchEnregistrementActivity(Context context) {
        if (!LocationUtils.isLocationGranted(context)) {
            Toast.makeText(context, "Il faut configurer GPS", Toast.LENGTH_SHORT).show();
            return;
        }
        Entity model = PrefUtils.getEntity();
        if (!Validator.isValid(model)) {
            Toast.makeText(context, "Il faut configurer commune", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(context, EnregistrementActivity.class);
        context.startActivity(intent);
    }

    public static void launchAccueilActivity(Context context,boolean showConfig) {
        Intent intent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("SHOW_CONFIG", showConfig);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }


    public static void launchPayConfirmActivity(Context context, Entity entity) {
        Intent intent = new Intent(context, PayConfirmActivity.class);
        intent.putExtra("entity", new Gson().toJson(entity));
        context.startActivity(intent);
    }

    public static void launchChoiceActivity(Context context, Entity entity) {
        Intent intent = new Intent(context, ChoiceActivity.class);
        intent.putExtra("entity", new Gson().toJson(entity));
        context.startActivity(intent);
    }

    public static void launchConfigActivity(Context context) {
        if (!LocationUtils.isLocationGranted(context)) {
            Toast.makeText(context, "Il faut configurer GPS", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(context, ConfigActivity.class);
        context.startActivity(intent);
    }

    public static void launchPaiementActivity(Context context) {
        if (!LocationUtils.isLocationGranted(context)) {
            Toast.makeText(context, "Il faut configurer GPS", Toast.LENGTH_SHORT).show();
            return;
        }
        Entity model = PrefUtils.getEntity();
        if (!Validator.isValid(model)) {
            Toast.makeText(context, "Il faut configurer Config", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Utils.isEmpty(PrefUtils.getString("mois"))) {
            Toast.makeText(context, "Il faut  mois Config", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Entity> res = LocalDatabase.instance().getRemoteModel();
        if (Utils.isNotEmptyList(res)) {

            Intent intent = new Intent(context, PayRechercheActivity.class);
            context.startActivity(intent);
        }else{
            Toast.makeText(context, "Il faut charger ou changer config", Toast.LENGTH_SHORT).show();

        }
    }

    public static boolean isKalana(String ville) {
        if (ville == null) return false;
        return "kalana".equalsIgnoreCase(ville.trim());
    }


    public static boolean isEmpty(String str) {
        return str == null || "".equalsIgnoreCase(str.trim()) || str.trim().length() < 2;
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        phone = phone.replace(" ", "").replace("-", "").replace("(", "").replace(")", "");
        phone = phone.trim();
        if (phone.length() != 8) return false;
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }


    public static int convertToNumber(String str, int fallback) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return fallback;
        }
    }

    public static String convertToString(Location location) {
        if (location == null) return null;
        String latitude = String.valueOf(location.getLatitude());
        if (latitude.length() > 12) {
            latitude = latitude.substring(0, 12);
        }
        String longitude = String.valueOf(location.getLongitude());
        if (longitude.length() > 12) {
            longitude = longitude.substring(0, 12);
        }

        return latitude + ";" + longitude;
    }



    public static boolean isSelectOrEmpty(String str) {
        return Utils.isEmpty(str) || str.toLowerCase().startsWith("select");
    }

    public static SpannableString getColoredStatus(String status) {
        if(Utils.isEmpty(status)) status ="NON_DEMANDÉ";
        SpannableString spannable = new SpannableString(status);
        int color = Color.BLACK; // couleur par défaut

        if ("PAYÉ".equalsIgnoreCase(status) || "PAYE_MAIRIE".equalsIgnoreCase(status)) {
            color = Color.GREEN;
        } else if ("REFUS".equalsIgnoreCase(status)) {
            color = Color.RED;
        } else if ("ABSENT".equalsIgnoreCase(status)
                || "FERMÉ".equalsIgnoreCase(status)
                || "AUTRE".equalsIgnoreCase(status)) {
            color = Color.parseColor("#FFA500"); // orange
        }

        spannable.setSpan(
                new ForegroundColorSpan(color),
                0, status.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        return spannable;
    }



    public static Location convertToLocation(String location) {
        try {
            if (Utils.isEmpty(location)) return null;
            String[] res = location.split(";");
            Location location1 = new Location("gps");
            location1.setLatitude(Double.parseDouble(res[0]));
            location1.setLongitude(Double.parseDouble(res[1]));
            return location1;
        } catch (Exception e) {
            return null;
        }


    }

    public static boolean shoulRequestConfig(){
        long time =  System.currentTimeMillis() -PrefUtils.getLong("time");
        return time > 1000*60*60*23;
    }

    public static String capitalizeFirst(String str) {
        if(isEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static boolean isNotEmptyList(List<Entity> list) {
        return list!= null && !list.isEmpty();
    }


}
