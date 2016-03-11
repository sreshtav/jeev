package com.jeev.cs8803.jeevapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class AnswerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Jeev");
        }

        String response = getIntent().getExtras().getString("RESPONSE");
        try {
            JSONObject reader = new JSONObject(response);
            TextView mAnswerTextView = (TextView) findViewById(R.id.answer);
            mAnswerTextView.setText(reader.getString("answer"));
            mAnswerTextView.setMovementMethod(new ScrollingMovementMethod());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button mNextQuestionButton = (Button) findViewById(R.id.next_question);
        mNextQuestionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent random = new Intent(getApplicationContext(), SelectPathActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(random);
            }
        });

        Button mExitButton = (Button) findViewById(R.id.exit);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

}
