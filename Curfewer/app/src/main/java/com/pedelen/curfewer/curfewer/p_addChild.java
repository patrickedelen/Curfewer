package com.pedelen.curfewer.curfewer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Mitch on 8/7/2016.
 */
public class p_addChild extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_add_child);
    }

    public void requestChild(View v) {
        EditText kidEmail = (EditText)findViewById(R.id.editText);

        FirebaseMessageService.addChild(kidEmail.toString());
        Log.d("addChild", kidEmail.toString());

        startActivity(new Intent(this, p_selectChild.class));
    }

}
