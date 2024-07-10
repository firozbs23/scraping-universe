package com.omnizia.scrapinguniverse.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {

  public static final String OMNIZIA_DB_DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss.SSS+00";

  public static String getCurrentTimeUTC() {
    return getCurrentTimeUTC(OMNIZIA_DB_DATE_FORMATTER);
  }

  public static String getCurrentTimeUTC(String format) {
    // Get the current date and time in UTC
    ZonedDateTime currentTime = ZonedDateTime.now(ZoneOffset.UTC);
    // Use the provided format string for formatting
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    // Format the current time to the desired format
    return currentTime.format(formatter);
  }

  public static Timestamp getCurrentTimestampUTC() {
    return Timestamp.valueOf(
        LocalDateTime.now(ZoneOffset.UTC)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
  }
}

// 2023-11-25 11:00:00.000  ==> yyyy-MM-dd HH:mm:ss.SSSXXX
// 2023-11-25 11:00:00+00  ==> yyyy-MM-dd HH:mm:ss'+00'
