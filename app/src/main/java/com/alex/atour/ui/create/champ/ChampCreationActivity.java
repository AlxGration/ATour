package com.alex.atour.ui.create.champ;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.app.DatePickerDialog;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.R;
import com.alex.atour.models.NetworkStateChangeReceiver;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

public class ChampCreationActivity extends AppCompatActivity implements NetworkStateChangeReceiver.NetworkStateChangeListener{

    private EditText etTitle;
    private Spinner spCity, spStatus;
    private ProgressBar pBar;
    private TextView tvDataFrom, tvDataTo;
    private ChampInfo champInfo;
    private ChampViewModel viewModel;
    private NetworkStateChangeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champ_creation);

        viewModel = new ViewModelProvider(this).get(ChampViewModel.class);

        tvDataFrom = findViewById(R.id.tv_data_from);
        tvDataTo = findViewById(R.id.tv_data_to);
        pBar = findViewById(R.id.progress_bar);
        etTitle = findViewById(R.id.et_title);
        spCity = findViewById(R.id.spin_city);
        spStatus = findViewById(R.id.spin_status);


        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        tvDataFrom.setText(day + "." + (mon + 1) + "." + year);
        tvDataTo.setText(day + "." + (mon + 1) + "." + year);

        champInfo = new ChampInfo();

        viewModel.getErrorMessage().observe(this, this::showError);
        viewModel.getIsLoading().observe(this, isLoading->{
            pBar.setVisibility(isLoading? View.VISIBLE: View.INVISIBLE);
        });
        viewModel.getIsRequestSuccess().observe(this, isSuccess->{
            if (isSuccess){
                Toast.makeText(ChampCreationActivity.this, "Чемпионат успешно создан", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        //проверка подключения internet
        receiver = new NetworkStateChangeReceiver();
        receiver.attach(this);
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void onClickBackBtn(View view) { finish();    }

    public void onClickSendBtn(View view) {
        viewModel.sendChampCreationRequest(
                etTitle.getText().toString(),
                tvDataFrom.getText().toString(),
                tvDataTo.getText().toString(),
                spCity.getSelectedItem().toString(),
                spStatus.getSelectedItem().toString(),
                champInfo
        );
    }

    public void onChooseType(View view) {
        boolean isChecked = ((ToggleButton)view).isChecked();

        switch (view.getId()) {
            case R.id.tb_walk:
                champInfo.setTypeWalk(isChecked);
                break;
            case R.id.tb_ski:
                champInfo.setTypeSki(isChecked);
                break;
            case R.id.tb_hike:
                champInfo.setTypeHike(isChecked);
                break;
            case R.id.tb_water:
                champInfo.setTypeWater(isChecked);
                break;
            case R.id.tb_speleo:
                champInfo.setTypeSpeleo(isChecked);
                break;
            case R.id.tb_bike:
                champInfo.setTypeBike(isChecked);
                break;
            case R.id.tb_auto:
                champInfo.setTypeAuto(isChecked);
                break;
            case R.id.tb_other:
                champInfo.setTypeOther(isChecked);
                break;
        }
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

    private void showError(String err){
        Snackbar.make(findViewById(R.id.main_layout), err, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onNetworkStateChanged(boolean isConnected) {
        if (isConnected) {
            showError("Подключение восстановленно");
        }else {
            showError("Отсутствует подключение к интернету" );
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver.detach();
        }
    }
}