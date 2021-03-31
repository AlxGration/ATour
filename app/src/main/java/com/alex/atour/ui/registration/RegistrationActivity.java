package com.alex.atour.ui.registration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.alex.atour.R;

import java.util.concurrent.Executor;

public class RegistrationActivity extends AppCompatActivity {

    private boolean isPasswordHidden;
    private EditText etFIO, etPhone, etEmail, etPass;
    private Spinner spCity;
    private RegViewModel viewModel;
    private ProgressBar pBar;

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
        pBar = findViewById(R.id.progress_bar);
        TextView tvError = findViewById(R.id.tv_error);

        //observers
        viewModel.getIsLoading().observe(this, isLoading->{
            pBar.setVisibility(
                    isLoading?
                            View.VISIBLE:
                            View.INVISIBLE
            );
        });
        viewModel.getErrorMessage().observe(this, tvError::setText);

        etPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    public void onClickShowHidePassword(View view) {
        isPasswordHidden = !isPasswordHidden;
        etPass.setInputType(
                isPasswordHidden ?
                        InputType.TYPE_CLASS_TEXT
                        :
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
}