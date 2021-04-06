package com.alex.atour.ui.profile;

import android.app.Activity;

import com.alex.atour.DTO.User;
import com.alex.atour.db.DBManager;
import com.alex.atour.ui.registration.RegViewModel;

public class ProfileModel {

    private final ProfileViewModel viewModel;
    private final DBManager db;

    ProfileModel(ProfileViewModel viewModel){
        this.viewModel = viewModel;
        db = DBManager.getInstance();
    }

    public void requestUserProfile(String userID){
        db.getUserData(userID, new DBManager.IUserInfoListener() {
            @Override
            public void onSuccess(User user) {
                String[] fio = user.getFio().split(" ");
                viewModel.setSecName(fio[0]);
                viewModel.setName(fio[1]+" "+fio[2]);
                viewModel.setCity(user.getCity());
                viewModel.setEmail(user.getEmail());
                viewModel.setPhone("+7 "+user.getPhone());
                viewModel.requestProfileSuccess();
            }

            @Override
            public void onFailed(String msg) {
                viewModel.requestProfileError(msg);
            }
        });
    }

    public void signOut(){
        db.signOut();
    }
}
