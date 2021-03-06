package com.example.alertclock02;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.Inflater;
//Класс нижнего Диалогового окна
public class ModalBottomSheet extends BottomSheetDialogFragment {
    ShareDataInterface shareDataInterface;
    private int layoutStyle;
    TimePicker timePicker;

    public ModalBottomSheet(int layoutStyle) {
        this.layoutStyle = layoutStyle;

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View view  =  View.inflate(getContext(), this.layoutStyle,null);
        if (this.layoutStyle == R.layout.modal_bs_one){
            view.findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //Часы
                    timePicker = (TimePicker)view.findViewById(R.id.timePicker);

                    int hour = timePicker.getCurrentHour();
                    int minutes = timePicker.getCurrentMinute();
                    //Проверка если меньше 10 минут то добавляем 0 спереди
                    String time;
                    if (minutes < 10) {
                        time = hour + ":" + "0" + minutes;
                    }
                    else {
                        time = hour + ":" + minutes;
                    }
                    shareDataInterface.sendData(time, hour, minutes);

                }
            });
        }
        if (this.layoutStyle == R.layout.modal_bs_one){
            view.findViewById(R.id.buttonClose).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
        }
        dialog.setContentView(view);

    }

    //функция для передачи текста с одного классса в другой
    public interface ShareDataInterface {
        public void sendData(String data, int hour, int min);

    }
    //Метод переноса текста
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
        shareDataInterface = (ShareDataInterface) context;
        }catch (RuntimeException e)
        {
            throw new RuntimeException(context.toString() + "Nust implemint method");
        }
    }
}
