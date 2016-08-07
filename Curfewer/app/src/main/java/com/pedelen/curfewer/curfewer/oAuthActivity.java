package com.pedelen.curfewer.curfewer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

public class oAuthActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener{

        public GoogleApiClient mGoogleApiClient;
        private static final int RC_SIGN_IN = 9001;
        private static final String TAG = "SignInActivity";

        private TextView mStatusTextView;
        private TextView mRoleTextView;
        public static final String PREFS = "test_prefs";
        private ProgressDialog mProgressDialog;

    @Override
    public void onCreate (Bundle savedInstanceState){

        Log.d(TAG, "Created activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_auth);

        //text to show status
        mStatusTextView = (TextView) findViewById(R.id.status);
        mRoleTextView = (TextView) findViewById(R.id.txtRole);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            Log.d(TAG, "Sign in not cached, trying silent login");
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    public void onClick(View v) {
        Log.d(TAG, "Clicked");
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d(TAG, "Sign in method reached");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Activity returned a result");
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            SharedPreferences getRole = getSharedPreferences(PREFS, 0);
            String user_role = getRole.getString("user_role", "nothing, bitch");
            Log.d("bacon", "MADE TO OAUTH");
            if(user_role.equals("parent")) {
                Log.d("bacon", "RENAVIGATING TO PARENT");
                this.startActivity(new Intent(this, p_selectChild.class));
            }
           /* else
                startActivity(new Intent(oAuthActivity.this, c_currectCurfews.class));*/

            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI
            updateUI(false);
        }
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void updateUI(boolean b) {
        if (b) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        }
    }

}
