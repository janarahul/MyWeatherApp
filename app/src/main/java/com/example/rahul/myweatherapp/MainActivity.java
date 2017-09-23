package com.example.rahul.myweatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private class DownloadJSON extends AsyncTask<String, Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                Log.i("before","eeeee");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                Log.i("after","eeeee");
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                int data ;
                String result=null;
                data = isr.read();
                while(data != -1){
                    result += (char)data;
                    data = isr.read();
                }
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("weatherinner",result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadJSON dj = new DownloadJSON();
        try {
            String json = dj.execute("http://api.openweathermap.org/data/2.5/weather?q=Bengaluru&appid=ba9b478ac03c34c89c858c6e03f66077").get();
            Log.i("weather",json);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}
