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
import com.example.alertclock02.mylistadapter.MyAdapter;
import com.example.alertclock02.timeModel.TimeID;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ModalBottomSheet.ShareDataInterface{
    private SwipeMenuListView list;
    private EditText textView;
    private TimeDBManager timeDBManager;

    ArrayList<TimeID> arrayList;
    TimePicker timePicker;
    MyAdapter adapter;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //dataBase
        timeDBManager = new TimeDBManager(this);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        list = (SwipeMenuListView) findViewById(R.id.listView1);

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
        arrayList = (ArrayList<TimeID>) timeDBManager.readDb();
        adapter = new MyAdapter(this, arrayList);
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
                        // Удаление будильника при выдвиге и нажатие на свайпа
                        TimeID timeID = arrayList.get(position);
                        arrayList.remove(arrayList.get(position));
                        adapter.notifyDataSetChanged();
                        list.setAdapter(adapter);

                        String idStr = String.valueOf(timeID.getId());
                        timeDBManager.deleteWidgetDb(idStr);



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
        arrayList = (ArrayList<TimeID>) timeDBManager.readDb();
        adapter = new MyAdapter(this,arrayList);

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
                        // Удаление будильника при выдвиге и нажатие на свайпа
                        TimeID timeID = arrayList.get(position);
                        
                        arrayList.remove(arrayList.get(position));
                        adapter.notifyDataSetChanged();
                        list.setAdapter(adapter);

                        String idStr = String.valueOf(timeID.getId());
                        timeDBManager.deleteWidgetDb(idStr);
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

}