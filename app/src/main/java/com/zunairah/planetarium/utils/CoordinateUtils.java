package com.zunairah.planetarium.utils;

import android.graphics.PointF;

public class CoordinateUtils {
    /**
     * Convert celestial coordinates (RA, Dec) to screen coordinates (x, y)
     *
     * @param ra Right Ascension in hours (0-24)
     * @param dec Declination in degrees (-90 to +90)
     * @param width Screen width
     * @param height Screen height
     * @return Point with x, y screen coordinates
     */
    public static PointF celestialToScreen(double ra, double dec, int width, int height) {
        // Simple equirectangular projection
        // RA goes from 0h to 24h (right to left)
        // Dec goes from -90° to +90° (bottom to top)

        // Normalize RA to 0-1 range (reversed for correct orientation)
        float x = (float) ((24 - ra) / 24.0 * width);

        // Normalize Dec to 0-1 range
        float y = (float) ((dec + 90) / 180.0 * height);

        return new PointF(x, y);
    }

    /**
     * Convert screen coordinates (x, y) to celestial coordinates (RA, Dec)
     *
     * @param x X coordinate on screen
     * @param y Y coordinate on screen
     * @param width Screen width
     * @param height Screen height
     * @return double[] with [ra, dec]
     */
    public static double[] screenToCelestial(float x, float y, int width, int height) {
        // Convert x to RA (0-24h, reversed)
        double ra = 24 - (x / width * 24.0);

        // Convert y to Dec (-90° to +90°)
        double dec = (y / height * 180.0) - 90;

        // Ensure RA is in 0-24 range
        ra = (ra + 24) % 24;

        // Clamp Dec to -90 to +90
        dec = Math.max(-90, Math.min(90, dec));

        return new double[] { ra, dec };
    }
}
