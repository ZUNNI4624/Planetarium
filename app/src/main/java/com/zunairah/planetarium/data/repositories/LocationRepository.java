package com.zunairah.planetarium.data.repositories;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zunairah.planetarium.models.Location;
import com.zunairah.planetarium.utils.AssetUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocationRepository {
    private static final String LOCATIONS_FILE = "locations.json";
    private static final String DEFAULT_LOCATION = "Islamabad";

    private final Context context;
    private final Gson gson;
    private List<Location> locations;

    public LocationRepository(Context context) {
        this.context = context;
        this.gson = new Gson();
        loadLocations();
    }

    private void loadLocations() {
        try {
            String json = AssetUtils.readAssetFile(context, LOCATIONS_FILE);
            Type listType = new TypeToken<ArrayList<Location>>(){}.getType();
            locations = gson.fromJson(json, listType);
        } catch (IOException e) {
            e.printStackTrace();
            // Create a default list with just Islamabad if loading fails
            locations = new ArrayList<>();
            locations.add(new Location(DEFAULT_LOCATION, 33.6844, 73.0479));
        }
    }

    public List<Location> getAllLocations() {
        return locations;
    }

    public Location getLocationByName(String name) {
        for (Location location : locations) {
            if (location.getName().equalsIgnoreCase(name)) {
                return location;
            }
        }
        return getDefaultLocation();
    }

    public Location getDefaultLocation() {
        return getLocationByName(DEFAULT_LOCATION);
    }
}
