package com.alex.atour.ui.champ;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.User;
import com.alex.atour.db.DBManager;

import java.util.ArrayList;

public class ChampModel {

    private final DBManager db;
    private final ChampViewModel viewModel;

    ChampModel(ChampViewModel viewModel){
        this.viewModel = viewModel;
        db = DBManager.getInstance();
    }

    public void requestUserData(String adminID){
        db.getUserData(adminID, new DBManager.IUserInfoListener() {
            @Override
            public void onSuccess(User user) {
                viewModel.setAdminData(user);
            }
            @Override
            public void onFailed(String msg) {
                viewModel.requestError(msg);
            }
        });
    }
}
