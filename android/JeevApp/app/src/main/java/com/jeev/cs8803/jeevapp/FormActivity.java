package com.jeev.cs8803.jeevapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

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
                String question = "What is mint?";
                try {
                    URL url = null;
                    String response = null;
                    String parameters = "qu_free_question=Does mint have healing properties?";
                    url = new URL("http://143.215.97.119/v1/user_question");

                    //create the connection
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    //set the request method to GET
                    connection.setRequestMethod("GET");
                    //get the output stream from the connection you created
                    OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());
                    //write your data to the ouputstream
                    request.write(parameters);
                    request.flush();
                    request.close();
                    String line = "";
                    //create your inputsream
                    InputStreamReader isr = new InputStreamReader(
                            connection.getInputStream());
                    //read in the data from input stream, this can be done a variety of ways
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    //get the string version of the response data
                    response = sb.toString();
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    //do what you want with the data now
                    //always remember to close your input and output streams
                    isr.close();
                    reader.close();
                } catch (Exception e) {
                    Log.e("HTTP GET:", e.toString());
                }
            }
        });
    }

}
