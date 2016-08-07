package com.pedelen.curfewer.curfewer;

import android.*;
import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Created by Mitch on 8/6/2016.
 */
public class c_currentCurfews extends AppCompatActivity {
    private Button btn_start, btn_stop;
    private TextView coords;
    private BroadcastReceiver broadcastReceiver;
    public AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    @Override
    protected void onResume(){
        super.onResume();
        if(broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    coords.append("\n" + intent.getExtras().get("coordinates"));
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_current_curfews);

        btn_start = (Button) findViewById(R.id.start_service);
        btn_stop = (Button) findViewById(R.id.stop_service);
        coords = (TextView) findViewById(R.id.coords);

        if(!runtime_permissions())
            enable_buttons();
    }

    private void enable_buttons() {

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(getApplicationContext(), GPS_Service.class));
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                stopService(new Intent(getApplicationContext(), GPS_Service.class));
            }
        });
        

       /* alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), GPS_Service.class);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, 500, alarmIntent);*/
    }
/*
    private long getTime(){
        long d1 = System.currentTimeMillis();
        long d2 = d1 + 5*1000;

        Log.d("gps_loc", "time set to " + (d2-d1));
        return d2-d1;
    }
*/
    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return true;
        }
        return false;
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enable_buttons();
            }else {
                runtime_permissions();
            }
        }

    }
}
