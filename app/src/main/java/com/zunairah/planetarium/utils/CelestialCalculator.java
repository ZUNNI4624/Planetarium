package com.zunairah.planetarium.utils;

import android.graphics.PointF;

import com.zunairah.planetarium.models.HorizontalCoords;

import java.util.Calendar;
import java.util.TimeZone;

public class CelestialCalculator {

    /**
     * Calculate Local Sidereal Time in degrees
     * @param longitudeDeg Observer's longitude in degrees
     * @param utcMillis UTC time in milliseconds
     * @return Local Sidereal Time in degrees
     */
    public static double calculateLST(double longitudeDeg, long utcMillis) {
        // Convert UTC milliseconds to calendar
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis(utcMillis);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar months are 0-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // Calculate Julian Date
        double jd = calculateJulianDate(year, month, day, hour, minute, second);

        // Calculate Greenwich Mean Sidereal Time (GMST)
        double gmst = calculateGMST(jd);

        // Convert GMST to Local Sidereal Time (LST)
        double lst = gmst + longitudeDeg;

        // Normalize to 0-360 degrees
        lst = normalizeAngle(lst);

        return lst;
    }

    /**
     * Calculate Julian Date
     */
    private static double calculateJulianDate(int year, int month, int day, int hour, int minute, int second) {
        if (month <= 2) {
            year -= 1;
            month += 12;
        }

        int a = year / 100;
        int b = 2 - a + (a / 4);

        double jd = Math.floor(365.25 * (year + 4716)) +
                Math.floor(30.6001 * (month + 1)) +
                day + b - 1524.5;

        // Add time fraction
        double timeFraction = (hour + minute / 60.0 + second / 3600.0) / 24.0;
        jd += timeFraction;

        return jd;
    }

    /**
     * Calculate Greenwich Mean Sidereal Time (GMST) in degrees
     */
    private static double calculateGMST(double jd) {
        // Days since J2000.0
        double t = (jd - 2451545.0) / 36525.0;

        // GMST at 0h UT in seconds
        double gmst0 = 24110.54841 + 8640184.812866 * t + 0.093104 * t * t - 0.0000062 * t * t * t;

        // Convert to degrees and normalize
        double gmstDeg = (gmst0 / 240.0) % 360.0; // 240 = 86400 / 360

        if (gmstDeg < 0) {
            gmstDeg += 360.0;
        }

        return gmstDeg;
    }

    /**
     * Convert Equatorial coordinates to Horizontal coordinates
     * @param raDeg Right Ascension in degrees
     * @param decDeg Declination in degrees
     * @param latitudeDeg Observer's latitude in degrees
     * @param lstDeg Local Sidereal Time in degrees
     * @return HorizontalCoords object with altitude and azimuth
     */
    public static HorizontalCoords convertEquatorialToHorizontal(double raDeg, double decDeg,
                                                                 double latitudeDeg, double lstDeg) {
        // Convert to radians
        double raRad = Math.toRadians(raDeg);
        double decRad = Math.toRadians(decDeg);
        double latRad = Math.toRadians(latitudeDeg);
        double lstRad = Math.toRadians(lstDeg);

        // Calculate Local Hour Angle (LHA)
        double lhaRad = lstRad - raRad;

        // Calculate altitude
        double sinAlt = Math.sin(decRad) * Math.sin(latRad) +
                Math.cos(decRad) * Math.cos(latRad) * Math.cos(lhaRad);
        double altRad = Math.asin(sinAlt);

        // Calculate azimuth
        double cosAz = (Math.sin(decRad) - Math.sin(altRad) * Math.sin(latRad)) /
                (Math.cos(altRad) * Math.cos(latRad));
        double sinAz = -Math.sin(lhaRad) * Math.cos(decRad) / Math.cos(altRad);

        double azRad = Math.atan2(sinAz, cosAz);

        // Convert back to degrees
        double altitudeDeg = Math.toDegrees(altRad);
        double azimuthDeg = Math.toDegrees(azRad);

        // Normalize azimuth to 0-360 degrees
        azimuthDeg = normalizeAngle(azimuthDeg);

        return new HorizontalCoords(altitudeDeg, azimuthDeg);
    }

    /**
     * Map Horizontal coordinates to screen coordinates
     * @param azimuthDeg Azimuth in degrees (0 = North, 90 = East, 180 = South, 270 = West)
     * @param altitudeDeg Altitude in degrees (0 = horizon, 90 = zenith)
     * @param viewWidth Screen width in pixels
     * @param viewHeight Screen height in pixels
     * @param zoomFactor Zoom factor (1.0 = normal, >1.0 = zoomed in)
     * @return PointF with screen coordinates
     */
    public static PointF mapHorizontalToScreen(double azimuthDeg, double altitudeDeg,
                                               float viewWidth, float viewHeight, float zoomFactor) {
        // Only show objects above horizon
        if (altitudeDeg < 0) {
            return new PointF(-1, -1); // Off-screen indicator
        }

        // Convert to radians
        double azRad = Math.toRadians(azimuthDeg);
        double altRad = Math.toRadians(altitudeDeg);

        // Use azimuthal equidistant projection
        // Distance from center is proportional to (90 - altitude)
        double r = (90.0 - altitudeDeg) / 90.0; // 0 at zenith, 1 at horizon

        // Apply zoom factor
        r = r / zoomFactor;

        // Calculate screen radius
        float maxRadius = Math.min(viewWidth, viewHeight) / 2.0f;
        float screenR = (float) (r * maxRadius);

        // Calculate screen coordinates
        // Azimuth 0 (North) points up, 90 (East) points right
        float centerX = viewWidth / 2.0f;
        float centerY = viewHeight / 2.0f;

        float x = centerX + screenR * (float) Math.sin(azRad);
        float y = centerY - screenR * (float) Math.cos(azRad); // Negative because screen Y increases downward

        return new PointF(x, y);
    }

    /**
     * Normalize angle to 0-360 degrees
     */
    private static double normalizeAngle(double angle) {
        angle = angle % 360.0;
        if (angle < 0) {
            angle += 360.0;
        }
        return angle;
    }

    /**
     * Calculate distance between two points
     */
    public static float calculateDistance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }
}
