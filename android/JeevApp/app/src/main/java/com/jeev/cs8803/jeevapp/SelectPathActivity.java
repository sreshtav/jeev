package com.jeev.cs8803.jeevapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class SelectPathActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_path);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button mFormButton = (Button) findViewById(R.id.select_form);
        if (toolbar != null) {
            System.out.println("Toolbar here");
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Jeev");
        }

        mFormButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(getApplicationContext(), FormActivity.class);
                startActivity(myIntent);
            }
        });
    }

}
