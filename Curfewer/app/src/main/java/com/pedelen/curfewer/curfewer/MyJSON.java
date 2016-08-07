package com.pedelen.curfewer.curfewer;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;


/**
 * Created by Mitch on 8/6/2016.
 */

public class MyJSON {
    static String fileName = "user_data.json";

    public static void saveData(String params, String mJsonResponse) {
        try {
            FileWriter file = new FileWriter("/Android/data/com.pedelen.curfewer.curfewer/" + params);
            file.write(mJsonResponse);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public static void getData(String params) {
        try {
            File f = new File("/Android/data/com.pedelen.curfewer.curfewer/" + params);
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String mResponse = new String(buffer);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
