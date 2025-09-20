package com.gouandiaka.market;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Context;
import android.location.LocationManager;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

public class LocationUtils {

    public static void setupLocation(Context context){
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGpsEnabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        }

    }

    public static  void  setupPermission(Context context){
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }
}
