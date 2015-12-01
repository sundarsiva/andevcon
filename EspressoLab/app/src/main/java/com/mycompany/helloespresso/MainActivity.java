/*******************************************************************************
 * Â© Copyright 2015, James P White,  All Rights Reserved.
 * Created by Jim White on 11/12/2015.
 ******************************************************************************/
package com.mycompany.helloespresso;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText eMailET, nameET, favoriteTeamET;
    private Spinner favoriteSportSP;
    private CheckBox seniorCB;
    private RadioButton maleRB, femaleRB;
    private Button submitBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eMailET = (EditText) findViewById(R.id.emailET);
        nameET = (EditText) findViewById(R.id.nameET);
        favoriteTeamET = (EditText) findViewById(R.id.teamET);
        favoriteSportSP = (Spinner) findViewById(R.id.sportSP);
        seniorCB = (CheckBox) findViewById(R.id.seniorCB);
        maleRB = (RadioButton) findViewById(R.id.maleRB);
        femaleRB = (RadioButton) findViewById(R.id.femaleRB);
        submitBT = (Button) findViewById(R.id.submitBT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.about_title)
                    .setMessage(R.string.about_message)
                    .setNeutralButton(android.R.string.ok, null)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void submit(View view) {
        if (eMailET.getText() != null && eMailET.getText().length() > 0) {
            Toast.makeText(this, R.string.data_saved, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, R.string.data_needed, Toast.LENGTH_LONG).show();
        }
    }

    public void findMe(View view) {
        if (eMailET.getText() != null && eMailET.getText().length() > 0) {
            // mimic service to populate the name
            nameET.setText("Barrack Obama");
            nameET.setEnabled(true);
            maleRB.setChecked(true);
            maleRB.setEnabled(true);
            femaleRB.setEnabled(true);
            seniorCB.setEnabled(true);
            favoriteSportSP.setSelection(1);
            favoriteSportSP.setClickable(true);
            favoriteTeamET.setText("Chicago Bulls");
            favoriteTeamET.setEnabled(true);
            submitBT.setEnabled(true);
        } else {
            nameET.setText("");
            nameET.setEnabled(false);
            maleRB.setChecked(false);
            maleRB.setEnabled(false);
            femaleRB.setEnabled(false);
            seniorCB.setEnabled(false);
            favoriteSportSP.setSelection(0);
            favoriteSportSP.setClickable(false);
            favoriteTeamET.setText("");
            favoriteTeamET.setEnabled(false);
            submitBT.setEnabled(false);
            Toast.makeText(this, R.string.data_needed, Toast.LENGTH_LONG).show();
        }
    }
}