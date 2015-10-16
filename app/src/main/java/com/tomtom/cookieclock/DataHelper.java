package com.tomtom.cookieclock;

/**
 * Copyright (C) TomTom International B.V., 2015
 * All rights reserved.
 */
public class DataHelper {

    public static String timeToString(long updatedTime) {
        String timeInString;

        int secs = (int) (updatedTime / 1000);
        int mins = secs / 60;
        secs = secs % 60;
        int milliseconds = (int) (updatedTime % 1000);
        timeInString = String.format("%02d", mins) + ":"
                + String.format("%02d", secs) + ":"
                + String.format("%03d", milliseconds);
        return timeInString;
    }
}
