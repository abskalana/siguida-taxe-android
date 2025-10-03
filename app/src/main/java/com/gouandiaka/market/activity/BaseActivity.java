package com.gouandiaka.market.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gouandiaka.market.LocalDatabase;
import com.gouandiaka.market.utils.LocationUtils;
import com.gouandiaka.market.utils.Utils;

public class BaseActivity extends Activity implements LocationListener {

    protected String coord;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalDatabase.init(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        LocationUtils.setupLocation(this);
        if (LocationUtils.isLocationGranted(this)) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 4, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, this);
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 1000, 5, this);
            this.coord = Utils.convertToString(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.coord = Utils.convertToString(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}

