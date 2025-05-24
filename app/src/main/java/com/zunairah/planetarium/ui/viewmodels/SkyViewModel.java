package com.zunairah.planetarium.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.zunairah.planetarium.data.repositories.CelestialRepository;
import com.zunairah.planetarium.data.repositories.LocationRepository;
import com.zunairah.planetarium.models.CelestialObject;
import com.zunairah.planetarium.models.Location;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SkyViewModel extends AndroidViewModel {

    private final CelestialRepository celestialRepository;
    private final LocationRepository locationRepository;

    private final MutableLiveData<List<CelestialObject>> celestialObjects = new MutableLiveData<>();
    private final MutableLiveData<Date> selectedDate = new MutableLiveData<>();
    private final MutableLiveData<Location> selectedLocation = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public SkyViewModel(@NonNull Application application) {
        super(application);
        celestialRepository = new CelestialRepository(application);
        locationRepository = new LocationRepository(application);

        // Set default date to current time
        selectedDate.setValue(Calendar.getInstance().getTime());

        // Set default location (Islamabad)
        selectedLocation.setValue(locationRepository.getDefaultLocation());
    }

    public LiveData<List<CelestialObject>> getCelestialObjects() {
        return celestialObjects;
    }

    public LiveData<Date> getSelectedDate() {
        return selectedDate;
    }

    public LiveData<Location> getSelectedLocation() {
        return selectedLocation;
    }

    public LiveData<Boolean> isLoading() {
        return isLoading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadCurrentSkyData() {
        loadSkyData(selectedDate.getValue(), selectedLocation.getValue());
    }

    public void loadSkyData(Date date, Location location) {
        selectedDate.setValue(date);
        selectedLocation.setValue(location);

        isLoading.setValue(true);
        errorMessage.setValue(null);

        // Load celestial data using repository
        celestialRepository.loadCelestialData(new CelestialRepository.DataCallback() {
            @Override
            public void onDataLoaded(List<CelestialObject> objects) {
                celestialObjects.postValue(objects);
                isLoading.postValue(false);
            }

            @Override
            public void onError(Exception e) {
                errorMessage.postValue("Failed to load celestial data: " + e.getMessage());
                isLoading.postValue(false);
            }
        });
    }

    public void setDate(Date date) {
        loadSkyData(date, selectedLocation.getValue());
    }

    public void setLocation(Location location) {
        loadSkyData(selectedDate.getValue(), location);
    }

    public CelestialObject searchObject(String query) {
        if (query == null || query.isEmpty() || celestialObjects.getValue() == null) {
            return null;
        }

        String normalizedQuery = query.toLowerCase().trim();

        for (CelestialObject object : celestialObjects.getValue()) {
            if (object.getName().toLowerCase().contains(normalizedQuery)) {
                return object;
            }
        }

        return null;
    }
}
