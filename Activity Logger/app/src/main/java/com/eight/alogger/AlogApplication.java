package com.eight.alogger;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class AlogApplication extends Application {
    public static final String NOTIFY_CHANNEL_ID_SERVICE = "log_service_channel";

    private static int lastNotificationId = 1;
    private static int lastRequestCode = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        NotificationChannel channel = new NotificationChannel(NOTIFY_CHANNEL_ID_SERVICE,
                "Service", NotificationManager.IMPORTANCE_LOW);
        notificationManager.createNotificationChannel(channel);
    }

    public static int getNotificationId() {
        return lastNotificationId++;
    }

    public static int getRequestCode() {
        return lastRequestCode++;
    }
}

