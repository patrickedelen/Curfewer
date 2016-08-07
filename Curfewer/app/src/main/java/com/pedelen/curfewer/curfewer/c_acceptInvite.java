package com.pedelen.curfewer.curfewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;

/**
 * Created by Mitch on 8/7/2016.
 */
public class c_acceptInvite extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_accept_invite);


    }

    private void goBack() {
        startActivity(new Intent(this, c_currentCurfews.class));
    }

    public void acceptParent (View v) {
        FirebaseMessageService.acceptParent();
        startActivity(new Intent(this, c_currentCurfews.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView t = (TextView)findViewById(R.id.textView3);
        try {
            t.setText(FirebaseMessageService.data.getString("_parentEmail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
