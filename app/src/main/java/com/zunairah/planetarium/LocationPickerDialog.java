package com.zunairah.planetarium; // Assuming this is the correct package

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
// import android.widget.EditText; // More specific type below

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText; // Use this if in your layout
import com.zunairah.planetarium.adapters.LocationAdapter;
import com.zunairah.planetarium.data.repositories.LocationRepository;
import com.zunairah.planetarium.models.Location;

// Removed unused List import as adapter handles its own list now

public class LocationPickerDialog extends Dialog {
    private LocationRepository locationRepository;
    private OnLocationSelectedListener listener;
    private LocationAdapter adapter;
    // No need to store allLocations here if adapter handles full list for filtering

    public interface OnLocationSelectedListener {
        void onLocationSelected(Location location);
    }

    public LocationPickerDialog(@NonNull Context context) {
        super(context);
        // Consider using getApplicationContext() for repository if it doesn't need activity context
        locationRepository = new LocationRepository(context.getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_location_picker);

        // --- Make Dialog Wider and Control Height ---
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();
            params.copyFrom(window.getAttributes());
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            params.width = (int) (displayMetrics.widthPixels * 0.90);
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
            // If your dialog_location_picker.xml root has a background,
            // you might want to make the dialog's window background transparent
            // to avoid overdraw or conflicting corners.
            // window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        // --- End Dialog Sizing ---

        RecyclerView recyclerView = findViewById(R.id.recycler_locations);
        Button cancelButton = findViewById(R.id.btn_cancel_location_picker);
        TextInputEditText searchEditText = findViewById(R.id.edit_search_location);


        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Pass all locations from the repository to the adapter
        adapter = new LocationAdapter(locationRepository.getAllLocations());
        adapter.setOnLocationClickListener(location -> {
            if (listener != null) {
                listener.onLocationSelected(location);
            }
            dismiss();
        });
        recyclerView.setAdapter(adapter);

        // Set up search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s); // Delegate filtering to the adapter
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Set up cancel button
        cancelButton.setOnClickListener(v -> dismiss());
    }

    public void setOnLocationSelectedListener(OnLocationSelectedListener listener) {
        this.listener = listener;
    }
}