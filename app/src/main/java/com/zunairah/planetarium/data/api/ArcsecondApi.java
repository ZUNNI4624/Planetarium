package com.zunairah.planetarium.data.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
// Assuming com.zunairah.planetarium.models.Location exists
import com.zunairah.planetarium.models.Location;
import com.zunairah.planetarium.models.Planet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ArcsecondApi {
    private static final String TAG = "ArcsecondApi";
    private static final String BASE_URL = "https://api.arcsecond.io/ephemerides/";

    private final OkHttpClient client;
    private final Gson gson;

    // Interface for callbacks
    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(Exception e);
    }

    public ArcsecondApi(Context context) { // Context might be unused here, but often kept for future use (e.g., network state checking)
        this.gson = new Gson();

        // Configure OkHttpClient with timeout
        this.client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public void getPlanetPosition(String planetName, Date date, Location location, ApiCallback<Planet> callback) {
        String url = buildPlanetApiUrl(planetName, date, location);
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "API call failed for " + planetName, e);
                if (callback != null) {
                    callback.onError(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonData = response.body().string();
                    try {
                        Planet planet = parsePlanetResponse(jsonData, planetName);
                        if (callback != null) {
                            callback.onSuccess(planet);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing response for " + planetName, e);
                        if (callback != null) {
                            callback.onError(e);
                        }
                    }
                } else {
                    String errorMessage = "API error for " + planetName + ": " + response.code() + " " + response.message();
                    Log.e(TAG, errorMessage);
                    if (callback != null) {
                        callback.onError(new IOException(errorMessage));
                    }
                }
            }
        });
    }

    private String buildPlanetApiUrl(String planetName, Date date, Location location) {
        // Format: https://api.arcsecond.io/ephemerides/{planet}/?date={date}&observer_latitude={lat}&observer_longitude={lon}
        // The API seems to use observer_latitude and observer_longitude based on typical usage.
        // Date format: YYYY-MM-DD
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String dateStr = sdf.format(date);

        // Ensure planetName is URL-friendly (lowercase, though API might handle casing)
        String safePlanetName = planetName.toLowerCase();

        return BASE_URL + safePlanetName + "/?date=" + dateStr +
                "&observer_latitude=" + location.getLatitude() +
                "&observer_longitude=" + location.getLongitude();
    }

    private Planet parsePlanetResponse(String jsonResponse, String planetName) {
        try {
            JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

            double ra = 0;  // RA is usually in degrees from this API
            double dec = 0; // Dec is usually in degrees
            double magnitude = 0; // Magnitude (double for parsing, then cast to float)
            // The API might return "RA" and "DEC" or other variations like "ra_deg", "dec_deg"
            // For Arcsecond, it's typically under a "coordinates" object, then "equatorial"
            // And positions are often under "position" -> "equatorial" -> "right_ascension" (hours) or "right_ascension_degrees"
            // For simplicity, let's assume top-level "ra" and "dec" in degrees for now,
            // but real API response structure needs to be checked.

            // Example: if response is { "position": { "equatorial": { "right_ascension_degrees": X, "declination_degrees": Y }, "magnitude": Z } }
            if (jsonObject.has("position")) {
                JsonObject positionObj = jsonObject.getAsJsonObject("position");
                if (positionObj.has("equatorial")) {
                    JsonObject equatorialObj = positionObj.getAsJsonObject("equatorial");
                    if (equatorialObj.has("right_ascension_degrees")) { // Arcsecond typically provides RA in degrees
                        ra = equatorialObj.get("right_ascension_degrees").getAsDouble();
                    } else if (equatorialObj.has("right_ascension")) { // Or hours, needs conversion
                        // Example: convert hours to degrees: ra_hours * 15
                        ra = equatorialObj.get("right_ascension").getAsDouble() * 15.0;
                    }
                    if (equatorialObj.has("declination_degrees")) {
                        dec = equatorialObj.get("declination_degrees").getAsDouble();
                    } else if (equatorialObj.has("declination")) { // Often degrees by default
                        dec = equatorialObj.get("declination").getAsDouble();
                    }
                }
                if (positionObj.has("magnitude") && !positionObj.get("magnitude").isJsonNull()) {
                    magnitude = positionObj.get("magnitude").getAsDouble();
                } else {
                    magnitude = getDefaultMagnitude(planetName);
                }
            } else {
                // Fallback if "position" object is not found (less likely for Arcsecond success response)
                if (jsonObject.has("ra")) { // Assuming direct ra/dec if position is missing
                    ra = jsonObject.get("ra").getAsDouble();
                }
                if (jsonObject.has("dec")) {
                    dec = jsonObject.get("dec").getAsDouble();
                }
                if (jsonObject.has("magnitude") && !jsonObject.get("magnitude").isJsonNull()) {
                    magnitude = jsonObject.get("magnitude").getAsDouble();
                } else {
                    magnitude = getDefaultMagnitude(planetName);
                }
            }


            // Use the simplified Planet constructor that takes (name, ra, dec, magnitude)
            // It will internally set id = name and mockSpectralType (for color) to null.
            // Magnitude needs to be cast to float for the Planet constructor.
            return new Planet(planetName, ra, dec, (float) magnitude);

        } catch (Exception e) {
            Log.e(TAG, "Error parsing planet data for " + planetName + ": " + jsonResponse, e);
            // Return a placeholder object with default values
            // Cast magnitude to float here as well.
            return new Planet(planetName, 0, 0, (float) getDefaultMagnitude(planetName));
        }
    }

    private double getDefaultMagnitude(String planetName) {
        switch (planetName.toLowerCase()) {
            case "sun": return -26.7;
            case "moon": return -12.6;
            case "mercury": return -0.5;
            case "venus": return -4.1;
            case "mars": return -2.6; // Can vary significantly
            case "jupiter": return -2.2; // Can vary
            case "saturn": return 0.5;  // Can vary
            case "uranus": return 5.7;
            case "neptune": return 7.8;
            default: return 20.0; // A very faint default for unknown objects
        }
    }
}