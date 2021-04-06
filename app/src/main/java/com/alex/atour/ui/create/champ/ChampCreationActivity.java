package com.alex.atour.ui.create.champ;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.alex.atour.R;

import java.util.Calendar;

public class ChampCreationActivity extends AppCompatActivity {

    TextView tvDataFrom, tvDataTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champ_creation);

        tvDataFrom = findViewById(R.id.tv_data_from);
        tvDataTo = findViewById(R.id.tv_data_to);

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        tvDataFrom.setText(day + "." + (mon + 1) + "." + year);
        tvDataTo.setText(day + "." + (mon + 1) + "." + year);
    }

    public void onClickBackBtn(View view) { finish();    }

    public void onClickSendBtn(View view) {
    }

    public void onChooseType(View view) {
    }

    public void onClickSetData(View view) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog;

        if (view.getId() == R.id.tv_data_from){
            dialog = new DatePickerDialog(this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateFromSetter,
                    year, mon, day);
        }else{//data to
            dialog = new DatePickerDialog(this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateToSetter,
                    year, mon, day);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private final  DatePickerDialog.OnDateSetListener dateFromSetter = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            tvDataFrom.setText(i2 + "." + (i1 + 1) + "." + i);
        }
    };
    private final DatePickerDialog.OnDateSetListener dateToSetter = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            tvDataTo.setText(i2 + "." + (i1 + 1) + "." + i);
        }
    };
}