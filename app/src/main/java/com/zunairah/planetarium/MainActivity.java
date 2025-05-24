package com.zunairah.planetarium;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
// import android.view.Menu; // Not strictly needed if not using system ActionBar menu
// import android.view.MenuItem; // Not strictly needed if not using system ActionBar menu
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton; // Import ImageButton
import android.widget.ProgressBar;
import android.widget.TextView;
// import android.widget.Toolbar; // Not using the system Toolbar here

import androidx.annotation.NonNull; // Still useful for other overrides
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.zunairah.planetarium.models.CelestialObject;
import com.zunairah.planetarium.models.Location;
import com.zunairah.planetarium.ui.viewmodels.SkyViewModel;
import com.zunairah.planetarium.ui.views.SkyView;
import com.zunairah.planetarium.utils.DateTimeUtils;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private SkyViewModel skyViewModel;
    private SkyView skyView;
    private Button locationButton;
    private Button timeButton;
    private CardView objectInfoPanel;
    private TextView objectName;
    private TextView objectDetails;
    private SearchView searchView;
    private ProgressBar progressBar;
    private TextView errorTextView;
    private ImageButton settingsButton; // Declare the settings ImageButton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- NO System Toolbar setup needed here as you have a custom top_bar ---
        // Toolbar mainToolbar = findViewById(R.id.your_main_activity_toolbar_id);
        // setSupportActionBar(mainToolbar);
        // --- End System Toolbar Setup ---

        // Initialize ViewModel
        skyViewModel = new ViewModelProvider(this).get(SkyViewModel.class);

        // Initialize views
        skyView = findViewById(R.id.sky_view);
        locationButton = findViewById(R.id.btn_location);
        timeButton = findViewById(R.id.btn_time);
        objectInfoPanel = findViewById(R.id.object_info_panel);
        objectName = findViewById(R.id.object_name);
        objectDetails = findViewById(R.id.object_details);
        searchView = findViewById(R.id.search_view); // From your custom top_bar
        progressBar = findViewById(R.id.progress_bar);
        errorTextView = findViewById(R.id.text_error);
        settingsButton = findViewById(R.id.btn_settings); // Initialize your custom settings button

        // Set up the sky view
        skyView.setOnObjectClickListener(this::showObjectInfo);

        // Observe changes in celestial data
        skyViewModel.getCelestialObjects().observe(this, celestialObjects -> {
            skyView.updateCelestialObjects(celestialObjects);
        });

        // Observe changes in location
        skyViewModel.getSelectedLocation().observe(this, location -> {
            locationButton.setText(location.getName());
            skyView.setObserverLocation(location.getLatitude(), location.getLongitude());
        });

        // Observe changes in date/time
        skyViewModel.getSelectedDate().observe(this, date -> {
            if (DateTimeUtils.isToday(date)) {
                timeButton.setText(R.string.now);
            } else {
                timeButton.setText(DateTimeUtils.formatDateTime(date));
            }
            skyView.setObservationTime(date.getTime());
        });

        // Observe loading state
        skyViewModel.isLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            skyView.setVisibility(isLoading ? View.INVISIBLE : View.VISIBLE);
        });

        // Observe error messages
        skyViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null && !errorMessage.isEmpty()) {
                errorTextView.setText(errorMessage);
                errorTextView.setVisibility(View.VISIBLE);
                Snackbar.make(skyView, errorMessage, Snackbar.LENGTH_LONG).show();
            } else {
                errorTextView.setVisibility(View.GONE);
            }
        });

        // Set up location button
        locationButton.setOnClickListener(v -> showLocationPicker());

        // Set up time button
        timeButton.setOnClickListener(v -> showDateTimePicker());

        // Set up close button for info panel
        findViewById(R.id.btn_close_info).setOnClickListener(v -> {
            objectInfoPanel.setVisibility(View.GONE);
        });

        // Set up search functionality
        setupSearch();

        // --- Set up click listener for your custom settings button ---
        settingsButton.setOnClickListener(v -> {
            Log.d("MainActivity", "Custom Settings ImageButton clicked!");
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
        // --- End Settings Button Setup ---

        // Load initial data
        skyViewModel.loadCurrentSkyData();
    }

    // --- onCreateOptionsMenu and onOptionsItemSelected are NOT NEEDED for your custom settings button ---
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("MainActivity", "onOptionsItemSelected: item ID " + item.getItemId() + ", R.id.action_settings: " + R.id.action_settings);
        if (item.getItemId() == R.id.action_settings) {
            Log.d("MainActivity", "Settings item clicked!");
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */
    // --- End of unused menu methods ---


    private void showObjectInfo(CelestialObject object) {
        objectName.setText(object.getName());
        StringBuilder details = new StringBuilder();
        switch (object.getType()) {
            case STAR:
                details.append(getString(R.string.star_details,
                        object.getMockRA(), object.getMockDec(), object.getMockMagnitude()));
                if (object.getMockSpectralType() != null) {
                    details.append("\nSpectral Type: ").append(object.getMockSpectralType());
                }
                break;
            case PLANET:
            case SUN:
            case MOON:
                details.append(getString(R.string.planet_details,
                        object.getMockRA(), object.getMockDec(), object.getMockMagnitude()));
                break;
        }
        objectDetails.setText(details.toString());
        objectInfoPanel.setVisibility(View.VISIBLE);
    }

    private void showLocationPicker() {
        LocationPickerDialog dialog = new LocationPickerDialog(this);
        dialog.setOnLocationSelectedListener(location -> {
            skyViewModel.setLocation(location);
        });
        dialog.show();
    }

    private void showDateTimePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.select_date)
                .setSelection(skyViewModel.getSelectedDate().getValue().getTime())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(selection);
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                    .setMinute(calendar.get(Calendar.MINUTE))
                    .setTitleText(R.string.select_time)
                    .build();
            timePicker.addOnPositiveButtonClickListener(v -> {
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                Date selectedDate = calendar.getTime();
                skyViewModel.setDate(selectedDate);
            });
            timePicker.show(getSupportFragmentManager(), "time_picker");
        });
        datePicker.show(getSupportFragmentManager(), "date_picker");
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search as text changes if desired, or leave false
                return false;
            }
        });
    }

    private void performSearch(String query) {
        CelestialObject result = skyViewModel.searchObject(query);
        if (result != null) {
            skyView.centerOnObject(result);
            showObjectInfo(result);
            searchView.clearFocus(); // Optional: clear focus after successful search
        } else {
            BottomSheetDialog dialog = new BottomSheetDialog(this);
            View view = getLayoutInflater().inflate(R.layout.dialog_search_not_found, null);
            TextView textView = view.findViewById(R.id.text_not_found);
            textView.setText(getString(R.string.search_not_found, query));
            Button closeButton = view.findViewById(R.id.btn_close);
            closeButton.setOnClickListener(v -> dialog.dismiss());
            dialog.setContentView(view);
            dialog.show();
        }
    }
}