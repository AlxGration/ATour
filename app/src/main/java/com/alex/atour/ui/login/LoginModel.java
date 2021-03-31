package com.alex.atour.ui.login;

import android.app.Activity;
import android.content.Context;

import com.alex.atour.db.DBManager;

import java.util.concurrent.Executor;

public class LoginModel {

    private final LoginViewModel viewModel;
    private final DBManager db;

    LoginModel(LoginViewModel viewModel){
        db = DBManager.getInstance();
        this.viewModel = viewModel;
    }

    void addExecutor(Activity activity){
        db.setExecutor(activity);
    }

    public void loginRequest(String login, String pass){
           db.login(login, pass, new DBManager.IonLoginListener() {
               @Override
               public void onSuccess() {
                   viewModel.loginSuccess();
               }
               @Override
               public void onFailed(String msg) {
                   viewModel.loginError(msg);
               }
           });
    }

    public void checkAuth(){
        if (db.checkUserAuth())
            viewModel.loginSuccess();
    }
}
