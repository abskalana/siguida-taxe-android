package com.gouandiaka.market.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gouandiaka.market.LocalDatabase;
import com.gouandiaka.market.activity.ConfigActivity;
import com.gouandiaka.market.activity.EnregistrementActivity;
import com.gouandiaka.market.activity.MainActivity;
import com.gouandiaka.market.activity.PayConfirmActivity;
import com.gouandiaka.market.activity.PayRechercheActivity;
import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.entity.EntityResponse;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static Map<String, String> users = new HashMap<>();


    public static void launchEnregistrementActivity(Context context) {
        Entity model = PrefUtils.getEntity();
        if(model.isInCorrect()){
            Toast.makeText(context, "Il faut configurer commune", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(context, EnregistrementActivity.class);
        context.startActivity(intent);
    }

    public static void launchAccueilActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void launchPayConfirmActivity(Context context, Entity entity) {
        Intent intent = new Intent(context, PayConfirmActivity.class);
        intent.putExtra("entity",  new Gson().toJson(entity));
        context.startActivity(intent);
    }
    public static void launchConfigActivity(Context context) {
        Intent intent = new Intent(context, ConfigActivity.class);
        context.startActivity(intent);
    }

    public static void launchPaiementActivity(Context context) {
        EntityResponse res =  LocalDatabase.instance().getRemoteModel() ;
        if(res == null || res.getEntities().isEmpty()){
            Toast.makeText(context, "Il faut charger", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(context, PayRechercheActivity.class);
        context.startActivity(intent);
    }

    public static  boolean isKalana(String ville){
        if(ville == null) return  false;
        return "kalana".equalsIgnoreCase(ville.trim());
    }


    public static boolean isEmpty(String str) {
        return str == null || "".equalsIgnoreCase(str.trim()) || str.trim().length()<2;
    }
    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        phone = phone.replace(" ", "").replace("-", "").replace("(", "").replace(")", "");
        phone = phone.trim();
        if (phone.length() != 8  && phone.length() != 17) return false;
        String [] phones = phone.split(",");
        boolean firstisOK = android.util.Patterns.PHONE.matcher(phones[0]).matches();
        boolean seconisOK = true;
        if(phones.length > 1) {
             seconisOK = android.util.Patterns.PHONE.matcher(phones[1]).matches();
        }
        return firstisOK && seconisOK;
    }



    public static int convertToNumber(String str, int fallback){
        try{
            return Integer.parseInt(str);
        }catch (Exception e){
            return fallback;
        }
    }

    public static String convertToString(Location location) {
        if (location==null) return null;
        String latitude = String.valueOf(location.getLatitude());
        if (latitude.length() > 12) {
            latitude = latitude.substring(0, 12);
        }
        String longitude = String.valueOf(location.getLongitude());
        if (longitude.length() > 12) {
            longitude = longitude.substring(0, 12);
        }

        return latitude + ";" + longitude ;
    }

    public static boolean isSelectOrEmpty(String str){
        return Utils.isEmpty(str) || str.toLowerCase().startsWith("select");
    }

    public static Location convertToLocation(String location) {
        try{
            if (Utils.isEmpty(location)) return null;
            String [] res = location.split(";");
            Location location1 =  new Location("gps");
            location1.setLatitude(Double.parseDouble(res[0]));
            location1.setLongitude(Double.parseDouble(res[1]));
            return location1;
        }catch (Exception e){
            return null;
        }



    }

}
