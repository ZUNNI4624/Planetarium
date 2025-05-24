package com.zunairah.planetarium.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String PREFS_NAME = "planetarium_settings";

    // Settings keys
    private static final String KEY_SHOW_STARS = "show_stars";
    private static final String KEY_SHOW_PLANETS = "show_planets";
    private static final String KEY_SHOW_LABELS = "show_labels";
    private static final String KEY_SHOW_FAINT_STARS = "show_faint_stars";
    private static final String KEY_SHOW_GLOW_EFFECTS = "show_glow_effects";
    private static final String KEY_BRIGHTNESS = "brightness";
    private static final String KEY_ZOOM_SENSITIVITY = "zoom_sensitivity";

    // Default values
    private static final boolean DEFAULT_SHOW_STARS = true;
    private static final boolean DEFAULT_SHOW_PLANETS = true;
    private static final boolean DEFAULT_SHOW_LABELS = true;
    private static final boolean DEFAULT_SHOW_FAINT_STARS = true;
    private static final boolean DEFAULT_SHOW_GLOW_EFFECTS = true;
    private static final float DEFAULT_BRIGHTNESS = 1.0f;
    private static final float DEFAULT_ZOOM_SENSITIVITY = 1.0f;

    private static SettingsManager instance;
    private final SharedPreferences preferences;

    private SettingsManager(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SettingsManager getInstance(Context context) {
        if (instance == null) {
            instance = new SettingsManager(context);
        }
        return instance;
    }

    // Getters
    public boolean isShowStars() {
        return preferences.getBoolean(KEY_SHOW_STARS, DEFAULT_SHOW_STARS);
    }

    public boolean isShowPlanets() {
        return preferences.getBoolean(KEY_SHOW_PLANETS, DEFAULT_SHOW_PLANETS);
    }

    public boolean isShowLabels() {
        return preferences.getBoolean(KEY_SHOW_LABELS, DEFAULT_SHOW_LABELS);
    }

    public boolean isShowFaintStars() {
        return preferences.getBoolean(KEY_SHOW_FAINT_STARS, DEFAULT_SHOW_FAINT_STARS);
    }

    public boolean isShowGlowEffects() {
        return preferences.getBoolean(KEY_SHOW_GLOW_EFFECTS, DEFAULT_SHOW_GLOW_EFFECTS);
    }

    public float getBrightness() {
        return preferences.getFloat(KEY_BRIGHTNESS, DEFAULT_BRIGHTNESS);
    }

    public float getZoomSensitivity() {
        return preferences.getFloat(KEY_ZOOM_SENSITIVITY, DEFAULT_ZOOM_SENSITIVITY);
    }

    // Setters
    public void setShowStars(boolean showStars) {
        preferences.edit().putBoolean(KEY_SHOW_STARS, showStars).apply();
    }

    public void setShowPlanets(boolean showPlanets) {
        preferences.edit().putBoolean(KEY_SHOW_PLANETS, showPlanets).apply();
    }

    public void setShowLabels(boolean showLabels) {
        preferences.edit().putBoolean(KEY_SHOW_LABELS, showLabels).apply();
    }

    public void setShowFaintStars(boolean showFaintStars) {
        preferences.edit().putBoolean(KEY_SHOW_FAINT_STARS, showFaintStars).apply();
    }

    public void setShowGlowEffects(boolean showGlowEffects) {
        preferences.edit().putBoolean(KEY_SHOW_GLOW_EFFECTS, showGlowEffects).apply();
    }

    public void setBrightness(float brightness) {
        preferences.edit().putFloat(KEY_BRIGHTNESS, brightness).apply();
    }

    public void setZoomSensitivity(float zoomSensitivity) {
        preferences.edit().putFloat(KEY_ZOOM_SENSITIVITY, zoomSensitivity).apply();
    }

    // Reset to defaults
    public void resetToDefaults() {
        preferences.edit()
                .putBoolean(KEY_SHOW_STARS, DEFAULT_SHOW_STARS)
                .putBoolean(KEY_SHOW_PLANETS, DEFAULT_SHOW_PLANETS)
                .putBoolean(KEY_SHOW_LABELS, DEFAULT_SHOW_LABELS)
                .putBoolean(KEY_SHOW_FAINT_STARS, DEFAULT_SHOW_FAINT_STARS)
                .putBoolean(KEY_SHOW_GLOW_EFFECTS, DEFAULT_SHOW_GLOW_EFFECTS)
                .putFloat(KEY_BRIGHTNESS, DEFAULT_BRIGHTNESS)
                .putFloat(KEY_ZOOM_SENSITIVITY, DEFAULT_ZOOM_SENSITIVITY)
                .apply();
    }
}
