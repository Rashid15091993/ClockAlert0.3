package com.example.alertclock02.mylistadapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alertclock02.AlarmReceiver;
import com.example.alertclock02.R;
import com.example.alertclock02.timeModel.TimeID;

import java.util.ArrayList;
import java.util.Calendar;

public class MyAdapter extends BaseAdapter {
    Context context;
    ArrayList<TimeID> arrayList;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    public MyAdapter(Context context, ArrayList<TimeID> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.time_item, null);
            TextView title = (TextView) convertView.findViewById(R.id.text_item);
            Switch aSwitch = (Switch) convertView.findViewById(R.id.switch1);
            TimeID timeID = arrayList.get(position);
            title.setText(timeID.getName());
        //Перетаскивание свифт кнопки
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {

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
    public void setTimeClock(int _id,int h, int m) {

        Calendar calendar = Calendar.getInstance();
        Intent my_intent = new Intent(context, AlarmReceiver.class);

        String timeString = h + ":" + m + "|" + _id;
        my_intent.putExtra("message", timeString);
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(Calendar.SECOND, 0);

        pendingIntent = PendingIntent.getBroadcast(context, _id, my_intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

}
