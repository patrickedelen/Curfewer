package com.pedelen.curfewer_personal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create the GPS service
        GPSService GPSService = new GPSService();
        GPSService.onCreate();
    }

    public void onResume(){
        super.onResume();

        TextView locationText = (TextView)findViewById(R.id.textView);
        locationText.setText(GPSService.locationArray.toString());
    }
}
