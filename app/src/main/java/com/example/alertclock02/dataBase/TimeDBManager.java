package com.example.alertclock02.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TimeDBManager {
    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public TimeDBManager(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);

    }
    public void openDb() {
        db = dbHelper.getWritableDatabase();

    }
    public void insertDb(String time, int hour, int min) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_TIME, time);
        contentValues.put(DBHelper.KEY_HOUR, hour);
        contentValues.put(DBHelper.KEY_MIN, min);
        db.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
    }

    public List<String> readDb() {
        List<String> timeList  = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.TABLE_CONTACTS, null, null, null,
                null,null, null);

        while(cursor.moveToNext()) {
            String timeText = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TIME));
            timeList.add(timeText);
        }
        cursor.close();
        return timeList;
    }
    public void closeDb() {
        dbHelper.close();
    }
}
