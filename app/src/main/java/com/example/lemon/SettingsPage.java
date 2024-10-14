
package com.example.lemon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;

public class SettingsPage extends AppCompatActivity {
    private static final String PREFS_NAME = "SavedState";

    private SharedPreferences sharedPreferences;

    private boolean changed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Initialises initial main page

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        //ScrollView sv = findViewById(R.id.main_scroll);
        Button configureUnits = findViewById(R.id.configureUnitsButton);
        Button configureNotifications = findViewById(R.id.configureNotificationsButton);
        Button configureWeatherFeatures = findViewById(R.id.configureWeatherFeaturesButton);
        Button resetButton = findViewById(R.id.ResetButton);

        ImageButton backButton = findViewById(R.id.back_button);

        boolean celsius = sharedPreferences.getBoolean("celsius", true);
        if (celsius) {
            configureUnits.setText("Unit: Celsius (°C)");
        }
        else {
            configureUnits.setText("Unit: Fahrenheit (°F)");
        }

        configureWeatherFeatures.setText("Configure Weather Features");
        configureNotifications.setText("Configure Notifications");
        resetButton.setText("Reset App Data");
//        Checks if Lemon icon is clicked, so that it goes to the top of the ScrollView
        configureUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changed = true;
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (configureUnits.getText() == "Unit: Celsius (°C)") {
                    configureUnits.setText("Unit: Fahrenheit (°F)");
                    editor.putBoolean("celsius",false);
                }
                else {
                    configureUnits.setText("Unit: Celsius (°C)");
                    editor.putBoolean("celsius",true);
                }
                editor.apply();
            }
        });
        configureNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingsPage.this, NotificationsPage.class);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(), "This has not been implemented!", Toast.LENGTH_SHORT).show();
            }
        });
        configureWeatherFeatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsPage.this, WeatherFeatures.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changed) {
                    Intent intent = new Intent(SettingsPage.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                finish(); // Ends the current activity and returns to the previous one.
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetConfirmationDialog();
            }
        });
    }


    private void showResetConfirmationDialog() {
        // Create an AlertDialog Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Reset Confirmation");
        builder.setMessage("Are you sure you want to reset all configurations? This action cannot be undone.");

        // Add the two buttons below. The RESET button is set as the negative button
        // because the negative button is displayed on the right.

        // Add the "RESET" button
        builder.setNegativeButton("RESET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Reset all of the user's configurations
                resetAllConfigurations();
            }
        });

        // Add the "CANCEL" button
        builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
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

    private void resetAllConfigurations() {
        Intent intent = new Intent(SettingsPage.this, MainActivity.class);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        Toast.makeText(getApplicationContext(), "App Data has been reset!", Toast.LENGTH_SHORT).show();
    }

}
