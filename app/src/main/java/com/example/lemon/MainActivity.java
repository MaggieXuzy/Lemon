package com.example.lemon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "SavedState";

    private SharedPreferences sharedPreferences;

    // Static data for additional weather attributes
    private static final String[][] HOURLY_FORECAST = {
            {"10:00", "-1", "Sunny"}, {"Now", "0", "Sunny"}, {"12:00", "1", "Sunny"}, {"13:00", "2", "Sunny"}, {"14:00", "2", "Cloudy"}
    };
    private static final String AIR_QUALITY = "High Health Risk";
    private static final String UV_INDEX = "10";
    private static final String TREE_POLLEN = "Moderate";
    private static final String GRASS_POLLEN = "High";
    private static final String WIND = "9.7 km/h to East";
    private static final String RAINFALL = "1.8 mm in last hour";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScrollView sv = findViewById(R.id.main_scroll);
        ImageButton scrollUp = findViewById(R.id.lemon_button);
        ImageButton locationSearch = findViewById(R.id.location_button);
        ImageButton settings_button = findViewById(R.id.settings_button);

        // Dynamic Text Boxes are defined HERE
        TextView current_location = findViewById(R.id.location_name);
        TextView current_temperature = findViewById(R.id.current_temperature);
        TextView current_weather = findViewById(R.id.current_weather);
        TextView high_low_conditions = findViewById(R.id.high_low_conditions);

        // Fetch from sharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String title = sharedPreferences.getString("loc_name", "Cambridge");
        String temp = sharedPreferences.getString("loc_temp", "20°");
        String condition = sharedPreferences.getString("loc_condition", "Sunny");
        boolean celsius = sharedPreferences.getBoolean("celsius", true);
        boolean _temp = sharedPreferences.getBoolean("_temperature", true);
        boolean _rain = sharedPreferences.getBoolean("_rainfall", true);
        boolean _wind = sharedPreferences.getBoolean("_wind", true);
        boolean _grasspollen = sharedPreferences.getBoolean("_grasspollen", true);
        boolean _treepollen = sharedPreferences.getBoolean("_treepollen", true);
        boolean _uvindex = sharedPreferences.getBoolean("_uvindex", true);
        boolean _airquality = sharedPreferences.getBoolean("_airquality", true);

        // Retain existing data
        current_location.setText(title);
        current_temperature.setText(CelsiusCheck(temp,celsius));
        current_weather.setText(condition);

        Double current_temp = convertTempDouble(temp);

        high_low_conditions.setText("H: " + CelsiusCheck(String.valueOf(Math.round(current_temp + 3))+"°",celsius) + "  L: " + CelsiusCheck(String.valueOf(Math.round(current_temp - 4))+"°",celsius));

        // Populate hourly forecast
        LinearLayout hourlyForecastContainer = findViewById(R.id.hourly_forecast_container);
        if (_temp) {
            LayoutInflater inflater = LayoutInflater.from(this);
            LinearLayout hourlyForecastLayout = hourlyForecastContainer.findViewById(R.id.hourly_forecast_layout);
            for (String[] forecast : HOURLY_FORECAST) {
                View forecastView = inflater.inflate(R.layout.hourly_forecast_item, hourlyForecastLayout, false);
                TextView timeView = forecastView.findViewById(R.id.forecast_time);
                TextView tempView = forecastView.findViewById(R.id.forecast_temp);
                timeView.setText(forecast[0]);
                tempView.setText(CelsiusCheck(String.valueOf(Math.round(Double.valueOf(forecast[1]) + current_temp)) + "°", celsius));
                hourlyForecastLayout.addView(forecastView);
            }
        } else {
            hourlyForecastContainer.setVisibility(View.GONE);
        }

        // Populate air quality
        LinearLayout airQualityContainer = findViewById(R.id.air_quality_container);
        if (_airquality) {
            TextView airQualityView = new TextView(this);
            airQualityView.setText("AIR QUALITY\n" + AIR_QUALITY);
            airQualityView.setTextColor(getResources().getColor(R.color.white));
            airQualityView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            airQualityView.setTextSize(1,18);
            airQualityContainer.addView(airQualityView);
        } else {
            airQualityContainer.setVisibility(View.GONE);
        }

        LinearLayout [] containers = {findViewById(R.id.half_box1),findViewById(R.id.half_box2),findViewById(R.id.half_box3),findViewById(R.id.half_box4),findViewById(R.id.half_box5),findViewById(R.id.half_box6),findViewById(R.id.half_box7),findViewById(R.id.half_box8)};
        Object[][] order = {{"UV INDEX\n" + UV_INDEX,_uvindex},{"GRASS POLLEN\n" + GRASS_POLLEN,_grasspollen},{"TREE POLLEN\n" + TREE_POLLEN,_treepollen},{"WIND\n" + WIND,_wind},{"RAINFALL\n" + RAINFALL,_rain}};
        int n = 0;
        for (int i = 0; i < containers.length; i++) {
            while ((n < order.length) && (!(Boolean) (order[n][1]))) {
                n++;
            }
            if (n >= order.length) {
                containers[i].setVisibility(View.GONE);
            } else {
                TextView text = new TextView(this);
                text.setText((String) order[n][0]);
                n++;
                text.setTextColor(getResources().getColor(R.color.white));
                text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                text.setTextSize(1,18);
                (containers[i]).addView(text);
            }
        }
        /*
        // Populate UV index
        LinearLayout uvIndexContainer = findViewById(R.id.uv_index_container);
        TextView uvIndexView = new TextView(this);
        uvIndexView.setText("UV INDEX\n" + UV_INDEX + "\nHigh");
        uvIndexView.setTextColor(getResources().getColor(R.color.white));
        uvIndexContainer.addView(uvIndexView);

        // Populate tree pollen
        LinearLayout treePollenContainer = findViewById(R.id.tree_pollen_container);
        TextView treePollenView = new TextView(this);
        treePollenView.setText("TREE POLLEN\n" + TREE_POLLEN);
        treePollenView.setTextColor(getResources().getColor(R.color.white));
        treePollenContainer.addView(treePollenView);

        // Populate wind
        LinearLayout windContainer = findViewById(R.id.wind_container);
        TextView windView = new TextView(this);
        windView.setText("WIND\n" + WIND);
        windView.setTextColor(getResources().getColor(R.color.white));
        windContainer.addView(windView);

        // Populate rainfall
        LinearLayout rainfallContainer = findViewById(R.id.rainfall_container);
        TextView rainfallView = new TextView(this);
        rainfallView.setText("RAINFALL\n" + RAINFALL);
        rainfallView.setTextColor(getResources().getColor(R.color.white));
        rainfallContainer.addView(rainfallView);
        */

        // Checks if Lemon icon is clicked, so that it goes to the top of the ScrollView
        scrollUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv.smoothScrollTo(0, 0);
            }
        });

        // Navigate to LocationSearch activity when location_button is clicked
        locationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocationSearch.class);
                startActivity(intent);
            }
        });

        settings_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsPage.class);
                startActivity(intent);
            }
        });
    }

    protected String CelsiusCheck(String temp, boolean c) {
        if (c) {
            return temp;
        }
        else {
            Double temp_double = convertTempDouble(temp);
            Integer new_temp = (int) Math.round((temp_double * 9/5) + 32);
            return (Integer.toString(new_temp)+"°");
        }
    }
    protected Double convertTempDouble(String temp) {
        return Double.valueOf(temp.substring(0, temp.length() - 1));
    }
}
