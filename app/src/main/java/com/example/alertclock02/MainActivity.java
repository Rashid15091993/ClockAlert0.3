package com.example.alertclock02;

import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.TimePicker;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.alertclock02.dataBase.TimeDBManager;
import com.example.alertclock02.mylistadapter.MyListAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MainActivity extends AppCompatActivity implements ModalBottomSheet.ShareDataInterface{
    private SwipeMenuListView list;
    private EditText textView;
    private TimeDBManager timeDBManager;

    TimePicker timePicker;
    MyListAdapter adapter;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //dataBase
        timeDBManager = new TimeDBManager(this);

        timePicker = (TimePicker) findViewById(R.id.timePicker);


        findViewById(R.id.toolbar1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialog(R.layout.modal_bs_one);
            }
        });
    }

    private void setDialog(int layoutStyle) {
        BottomSheetDialogFragment bottomSheetDialogFragment = new ModalBottomSheet(layoutStyle);
        bottomSheetDialogFragment.setShowsDialog(true);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }
//Чтение с базы данных и вывод в приложение
    @Override
    protected void onResume() {
        super.onResume();
        timeDBManager.openDb();

        //Adapter
        adapter = new MyListAdapter(this, R.layout.time_item, timeDBManager.readDb());

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
                        adapter.remove(list.getItemAtPosition(position).toString());
                        adapter.notifyDataSetChanged();
//                        database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + "- " + adapter.getItemId(position), null);
                        break;
                }
                return false;
            }
        });
    }
    //Выбираем время и записываем в базу данных и воводим в приложение
    @Override
    public void sendData(String data, int hour, int min) {
        //Запись в Базу данных
        timeDBManager.insertDb(data, hour, min);

        adapter = new MyListAdapter(this, R.layout.time_item, timeDBManager.readDb());
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
                        adapter.remove(list.getItemAtPosition(position).toString());
                        adapter.notifyDataSetChanged();
                        Log.d("...", "position = " + adapter.getItemId(position));
//                        database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + "- " + adapter.getItemId(position), null);
                        break;
                }
                return false;
            }
        });
    }
//close dataBase
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timeDBManager.closeDb();
    }

//    private void deleteStringDataBase(long widgetText) {
//
//        SQLiteDatabase databaseDel = dbHelper.getWritableDatabase();
//        String str = String.valueOf(widgetText);
//        if (str.equalsIgnoreCase("")){
//            return;
//        }
//        int numId = (int) widgetText;
//        int count = databaseDel.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + "- " + numId, null);
//
//        Log.d("mLog",String.valueOf(count));
//
//    }

}