package com.example.alertclock02;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.util.Log;
import android.widget.Toast;
import android.net.Uri;
//Класс отвечающий за включение мелодии когда приходит время
public class AlarmReceiver extends BroadcastReceiver {
    MediaPlayer mPlayer;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context.getApplicationContext(), "time", Toast.LENGTH_SHORT).show();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(context.getApplicationContext(), notification);
        ringtone.play();
    }
}
