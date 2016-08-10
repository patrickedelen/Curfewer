package com.pedelen.curfewer.curfewer;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * Created by Mitch on 8/6/2016.
 */
public class p_selectChild extends AppCompatActivity {

    private Button set_home;
    private TextView gps_coords_res;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public static String kidEmailPublic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);







        setContentView(R.layout.p_select_child);
        set_home = (Button) findViewById(R.id.set_home_loc);
        gps_coords_res = (TextView) findViewById(R.id.lat_long);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                gps_coords_res.setText(location.getLatitude() +" "+ location.getLongitude());
                FirebaseMessageService.updateHome(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        };

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET
                }, 10);
                return;
            }
        } else {
            configureButton();
        }
        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

    }

    public void onRequestPermissionsResults(int requestCode, String[] permissions, int[] grantResults){
        switch(requestCode) {
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    configureButton();
                }
                return;
        }
    }

    private void configureButton() {
        set_home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
            }
        });
    }

    @Override
    public void onStart() {
          super.onStart();
            TextView text = (TextView)findViewById(R.id.textView5);
        //old code :{
//        try {
//            JSONArray kids = FirebaseMessageService.data.getJSONArray("Kids");
//            Log.d("ChildList", "Kids " + kids.getJSONObject(0).getString("kidEmail"));
//
//            String kidString = kids.getJSONObject(0).getString("kidEmail");
//
//            kidEmailPublic = kidString;
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        //check if getAll has returned code
        if(FirebaseMessageService.kidEmails != null) {
            String allKids = "";
            for(String kidEmail : FirebaseMessageService.kidEmails) {
                allKids += kidEmail + "\n";
            }

            text.setText(allKids);
        }
    }

    public void viewCurfews(View v) {
        startActivity(new Intent(this, p_viewCurfew.class));
    }
    public void addCurfews(View v) {
        startActivity(new Intent(this, p_setCurfew.class));
    }
    public void addChild(View v) {
        startActivity(new Intent(this, p_addChild.class));
    }

}


