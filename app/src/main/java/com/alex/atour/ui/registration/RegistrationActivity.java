package com.alex.atour.ui.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.alex.atour.R;
import com.alex.atour.models.NetworkStateChangeReceiver;
import com.google.android.material.snackbar.Snackbar;

public class RegistrationActivity extends AppCompatActivity implements NetworkStateChangeReceiver.NetworkStateChangeListener {

    private boolean isPasswordHidden;
    private EditText etFIO, etPhone, etEmail, etPass;
    private Spinner spCity;
    private RegViewModel viewModel;
    private Button btnSend;
    private NetworkStateChangeReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        viewModel = new ViewModelProvider(this).get(RegViewModel.class);
        viewModel.addExecutor(this);

        //init ui
        etPass = findViewById(R.id.et_password);
        etFIO = findViewById(R.id.et_fio);
        etPhone = findViewById(R.id.et_phone);
        etEmail = findViewById(R.id.et_email);
        spCity = findViewById(R.id.spin_city);
        btnSend = findViewById(R.id.btn_send);
        ProgressBar pBar = findViewById(R.id.progress_bar);

        //observers
        viewModel.getIsLoading().observe(this, isLoading->{
            pBar.setVisibility(isLoading?
                            View.VISIBLE:
                            View.INVISIBLE
            );
            btnSend.setEnabled(!isLoading);
        });
        viewModel.getRegFlag().observe(this, isRegSuccess ->{
            if (isRegSuccess){
                Toast.makeText(this, "Успешно", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        viewModel.getErrorMessage().observe(this, this::showError);

        etPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));

        //проверка подключения internet
        receiver = new NetworkStateChangeReceiver();
        receiver.attach(this);
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void onClickShowHidePassword(View view) {
        isPasswordHidden = !isPasswordHidden;
        etPass.setInputType(isPasswordHidden ?
                        InputType.TYPE_CLASS_TEXT :
                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
        );
    }

    public void onClickBackBtn(View view) {
        finish();
    }

    public void onClickSendBtn(View view) {
        viewModel.registrationRequest(
                etFIO.getText().toString(),
                spCity.getSelectedItem().toString(),
                etPhone.getText().toString(),
                etEmail.getText().toString(),
                etPass.getText().toString()
        );
    }
    private void showError(String err){
        if (err.length() == 0) return;
        Snackbar.make(findViewById(R.id.main_layout), err, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onNetworkStateChanged(boolean isConnected) {
        if (isConnected) {
            viewModel.registrationError("Подключение восстановленно");
        }else {
            viewModel.registrationError("Отсутствует подключение к интернету" );
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