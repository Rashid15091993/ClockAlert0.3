package com.example.alertclock02.mylistadapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.alertclock02.AlarmReceiver;
import com.example.alertclock02.DBHelper;
import com.example.alertclock02.MainActivity;
import com.example.alertclock02.R;

import java.util.Calendar;
import java.util.List;

public class MyListAdapter extends ArrayAdapter<String> {

    private int layout;
    DBHelper dbHelper;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    public MyListAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        dbHelper = new DBHelper(getContext());
        layout = resource;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder mainViewHolder = null;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(layout, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.text_item);
            viewHolder.aSwitch = (Switch) convertView.findViewById(R.id.switch1);
            convertView.setTag(viewHolder);
            //Добавлять в лист если пустой лист
            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.title.setText(getItem(position));
            //Перетаскивание свифт кнопки
            viewHolder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        readDataBase();
                        Toast.makeText(getContext(), "ON", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), "OFF", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else {
            //До добавлять в лист если не пустой
            mainViewHolder = (ViewHolder) convertView.getTag();
            mainViewHolder.title.setText(getItem(position));
        }
        return convertView;


    }
    public class ViewHolder {
        TextView title;
        Switch aSwitch;

    }
    private void readDataBase(){
        //Чтение с Базы данных и включение будильника
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null,null,null,null,null,null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int hourIndex = cursor.getColumnIndex(DBHelper.KEY_HOUR);
            int minIndex = cursor.getColumnIndex(DBHelper.KEY_MIN);
            do {
                setTimeClock(cursor.getInt(idIndex), cursor.getInt(hourIndex), cursor.getInt(minIndex));

            } while (cursor.moveToNext());

        }else {
            Log.d("mLog","0row");
        }
        cursor.close();
        dbHelper.close();
    }

    //Активируйться будильник по нужному времение
    public void setTimeClock(int _id,int h, int m) {

        Calendar calendar = Calendar.getInstance();
        Intent my_intent = new Intent(getContext(), AlarmReceiver.class);

        String timeString = h + ":" + m + "|" + _id;
        my_intent.putExtra("message", timeString);
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(Calendar.SECOND, 0);

        pendingIntent = PendingIntent.getBroadcast(getContext(), _id, my_intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}

