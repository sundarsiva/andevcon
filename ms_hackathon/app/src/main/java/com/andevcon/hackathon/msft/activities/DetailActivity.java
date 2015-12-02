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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.andevcon.hackathon.msft.R;
import com.andevcon.hackathon.msft.api.ApiClient;
import com.andevcon.hackathon.msft.model.Images;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailActivity extends AppCompatActivity {

    public static final String TAG = DetailActivity.class.getCanonicalName();

    public static final String EXTRA_PAGE_ID = "pageId";
    public static final String EXTRA_PAGE_NAME = "pageName";

    WebView mWebView;
    String mPageId;

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

        loadBackdrop();

        mWebView = (WebView) findViewById(R.id.detail_tv_notes_content);


        loadPageContent();
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        Picasso.with(this).load(Images.getRandomCheeseDrawable()).into(imageView);
    }

    private void loadPageContent() {
        ApiClient.apiService.getPageContentById(mPageId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    InputStream is = response.getBody().in();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] contents = new byte[1024];

                    int bytesRead = 0;
                    String strFileContents = null;
                    while( (bytesRead = bis.read(contents)) != -1){
                        strFileContents = new String(contents, 0, bytesRead);
                    }
                    mWebView.loadData(strFileContents, "text/html", "UTF-8");
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
}
