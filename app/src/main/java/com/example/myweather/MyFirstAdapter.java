package com.example.myweather;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myweather.R;
import com.example.myweather.Weather;

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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MyFirstAdapter extends BaseAdapter {
    TextView cityText1, tempText1, cityText2, tempText2;
    TextView cityText3, tempText3, cityText4, tempText4;
    TextView cityText5, tempText5, cityText6, tempText6, cityText7, tempText7;
    ImageView weather1, weather2, weather3, weather4, weather5, weather6, weather7;
    private List<Weather> mData;
    private Map<String, Integer> mWeatherImageMap;

    public MyFirstAdapter(ArrayList<Weather> data) {
        this.mData = data;
        mWeatherImageMap = new HashMap<>();
        mWeatherImageMap.put("Clear", R.drawable.sun);
        mWeatherImageMap.put("Clouds", R.drawable.cloud);
        mWeatherImageMap.put("Rain", R.drawable.rain);
        mWeatherImageMap.put("Snow", R.drawable.snow);
        mWeatherImageMap.put("Drizzle", R.drawable.rain);
        mWeatherImageMap.put("Thunderstorm", R.drawable.thunderstorm);
        mWeatherImageMap.put("Mist", R.drawable.cloud);
        mWeatherImageMap.put("Smoke", R.drawable.cloud);
        mWeatherImageMap.put("Haze", R.drawable.cloud);
        mWeatherImageMap.put("Dust", R.drawable.cloud);
        mWeatherImageMap.put("Fog", R.drawable.cloud);
        mWeatherImageMap.put("Sand", R.drawable.cloud);
        mWeatherImageMap.put("Dust", R.drawable.cloud);
        mWeatherImageMap.put("Ash", R.drawable.cloud);
        mWeatherImageMap.put("Squall", R.drawable.cloud);
        mWeatherImageMap.put("Tornado", R.drawable.cloud);


    }

    @Override
    public int getCount() {
        // 아이템개수
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_weather, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView weatherImage = (ImageView)convertView.findViewById(R.id.weather_image);
        TextView cityText = (TextView)convertView.findViewById(R.id.city_text);
        TextView tempText = (TextView)convertView.findViewById(R.id.temp_text);

        Weather weather = mData.get(position);
        cityText.setText(weather.getCity());
        tempText.setText(weather.getTemp());
        weatherImage.setImageResource(mWeatherImageMap.get(weather.getWeather()));

        if(weather.getCity().equals("Seoul")) {
            cityText1 = cityText;
            tempText1 = tempText;
            weather1 = weatherImage;
            new weatherTask().execute();
        } else if(weather.getCity().equals("Suwon-si")) {
            cityText2 = cityText;
            tempText2 = tempText;
            weather2 = weatherImage;
            new weatherTask2().execute();
        } else if(weather.getCity().equals("Incheon")) {
            cityText3 = cityText;
            tempText3 = tempText;
            weather3 = weatherImage;
            new weatherTask3().execute();
        } else if(weather.getCity().equals("Busan")) {
            cityText4 = cityText;
            tempText4 = tempText;
            weather4 = weatherImage;
            new weatherTask4().execute();
        } else if(weather.getCity().equals("Daegu")) {
            cityText5 = cityText;
            tempText5 = tempText;
            weather5 = weatherImage;
            new weatherTask5().execute();
        } else if(weather.getCity().equals("Jeju City")) {
            cityText6 = cityText;
            tempText6 = tempText;
            weather6 = weatherImage;
            new weatherTask6().execute();
        } else if(weather.getCity().equals("Gangneung")) {
            cityText7 = cityText;
            tempText7 = tempText;
            weather7 = weatherImage;
            new weatherTask7().execute();
        }


        return convertView;
    }

    class weatherTask extends AsyncTask<String, Void, String> {
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

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 14;

            try {
                final String FORECAST_BASE_URL =
                        "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=5fd2f2cde90c1533efb95b19c048a528&units=metric";

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
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                String temp = main.getString("temp") + "°C";
                String tempMin = main.getString("temp_min") + "°C";
                String tempMax = main.getString("temp_max") + "°C";
                String address = jsonObj.getString("name");
                String description = weather.getString("main");

                weather1.setImageResource(mWeatherImageMap.get(description));


                /* Populating extracted data into our views */
                cityText1.setText(address);
                tempText1.setText(temp+"  ("+tempMin+" ~ "+tempMax+")");
            } catch (JSONException e) {

            }

        }
    }
    class weatherTask2 extends AsyncTask<String, Void, String> {
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

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 14;

            try {

                final String FORECAST_BASE_URL =
                        "https://api.openweathermap.org/data/2.5/weather?q=suwon&appid=5fd2f2cde90c1533efb95b19c048a528&units=metric";
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
                    buffer.append(line + "\n");
                    Log.v(LOG_TAG, "Forecast string: " + line);
                }


                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
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
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                String temp = main.getString("temp") + "°C";
                String tempMin = main.getString("temp_min") + "°C";
                String tempMax = main.getString("temp_max") + "°C";
                String address = jsonObj.getString("name");
                String description = weather.getString("main");

                weather2.setImageResource(mWeatherImageMap.get(description));
                /* Populating extracted data into our views */
                cityText2.setText(address);
                tempText2.setText(temp+"  ("+tempMin+" ~ "+tempMax+")");

            } catch (JSONException e) {
            }

        }
    }
    class weatherTask3 extends AsyncTask<String, Void, String> {
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

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 14;

            try {

                final String FORECAST_BASE_URL =
                        "https://api.openweathermap.org/data/2.5/weather?q=inchon&appid=5fd2f2cde90c1533efb95b19c048a528&units=metric";
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
                    buffer.append(line + "\n");
                    Log.v(LOG_TAG, "Forecast string: " + line);
                }


                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
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
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                String temp = main.getString("temp") + "°C";
                String tempMin = main.getString("temp_min") + "°C";
                String tempMax = main.getString("temp_max") + "°C";
                String address = jsonObj.getString("name");
                String description = weather.getString("main");

                weather3.setImageResource(mWeatherImageMap.get(description));

                /* Populating extracted data into our views */
                cityText3.setText(address);
                tempText3.setText(temp+"  ("+tempMin+" ~ "+tempMax+")");

            } catch (JSONException e) {
            }

        }
    }
    class weatherTask4 extends AsyncTask<String, Void, String> {
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

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 14;

            try {

                final String FORECAST_BASE_URL =
                        "https://api.openweathermap.org/data/2.5/weather?q=busan&appid=5fd2f2cde90c1533efb95b19c048a528&units=metric";
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
                    buffer.append(line + "\n");
                    Log.v(LOG_TAG, "Forecast string: " + line);
                }


                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
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
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                String temp = main.getString("temp") + "°C";
                String tempMin = main.getString("temp_min") + "°C";
                String tempMax = main.getString("temp_max") + "°C";
                String address = jsonObj.getString("name");
                String description = weather.getString("main");

                weather4.setImageResource(mWeatherImageMap.get(description));

                /* Populating extracted data into our views */
                cityText4.setText(address);
                tempText4.setText(temp+"  ("+tempMin+" ~ "+tempMax+")");

            } catch (JSONException e) {
            }

        }
    }
    class weatherTask5 extends AsyncTask<String, Void, String> {
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

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 14;

            try {

                final String FORECAST_BASE_URL =
                        "https://api.openweathermap.org/data/2.5/weather?q=daegu&appid=5fd2f2cde90c1533efb95b19c048a528&units=metric";
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
                    buffer.append(line + "\n");
                    Log.v(LOG_TAG, "Forecast string: " + line);
                }


                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
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
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                String temp = main.getString("temp") + "°C";
                String tempMin = main.getString("temp_min") + "°C";
                String tempMax = main.getString("temp_max") + "°C";
                String address = jsonObj.getString("name");
                String description = weather.getString("main");

                weather5.setImageResource(mWeatherImageMap.get(description));

                /* Populating extracted data into our views */
                cityText5.setText(address);
                tempText5.setText(temp+"  ("+tempMin+" ~ "+tempMax+")");

            } catch (JSONException e) {
            }

        }
    }
    class weatherTask6 extends AsyncTask<String, Void, String> {
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

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 14;

            try {

                final String FORECAST_BASE_URL =
                        "https://api.openweathermap.org/data/2.5/weather?q=jeju&appid=5fd2f2cde90c1533efb95b19c048a528&units=metric";
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
                    buffer.append(line + "\n");
                    Log.v(LOG_TAG, "Forecast string: " + line);
                }


                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
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
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                String temp = main.getString("temp") + "°C";
                String tempMin = main.getString("temp_min") + "°C";
                String tempMax = main.getString("temp_max") + "°C";
                String address = jsonObj.getString("name");
                String description = weather.getString("main");

                weather6.setImageResource(mWeatherImageMap.get(description));

                /* Populating extracted data into our views */
                cityText6.setText(address);
                tempText6.setText(temp+"  ("+tempMin+" ~ "+tempMax+")");

            } catch (JSONException e) {
            }

        }
    }
    class weatherTask7 extends AsyncTask<String, Void, String> {
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

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 14;

            try {

                final String FORECAST_BASE_URL =
                        "https://api.openweathermap.org/data/2.5/weather?q=gangneung&appid=5fd2f2cde90c1533efb95b19c048a528&units=metric";
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
                    buffer.append(line + "\n");
                    Log.v(LOG_TAG, "Forecast string: " + line);
                }


                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
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
                String updatedAtText = new SimpleDateFormat("yyyy.MM.dd hh:mm a", Locale.KOREA).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = main.getString("temp_min") + "°C";
                String tempMax = main.getString("temp_max") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");

                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String description = weather.getString("main");
                String address = jsonObj.getString("name");

                weather7.setImageResource(mWeatherImageMap.get(description));

                /* Populating extracted data into our views */
                cityText7.setText(address);
                tempText7.setText(temp+"  ("+tempMin+" ~ "+tempMax+")");


            } catch (JSONException e) {
            }

        }
    }

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false);
            ImageView weatherImage = (ImageView)convertView.findViewById(R.id.weather_image);
            TextView cityText = (TextView)convertView.findViewById(R.id.city_text);
            TextView tempText = (TextView)convertView.findViewById(R.id.temp_text);

            holder.weatherImage = weatherImage;
            holder.cityText = cityText;
            holder.tempText = tempText;

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Weather weather = mData.get(position);
        holder.cityText.setText(weather.getCity());
        holder.tempText.setText(weather.getTemp());
        holder.weatherImage.setImageResource(mWeatherImageMap.get(weather.getWeather()));

        return convertView;
    }
*/
    static class ViewHolder {
        ImageView weatherImage;
        TextView cityText;
        TextView tempText;
    }


}
