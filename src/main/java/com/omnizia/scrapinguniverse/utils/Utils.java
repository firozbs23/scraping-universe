package com.omnizia.scrapinguniverse.utils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static String getCurrentTimeUTC() {
        // Get the current date and time in UTC
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneOffset.UTC);
        // Define the format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSXXX");
        // Format the current time to the desired format
        return currentTime.format(formatter);
    }
}
