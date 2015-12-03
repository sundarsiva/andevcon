package com.andevcon.hackathon.msft.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.andevcon.hackathon.msft.R;
import com.andevcon.hackathon.msft.helpers.Constants;
import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.UserInfo;
import com.microsoft.office365.connectmicrosoftgraph.AuthenticationManager;

import java.net.URI;
import java.util.UUID;

public class GetStartedActivity extends AppCompatActivity {

    private static final String TAG = GetStartedActivity.class.getCanonicalName();

    ProgressBar mPbGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_started);

        getWindow().setStatusBarColor(getResources().getColor(R.color.travelogColorPrimaryDark));

        findViewById(R.id.button_get_started).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPbGetStarted.setVisibility(View.VISIBLE);
                connectToOffice365();
            }
        });

        mPbGetStarted = (ProgressBar) findViewById(R.id.pb_get_started);
    }

    private void connectToOffice365() {
        //check that client id and redirect have been configured
        if (!hasAzureConfiguration()) {
            Toast.makeText(
                    GetStartedActivity.this,
                    getString(R.string.warning_clientid_redirecturi_incorrect),
                    Toast.LENGTH_LONG).show();
            return;
        }

        connect();
    }

    private void connect() {
        // define the post-auth callback
        AuthenticationCallback<AuthenticationResult> callback =
                new AuthenticationCallback<AuthenticationResult>() {

                    @Override
                    public void onSuccess(AuthenticationResult result) {
                        // get the UserInfo from the auth response
                        UserInfo user = result.getUserInfo();

                        // get the user's given name
                        String givenName = user.getGivenName();

                        // get the user's displayable Id
                        String displayableId = user.getDisplayableId();

                        // start the SendMailActivity
                        Intent sendMailActivity =
                                new Intent(GetStartedActivity.this, TravelogMainActivity.class);

                        // take the user's info along
                        sendMailActivity.putExtra(TravelogMainActivity.ARG_GIVEN_NAME, givenName);
                        sendMailActivity.putExtra(TravelogMainActivity.ARG_DISPLAY_ID, displayableId);

                        mPbGetStarted.setVisibility(View.GONE);

                        // actually start the Activity
                        startActivity(sendMailActivity);

                        finish();
                    }

                    @Override
                    public void onError(Exception exc) {
                        mPbGetStarted.setVisibility(View.GONE);
                    }
                };

        AuthenticationManager mgr = AuthenticationManager.getInstance();
        mgr.setContextActivity(this);
        mgr.connect(callback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult - AuthenticationActivity has come back with results");
        super.onActivityResult(requestCode, resultCode, data);
        AuthenticationManager
                .getInstance()
                .getAuthenticationContext()
                .onActivityResult(requestCode, resultCode, data);
    }

    private static boolean hasAzureConfiguration() {
        try {
            UUID.fromString(Constants.CLIENT_ID);
            URI.create(Constants.REDIRECT_URI);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
