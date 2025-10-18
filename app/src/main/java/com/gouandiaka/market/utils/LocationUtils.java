package com.gouandiaka.market.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import com.gouandiaka.market.entity.Entity;
import com.gouandiaka.market.entity.EntityComparator;

import java.util.ArrayList;
import java.util.List;

public class LocationUtils {

    public static void setupLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGpsEnabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        }

    }

    public static boolean isLocationGranted(Context context) {
        boolean location = context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        return location;
    }

    public static void setupPermission(Context context) {
        if (isLocationGranted(context)) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (!isGpsEnabled) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show();
            }
        } else {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        }

    }

    public static List<Entity> filterByLocation(List<Entity> entities, String coord,int limit){
        Location myLocation = Utils.convertToLocation(coord);
        if(myLocation == null) return null;
        List<Entity> entityList = new ArrayList<>();
        for(Entity entity : entities){
            Location location = Utils.convertToLocation(entity.getCoord());
            if(location != null && myLocation.distanceTo(location) < 100 ) {
                entityList.add(entity);
            }
        }
        EntityComparator.sort(entities,EntityComparator.SORT_DISTANCE,coord);
        return entityList;
    }

    public static double getDistanceLocation(Entity entity, Location myLocation){
        if(entity == null ) return  Double.MAX_VALUE;
        Location location = Utils.convertToLocation(entity.getCoord());
        if(location != null && myLocation != null){
            return location.distanceTo(myLocation);
        }
        return Double.MAX_VALUE;
    }

    public static void launchGooglemap(Context context, String coord,String label){
        if(Utils.isEmpty(coord)) return ;
        coord = coord.replace(";",",");
        String uri = "geo:" + coord + "?q=" + coord + "(" + Uri.encode(label) + ")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }

    public static void launchNavigation(Context context, String coord){
        if(Utils.isEmpty(coord)) return ;
        coord = coord.replace(";",",");
        String uri = "google.navigation:q=" +coord;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }
}
