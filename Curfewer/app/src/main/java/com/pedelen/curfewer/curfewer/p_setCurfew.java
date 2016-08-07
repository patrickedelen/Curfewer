package com.pedelen.curfewer.curfewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Mitch on 8/7/2016.
 */
public class p_setCurfew extends AppCompatActivity {

    private Button back_btn, confirm_btn;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_set_curfew);
    }

    private void goBack() {
        startActivity(new Intent(this, p_selectChild.class));
    }



    public void confirm(View v) {

        //EditText date = (EditText)findViewById(R.id.editText2);
        //Long ParsedDate = Date.parse(date.toString());
        FirebaseMessageService.addCurfewFake(p_selectChild.kidEmailPublic);
        startActivity(new Intent(this, p_selectChild.class));
    }

}