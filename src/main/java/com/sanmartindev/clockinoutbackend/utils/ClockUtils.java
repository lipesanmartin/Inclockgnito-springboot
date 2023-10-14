package com.sanmartindev.clockinoutbackend.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClockUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static LocalTime parseTimeString(String timeString) {
        return LocalTime.parse(timeString, formatter);
    }

    public static String formatTime(LocalTime time) {
        return time.format(formatter);
    }
}
