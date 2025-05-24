package com.zunairah.planetarium.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MMM d, yyyy", Locale.US);
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.US);
    private static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("MMM d, yyyy HH:mm", Locale.US);

    /**
     * Check if a date is today
     *
     * @param date Date to check
     * @return true if the date is today
     */
    public static boolean isToday(Date date) {
        Calendar today = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Format a date for display
     *
     * @param date Date to format
     * @return Formatted date string
     */
    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    /**
     * Format a time for display
     *
     * @param date Date to format
     * @return Formatted time string
     */
    public static String formatTime(Date date) {
        return TIME_FORMAT.format(date);
    }

    /**
     * Format a date and time for display
     *
     * @param date Date to format
     * @return Formatted date and time string
     */
    public static String formatDateTime(Date date) {
        return DATE_TIME_FORMAT.format(date);
    }
}
