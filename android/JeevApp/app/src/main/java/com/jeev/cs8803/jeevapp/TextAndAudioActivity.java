package com.jeev.cs8803.jeevapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

public class TextAndAudioActivity extends AppCompatActivity {

    private String response = "";
    private String imageResponse = "";
    private String animal = "";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private ProgressDialog pDialog;
    private Bitmap bitmap;
    private String animalImage = "";

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

        if (getIntent().getExtras() != null) {
            animal = getIntent().getExtras().getString("ANIMAL");
        }
        if (! animal.isEmpty()) {
            Toast.makeText(getApplicationContext(), animal, Toast.LENGTH_SHORT).show();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    animalImage = getImage();
                    TextAndAudioActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            new LoadImage().execute(animalImage);
                        }
                    });
                }
            });
            thread.start();

        }

        Button mButtonSubmitButton = (Button) findViewById(R.id.submit);
        mButtonSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Log.d("Watson", "clicked");
                EditText mEditText = (EditText) findViewById(R.id.question_value);
                String animalString = "";
                if (!animal.isEmpty()) {
                    animalString = "&qu_animal_name=" + animal.replace(" ", "%20");
                }
                final String question = "qu_free_question=" + encoder(mEditText.getText().toString() + animalString);
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

    private String getImage() {
        try {
            URL url = new URL("https://bingapis.azure-api.net/api/v5/images/search?q=" + animal.trim().replace(" ", "+"));
            //create the connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Ocp-Apim-Subscription-Key", "e70d815227094f13a447333fc5664b56");
            BufferedReader rd =  new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                result.append(line + "\n");
            }
            imageResponse = result.toString();
            rd.close();
            JSONObject reader = new JSONObject(imageResponse);
            JSONObject image = (JSONObject) reader.getJSONArray("value").get(0);
            return image.getString("contentUrl");
        } catch (Exception e) {
            Log.e("HTTP GET:", e.toString());
            return null;
        }
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TextAndAudioActivity.this);
            pDialog.setMessage("Loading Image ....");
            pDialog.show();

        }

        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);
                BitmapDrawable bmpDrawable = new BitmapDrawable(getResources(), image);
                bmpDrawable.setAlpha(100);
                scrollView.setBackground(bmpDrawable);
                pDialog.dismiss();
            }else{
                pDialog.dismiss();
                Toast.makeText(TextAndAudioActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
