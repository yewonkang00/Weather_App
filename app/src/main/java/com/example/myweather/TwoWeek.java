package com.example.myweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class TwoWeek extends AppCompatActivity {
 //  TextView[] textView = {(TextView)findViewById(R.id.textViewa), (TextView)findViewById(R.id.textViewb), (TextView)findViewById(R.id.textViewc), (TextView)findViewById(R.id.textViewd), (TextView)findViewById(R.id.textViewe),
  //        (TextView)findViewById(R.id.textViewf), (TextView)findViewById(R.id.textViewg), (TextView)findViewById(R.id.textViewh), (TextView)findViewById(R.id.textViewi), (TextView)findViewById(R.id.textViewj)};
    TextView[] textView ;
    TextView textView9;
    String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Intent intent = getIntent();
        final int pos = intent.getIntExtra("position", 0);
        */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_week);

        textView = new TextView[]{(TextView) findViewById(R.id.textViewa), (TextView) findViewById(R.id.textViewb), (TextView) findViewById(R.id.textViewc), (TextView) findViewById(R.id.textViewd), (TextView) findViewById(R.id.textViewe),
                (TextView) findViewById(R.id.textViewf), (TextView) findViewById(R.id.textViewg), (TextView) findViewById(R.id.textViewh), (TextView) findViewById(R.id.textViewi), (TextView) findViewById(R.id.textViewj)};

        textView9 = (TextView)findViewById(R.id.textViewk);
        /*

        textViewa = (TextView) findViewById(R.id.textViewa);
        textViewb = (TextView) findViewById(R.id.textViewb);
        textViewc = (TextView) findViewById(R.id.textViewc);
        textViewd = (TextView) findViewById(R.id.textViewd);
        textViewe = (TextView) findViewById(R.id.textViewe);
        textViewf = (TextView) findViewById(R.id.textViewf);
        textViewg = (TextView) findViewById(R.id.textViewg);
        textViewh = (TextView) findViewById(R.id.textViewh);
        textViewi = (TextView) findViewById(R.id.textViewi);
        textViewj = (TextView) findViewById(R.id.textViewj);*/



        new weekTask().execute();

    }


    class weekTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        private void getWeatherDataFromJson(String forecastJsonStr, int numDays) throws JSONException {

        }

        protected String doInBackground(String... args) {
            final String LOG_TAG = DetailActivity.weatherTask.class.getSimpleName();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 14;




            Intent intent = getIntent();
            int pos = intent.getIntExtra("position", 0);
            String keys = "jeju";
            switch (pos) {
                case 0:
                    keys = "Seoul";
                    break;
                case 1:
                    keys = "Suwon";
                    break;
                case 2:
                    keys = "Incheon";
                    break;
                case 3:
                    keys = "Busan";
                    break;
                case 4:
                    keys = "Daegu";
                    break;
                case 5:
                    keys = "Jeju";
                    break;
                case 6:
                    keys = "Gangneung";
                    break;
            }

            city = keys;
            try {
               final String FORECAST_BASE_URL = "https://api.openweathermap.org/data/2.5/forecast/daily?q="+ keys +"&cnt=10&appid=5fd2f2cde90c1533efb95b19c048a528&units=metric";
           //     final String FORECAST_BASE_URL = "https://api.openweathermap.org/data/2.5/forecast/daily?q=seoul&cnt=10&appid=5fd2f2cde90c1533efb95b19c048a528&units=metric";
                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon().build();
                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                    Log.v(LOG_TAG, "Forecast string: " + line);
                }


                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                getWeatherDataFromJson(forecastJsonStr, numDays);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return forecastJsonStr;
        }

        protected void onPostExecute(String result) {

             try{
                 JSONObject jsonObj = new JSONObject(result);
  //               JSONObject main = jsonObj.getJSONObject("list");
 //                JSONObject sys = jsonObj.getJSONObject("sys");
  //               JSONObject wind = jsonObj.getJSONObject("wind");
                 JSONObject[] list = new JSONObject[10];
                 JSONObject[] temp = new JSONObject[10];
                 JSONObject[] weather = new JSONObject[10];

                 for(int i = 0; i < 10; i++){
                     list[i] = jsonObj.getJSONArray("list").getJSONObject(i);
                     temp[i] = list[i].getJSONObject("temp");
                     weather[i] = list[i].getJSONArray("weather").getJSONObject(0);
 //                    temp[i] = list[i].getJSONArray("temp").getString(0);
 //                    weather[i] = list[i].getJSONArray("weather").getJSONObject(i);

                 }

                 //JSONObject weather = jsonObj.getJSONArray("list").getJSONObject(0);
                /* JSONObject weather2 = jsonObj.getJSONArray("list").getJSONObject(1);
                 JSONObject weather3 = jsonObj.getJSONArray("list").getJSONObject(2);
                 JSONObject weather4 = jsonObj.getJSONArray("list").getJSONObject(3);
                 JSONObject weather5 = jsonObj.getJSONArray("list").getJSONObject(4);
                 JSONObject weather6 = jsonObj.getJSONArray("list").getJSONObject(5);
                 JSONObject weather7 = jsonObj.getJSONArray("list").getJSONObject(6);
                 JSONObject weather8 = jsonObj.getJSONArray("list").getJSONObject(7);
                 JSONObject weather9 = jsonObj.getJSONArray("list").getJSONObject(8);
                 JSONObject weather10 = jsonObj.getJSONArray("list").getJSONObject(9);
*/

//                 Long updateAt = jsonObj.getLong("dt");
 //                String updatedAtText = new SimpleDateFormat("MM/dd", Locale.KOREA).format(new Date(updateAt * 1000));
 //                String day = new SimpleDateFormat("EE", Locale.KOREA).format(new Date(updateAt * 1000));
 //                String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
 //                String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                 Long[] dt = new Long[10];
                 String[] updatedAtText = new String[10];
                 String[] day = new String[10];
                 int[] tempMin = new int[10];
                 int[] tempMax = new int[10];
                 String[] description = new String[10];



                // textViewa.setText(temp[0].getString("day"));
//                 textViewa.setText("sss");
                 for(int i = 0; i < 10; i++){
                     dt[i] = list[i].getLong("dt");
                     updatedAtText[i] = new SimpleDateFormat("MM/dd", Locale.ENGLISH).format(new Date(dt[i] * 1000));
                     day[i] =  new SimpleDateFormat("EE", Locale.ENGLISH).format(new Date(dt[i] * 1000));
                     tempMax[i] = temp[i].getInt("max");
                     tempMin[i] = temp[i].getInt("min");
                     description[i] = weather[i].getString("main");
                    // day[i] = temp[i].getString("day");
                     //textView[i].setText(day[i]);
 //                    textView[i].setText("ddd");
                     textView[i].setText("- " + day[i] + " " + updatedAtText[i] + " : " + description[i] + " - " + tempMax[i] + " ~ " + tempMin[i] +" °C");
                 }

                 textView9.setText(city + " 10 days weather");

                 /*String dt = weather.getString("dt");
                 String dt2 = weather2.getString("dt");
                 String
*/

             } catch (JSONException e) {

                }


        }
    }

}
