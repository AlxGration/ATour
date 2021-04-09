package com.alex.atour.ui.champ;

import android.util.Log;

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
                //todo: remove this code from here
                String curUserID = db.getPrefs().getUserID();
                if (curUserID.equals(user.getId())){  //если айди совпадают, то админ-мод
                    viewModel.setRole(0);
                }
            }
            @Override
            public void onFailed(String msg) {
                viewModel.requestError(msg);
            }
        });
    }

    //todo: запрос статуса чела в этом ЧМ

    /*

    User curUser = db.getPrefs().getUserInfo();
                if (curUser.getId().equals(user.getId())){  //если айди совпадают, то админ-мод
                    viewModel.setRole(0);
                }
     */
}
