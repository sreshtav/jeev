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
import android.widget.EditText;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TextAndAudioActivity extends AppCompatActivity {

    private String response = "test";

    private String encoder (String text) {
        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(getBaseContext(), AnswerActivity.class);
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
                final String question = "qu_free_question="+encoder(mEditText.getText().toString());
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        response = WatsonConnect.getAnswerFromWatson(question);
                        Log.d("Watson", response);
                        messageHandler.sendEmptyMessage(0);
                    }
                });
                thread.start();
            }
        });
    }

}
