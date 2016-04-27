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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

public class FormActivity extends AppCompatActivity {

    private String response;

    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(getBaseContext(), AnswerActivity.class);
            intent.putExtra("RESPONSE", response);
            startActivity(intent);
        }
    };

    private String encoder (String text) {
        try {
            return URLEncoder.encode(text, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

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

        Button mSubmitButton = (Button) findViewById(R.id.submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
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
                boolean canSwim = mCheckBox.isChecked();
                mSpinner = (Spinner)findViewById(R.id.eating_habits_value);
                String eatingHabits = mSpinner.getSelectedItem().toString();
                mEdit = (EditText)findViewById(R.id.teeth_value);
                String teeth = mEdit.getText().toString();
                mEdit = (EditText)findViewById(R.id.tail_value);
                String tail = mEdit.getText().toString();
                mCheckBox = (CheckBox)findViewById(R.id.nearby);
                boolean nearby = mCheckBox.isChecked();
                //if nearby get GPS coordinates
                mEdit = (EditText)findViewById(R.id.native_value);
                String nativeTo = mEdit.getText().toString();
                String tempQuestion = "";
                if (!size.isEmpty()) {
                    tempQuestion = "qu_size="+encoder(size)+"&qu_size_units="+encoder(sizeUnits)+"&qu_body_color="+encoder(bodyColor)+"&qu_body_coat="+encoder(bodyCoat)+"&qu_pattern_on_body="+encoder(patternOnBody)+
                            "&qu_can_swim="+canSwim+"&qu_eating_habits="+encoder(eatingHabits)+"&qu_teeth="+encoder(teeth)+"&qu_tail="+encoder(tail)+"&qu_nearby="+nearby+"&qu_native="+encoder(nativeTo);
                } else {
                    tempQuestion = "&qu_body_color="+encoder(bodyColor)+"&qu_body_coat="+encoder(bodyCoat)+"&qu_pattern_on_body="+encoder(patternOnBody)+
                            "&qu_can_swim="+canSwim+"&qu_eating_habits="+encoder(eatingHabits)+"&qu_teeth="+encoder(teeth)+"&qu_tail="+encoder(tail)+"&qu_nearby="+nearby+"&qu_native="+encoder(nativeTo);
                }
                final String question = tempQuestion;
                Log.d("Watson", question);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        response = WatsonConnect.getAnswerFromWatson(question);
                        messageHandler.sendEmptyMessage(0);
                    }
                });
                thread.start();
            }
        });
    }
}
