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

import org.json.JSONArray;
import org.json.JSONException;


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

        //btn_start = (Button) findViewById(R.id.start_service);
        //btn_stop = (Button) findViewById(R.id.stop_service);
        coords = (TextView) findViewById(R.id.coords);

        if(!runtime_permissions())
            enable_buttons();
    }

    private static boolean gps_started = false;
    private void enable_buttons() {

        while(!gps_started) {
            /** need to implement date object in two locations **/
            if ((System.currentTimeMillis() + 10 * 1000)/* specify date object.getTime() here */ - System.currentTimeMillis() <= 7200000 && (System.currentTimeMillis() + 10 * 1000)/* specify date object.getTime() here */ - System.currentTimeMillis() > 0) {
                Log.d("gps_loc", "Service Started");
                startService(new Intent(getApplicationContext(), GPS_Service.class));
                gps_started = true;
            } else {
                Log.d("gps_loc", "Service Stopped");
                stopService(new Intent(getApplicationContext(), GPS_Service.class));
                gps_started = false;
            }
        }
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


    public void onStart() {
        super.onStart();
//        try {
//            if(FirebaseMessageService.data.getJSONArray("Curfews")!= null) {
//                try {
//                    JSONArray curfews = FirebaseMessageService.data.getJSONArray("Curfews");
//                    Log.d("ChildView", curfews.toString());
//                    Log.d("CurrentCurfews", "Curfews " + curfews.getJSONObject(1).getString("date"));
//
//                    String curfewString = curfews.getJSONObject(1).getString("date");
//
//                    TextView text = (TextView)findViewById(R.id.textView7);
//                    text.setText(curfewString);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        TextView text = (TextView)findViewById(R.id.textView7);
        text.setText("No current curfews");
    }

    public void changeToAccept(View v) {
        startActivity(new Intent(this, c_acceptInvite.class));
    }
}
