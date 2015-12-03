package com.andevcon.hackathon.msft.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.andevcon.hackathon.msft.R;
import com.andevcon.hackathon.msft.adapters.FriendsListAdapter;
import com.andevcon.hackathon.msft.helpers.DataStore;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by krunalshah on 12/2/15.
 */
public class FriendsListActivity extends AppCompatActivity {

    @Bind(R.id.friends_toolbar)
    Toolbar toolbar;

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.friends_activity);
        final ListView listView = (ListView) findViewById(R.id.lvUsers);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Friends");
            getWindow().setStatusBarColor(getResources().getColor(R.color.travelogColorPrimaryDark));
        }

        FriendsListAdapter adapter = new FriendsListAdapter(getApplicationContext(), DataStore.getUsersValue().getValue());
        listView.setAdapter(adapter);
    }
}
