package com.pedelen.curfewer.curfewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

/**
 * Created by Mitch on 8/7/2016.
 */
public class p_setCurfew extends AppCompatActivity {

    private Button back_btn, confirm_btn;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_add_child);
    }

    private void goBack() {
        startActivity(new Intent(this, p_selectChild.class));
    }

}