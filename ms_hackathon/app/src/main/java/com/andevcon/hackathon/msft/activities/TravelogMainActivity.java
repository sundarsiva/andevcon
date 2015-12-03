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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andevcon.hackathon.msft.R;
import com.andevcon.hackathon.msft.api.ApiClient;
import com.andevcon.hackathon.msft.fragments.PagesListFragment;
import com.andevcon.hackathon.msft.helpers.DataStore;
import com.andevcon.hackathon.msft.model.UsersDTO;
import com.andevcon.hackathon.msft.model.UsersValue;
import com.microsoft.onenotevos.Envelope;
import com.microsoft.onenotevos.Section;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TravelogMainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    // arguments for this activity
    public static final String ARG_GIVEN_NAME = "givenName";
    public static final String ARG_DISPLAY_ID = "displayableId";
    public static String userName;
    public static String userDisplayName;

    private TextView tvUserName;
    private CircleImageView cvUserImg;

    public static final String TAG = TravelogMainActivity.class.getCanonicalName();

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private Adapter mAdapter;
    private Section[] mSections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userName = bundle.getString(ARG_GIVEN_NAME);
            userDisplayName = bundle.getString(ARG_DISPLAY_ID);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                launchNewPageActivity();
            }
        });

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        fetchSections();
        cvUserImg = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.cv_user_img);
        tvUserName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_user_name);
        if(!TextUtils.isEmpty(userName)){
            tvUserName.setText(userName);
        }
        ApiClient.apiService.getUserPhoto(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Bitmap bitMap = null;
                try {
                    bitMap = BitmapFactory.decodeStream(new BufferedInputStream(response.getBody().in()));
                    if(bitMap !=null){
                        cvUserImg.setImageBitmap(bitMap);
                    }
                } catch (IOException e) {
                    //Do Nothing
                } finally {
                    //Close Input Stream
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        Menu navMenu = navigationView.getMenu();
        MenuItem friends = navMenu.findItem(R.id.nav_friends);
        friends.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ApiClient.apiService.getUsers(new Callback<UsersValue>() {
                    @Override
                    public void success(UsersValue usersDTOs, Response response) {
                        DataStore.setUsersValue(usersDTOs);
                        Toast.makeText(getApplicationContext(), String.valueOf(usersDTOs.getValue().size()), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getApplicationContext(), "Failed to fetch Friends", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }
        });
    }

    private void launchNewPageActivity() {
        String sectionId = mSections[mViewPager.getCurrentItem()].id;
        Intent i = new Intent(this, CreatePostActivity.class);
        i.putExtra(CreatePostActivity.EXTRA_SECTION_ID, sectionId);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetchSections() {
        mAdapter = new Adapter(getSupportFragmentManager());
        ApiClient.apiService.getSections(new Callback<Envelope<Section>>() {
            @Override
            public void success(Envelope<Section> sectionEnvelope, Response response) {
                mSections = sectionEnvelope.value;
                for(Section section: mSections) {
                    mAdapter.addFragment(PagesListFragment.newInstance(section.id), section.name);
                }
                setupViewPager(mAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, error.toString());
            }
        });
    }

    private void setupViewPager(Adapter adapter) {
        mViewPager.setOffscreenPageLimit(mSections.length);
        mViewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
