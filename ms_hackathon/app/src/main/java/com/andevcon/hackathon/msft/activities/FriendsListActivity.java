package com.andevcon.hackathon.msft.activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.andevcon.hackathon.msft.R;
import com.andevcon.hackathon.msft.adapters.FriendsListAdapter;
import com.andevcon.hackathon.msft.helpers.DataStore;

/**
 * Created by krunalshah on 12/2/15.
 */
public class FriendsListActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.friends_activity);
        final ListView listView = (ListView) findViewById(R.id.lvUsers);

        FriendsListAdapter adapter = new FriendsListAdapter(getApplicationContext(), DataStore.getUsersValue().getValue());
        listView.setAdapter(adapter);
    }
}
