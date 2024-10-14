package com.example.lemon;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationsPage extends AppCompatActivity {
    private static final String PREFS_NAME = "SavedState";
    private SharedPreferences sharedPreferences;
    private boolean changes=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Initialises initial main page

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_page);

        //Checkboxes to toggle notifications on and off
        CheckBox grass_pollen = (CheckBox) findViewById(R.id.checkBox_grass_pollen);
        CheckBox tree_pollen = (CheckBox) findViewById(R.id.checkBox_tree_pollen);
        CheckBox uv_index = (CheckBox) findViewById(R.id.checkBox_uv_index);

        ImageButton backButton = findViewById(R.id.back_button);

        //Set text
        grass_pollen.setText("Grass Pollen");
        tree_pollen.setText("Tree Pollen");
        uv_index.setText("UV Index");

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        grass_pollen.setChecked(sharedPreferences.getBoolean("notif_grasspollen", false));
        tree_pollen.setChecked(sharedPreferences.getBoolean("notif_treepollen", false));
        uv_index.setChecked(sharedPreferences.getBoolean("notif_uvindex", false));

        boolean _grass = sharedPreferences.getBoolean("notif_grasspollen", false);
        boolean _tree = sharedPreferences.getBoolean("notif_treepollen", false);
        boolean _uv = sharedPreferences.getBoolean("notif_uvindex", false);

        int thresh_grass = sharedPreferences.getInt("notif_grasspollen_thresh",1);
        int thresh_tree = sharedPreferences.getInt("notif_treepollen_thresh",1);
        int thresh_uv = sharedPreferences.getInt("notif_uv_thresh",4);

        TextView title = findViewById(R.id.change_notification);
        TextView choose_features = findViewById(R.id.choose_features);

        title.setText("Notifications");
        choose_features.setText("Tick the checkboxes for features you want to get notifications for");


        //Setup seekbars and their values
        SeekBar seekBar_uv_index = findViewById(R.id.seekBar_uv_index);
        TextView seekBarIndicator_uv_index = findViewById(R.id.seekBarIndicator_uv_index);
        seekBar_uv_index.setProgress(thresh_uv);
        seekBar_update(seekBar_uv_index, seekBarIndicator_uv_index, _uv);

        SeekBar seekBar_grass_pollen = findViewById(R.id.seekBar_grass_pollen);
        TextView seekBarIndicator_grass_pollen = findViewById(R.id.seekBarIndicator_grass_pollen);
        seekBar_grass_pollen.setProgress(thresh_grass);
        seekBar_update(seekBar_grass_pollen, seekBarIndicator_grass_pollen, _grass);

        SeekBar seekBar_tree_pollen = findViewById(R.id.seekBar_tree_pollen);
        TextView seekBarIndicator_tree_pollen = findViewById(R.id.seekBarIndicator_tree_pollen);
        seekBar_tree_pollen.setProgress(thresh_tree);
        seekBar_update(seekBar_tree_pollen, seekBarIndicator_tree_pollen, _tree);

        //Set default values for seekbar indicators
        if (thresh_grass == 0) {
            seekBarIndicator_grass_pollen.setText("Threshold: >= Low");
        }
        else if (thresh_grass == 1) {
            seekBarIndicator_grass_pollen.setText("Threshold: >= Medium");
        }
        else {
            seekBarIndicator_grass_pollen.setText("Threshold: >= High");
        }
        if (thresh_tree == 0) {
            seekBarIndicator_tree_pollen.setText("Threshold: >= Low");
        }
        else if (thresh_tree == 1) {
            seekBarIndicator_tree_pollen.setText("Threshold: >= Medium");
        }
        else {
            seekBarIndicator_tree_pollen.setText("Threshold: >= High");
        }
        seekBarIndicator_uv_index.setText("Threshold: >=" + (thresh_uv + 1));

        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Checks if seekbar is interacted with

        seekBar_uv_index.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar_uv_index, int progress, boolean fromUser) {
                changes = true;
                seekBarIndicator_uv_index.setText("Threshold: >= " + (progress+1));
                editor.putInt("notif_uv_thresh",progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar_uv_index) {
                // Do something when the touch event starts, if needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_uv_index) {
                // Do something when the touch event stops, if needed
            }
        });

        seekBar_tree_pollen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar_tree_pollen, int progress, boolean fromUser) {
                changes = true;
                if (progress == 0) {
                    seekBarIndicator_tree_pollen.setText("Threshold: >= Low");
                }
                else if (progress == 1) {
                    seekBarIndicator_tree_pollen.setText("Threshold: >= Medium");
                }
                else {
                    seekBarIndicator_tree_pollen.setText("Threshold: >= High");
                }
                editor.putInt("notif_treepollen_thresh",progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar_tree_pollen) {
                // Do something when the touch event starts, if needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_tree_pollen) {
                // Do something when the touch event stops, if needed
            }
        });

        seekBar_grass_pollen.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar_grass_pollen, int progress, boolean fromUser) {
                changes = true;
                if (progress == 0) {
                    seekBarIndicator_grass_pollen.setText("Threshold: >= Low");
                }
                else if (progress == 1) {
                    seekBarIndicator_grass_pollen.setText("Threshold: >= Medium");
                }
                else {
                    seekBarIndicator_grass_pollen.setText("Threshold: >= High");
                }
                editor.putInt("notif_grasspollen_thresh",progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar_grass_pollen) {
                // Do something when the touch event starts, if needed
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar_grass_pollen) {
                // Do something when the touch event stops, if needed
            }
        });

        uv_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changes = true;
                editor.putBoolean("notif_uvindex",(uv_index.isChecked()));
                seekBar_update(seekBar_uv_index,seekBarIndicator_uv_index,uv_index.isChecked());
            }
        });

        tree_pollen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changes = true;
                editor.putBoolean("notif_treepollen", (tree_pollen.isChecked()));
                seekBar_update(seekBar_tree_pollen,seekBarIndicator_tree_pollen,tree_pollen.isChecked());
            }
        });

        grass_pollen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changes = true;
                editor.putBoolean("notif_grasspollen", (grass_pollen.isChecked()));
                seekBar_update(seekBar_grass_pollen,seekBarIndicator_grass_pollen,grass_pollen.isChecked());
            }
        });
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
    }
    private void seekBar_update (SeekBar bar, TextView indicator, boolean enabled) {
        if (!enabled) {
            bar.setVisibility(View.GONE);
            indicator.setVisibility(View.GONE);
        }
        else {
            bar.setVisibility(View.VISIBLE);
            indicator.setVisibility(View.VISIBLE);
        }
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
                Intent intent = new Intent(NotificationsPage.this, MainActivity.class);
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