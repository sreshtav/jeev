package com.jeev.cs8803.jeevapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class FormActivity extends AppCompatActivity {

    private String response = "test";

    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(getBaseContext(), AnswerActivity.class);
            intent.putExtra("RESPONSE", response); //
            startActivity(intent);
            //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
        }
    };

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
                mEdit = (EditText)findViewById(R.id.body_color_value);
                String bodyColor = mEdit.getText().toString();
                mEdit = (EditText)findViewById(R.id.body_coat_value);
                String bodyCoat = mEdit.getText().toString();
                mEdit = (EditText)findViewById(R.id.pattern_on_body_value);
                String patternOnBody = mEdit.getText().toString();
                CheckBox mCheckBox = (CheckBox)findViewById(R.id.can_swim_value);
                String canSwim = mCheckBox.getText().toString();
                mSpinner = (Spinner)findViewById(R.id.eating_habits_value);
                String eatingHabits = mSpinner.getSelectedItem().toString();
                mEdit = (EditText)findViewById(R.id.teeth_value);
                String teeth = mEdit.getText().toString();
                mEdit = (EditText)findViewById(R.id.tail_value);
                String tail = mEdit.getText().toString();
                mCheckBox = (CheckBox)findViewById(R.id.nearby);
                String nearby = mCheckBox.getText().toString();
                //if nearby get GPS coordinates
                final String question = "qu_size="+size+"&qu_size_units="+sizeUnits+"&qu_body_color="+bodyColor+"&qu_body_coat="+bodyCoat+"&qu_pattern_on_body="+patternOnBody+
                        "&qu_can_swim="+canSwim+"&qu_eating_habits="+eatingHabits+"&qu_teeth="+teeth+"&qu_tail="+tail+"&qu_nearby="+nearby;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //getWatsonAnswer(question);
                        messageHandler.sendEmptyMessage(0);
                    }
                });
                thread.start();
            }
        });
    }

    private void getWatsonAnswer (String question) {
        try {
            Log.d("Watson", "starting");
            URL url = new URL("http://143.215.97.119/v1/user_question?"+ URLEncoder.encode(question,"UTF-8"));

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
    }
}
