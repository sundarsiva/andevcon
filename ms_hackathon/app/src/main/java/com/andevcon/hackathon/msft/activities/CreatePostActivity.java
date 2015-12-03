package com.andevcon.hackathon.msft.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.andevcon.hackathon.msft.R;
import com.andevcon.hackathon.msft.api.ApiClient;
import com.microsoft.onenoteapi.service.OneNotePartsMap;
import com.microsoft.onenotevos.Envelope;
import com.microsoft.onenotevos.Page;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

public class CreatePostActivity extends AppCompatActivity {

    public static final String EXTRA_SECTION_ID = "EXTRA_SECTION_ID";
    private static final String TAG = CreatePostActivity.class.getSimpleName();

    @Bind(R.id.etDesc)
    EditText etDesc;

    @Bind(R.id.etTitle)
    EditText etTitle;

    @Bind(R.id.friends_toolbar)
    Toolbar toolbar;

    @Bind(R.id.ivImg)
    ImageView ivImg;

    private String mSectionId;

    private boolean toLoadImage = false;

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

        if (toLoadImage) {
            postPageWithImage(title, desc);
        } else {
            postSimplePage(title, desc);
        }
    }

    private void postSimplePage(String title, String desc) {
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
                toggleImage();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleImage() {
        toLoadImage = !toLoadImage;
        ivImg.setVisibility(toLoadImage ? View.VISIBLE : View.GONE);
    }

    private void postPageWithImage(String title, String desc) {

        DateTime date = DateTime.now();
        String imagePartName = date.toString();
        String simpleHtml = getSimplePageContentBody(
                getResources().openRawResource(R.raw.create_page_with_image),
                title, desc, date.toString(), imagePartName);

        TypedString presentationString = new TypedString(simpleHtml) {
            @Override
            public String mimeType() {
                return "text/html";
            }
        };

        OneNotePartsMap oneNotePartsMap = new OneNotePartsMap(presentationString);

        TypedFile typedFile = new TypedFile("image/jpg", getImageFile());
        oneNotePartsMap.put(imagePartName, typedFile);

        ApiClient.apiService.postPageWithImages(mSectionId, oneNotePartsMap, new Callback<Envelope<Page>>() {
            @Override
            public void success(Envelope<Page> pageEnvelope, Response response) {
                Log.d(TAG, "Successful response - " + response.getStatus());
                launchHome();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Failed to post image - " + Log.getStackTraceString(error));
            }
        });
    }

    static String getSimplePageContentBody(InputStream input, String title, String desc, String createDate, String imagePartName) {

        String simpleHtml = "";
        try {
            simpleHtml = IOUtils.toString(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(title)) {
            simpleHtml = simpleHtml.replace("{title}", title);
        }
        if (!TextUtils.isEmpty(desc)) {
            simpleHtml = simpleHtml.replace("{desc}", desc);
        }
        if (!TextUtils.isEmpty(createDate)) {
            simpleHtml = simpleHtml.replace("{contentDate}", createDate);
        }
        if (!TextUtils.isEmpty(imagePartName)) {
            simpleHtml = simpleHtml.replace("{partName}", imagePartName);
        }

        return simpleHtml;
    }


    /*
    * @param imagePath The path to the image
    * @return File. the image to attach to a OneNote page
    */
    protected File getImageFile() {
        InputStream imgStream = getResources().openRawResource(R.raw.get_started);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(
                    FilenameUtils.getBaseName("test_image"),
                    FilenameUtils.getExtension("jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            IOUtils.copy(imgStream, FileUtils.openOutputStream(imageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }
}
