package com.alex.atour.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.atour.DTO.ExcelModule;
import com.alex.atour.R;
import com.alex.atour.ui.list.MainActivity;
import com.alex.atour.ui.registration.RegistrationActivity;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    private boolean isPasswordHidden;
    private EditText etPass, etLogin;
    private ProgressBar pBar;
    private Button btnLogin;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.addExecutor(this);
        viewModel.checkAuth();

        //init ui
        etPass = findViewById(R.id.et_password);
        etLogin = findViewById(R.id.et_login);
        btnLogin = findViewById(R.id.btn_login);
        pBar = findViewById(R.id.progress_bar);

        //observers
        viewModel.getAuthFlag().observe(this, isAuthSuccess->{
            if (isAuthSuccess){
                startActivity(
                        new Intent(this, MainActivity.class)
                );
                finish();
            }
        });
        viewModel.getIsLoading().observe(this, isLoading->{
            pBar.setVisibility(isLoading?
                            View.VISIBLE:
                            View.INVISIBLE
            );
            btnLogin.setEnabled(!isLoading);
        });
        viewModel.getErrorMessage().observe(this, this::showError);
    }

    private void showError(String err){
        Snackbar.make(findViewById(R.id.main_layout), err, Snackbar.LENGTH_SHORT).show();
    }

    public void onClickRegistration(View view) {
        startActivity(
                new Intent(this, RegistrationActivity.class)
        );
    }

    public void onClickShowHidePassword(View view) {
        isPasswordHidden = !isPasswordHidden;
        etPass.setInputType(isPasswordHidden ?
                        InputType.TYPE_CLASS_TEXT :
                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
        );
    }

    public void onClickEnter(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Log.e("TAG", "start process");
            String [] fios = {
                    "Винов Александр Сергеевич",
                    "Команда 2",
                    "Команда 3",
                    "Команда 4",
                    "Команда 5",
                    "Команда 6",
                    "Команда 7",
                    "Команда 8",
                    "Команда 9",
            };
            ExcelModule excelModule = new ExcelModule(this);
            excelModule.createReportForReferee("refereeAlex.xlsx", fios);

        }else {
            //запрашиваем разрешение
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }


        //todo: !!!uncomment!!!
        /*
        viewModel.loginRequest(
                etLogin.getText().toString(),
                etPass.getText().toString()
        );

         */
    }
}