package com.example.alertclock02.mylistadapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alertclock02.AlarmReceiver;
import com.example.alertclock02.MainActivity;
import com.example.alertclock02.R;
import com.example.alertclock02.timeModel.TimeID;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MyAdapter extends BaseAdapter {
    Context context;
    ArrayList<TimeID> arrayList;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Intent my_intent;



    public MyAdapter(Context context, ArrayList<TimeID> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.time_item, null);
            TextView title = (TextView) convertView.findViewById(R.id.text_item);
            Switch aSwitch = (Switch) convertView.findViewById(R.id.switch1);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            TimeID timeID = arrayList.get(position);
            title.setText(timeID.getName());

        //Перетаскивание свифт кнопки
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {

                        int hour = timeID.getHour();
                        int minute = timeID.getMinute();
                        setTimeClock(hour, minute);

                        Log.d("...", "id" + hour);
                        Toast.makeText(context, "ON", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Toast.makeText(context, "OFF", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        return convertView;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    //Активируйться будильник по нужному времение
    public void setTimeClock(int h, int m) {

        Calendar calendar = Calendar.getInstance();

        String timeString = h + ":" + m;

        Intent my_intent = new Intent(context, AlarmReceiver.class);
        my_intent.putExtra("message", timeString);

        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(calendar.SECOND, 0);

        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(),getAlarmInfoPendingIntent());
        alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent());
    }
    //Отмена будильника
    public void cancelTimeClock() {

        alarmManager.cancel(pendingIntent);
    }
    private PendingIntent getAlarmInfoPendingIntent() {
        Intent alarmInfoIntent = new Intent(context, MainActivity.class);
        alarmInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getBroadcast(context, 0, alarmInfoIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private PendingIntent getAlarmActionPendingIntent() {
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getBroadcast(context, 1, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
