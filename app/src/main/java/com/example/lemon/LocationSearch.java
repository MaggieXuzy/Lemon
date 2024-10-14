package com.example.lemon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static com.google.android.material.internal.ViewUtils.hideKeyboard;

public class LocationSearch extends AppCompatActivity {
    private static final String PREFS_NAME = "SavedState";

    private SharedPreferences sharedPreferences;
    private static final String[] CITIES = {
            "Cambridge", "San Francisco", "London", "Edinburgh", "Melbourne",
            "Munich", "Tbilisi", "New York", "Tokyo", "Paris",
            "Sydney", "Berlin", "Madrid", "Rome", "Moscow",
            "Dubai", "Toronto", "Vancouver", "Chicago", "Los Angeles",
            "Buenos Aires", "Cape Town", "Rio de Janeiro", "Singapore", "Hong Kong",
            "Bangkok", "Beijing", "Seoul", "Mexico City", "Sao Paulo"
    };

    private static final String[] WEATHERS = {
            "Sunny", "Sunny", "Cloudy", "Cloudy", "Partly Cloudy",
            "Rainy", "Sunny", "Sunny", "Cloudy", "Rainy",
            "Sunny", "Cloudy", "Sunny", "Sunny", "Snowy",
            "Sunny", "Snowy", "Rainy", "Cloudy", "Sunny",
            "Sunny", "Cloudy", "Sunny", "Sunny", "Rainy",
            "Sunny", "Snowy", "Cloudy", "Sunny", "Rainy"
    };

    private static final String[] TEMPERATURES = {
            "20°", "15°", "18°", "12°", "25°",
            "10°", "28°", "22°", "20°", "17°",
            "30°", "16°", "25°", "22°", "0°",
            "35°", "-5°", "8°", "18°", "25°",
            "27°", "22°", "30°", "32°", "28°",
            "33°", "0°", "10°", "20°", "24°"
    };

    private HashSet<String> displayedCities = new HashSet<>();
    private ListPopupWindow listPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_search);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        EditText searchCity = findViewById(R.id.search_city);
        ImageButton searchButton = findViewById(R.id.search_button);
        ImageButton backButton = findViewById(R.id.back_button);
        LinearLayout cityList = findViewById(R.id.city_list);
        LayoutInflater inflater = LayoutInflater.from(this);

        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAnchorView(searchCity);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Ends the current activity and returns to the previous one.
            }
        });

        searchCity.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    List<String> matches = new ArrayList<>();
                    for (int i = 0; i < CITIES.length; i++) {
                        if (CITIES[i].toLowerCase().contains(query.toLowerCase()) && !displayedCities.contains(CITIES[i])) {
                            matches.add(CITIES[i]);
                        }
                    }
                    if (!matches.isEmpty()) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(LocationSearch.this, android.R.layout.simple_list_item_1, matches);
                        listPopupWindow.setAdapter(adapter);
                        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String selectedCity = matches.get(position);
                                int index = findCityIndex(selectedCity);
                                if (index != -1) {
                                    addCityWeatherCard(inflater, cityList, CITIES[index], WEATHERS[index], TEMPERATURES[index]);
                                    displayedCities.add(CITIES[index]);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(selectedCity, true);
                                    editor.apply();
                                }
                                listPopupWindow.dismiss();
                                hideKeyboard();
                                Toast.makeText(getApplicationContext(), "Successfully added location!", Toast.LENGTH_SHORT).show();
                            }

                            private void hideKeyboard() {
                                // Get the InputMethodManager
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                                // Hide the keyboard from the currently focused view
                                if (imm != null && getCurrentFocus() != null) {
                                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                                }
                            }
                        });
                        listPopupWindow.show();
                    } else {
                        listPopupWindow.dismiss();
                    }
                } else {
                    listPopupWindow.dismiss();
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
        String[] Cities = new String[0];

        //Load cities into memory
        for (String city: CITIES) {
            boolean showCity = sharedPreferences.getBoolean(city, false);
            if (showCity) {
                // Create a new array with one more element than the original array
                String[] newCities = new String[Cities.length + 1];

                // Copy the original array elements to the new array
                System.arraycopy(Cities, 0, newCities, 0, Cities.length);

                // Add the new string to the last position of the new array
                newCities[Cities.length] = city;

                Cities = newCities;
            }
        }


        // Show only default cities initially
        if(Cities.length == 0) {
            Cities = new String[]{"Cambridge", "San Francisco", "London"};
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("Cambridge", true);
            editor.putBoolean("San Francisco", true);
            editor.putBoolean("London", true);
            editor.apply();
        }
        for (String City : Cities) {
            int index = findCityIndex(City);
            if (index != -1) {
                addCityWeatherCard(inflater, cityList, CITIES[index], WEATHERS[index], TEMPERATURES[index]);
                displayedCities.add(CITIES[index]);
            }
        }
    }

    private int findCityIndex(String cityName) {
        for (int i = 0; i < CITIES.length; i++) {
            if (CITIES[i].equals(cityName)) {
                return i;
            }
        }
        return -1;
    }

    private void addCityWeatherCard(LayoutInflater inflater, LinearLayout cityList, String cityName, String weather, String temperature) {
        CardView cardView = (CardView) inflater.inflate(R.layout.city_weather_card, cityList, false);

        TextView cityNameView = cardView.findViewById(R.id.city_name);
        TextView weatherView = cardView.findViewById(R.id.weather);
        TextView temperatureView = cardView.findViewById(R.id.temperature);
        TextView viewbutton = cardView.findViewById(R.id.see_more);
        ImageButton deletebutton = cardView.findViewById(R.id.delete_button);

        cityNameView.setText(cityName);
        weatherView.setText(weather);
        temperatureView.setText(CelsiusCheck(temperature,sharedPreferences.getBoolean("celsius", true)));

        cityList.addView(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationSearch.this, MainActivity.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("loc_name", cityName);
                editor.putString("loc_temp", temperature);
                editor.putString("loc_condition", weather);
                editor.apply();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });
        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(Objects.equals(cityName, sharedPreferences.getString("loc_name", "Cambridge")))) {
                    Intent intent = new Intent(LocationSearch.this, LocationSearch.class);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(cityName, false);
                    editor.apply();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Successfully deleted location!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "You cannot delete the location you are currently viewing!", Toast.LENGTH_SHORT).show();
                }
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
