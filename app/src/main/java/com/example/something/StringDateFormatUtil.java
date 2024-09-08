package com.example.something;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class StringDateFormatUtil {

    // Updated the date format to MM/dd/yyyy
    public static String dateFormat = "MM/dd/yyyy";
    public static SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
}
