package com.iammukesh.jsondemomy;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    TextView resulttext;
    public void checkWeather(View view){
        DownloadTemp downloader = new DownloadTemp();
        TextView city = (TextView)findViewById(R.id.cityName);
        String filterCity = city.getText().toString().replaceAll(" ", "");
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(city.getWindowToken(), 0);
       downloader.execute("http://api.openweathermap.org/data/2.5/weather?q=" + filterCity + "&appid=44db6a862fba0b067b1930da0d769e98");
    }

    public class DownloadTemp extends AsyncTask<String , Void , String>{


        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpURLConnection connection= null;
            String result="";

            try {

                url= new URL(urls[0]);

                connection = (HttpURLConnection)url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                    char current =(char) data;
                    result += current;
                    data =reader.read();

                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject json = null;
            try {
                json = new JSONObject(result);
                String weatherInfo = json.getString("weather");
                Log.i("Whether of City",weatherInfo);
                JSONArray arr = new JSONArray(weatherInfo);
                int i = 0;
                String main="'";
                String description="";
                String message ="";
                while (i < arr.length()){
                    JSONObject JSONpart = arr.getJSONObject(i);
                    main = JSONpart.getString("main");
                    description = JSONpart.getString("description");
                    if(main !="" && description != ""){
                        message +="Weather Report\n"+ main + " : " + description+"\r\n";
                        resulttext.setText(message);
                        Log.i("City",message);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "City Not Found", Toast.LENGTH_SHORT).show();
                    }
                    i++;

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"Opps! Something Went Wrong with Weather App",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         resulttext = (TextView)findViewById(R.id.textView2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
