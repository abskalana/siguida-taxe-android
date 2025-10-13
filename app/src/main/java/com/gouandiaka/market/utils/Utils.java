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
import com.gouandiaka.market.entity.ApplicationConfig;
import com.gouandiaka.market.entity.Entity;

import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
public class Utils {



    private static final int MIN_PHONE = 49999999;
    private static final int MAX_PHONE = 99999999;

    public static final String [] PHONES = new String[]{"000","123","122","121","112","111","222","333","444"};

    public static void launchEnregistrementActivity(Context context) {
        ApplicationConfig applicationConfig = LocalDatabase.instance().getConfig();
        if(!Validator.isValid(applicationConfig)){
            Toast.makeText(context, "Invalide configuration", Toast.LENGTH_SHORT).show();
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
        ApplicationConfig applicationConfig = LocalDatabase.instance().getConfig();
        if(!Validator.isValid(applicationConfig)){
            Toast.makeText(context, "Invalide configuration", Toast.LENGTH_SHORT).show();
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
        if (Utils.isEmpty(phone)) return false;
        if(isDefaultPhone(phone)) return true;
        phone = phone.trim();
        if (phone.length() != 8) return false;
        long tel = -1;
        try {
            tel = Long.parseLong(phone);
        }catch (Exception ignored){
        }
        if(tel < MIN_PHONE || tel > MAX_PHONE) return false;
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

    public static boolean shouldRequestConfig(){
        ApplicationConfig applicationConfig = LocalDatabase.instance().getConfig();
        long time =  System.currentTimeMillis() - applicationConfig.getLastTime();
        boolean success =  time > 1000*60*60*8;
        if(success){
            LocalDatabase.instance().clearRemote();
        }
        return success;
    }

    public static String capitalizeFirst(String str) {
        if(isEmpty(str)) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static boolean isNotEmptyList(List<Entity> list) {
        return list!= null && !list.isEmpty();
    }


    public static Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            return sdf.parse(dateStr);
        } catch (Exception e1) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                return sdf.parse(dateStr);
            } catch (Exception e2) {
                return null;
            }
        }
    }

    public static boolean isDefaultPhone(String phone){
        for(String item: PHONES){
            if(item.equalsIgnoreCase(phone)) return true;
        }
        return false;
    }
}
