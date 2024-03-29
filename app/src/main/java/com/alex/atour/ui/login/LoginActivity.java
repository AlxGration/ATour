package com.alex.atour.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alex.atour.R;
import com.alex.atour.models.NetworkStateChangeReceiver;
import com.alex.atour.ui.list.MainActivity;
import com.alex.atour.ui.registration.RegistrationActivity;
import com.google.android.material.snackbar.Snackbar;

import io.realm.Realm;

public class LoginActivity extends AppCompatActivity implements NetworkStateChangeReceiver.NetworkStateChangeListener {

    private boolean isPasswordHidden;
    private EditText etPass, etLogin;
    private ProgressBar pBar;
    private Button btnLogin;
    private LoginViewModel viewModel;
    private NetworkStateChangeReceiver receiver;
    private TextView tvNetworkState;

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
        tvNetworkState = findViewById(R.id.tv_network_bar);

        //observers
        viewModel.getAuthFlag().observe(this, isAuthSuccess->{
            if (isAuthSuccess){
                Realm.init(this);
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

        //проверка подключения internet
        receiver = new NetworkStateChangeReceiver();
        receiver.attach(this);
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void showError(String err){
        if (err.length() == 0) return;
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
        viewModel.loginRequest(
                etLogin.getText().toString(),
                etPass.getText().toString()
        );
    }

    @Override
    public void onNetworkStateChanged(boolean isConnected) {
        if (isConnected) {
            tvNetworkState.setVisibility(View.GONE);
        }else {
            tvNetworkState.setVisibility(View.VISIBLE);
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