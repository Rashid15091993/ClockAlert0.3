package com.example.alertclock02.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.alertclock02.timeModel.TimeID;

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
    //Открытие базы
    public void openDb() {
        db = dbHelper.getWritableDatabase();

    }
    //запись в базу
    public void insertDb(String time, int hour, int min) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_TIME, time);
        contentValues.put(DBHelper.KEY_HOUR, hour);
        contentValues.put(DBHelper.KEY_MIN, min);
        db.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
    }
//чтение базы
    public List<TimeID> readDb() {
        List<TimeID> timeList  = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.TABLE_CONTACTS, null, null, null,
                null,null, null);

        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID));
            String timeText = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TIME));
            TimeID timeID = new TimeID(id, timeText);

            timeList.add(timeID);
        }
        cursor.close();
        return timeList;
    }
    //чтение базы
    public List<String> readDbId() {
        List<String> idList  = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.TABLE_CONTACTS, null, null, null,
                null,null, null);

        while(cursor.moveToNext()) {
            String idText = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ID));
            idList.add(idText);
        }
        cursor.close();
        return idList;
    }

//удаление записи
    public void deleteWidgetDb(String time) {
        db.delete(dbHelper.TABLE_CONTACTS, dbHelper.KEY_ID + "= ? ", new String[]{time});

    }
//закрытие базы
    public void closeDb() {

        dbHelper.close();
    }


}
