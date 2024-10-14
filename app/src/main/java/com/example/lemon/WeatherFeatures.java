package com.example.lemon;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.*;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class WeatherFeatures extends AppCompatActivity {
    private static final String PREFS_NAME = "SavedState";
    private SharedPreferences sharedPreferences;

    private Boolean changes=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Initialises initial main page

        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_features);

        ScrollView sv = findViewById(R.id.weather_feature_scroll);
        ImageButton backButton = findViewById(R.id.back_button);


        //Dynamic Text Boxes are defined HERE

        TextView search_for_city = (TextView) findViewById(R.id.change_notification);
        TextView choose_features = (TextView) findViewById(R.id.choose_features);

        CheckBox temperature = (CheckBox) findViewById(R.id.checkBox_temperature);
        CheckBox rainfall = (CheckBox) findViewById(R.id.checkBox_rain_fall);
        CheckBox wind = (CheckBox) findViewById(R.id.checkBox_wind);
        CheckBox grass_pollen = (CheckBox) findViewById(R.id.checkBox_grass_pollen);
        CheckBox tree_pollen = (CheckBox) findViewById(R.id.checkBox_tree_pollen);
        CheckBox uv_index = (CheckBox) findViewById(R.id.checkBox_uv_index);
        CheckBox air_quality = (CheckBox) findViewById(R.id.checkBox_air_quality);

        //Placeholder text until API fetches are implemented

        search_for_city.setText("Weather Features");
        choose_features.setText("Choose which weather features you want to see: ");
        temperature.setText("Hourly Forecast");
        rainfall.setText("Rainfall");
        wind.setText("Wind");
        grass_pollen.setText("Grass Pollen");
        tree_pollen.setText("Tree Pollen");
        uv_index.setText("UV Index");
        air_quality.setText("Air Quality");


        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        temperature.setChecked(sharedPreferences.getBoolean("_temperature", true));
        rainfall.setChecked(sharedPreferences.getBoolean("_rainfall", true));
        wind.setChecked(sharedPreferences.getBoolean("_wind", true));
        grass_pollen.setChecked(sharedPreferences.getBoolean("_grasspollen", true));
        tree_pollen.setChecked(sharedPreferences.getBoolean("_treepollen", true));
        uv_index.setChecked(sharedPreferences.getBoolean("_uvindex", true));
        air_quality.setChecked(sharedPreferences.getBoolean("_airquality", true));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Back is pressed... Finishing the activity
                if (!changes) {
                    finish(); // Ends the current activity and returns to the previous one.
                } else {
                    showConfirmationDialog(editor);
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!changes) {
                    finish(); // Ends the current activity and returns to the previous one.
                } else {
                    showConfirmationDialog(editor);
                }
            }
        });

        //Modify this part when we need to customize
        temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changes = true;
                editor.putBoolean("_temperature",(temperature.isChecked()));
            }
        });

        air_quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changes = true;
                editor.putBoolean("_airquality", (air_quality.isChecked()));
            }
        });

        uv_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changes = true;
                editor.putBoolean("_uvindex", (uv_index.isChecked()));
            }
        });

        tree_pollen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changes = true;
                editor.putBoolean("_treepollen", (tree_pollen.isChecked()));
            }
        });

        grass_pollen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changes = true;
                editor.putBoolean("_grasspollen", (grass_pollen.isChecked()));
            }
        });

        rainfall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changes = true;
                editor.putBoolean("_rainfall", (rainfall.isChecked()));
            }
        });

        wind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changes = true;
                editor.putBoolean("_wind", (wind.isChecked()));
            }
        });


    }
    private void showConfirmationDialog(SharedPreferences.Editor editor) {
        // Create an AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unsaved Changes");
        builder.setMessage("There are unsaved changes! Are you sure you want to leave this page without saving your changes?");

        // Add the two buttons below. The RESET button is set as the negative button
        // because the negative button is displayed on the right.

        // Add the "RESET" button
        builder.setPositiveButton("Save and Leave", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Reset all of the user's configurations
                editor.apply();
                Intent intent = new Intent(WeatherFeatures.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        // Add the "CANCEL" button
        builder.setNegativeButton("Leave without Saving", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                finish();
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });


        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}