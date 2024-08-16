package com.example.something;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ExcursionNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("excursion_title");
        String message = intent.getStringExtra("message");
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

        // Here, you could implement a notification instead of a Toast.
    }
}
