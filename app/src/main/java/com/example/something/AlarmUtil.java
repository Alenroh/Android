package com.example.something;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class AlarmUtil {

    public static void setAlarm(Context context, Calendar calendar, boolean startOrEnd, String title) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (title != null && !title.isEmpty()) {
            Date localDate = truncateDate(calendar);
            Date currentDate = truncateDate(Calendar.getInstance());

            if (startOrEnd) {
                if (!localDate.before(currentDate)) {
                    Intent intent = new Intent(context, NotificationReceiver.class);
                    intent.putExtra("title", title + " is starting today.");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ++MainActivity.numAlert, intent, FLAG_IMMUTABLE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, localDate.getTime(), pendingIntent);
                    Toast.makeText(context, "Start Date Alert set successfully!", Toast.LENGTH_LONG).show();
                }
            } else {
                if (!localDate.before(currentDate)) {
                    Intent intent = new Intent(context, NotificationReceiver.class);
                    intent.putExtra("title", title + " is ending today.");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ++MainActivity.numAlert, intent, FLAG_IMMUTABLE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, localDate.getTime(), pendingIntent);
                    Toast.makeText(context, "End Date Alert set successfully!", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Toast.makeText(context, "You must set and save the title and dates to set alerts.", Toast.LENGTH_LONG).show();
        }
    }

    public static void setExcursionAlarm(Context context, Calendar calendar, String title) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (title != null && !title.isEmpty()) {
            Date localDate = truncateDate(calendar);
            Date currentDate = truncateDate(Calendar.getInstance());

            if (!localDate.before(currentDate)) {
                Intent intent = new Intent(context, NotificationReceiver.class);
                intent.putExtra("title", "Excursion: " + title + " is starting today.");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ++MainActivity.numAlert, intent, FLAG_IMMUTABLE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, localDate.getTime(), pendingIntent);
                Toast.makeText(context, "Excursion Alarm set successfully!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "You must set and save the title and date to set alerts.", Toast.LENGTH_LONG).show();
        }
    }

    public static Date truncateDate(@NonNull Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
