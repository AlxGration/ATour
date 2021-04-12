package com.alex.atour.ui.profile;

import com.alex.atour.DTO.User;
import com.alex.atour.db.DBManager;

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
                viewModel.setUserInfo(user);
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
