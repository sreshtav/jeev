package com.jeev.cs8803.jeevapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class TextAndAudioActivity extends AppCompatActivity {

    private String response = "test";
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private String encoder(String text) {
        try {
            return URLEncoder.encode(text, "UTF-8").replace("+", "%20");
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
                final String question = "qu_free_question=" + encoder(mEditText.getText().toString());
                //final String question = "qu_free_question="+mEditText.getText().toString();
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

        ImageButton mButtonAudioButton = (ImageButton) findViewById(R.id.audio);
        mButtonAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    EditText mEditText = (EditText) findViewById(R.id.question_value);
                    mEditText.setText(result.get(0));
                }
                break;
            }

        }
    }
}
