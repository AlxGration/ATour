package com.alex.atour.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alex.atour.R;
import com.alex.atour.db.DBManager;
import com.alex.atour.ui.memrequest.MembershipRequestActivity;
import com.alex.atour.ui.registration.RegistrationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private boolean isPasswordHidden;
    private EditText etPass, etLogin;
    private ProgressBar pBar;
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
        pBar = findViewById(R.id.progress_bar);
        TextView tvError = findViewById(R.id.tv_error);

        //observers
        viewModel.getAuthFlag().observe(this, isAuthSuccess->{
            if (isAuthSuccess){
                //TODO: start MainActivity
                startActivity(
                        new Intent(this, MembershipRequestActivity.class)
                );
            }
        });
        viewModel.getIsLoading().observe(this, isLoading->{
            pBar.setVisibility(
                    isLoading?
                            View.VISIBLE:
                            View.INVISIBLE
            );
        });
        viewModel.getErrorMessage().observe(this, tvError::setText);

    }

    public void onClickRegistration(View view) {
        startActivity(
                new Intent(this, RegistrationActivity.class)
        );
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

    public void onClickEnter(View view) {
        viewModel.loginRequest(
                etLogin.getText().toString(),
                etPass.getText().toString()
        );
    }
}