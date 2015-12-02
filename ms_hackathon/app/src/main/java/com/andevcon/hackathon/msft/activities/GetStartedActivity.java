package com.andevcon.hackathon.msft.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andevcon.hackathon.msft.R;
import com.andevcon.hackathon.msft.helpers.Constants;
import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationCancelError;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.UserInfo;
import com.microsoft.office365.connectmicrosoftgraph.AuthenticationManager;

import java.net.URI;
import java.util.UUID;

public class GetStartedActivity extends AppCompatActivity {

    private static final String TAG = GetStartedActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_started);

        getWindow().setStatusBarColor(getResources().getColor(R.color.travelogColorPrimaryDark));

        findViewById(R.id.button_get_started).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(GetStartedActivity.this, TravelogMainActivity.class));
                connectToOffice365();
            }
        });
    }

    private void connectToOffice365() {
//        showConnectingInProgressUI();

        //check that client id and redirect have been configured
        if (!hasAzureConfiguration()) {
            Toast.makeText(
                    GetStartedActivity.this,
                    getString(R.string.warning_clientid_redirecturi_incorrect),
                    Toast.LENGTH_LONG).show();
//            resetUIForConnect();
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

                        // actually start the Activity
                        startActivity(sendMailActivity);

                        finish();
                    }

                    @Override
                    public void onError(Exception exc) {
                        if (userCancelledConnect(exc)) {
//                            resetUIForConnect();
                        } else {
//                            showConnectErrorUI();
                        }
                    }
                };

        AuthenticationManager mgr = AuthenticationManager.getInstance();
        mgr.setContextActivity(this);
        mgr.connect(callback);
    }

    /**
     * This activity gets notified about the completion of the ADAL activity through this method.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its
     *                    setResult().
     * @param data        An Intent, which can return result data to the caller (various data
     *                    can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult - AuthenticationActivity has come back with results");
        super.onActivityResult(requestCode, resultCode, data);
        AuthenticationManager
                .getInstance()
                .getAuthenticationContext()
                .onActivityResult(requestCode, resultCode, data);
    }

    private static boolean userCancelledConnect(Exception e) {
        return e instanceof AuthenticationCancelError;
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

//    private void resetUIForConnect() {
//        mConnectButton.setVisibility(View.VISIBLE);
//        mTitleTextView.setVisibility(View.GONE);
//        mDescriptionTextView.setVisibility(View.GONE);
//        mConnectProgressBar.setVisibility(View.GONE);
//    }
//
//    private void showConnectingInProgressUI() {
//        mConnectButton.setVisibility(View.GONE);
//        mTitleTextView.setVisibility(View.GONE);
//        mDescriptionTextView.setVisibility(View.GONE);
//        mConnectProgressBar.setVisibility(View.VISIBLE);
//    }
//
//    private void showConnectErrorUI() {
//        mConnectButton.setVisibility(View.VISIBLE);
//        mConnectProgressBar.setVisibility(View.GONE);
//        mTitleTextView.setText(R.string.title_text_error);
//        mTitleTextView.setVisibility(View.VISIBLE);
//        mDescriptionTextView.setText(R.string.connect_text_error);
//        mDescriptionTextView.setVisibility(View.VISIBLE);
//        Toast.makeText(
//                ConnectActivity.this,
//                R.string.connect_toast_text_error,
//                Toast.LENGTH_LONG).show();
//    }

}
