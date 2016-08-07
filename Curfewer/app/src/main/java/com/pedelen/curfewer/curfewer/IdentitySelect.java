package com.pedelen.curfewer.curfewer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

import java.io.FileOutputStream;

public class IdentitySelect extends AppCompatActivity {

    public static String role;
    public static final String PREFS = "test_prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences getRole = getSharedPreferences(PREFS, 0);
        String user_role = getRole.getString("user_role", "nothing, bitch");
        Log.d("test",user_role);
        if(user_role.equals("child") || user_role.equals("parent")){
            Log.d("test","test2");
            role = user_role;
            changeToAuth();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_select);
    }

    public void changeToAuth() {
        SharedPreferences test_pref = getSharedPreferences(PREFS, 0);
        SharedPreferences.Editor editor = test_pref.edit();
        editor.putString("user_role", role);
        editor.commit();

        startActivity(new Intent(IdentitySelect.this, oAuthActivity.class));
    }

    public void setChild(View v){
        //Log.d("debug", "Child Called");
        role = "child";
        changeToAuth();
    }

    public void setParent(View v){
        role = "parent";
        changeToAuth();
    }
}
