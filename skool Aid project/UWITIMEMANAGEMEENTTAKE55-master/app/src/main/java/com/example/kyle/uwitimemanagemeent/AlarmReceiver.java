package com.example.kyle.uwitimemanagemeent;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


public class AlarmReceiver extends BroadcastReceiver {



    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"THIS IS MY ALARM",Toast.LENGTH_LONG).show();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);



        builder.setAutoCancel(true)

                .setDefaults(Notification.DEFAULT_ALL)

                .setWhen(System.currentTimeMillis())

                .setSmallIcon(R.mipmap.ic_launcher)

                .setContentTitle("Alarm actived!")

                .setContentText("THIS IS MY ALARM")

                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)

                .setContentInfo("Info");



        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1,builder.build());

    }

}