package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    Button button;
    TextView weatherInfoText;
    EditText editText;

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection httpURLConnection = null;


            try {
                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader((inputStream));
                int data = inputStreamReader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = inputStreamReader.read();

                }

                return result;
            } catch (Exception e) {
                e.printStackTrace();


                return null;
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String weatherInfo = jsonObject.getString("weather");
                    JSONArray jsonArray = new JSONArray(weatherInfo);
                    String main = (jsonArray.getJSONObject(0).getString("main"));
                    String description = jsonArray.getJSONObject(0).getString("description");
                    String message;

                    message = main + ":" + description;

                    weatherInfoText.setText(message);
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
            else
                Toast.makeText(getApplicationContext(), "Could not find weather ", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
        weatherInfoText=findViewById(R.id.weatherInfo);
        editText=findViewById(R.id.editText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
               imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                DownloadTask task=new DownloadTask();
                task.execute("http://openweathermap.org/data/2.5/weather?q="+editText.getText()+"&appid=439d4b804bc8187953eb36d2a8c26a02");

            }
        });
    }


}
