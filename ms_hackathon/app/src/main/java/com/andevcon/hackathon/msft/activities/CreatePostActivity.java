package com.andevcon.hackathon.msft.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.andevcon.hackathon.msft.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreatePostActivity extends AppCompatActivity {

    @Bind(R.id.etDesc)
    EditText etDesc;

    @Bind(R.id.etTitle)
    EditText etTitle;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Create new Page");
            getWindow().setStatusBarColor(getResources().getColor(R.color.travelogColorPrimaryDark));
        }
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
                postNewPost();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void postNewPost() {
        Snackbar.make(etDesc.getRootView(), "Post now", Snackbar.LENGTH_SHORT).show();
    }

}
