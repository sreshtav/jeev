package com.jeev.cs8803.jeevapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TextAndAudioActivity extends AppCompatActivity {

    private String response = "test";

    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(getBaseContext(), AnswerActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);;
            intent.putExtra("RESPONSE", response);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_and_audio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Jeev");
        }

        Button mButtonSubmitButton = (Button) findViewById(R.id.submit);
        mButtonSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.d("Watson", "clicked");
                EditText mEditText = (EditText) findViewById(R.id.question_value);
                final String question = "qu_free_question="+mEditText.getText().toString();
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
