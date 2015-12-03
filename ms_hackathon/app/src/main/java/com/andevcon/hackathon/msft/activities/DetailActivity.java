/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andevcon.hackathon.msft.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andevcon.hackathon.msft.R;
import com.andevcon.hackathon.msft.api.ApiClient;
import com.andevcon.hackathon.msft.model.Images;
import com.microsoft.office365.connectmicrosoftgraph.MSGraphAPIController;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = DetailActivity.class.getCanonicalName();

    public static final String EXTRA_PAGE_ID = "pageId";
    public static final String EXTRA_PAGE_NAME = "pageName";

    TextView mContentView;
    String mPageId;
    static String mEmailContents = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        mPageId = intent.getStringExtra(EXTRA_PAGE_ID);
        String pageName = intent.getStringExtra(EXTRA_PAGE_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(pageName);

        View emailButton = findViewById(R.id.email_contact);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContactsChooser();
            }
        });

        mContentView = (TextView) findViewById(R.id.detail_tv_notes_content);

        loadPageContent();
    }

    private void showContactsChooser() {
        new ContactsDialogFragment().show(getSupportFragmentManager(), "ContactsDialogFragment");
    }

    public static class ContactsDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.pick_a_contact)
                    .setItems(new String[]{"A", "B", "C"}, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            onSendMail(mEmailContents, "A", "sundar5583@gmail.com");
                            dismiss();
                        }
                    });
            return builder.create();
        }
    }

    private void loadBackdrop(String imageUrl) {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        if(!TextUtils.isEmpty(imageUrl)) {
            imageUrl = imageUrl.replace("/$value", "");
            String resourceId = imageUrl.substring(imageUrl.lastIndexOf("/")+1, imageUrl.length());
            //ApiClient.getGenericApiService(imageUrl).getSomething(new Callback<Response>() {
            ApiClient.apiService.getPageImageResource(resourceId, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    Bitmap bitMap;
                    BufferedInputStream bis = null;
                    try {
                        bis = new BufferedInputStream(response.getBody().in());
                        bitMap = BitmapFactory.decodeStream(bis);
                        if(bitMap !=null){
                            imageView.setImageBitmap(bitMap);
                        }
                    } catch (IOException e) {
                        //Do Nothing
                    } finally {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Picasso.with(DetailActivity.this).load(Images.getRandomCheeseDrawable()).into(imageView);
                }
            });
        } else {
            Picasso.with(this).load(Images.getRandomCheeseDrawable()).into(imageView);
        }
    }

    private void loadPageContent() {
        ApiClient.apiService.getPageContentById(mPageId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                String imageUrl = null;
                try {
                    InputStream is = response.getBody().in();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] contents = new byte[1024];

                    int bytesRead = 0;
                    while( (bytesRead = bis.read(contents)) != -1){
                        mEmailContents = mEmailContents + new String(contents, 0, bytesRead);
                    }

                    Pattern p = Pattern.compile("src=\"(.*?)\"");
                    Matcher m = p.matcher(mEmailContents);

                    if (m.find()) {
                        imageUrl = m.group(1); // prints http://www.01net.com/images/article/mea/150.100.790233.jpg
                    }
                    String withoutImage = mEmailContents.replaceAll("<img .*?/>","");
                    mContentView.setText(Html.fromHtml(withoutImage));
                    loadBackdrop(imageUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "failure: ", error);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    private static void onSendMail(String emailBody, String toEmail, String subject) {

        new MSGraphAPIController()
                .sendMail(
                        toEmail,
                        subject,
                        emailBody,
                        new Callback<Void>() {
                            @Override
                            public void success(Void aVoid, Response response) {
                            }

                            @Override
                            public void failure(RetrofitError error) {
                            }
                        });
    }
}
