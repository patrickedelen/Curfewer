package com.pedelen.curfewer.curfewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class IdentitySelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_select);
    }

    public void changeToAuth(View v) {
        startActivity(new Intent(IdentitySelect.this, oAuthActivity.class));
    }
}
