package com.pedelen.curfewer.curfewer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Map;

/**
 * Created by Patrick-XPS on 8/6/2016.
 */
public class FirebaseMessageService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessage";

    public static JSONObject data;
    public static final String PREFS = "test_prefs";
    public static String ROLE;
    public static String EMAIL;

    @Override
    public void onMessageReceived (RemoteMessage message) {
        Log.d(TAG, "Message Recived");
        Log.d(TAG, "Data: " + message.getData().toString());
        Log.d(TAG, "MessageType: " + message.getData().get("type"));

        if(message.getData().get("type").equals("UserLoggedIn")) {
            getAllData();
        } else if(message.getData().get("type").equals("AllDataReturn")) {
            try {
                data = new JSONObject(message.getData().get("data"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, data.toString());
        } else {
            getAllData();
        }

    }

    @Override
    public void onMessageSent (String messageId) {
        Log.d(TAG, "Message Sent");
        Log.d(TAG, messageId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(TAG, "Service started");
        super.onStart(intent, startId);
    }

    public static void getAllData () {

        FirebaseMessaging fm = FirebaseMessaging.getInstance();

        fm.send(new RemoteMessage.Builder("45945011648@gcm.googleapis.com")
                .setMessageId("GetAll")
                .addData("email", EMAIL)
                .addData("role",ROLE)
                .addData("token", FirebaseIdService.getFirebaseToken())
                .build());

        Log.d(TAG, "Requested all data");
    }

    public static void loginUser (String email, String role) {

        String formattedRole = role.equals("child") ? "Kid" : "Parent";

        ROLE = formattedRole;
        EMAIL = email;

        FirebaseMessaging fm = FirebaseMessaging.getInstance();

        fm.send(new RemoteMessage.Builder("45945011648@gcm.googleapis.com")
                .setMessageId("UserLoginMessage")
                .addData("email", email)
                .addData("role", formattedRole)
                .addData("token", FirebaseIdService.getFirebaseToken())
                .build());

        Log.d(TAG, "Requested login");
    }

    public static void updateHome (String latitude, String longitude) {
        FirebaseMessaging fm = FirebaseMessaging.getInstance();

        fm.send(new RemoteMessage.Builder("45945011648@gcm.googleapis.com")
                .setMessageId("UpdateHomeMessage")
                .addData("email", EMAIL)
                .addData("role", ROLE)
                .addData("latitude", latitude)
                .addData("longitude", longitude)
                .addData("token", FirebaseIdService.getFirebaseToken())
                .build());

        Log.d(TAG, "Updated home location");
    }

    public static void addChild (String kidEmail) {
        FirebaseMessaging fm = FirebaseMessaging.getInstance();

        fm.send(new RemoteMessage.Builder("45945011648@gcm.googleapis.com")
                .setMessageId("AddChildMessage")
                .addData("email", EMAIL)
                .addData("kidEmail", kidEmail)
                .addData("token", FirebaseIdService.getFirebaseToken())
                .build());

        Log.d(TAG, "Added child to parent");
    }

    public static void acceptParent() {
        FirebaseMessaging fm = FirebaseMessaging.getInstance();

        fm.send(new RemoteMessage.Builder("45945011648@gcm.googleapis.com")
                .setMessageId("AcceptParent")
                .addData("email", EMAIL)
                .addData("token", FirebaseIdService.getFirebaseToken())
                .build());

        Log.d(TAG, "Accepted parent request");
    }
    public static void addCurfew(String kidEmail) {

       //String ms = Long.toString(d);

        FirebaseMessaging fm = FirebaseMessaging.getInstance();

        fm.send(new RemoteMessage.Builder("45945011648@gcm.googleapis.com")
                .setMessageId("AddCurfew")
                .addData("email", EMAIL)
                .addData("token", FirebaseIdService.getFirebaseToken())
                .addData("kidEmail", kidEmail)
                .build());

        Log.d(TAG, "Added curfew");
    }

    public static void addCurfewFake(String kidEmail) {

        //String ms = Long.toString(d);

        FirebaseMessaging fm = FirebaseMessaging.getInstance();

        fm.send(new RemoteMessage.Builder("45945011648@gcm.googleapis.com")
                .setMessageId("AddCurfewFake")
                .addData("email", EMAIL)
                .addData("token", FirebaseIdService.getFirebaseToken())
                .addData("kidEmail", kidEmail)
                .build());

        Log.d(TAG, "Added curfew");
    }

}
