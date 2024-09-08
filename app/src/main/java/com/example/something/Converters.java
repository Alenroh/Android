package com.example.something;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {
    // Convert from Long (timestamp) to Date
    @TypeConverter
    public static Date fromTimeStamp(Long value){
        return value == null ? null : new Date(value);
    }

    // Convert from Date to Long (timestamp)
    @TypeConverter
    public static Long dateToTimeStamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
