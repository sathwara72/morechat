package com.morechat.statusapp.Notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class NotificationScheduler {
    public static void scheduleDailyNotification(Context context) {
        // Set the time for the notification (10:00 AM)
        int hourOfDay = 15;
        int minute = 25;

        // Create an intent for the BroadcastReceiver
        Intent intent = new Intent(context, NotificationReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Set up the AlarmManager to trigger the BroadcastReceiver at the specified time
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getTriggerTime(hourOfDay, minute), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private static long getTriggerTime(int hourOfDay, int minute) {
        // Calculate the trigger time in milliseconds
        long currentTime = System.currentTimeMillis();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.set(java.util.Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(java.util.Calendar.MINUTE, minute);
        calendar.set(java.util.Calendar.SECOND, 0);
        long triggerTime = calendar.getTimeInMillis();

        // If the trigger time is in the past, schedule it for the next day
        if (triggerTime <= currentTime) {
            triggerTime += AlarmManager.INTERVAL_DAY;
        }

        return triggerTime;
    }
}
