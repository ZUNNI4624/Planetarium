package com.zunairah.planetarium;

import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import androidx.preference.SeekBarPreference;

import com.zunairah.planetarium.utils.SettingsManager;

public class SettingsFragment extends PreferenceFragmentCompat {
    private SettingsManager settingsManager;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        settingsManager = SettingsManager.getInstance(requireContext());

        // Initialize preferences
        setupPreferences();
    }

    private void setupPreferences() {
        // Stars
        SwitchPreferenceCompat showStars = findPreference("show_stars");
        if (showStars != null) {
            showStars.setChecked(settingsManager.isShowStars());
            showStars.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean value = (Boolean) newValue;
                settingsManager.setShowStars(value);
                return true;
            });
        }

        // Planets
        SwitchPreferenceCompat showPlanets = findPreference("show_planets");
        if (showPlanets != null) {
            showPlanets.setChecked(settingsManager.isShowPlanets());
            showPlanets.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean value = (Boolean) newValue;
                settingsManager.setShowPlanets(value);
                return true;
            });
        }

        // Labels
        SwitchPreferenceCompat showLabels = findPreference("show_labels");
        if (showLabels != null) {
            showLabels.setChecked(settingsManager.isShowLabels());
            showLabels.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean value = (Boolean) newValue;
                settingsManager.setShowLabels(value);
                return true;
            });
        }

        // Faint Stars
        SwitchPreferenceCompat showFaintStars = findPreference("show_faint_stars");
        if (showFaintStars != null) {
            showFaintStars.setChecked(settingsManager.isShowFaintStars());
            showFaintStars.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean value = (Boolean) newValue;
                settingsManager.setShowFaintStars(value);
                return true;
            });
        }

        // Glow Effects
        SwitchPreferenceCompat showGlowEffects = findPreference("show_glow_effects");
        if (showGlowEffects != null) {
            showGlowEffects.setChecked(settingsManager.isShowGlowEffects());
            showGlowEffects.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean value = (Boolean) newValue;
                settingsManager.setShowGlowEffects(value);
                return true;
            });
        }

        // Brightness
        SeekBarPreference brightness = findPreference("brightness");
        if (brightness != null) {
            brightness.setMin(10);
            brightness.setMax(100);
            brightness.setValue((int) (settingsManager.getBrightness() * 100));
            brightness.setOnPreferenceChangeListener((preference, newValue) -> {
                int value = (Integer) newValue;
                settingsManager.setBrightness(value / 100f);
                return true;
            });
        }

        // Zoom Sensitivity
        SeekBarPreference zoomSensitivity = findPreference("zoom_sensitivity");
        if (zoomSensitivity != null) {
            zoomSensitivity.setMin(1);
            zoomSensitivity.setMax(10);
            zoomSensitivity.setValue((int) (settingsManager.getZoomSensitivity() * 10));
            zoomSensitivity.setOnPreferenceChangeListener((preference, newValue) -> {
                int value = (Integer) newValue;
                settingsManager.setZoomSensitivity(value / 10f);
                return true;
            });
        }

        // Reset to defaults
        Preference resetDefaults = findPreference("reset_defaults");
        if (resetDefaults != null) {
            resetDefaults.setOnPreferenceClickListener(preference -> {
                settingsManager.resetToDefaults();
                updatePreferencesFromSettings();
                return true;
            });
        }
    }

    private void updatePreferencesFromSettings() {
        // Update all preferences to match settings
        SwitchPreferenceCompat showStars = findPreference("show_stars");
        if (showStars != null) {
            showStars.setChecked(settingsManager.isShowStars());
        }

        SwitchPreferenceCompat showPlanets = findPreference("show_planets");
        if (showPlanets != null) {
            showPlanets.setChecked(settingsManager.isShowPlanets());
        }

        SwitchPreferenceCompat showLabels = findPreference("show_labels");
        if (showLabels != null) {
            showLabels.setChecked(settingsManager.isShowLabels());
        }

        SwitchPreferenceCompat showFaintStars = findPreference("show_faint_stars");
        if (showFaintStars != null) {
            showFaintStars.setChecked(settingsManager.isShowFaintStars());
        }

        SwitchPreferenceCompat showGlowEffects = findPreference("show_glow_effects");
        if (showGlowEffects != null) {
            showGlowEffects.setChecked(settingsManager.isShowGlowEffects());
        }

        SeekBarPreference brightness = findPreference("brightness");
        if (brightness != null) {
            brightness.setValue((int) (settingsManager.getBrightness() * 100));
        }

        SeekBarPreference zoomSensitivity = findPreference("zoom_sensitivity");
        if (zoomSensitivity != null) {
            zoomSensitivity.setValue((int) (settingsManager.getZoomSensitivity() * 10));
        }
    }
}
