package com.pedelen.curfewer.curfewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Mitch on 8/7/2016.
 */
public class p_viewCurfew extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_view_curfew);
    }

    private void goBack() {
        startActivity(new Intent(this, p_selectChild.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            if(FirebaseMessageService.data.getJSONArray("Curfews")!= null) {
        try {
            JSONArray curfews = FirebaseMessageService.data.getJSONArray("Curfews");
            Log.d("ParentView", curfews.toString());
            Log.d("ParentCurfews", "Curfews " + curfews.getJSONObject(1).getString("date"));

            String curfewString = curfews.getJSONObject(1).getString("date");

            TextView text = (TextView)findViewById(R.id.textView9);
            text.setText(curfewString);

        } catch (JSONException e) {
            e.printStackTrace();
        }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
