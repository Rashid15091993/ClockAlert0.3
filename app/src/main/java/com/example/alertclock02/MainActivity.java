package com.example.alertclock02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.alertclock02.mylistadapter.MyListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ModalBottomSheet.ShareDataInterface{
    private SwipeMenuListView list;
    private List timeList = new ArrayList();
    private EditText textView;
    private NotificationManager notificationManager;
    private static final int NOTIFY_ID = 101;

    DBHelper dbHelper;
    TimePicker timePicker;
    MyListAdapter adapter;

    // Идентификатор канала
    private static String CHANNEL_ID = "TimeClock";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        //DataBase
        dbHelper = new DBHelper(this);
        //Adapter
        adapter = new MyListAdapter(this, R.layout.time_item, timeList);

        findViewById(R.id.toolbar1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog(R.layout.modal_bs_one);
            }
        });
    }
    //функция для старта всех виджетов во время включения приложения
    @Override
    protected void onStart() {

        super.onStart();
        readDataBase();
    }

    private void setDialog(int layoutStyle) {
        BottomSheetDialogFragment bottomSheetDialogFragment = new ModalBottomSheet(layoutStyle);
        bottomSheetDialogFragment.setShowsDialog(true);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }
    //Функция которая выводит текст с временим и воводит в в список лист
    @Override
    public void sendData(String data, int hour, int min) {
        writeDataBase(data, hour, min);
        timeList.add(data);
        list = (SwipeMenuListView) findViewById(R.id.listView1);
        list.setAdapter(adapter);
        onManager();

        list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                switch (index) {

                    case 0:
                        // delete
                        timeList.remove(position);
                        adapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

    }

    private void onManager() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Напоминание")
                        .setContentText("Пора покормить кота")
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
        
        createChannellfNeeded(notificationManager);
        notificationManager.notify(NOTIFY_ID, builder.build());

    }

    public static void createChannellfNeeded(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    //Запись в Базу данных
    private void writeDataBase(String time, int hour, int min){

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_TIME, time);
        contentValues.put(DBHelper.KEY_HOUR, hour);
        contentValues.put(DBHelper.KEY_MIN, min);
        database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
    }

    private void readDataBase(){
        //Чтение с Базы данных и вывод списка
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null,null,null,null,null,null);

        if (cursor.moveToFirst()) {
            int timeIndex = cursor.getColumnIndex(DBHelper.KEY_TIME);
            do {
                timeList.add(cursor.getString(timeIndex));
                list = (SwipeMenuListView) findViewById(R.id.listView1);
                list.setAdapter(adapter);
                //Метод свайпа для удаления
                SwipeMenuCreator creator = new SwipeMenuCreator() {

                    @Override
                    public void create(SwipeMenu menu) {
                        // create "delete" item
                        SwipeMenuItem deleteItem = new SwipeMenuItem(
                                getApplicationContext());
                        // set item background
                        deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                                0x3F, 0x25)));
                        // set item width
                        deleteItem.setWidth(270);
                        // set a title
                        deleteItem.setTitle("Удалить");
                        deleteItem.setTitleSize(10);
                        deleteItem.setTitleColor(Color.WHITE);
                        // add to menu
                        menu.addMenuItem(deleteItem);
                    }
                };

// set creator
                list.setMenuCreator(creator);
                list.setOpenInterpolator(new BounceInterpolator());
                list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

                        switch (index) {

                            case 0:
                                // delete
                                timeList.remove(position);
                                adapter.notifyDataSetChanged();
                                break;
                        }
                        return false;
                    }
                });

            } while (cursor.moveToNext());

        }else {
            Log.d("mLog","0row");
        }
        cursor.close();
        dbHelper.close();
    }

}