package com.andevcon.hackathon.msft.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.andevcon.hackathon.msft.R;
import com.andevcon.hackathon.msft.api.ApiClient;
import com.microsoft.onenotevos.Page;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedString;

public class CreatePostActivity extends AppCompatActivity {

    public static final String EXTRA_SECTION_ID = "EXTRA_SECTION_ID";
    private static final String TAG = CreatePostActivity.class.getSimpleName();

    @Bind(R.id.etDesc)
    EditText etDesc;

    @Bind(R.id.etTitle)
    EditText etTitle;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private String mSectionId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            mSectionId = intent.getStringExtra(EXTRA_SECTION_ID);
            Log.d(TAG, "onCreate() called with: " + "mSectionId = [" + mSectionId + "]");
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Create new Page");
            getWindow().setStatusBarColor(getResources().getColor(R.color.travelogColorPrimaryDark));
        }
    }

    @OnClick(R.id.fabSend)
    public void postPage() {

        if (TextUtils.isEmpty(mSectionId)) {
            Toast.makeText(this, "section is null", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = etTitle.getText().toString();
        String desc = etDesc.getText().toString();

        if (TextUtils.isEmpty(title)) {
            etTitle.setError("Enter Title");
            return;
        }

        ApiClient.apiService.postSimplePage(mSectionId, getHtmlRequestBody(title, desc),
                new Callback<Page>() {
                    @Override
                    public void success(Page page, Response response) {
                        Log.d(TAG, "Successful response - " + page.title);
                        launchHome();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "Failed to post image - " + Log.getStackTraceString(error));
                    }
                });

    }

    private void launchHome() {
        Intent intent = new Intent(this, TravelogMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finish();
    }

    public TypedString getHtmlRequestBody(String title, String desc) {

        String imgPath = "http://developer.android.com/assets/images/android_logo.png";

        String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <head>\n" +
                "    <title>" + title + "</title>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <p> " + desc + "</p>\n" +
                "    <img src=\"" + imgPath + "\" alt=\"androidicon\" width=\"500\" />\n" +
                "  </body>\n" +
                "</html>\n";

        return new TypedString(html);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_send:
                getCameraImage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCameraImage() {
        Snackbar.make(etDesc.getRootView(), "Get Camera Image", Snackbar.LENGTH_SHORT).show();
    }

}
