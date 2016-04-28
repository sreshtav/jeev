package com.jeev.cs8803.jeevapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class AnswerActivity extends AppCompatActivity {

    String animal = "";
    private ProgressDialog pDialog;
    private Bitmap bitmap;
    private String animalImage = "";
    private String imageResponse = "";

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
            JSONArray jsonIndexes = reader.optJSONArray("indexes");
            int[] indexes = new int[jsonIndexes.length()];
            for (int i = 0; i < jsonIndexes.length(); ++i) {
                indexes[i] = jsonIndexes.optInt(i);
                Log.d("Answer", "value: " + indexes[i]);
            }
            Arrays.sort(indexes);
            for (int i=0; i<indexes.length; i++) {
                Log.d("Answer", "value: " + indexes[i]);
            }
            TextView mAnswerTextView = (TextView) findViewById(R.id.answer);
            mAnswerTextView.setText(reader.getString("answer"));
            mAnswerTextView.setMovementMethod(new ScrollingMovementMethod());
            animal = reader.getString("animal");
            if (! animal.isEmpty()) {
                //Toast.makeText(getApplicationContext(), animal, Toast.LENGTH_SHORT).show();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        animalImage = getImage();
                        AnswerActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                new LoadImage().execute(animalImage);
                            }
                        });
                    }
                });
                thread.start();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button mStartOverButton = (Button) findViewById(R.id.start_over);
        mStartOverButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent random = new Intent(getApplicationContext(), SelectPathActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(random);
            }
        });

        Button mNextQuestionButton = (Button) findViewById(R.id.next_question);
        mNextQuestionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent random = new Intent(getApplicationContext(), TextAndAudioActivity.class);
                random.putExtra("ANIMAL", animal);
                startActivity(random);
            }
        });

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
            JSONObject image;
            if (animal.equalsIgnoreCase("virginia opossum")) {
                image = (JSONObject) reader.getJSONArray("value").get(1);
            } else {
                image = (JSONObject) reader.getJSONArray("value").get(0);
            }
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
            pDialog = new ProgressDialog(AnswerActivity.this);
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
                Toast.makeText(AnswerActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private String getShortenedAnswer (String answer, int[] indexes) {
        String shortenedAnswer = "";
        shortenedAnswer += ".....";
        String[] sentences = answer.split("\\.");
        Log.d("Answer", "after split length - " + sentences.length);
        for (int i=0; i<indexes.length; i++) {
            shortenedAnswer += sentences[indexes[i]];
        }
        shortenedAnswer += ".....";
        Log.d("Answer", shortenedAnswer);
        return shortenedAnswer;
    }

}
