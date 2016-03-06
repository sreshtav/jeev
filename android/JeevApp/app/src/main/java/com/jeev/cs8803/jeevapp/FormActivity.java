package com.jeev.cs8803.jeevapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class FormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            System.out.println("Toolbar here");
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Jeev");
        }


        Button mEmailSignInButton = (Button) findViewById(R.id.select_question);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.d("Watson", "clicked");
                EditText mEdit   = (EditText)findViewById(R.id.size_value);
                String size = mEdit.getText().toString();
                Spinner mSpinner = (Spinner)findViewById(R.id.size_units);
                String sizeUnits = mSpinner.getSelectedItem().toString();
                final String question = "";
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getWatsonAnswer(question);
                    }
                });
                thread.start();
            }
        });
    }

    private String getWatsonAnswer (String question) {
        String response = null;

        try {
            Log.d("Watson", "starting");
            URL url = new URL("http://143.215.97.119/v1/user_question?qu_free_question="+ URLEncoder.encode(question,"UTF-8"));

            //create the connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            Log.d("Watson" , connection.toString());
            BufferedReader rd =  new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                result.append(line + "\n");
            }
            Log.d("Watson", result.toString());
            response = result.toString();
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
            rd.close();
        } catch (Exception e) {
            Log.e("HTTP GET:", e.toString());
        }
        return response;
    }
}
