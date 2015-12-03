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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andevcon.hackathon.msft.R;
import com.andevcon.hackathon.msft.api.ApiClient;
import com.andevcon.hackathon.msft.model.Images;
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
    public static final String EXTRA_PAGE_NAME = "mPageName";

    TextView mContentView;
    String mPageId;
    private String mPageName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        mPageId = intent.getStringExtra(EXTRA_PAGE_ID);
        mPageName = intent.getStringExtra(EXTRA_PAGE_NAME);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mPageName);

        mContentView = (TextView) findViewById(R.id.detail_tv_notes_content);

        loadPageContent();
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
                    String strFileContents = "";
                    while( (bytesRead = bis.read(contents)) != -1){
                        strFileContents = strFileContents + new String(contents, 0, bytesRead);
                    }

                    Pattern p = Pattern.compile("src=\"(.*?)\"");
                    Matcher m = p.matcher(strFileContents);

                    if (m.find()) {
                        imageUrl = m.group(1); // prints http://www.01net.com/images/article/mea/150.100.790233.jpg
                    }
                    String withoutImage = strFileContents.replaceAll("<img .*?/>","");
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
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.action_edit:
                showToast("Edit the post");
                return true;
            case R.id.action_delete:
                deletePost();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deletePost() {
        ApiClient.apiService.deletePage(mPageId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log.d(TAG, "Successful delete - " + response2.getStatus());
                launchHome();
            }
            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, Log.getStackTraceString(error));
                showToast("Error occured while trying to delete this post");
            }
        });
    }

    private void launchHome() {
        Intent intent = new Intent(this, TravelogMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finish();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
