package com.pedelen.curfewer_personal;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Patrick-XPS on 10/7/2016.
 */
public class GPSService  extends Service{

    private LocationListener locationListener;
    private LocationManager locationManager;

    public static ArrayList<String> locationArray = new ArrayList<>();

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Log.d("gps_loc", "Started GPSService");

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                Intent i = new Intent("location_update");
//                i.putExtra("coordinates", location.getLongitude()+ " " +location.getLatitude());
//                sendBroadcast(i);

                Log.d("gps_loc", "Got new location " + location.toString());
                Date now = new Date();

                String locationString = now.toString() + " " + location.toString();
                Log.d("gps_loc", locationString);
                locationArray.add(locationString);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("gps_loc", "looking for location");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }

        };
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //noinspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);

    }

    @Override
    public void onDestroy() {
        Log.d("gps_loc", "stopped looking for location");
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(locationListener);
        }
    }
}
