package com.apkglobal.parking.easypark;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.apkglobal.parking.R;

public class NotificationPublisher extends BroadcastReceiver {
 
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
 
    public void onReceive(Context context, Intent intent) {
 
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.i("NotificationPublisher","Inside onReceive");
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
 


    }
}