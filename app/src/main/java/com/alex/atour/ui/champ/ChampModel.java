package com.alex.atour.ui.champ;

import android.util.Log;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.Member;
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

    public void loadPage(String adminID, String champID){
        String curUserID = db.getPrefs().getUserID();

        db.getUserData(adminID, new DBManager.IUserInfoListener() {
            @Override
            public void onSuccess(User user) {
                viewModel.setAdminData(user);
                if (curUserID.equals(user.getId())){  //если айди совпадают, то админ-мод
                    viewModel.setRole(0);
                }else{
                    //иначе это может быть или судья или участник (для них нужно знать о состоянии членства)
                    requestMyRoleAndStateInChamp(champID);
                }
            }
            @Override
            public void onFailed(String msg) {
                viewModel.requestError(msg);
            }
        });

    }

    //запрос статуса чела в этом ЧМ
    private void requestMyRoleAndStateInChamp(String champID){
        String curUserID = db.getPrefs().getUserID();

        db.getMemberByID(curUserID, champID, new DBManager.IMembersListListener() {
            @Override
            public void onSuccess(ArrayList<Member> members) {
                if (members == null || members.size() == 0) return;//new user

                Member member = members.get(0);
                viewModel.setRole(member.getRole());
                viewModel.setState(member.getState());
            }

            @Override
            public void onFailed(String msg) { viewModel.requestError(msg); }
        });
    }
}
