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
        return entityList;
    }
}
