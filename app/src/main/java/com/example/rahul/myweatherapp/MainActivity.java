package com.example.rahul.myweatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String main,description;
    TextView textView;
    EditText editText;
    private class DownloadJSON extends AsyncTask<String, Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                int data ;
                String result=null;
                data = isr.read();
                while(data != -1){
                    result += (char)data;
                    data = isr.read();
                }
                result = result.replace("null","");
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject resultJSON = new JSONObject(result);
                String weather = resultJSON.getString("weather");
                Log.i("weather",weather);

                JSONArray jsonArray = new JSONArray(weather);

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonPart = jsonArray.getJSONObject(i);
                    textView.append(jsonPart.getString("main")+"\n");
                    //textView.append(jsonPart.getString("description")+"\n");
                }

                String metrics = resultJSON.getString("main");
                Log.i("metricmain",metrics);

                JSONObject jsonObject = new JSONObject(metrics);


                    textView.append("Temperature: "+jsonObject.getString("temp")+"°C\n");
                    textView.append("Humidity: "+jsonObject.getString("humidity")+"\n");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText)findViewById(R.id.editText);
        textView =(TextView)findViewById(R.id.textView);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    textView.setText("");
                    DownloadJSON dj = new DownloadJSON();
                    dj.execute("http://api.openweathermap.org/data/2.5/weather?q="+editText.getText()+"&units=metric&appid=ba9b478ac03c34c89c858c6e03f66077");

                    return true;

                }
                return false; // pass on to other listeners.
            }
        });




    }
}
