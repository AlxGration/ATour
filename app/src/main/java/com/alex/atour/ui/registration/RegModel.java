package com.alex.atour.ui.registration;

import android.app.Activity;

import com.alex.atour.DTO.User;
import com.alex.atour.db.DBManager;

public class RegModel {

    private final RegViewModel viewModel;
    private final DBManager db;

    RegModel(RegViewModel viewModel){
        this.viewModel = viewModel;
        db = DBManager.getInstance();
    }

    void addExecutor(Activity activity){
        db.setExecutor(activity);
    }

    public void registrationRequest(String fio, String city, String phone, String email, String pass){
        User user = new User("", fio, city, phone, email);
        db.registration(user, pass, new DBManager.IRequestListener() {
            @Override
            public void onSuccess() {
                viewModel.registrationSuccess();
            }

            @Override
            public void onFailed(String msg) {
                viewModel.registrationError(msg);
            }
        });
    }
}
