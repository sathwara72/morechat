package com.morechat.statusapp.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.morechat.statusapp.Activity.QuoteActivity;
import com.morechat.statusapp.R;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Create and send the notification
        sendNotification(context, "Quotes of the day", "Click here to read today's Motivational Quotes");
    }

    private void sendNotification(Context context, String title, String message) {
        // Create a notification channel (required for Android 8.0+)
        String channelId = "daily_notification_channel";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Daily Notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Create an Intent that opens your desired activity when the notification is clicked
        Intent notificationIntent = new Intent(context, QuoteActivity.class); // Replace YourTargetActivity with the name of your activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Set the PendingIntent here
                .setAutoCancel(true); // Close the notification when clicked

        // Show the notification
        int notificationId = 1; // Unique ID for the notification
        notificationManager.notify(notificationId, builder.build());
    }
}
