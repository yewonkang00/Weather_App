package com.example.myweather;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.myweather.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {
    TextView t1_city, t2_date, t3_temp, t4_description, t5_max, t6_min, t7_humidity, t8_speed;
    ImageView picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        final int key = intent.getIntExtra("citykey", 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        t1_city = (TextView) findViewById(R.id.textView3);
        t2_date = (TextView) findViewById(R.id.textView2);
        t3_temp = (TextView) findViewById(R.id.textView);
        t4_description = (TextView) findViewById(R.id.textView4);
        t5_max = (TextView)findViewById(R.id.textView5);
        t6_min = (TextView)findViewById(R.id.textView6);
        t7_humidity = (TextView)findViewById(R.id.textView7);
        t8_speed = (TextView)findViewById(R.id.textView8);
        picture = (ImageView)findViewById(R.id.pic);

        Button b = (Button)findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), TwoWeek.class);
                intent.putExtra("position", key);
                startActivity(intent);
            }
        });



        //      find_weather();

        new weatherTask().execute();
    }

    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /* Showing the ProgressBar, Making the main design GONE */
//            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            //           findViewById(R.id.mainContainer).setVisibility(View.GONE);
            //          findViewById(R.id.errorText).setVisibility(View.GONE);
        }
        private void getWeatherDataFromJson(String forecastJsonStr, int numDays) throws JSONException {

        }

        protected String doInBackground(String... args) {
//            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
//            String response = HttpRequest.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=seoul&appid=5fd2f2cde90c1533efb95b19c048a528&units=Imperial");
            //           return response;
            final String LOG_TAG = weatherTask.class.getSimpleName();
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 14;

            Intent intent = getIntent();
            int key = intent.getIntExtra("citykey", 0);
            String keys = "jeju";
            switch (key){
                case 0 :
                    keys = "seoul";
                    break;
                case 1 :
                    keys = "suwon";
                    break;
                case 2 :
                    keys = "incheon";
                    break;
                case 3 :
                    keys = "busan";
                    break;
                case 4 :
                    keys = "daegu";
                    break;
                case 5 :
                    keys = "jeju";
                    break;
                case 6 :
                    keys = "gangneung";
                    break;
            }


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String FORECAST_BASE_URL =
                        "https://api.openweathermap.org/data/2.5/weather?q="+ keys +"&appid=5fd2f2cde90c1533efb95b19c048a528&units=metric";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon().build();

                URL url = new URL(builtUri.toString());


                // Create the request to OpenWeatherMap, and open the connection
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

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                Long updatedAt = jsonObj.getLong("dt");
                String clock = new SimpleDateFormat("a", Locale.ENGLISH).format(new Date(updatedAt * 1000));

                String time = new SimpleDateFormat("hh", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String updatedAtText = new SimpleDateFormat("yyyy.MM.dd hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                int temp = main.getInt("temp");
                int tempMin = main.getInt("temp_min");
                int tempMax = main.getInt("temp_max");
                String pressure = main.getString("pressure");
                String humidity = "Humidity: " + main.getString("humidity") + "%";

                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = "Wind Speed: " + wind.getString("speed") + "m/s";
                String description = weather.getString("main");

                String address = jsonObj.getString("name");


                /* Populating extracted data into our views */
                t1_city.setText(address);
                t2_date.setText(updatedAtText);
//                statusTxt.setText(weatherDescription.toUpperCase());
                t3_temp.setText(temp + "°C");
                t4_description.setText(description);
                t5_max.setText("Max Temp: " + tempMax + "°C");
                t6_min.setText("Min Temp: " + tempMin + "°C");
                t7_humidity.setText(humidity);
                t8_speed.setText(windSpeed);

         //       picture.setImageResource(R.drawable.afterr);
                if("AM".equals(clock)){
                    if(Integer.parseInt(time) >= 6 && Integer.parseInt(time) < 9){
                        if("Rain".equals(description)){
                            picture.setImageResource(R.drawable.afterr);
                            t1_city.setTextColor(Color.parseColor("#FFFFFF"));
                            t2_date.setTextColor(Color.parseColor("#FFFFFF"));
                            t3_temp.setTextColor(Color.parseColor("#FFFFFF"));
                            t4_description.setTextColor(Color.parseColor("#FFFFFF"));
                            t5_max.setTextColor(Color.parseColor("#FFFFFF"));
                            t6_min.setTextColor(Color.parseColor("#FFFFFF"));
                            t7_humidity.setTextColor(Color.parseColor("#FFFFFF"));
                            t8_speed.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else if("Clear".equals(description)){
                            picture.setImageResource(R.drawable.mornings);
                        }
                        else{
                            picture.setImageResource(R.drawable.morningc);
                        }
                    }
                    else if(Integer.parseInt(time) >= 9 && Integer.parseInt(time) < 12){
                        if("Rain".equals(description)){
                            picture.setImageResource(R.drawable.afterr);
                            t1_city.setTextColor(Color.parseColor("#FFFFFF"));
                            t2_date.setTextColor(Color.parseColor("#FFFFFF"));
                            t3_temp.setTextColor(Color.parseColor("#FFFFFF"));
                            t4_description.setTextColor(Color.parseColor("#FFFFFF"));
                            t5_max.setTextColor(Color.parseColor("#FFFFFF"));
                            t6_min.setTextColor(Color.parseColor("#FFFFFF"));
                            t7_humidity.setTextColor(Color.parseColor("#FFFFFF"));
                            t8_speed.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else if("Clear".equals(description)){
                            picture.setImageResource(R.drawable.leather);
                            t1_city.setTextColor(Color.parseColor("#FFFFFF"));
                            t2_date.setTextColor(Color.parseColor("#FFFFFF"));
                            t3_temp.setTextColor(Color.parseColor("#FFFFFF"));
                            t4_description.setTextColor(Color.parseColor("#FFFFFF"));
                            t5_max.setTextColor(Color.parseColor("#FFFFFF"));
                            t6_min.setTextColor(Color.parseColor("#FFFFFF"));
                            t7_humidity.setTextColor(Color.parseColor("#FFFFFF"));
                            t8_speed.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else{
                            picture.setImageResource(R.drawable.aftercloud);
                        }
                    }
                    else{
                        if("Rain".equals(description)){
                            picture.setImageResource(R.drawable.nightr);
                            t1_city.setTextColor(Color.parseColor("#FFFFFF"));
                            t2_date.setTextColor(Color.parseColor("#FFFFFF"));
                            t3_temp.setTextColor(Color.parseColor("#FFFFFF"));
                            t4_description.setTextColor(Color.parseColor("#FFFFFF"));
                            t5_max.setTextColor(Color.parseColor("#FFFFFF"));
                            t6_min.setTextColor(Color.parseColor("#FFFFFF"));
                            t7_humidity.setTextColor(Color.parseColor("#FFFFFF"));
                            t8_speed.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else if("Clear".equals(description)){
                            picture.setImageResource(R.drawable.black);
                            t1_city.setTextColor(Color.parseColor("#FFFFFF"));
                            t2_date.setTextColor(Color.parseColor("#FFFFFF"));
                            t3_temp.setTextColor(Color.parseColor("#FFFFFF"));
                            t4_description.setTextColor(Color.parseColor("#FFFFFF"));
                            t5_max.setTextColor(Color.parseColor("#FFFFFF"));
                            t6_min.setTextColor(Color.parseColor("#FFFFFF"));
                            t7_humidity.setTextColor(Color.parseColor("#FFFFFF"));
                            t8_speed.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else{
                            picture.setImageResource(R.drawable.nightcloud);
                        }
                    }
                }
                else if(clock.equals("PM")){
                    if(Integer.parseInt(time) == 12){
                        if("Rain".equals(description)){
                            picture.setImageResource(R.drawable.afterr);
                            t1_city.setTextColor(Color.parseColor("#FFFFFF"));
                            t2_date.setTextColor(Color.parseColor("#FFFFFF"));
                            t3_temp.setTextColor(Color.parseColor("#FFFFFF"));
                            t4_description.setTextColor(Color.parseColor("#FFFFFF"));
                            t5_max.setTextColor(Color.parseColor("#FFFFFF"));
                            t6_min.setTextColor(Color.parseColor("#FFFFFF"));
                            t7_humidity.setTextColor(Color.parseColor("#FFFFFF"));
                            t8_speed.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else if("Clear".equals(description)){
                            picture.setImageResource(R.drawable.cloudsun);
                        }
                        else{
                            picture.setImageResource(R.drawable.aftercloud);
                            t1_city.setTextColor(Color.parseColor("#FFFFFF"));
                            t2_date.setTextColor(Color.parseColor("#FFFFFF"));
                            t3_temp.setTextColor(Color.parseColor("#FFFFFF"));
                            t4_description.setTextColor(Color.parseColor("#FFFFFF"));
                            t5_max.setTextColor(Color.parseColor("#FFFFFF"));
                            t6_min.setTextColor(Color.parseColor("#FFFFFF"));
                            t7_humidity.setTextColor(Color.parseColor("#FFFFFF"));
                            t8_speed.setTextColor(Color.parseColor("#FFFFFF"));

                        }
                    }
                    else if(Integer.parseInt(time) >= 1 && Integer.parseInt(time) < 6){
                        if(description.equals("Rain")){
                            picture.setImageResource(R.drawable.afterr);
                            t1_city.setTextColor(Color.parseColor("#FFFFFF"));
                            t2_date.setTextColor(Color.parseColor("#FFFFFF"));
                            t3_temp.setTextColor(Color.parseColor("#FFFFFF"));
                            t4_description.setTextColor(Color.parseColor("#FFFFFF"));
                            t5_max.setTextColor(Color.parseColor("#FFFFFF"));
                            t6_min.setTextColor(Color.parseColor("#FFFFFF"));
                            t7_humidity.setTextColor(Color.parseColor("#FFFFFF"));
                            t8_speed.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                        else if("Clear".equals(description)){
                            picture.setImageResource(R.drawable.cloudsun);
                        }
                        else {
                            picture.setImageResource(R.drawable.aftercloud);
                            t1_city.setTextColor(Color.parseColor("#FFFFFF"));
                            t2_date.setTextColor(Color.parseColor("#FFFFFF"));
                            t3_temp.setTextColor(Color.parseColor("#FFFFFF"));
                            t4_description.setTextColor(Color.parseColor("#FFFFFF"));
                            t5_max.setTextColor(Color.parseColor("#FFFFFF"));
                            t6_min.setTextColor(Color.parseColor("#FFFFFF"));
                            t7_humidity.setTextColor(Color.parseColor("#FFFFFF"));
                            t8_speed.setTextColor(Color.parseColor("#FFFFFF"));
                        }
                    }
                    else if(Integer.parseInt(time) >= 6 && Integer.parseInt(time) < 10){
                        if("Rain".equals(description)){
                            picture.setImageResource(R.drawable.nightr);
                        }
                        else if("Clear".equals(description)){
                            picture.setImageResource(R.drawable.nights);
                        }
                        else {
                            picture.setImageResource(R.drawable.nightcloud);
                        }
                        t1_city.setTextColor(Color.parseColor("#FFFFFF"));
                        t2_date.setTextColor(Color.parseColor("#FFFFFF"));
                        t3_temp.setTextColor(Color.parseColor("#FFFFFF"));
                        t4_description.setTextColor(Color.parseColor("#FFFFFF"));
                        t5_max.setTextColor(Color.parseColor("#FFFFFF"));
                        t6_min.setTextColor(Color.parseColor("#FFFFFF"));
                        t7_humidity.setTextColor(Color.parseColor("#FFFFFF"));
                        t8_speed.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                    else {
                        if("Rain".equals(description)){
                            picture.setImageResource(R.drawable.nightr);
                        }
                        else if("Clear".equals(description)){
                            picture.setImageResource(R.drawable.black);
                        }
                        else{
                            picture.setImageResource(R.drawable.nightcloud);
                        }
                        t1_city.setTextColor(Color.parseColor("#FFFFFF"));
                        t2_date.setTextColor(Color.parseColor("#FFFFFF"));
                        t3_temp.setTextColor(Color.parseColor("#FFFFFF"));
                        t4_description.setTextColor(Color.parseColor("#FFFFFF"));
                        t5_max.setTextColor(Color.parseColor("#FFFFFF"));
                        t6_min.setTextColor(Color.parseColor("#FFFFFF"));
                        t7_humidity.setTextColor(Color.parseColor("#FFFFFF"));
                        t8_speed.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
//                temp_minTxt.setText(tempMin);
                //               temp_maxTxt.setText(tempMax);
                ///               sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                //               sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                //               windTxt.setText(windSpeed);
                //               pressureTxt.setText(pressure);
//                humidityTxt.setText(humidity);

                /* Views populated, Hiding the loader, Showing the main design */
//                findViewById(R.id.loader).setVisibility(View.GONE);
//                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                //               findViewById(R.id.loader).setVisibility(View.GONE);
                //               findViewById(R.id.errorText).setVisibility(View.VISIBLE);
            }

        }
    }
}
/*
    public void find_weather(){
        t1_city.setText("TEST");
     //   String url = "http://api.openweathermap.org/data/2.5/weather?q=islamabad,pakistan&appid=90ebdc57172a838d0abbc044df8e&units=Imperial";
        String url = "http://api.openweathermap.org/data/2.5/weather?q=seoul&appid=5fd2f2cde90c1533efb95b19c048a528&units=Imperial";
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("main");

                    JSONArray array = response.getJSONArray("weather");
                    JSONObject object = array.getJSONObject(0);
                    String temp = String.valueOf(main_object.getDouble("temp"));
                    String description = object.getString("description");
                    String city = response.getString("name");

                    t1_city.setText(city);
                    t3_temp.setText(temp);
                    t4_description.setText(description);

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE==MM-dd");
                    String formatted_date = sdf.format(calendar.getTime());

                    t2_date.setText(formatted_date);

                    double temp_int = Double.parseDouble(temp);
                    double centi = (temp_int - 32) / 1.8000;
                    centi = Math.round(centi);
                    int i = (int)centi;
                    t3_temp.setText(String.valueOf(i));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);

    }

}
*/