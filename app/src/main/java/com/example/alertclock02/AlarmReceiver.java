package com.example.alertclock02;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

//Класс отвечающий за включение мелодии когда приходит время
public class AlarmReceiver extends BroadcastReceiver {
    MediaPlayer mPlayer;
    private static final int NOTIFY_ID = 101;
    private static String CHANNEL_ID = "TimeClock";

    @Override
    public void onReceive(Context context, Intent intent) {
        //Уведомление
        String message = intent.getStringExtra("message");

        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Будильник")
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
        createChannellfNeeded(notificationManager);
        notificationManager.notify(NOTIFY_ID, builder.build());
        //Рингтон
        Toast.makeText(context.getApplicationContext(), "time", Toast.LENGTH_SHORT).show();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
        ringtone.play();
    }

    public static void createChannellfNeeded(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }
}
