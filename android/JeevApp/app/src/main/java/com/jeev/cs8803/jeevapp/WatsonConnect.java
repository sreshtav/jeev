package com.jeev.cs8803.jeevapp;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by sreshtav on 3/5/16.
 */
public class WatsonConnect {

    public static String getAnswerFromWatson (String question) {
        String response = "";
        try {
            Log.d("Watson", "starting");
            URL url = new URL("http://143.215.97.119/v1/user_question?"+ URLEncoder.encode(question, "UTF-8"));

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
            rd.close();
        } catch (Exception e) {
            Log.e("HTTP GET:", e.toString());
        }
        return response;
    }

}
