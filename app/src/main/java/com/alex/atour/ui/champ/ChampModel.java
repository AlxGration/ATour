package com.alex.atour.ui.champ;

import android.content.Context;

import com.alex.atour.DTO.Member;
import com.alex.atour.DTO.User;
import com.alex.atour.DTO._Estimation;
import com.alex.atour.db.DBManager;
import com.alex.atour.models.ExcelModule;

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

    public void closeEnrollment(String champID){
        db.closeEnrollmentAndCreateRefereeProtocols(champID, new DBManager.IRequestListener() {
            @Override
            public void onSuccess() { viewModel.setInEnrollmentOpen(false); }
            @Override
            public void onFailed(String msg) { viewModel.requestError(msg); }
        });
    }

    //запрос статуса чела в этом ЧМ
    private void requestMyRoleAndStateInChamp(String champID){
        String curUserID = db.getPrefs().getUserID();

        db.getMemberByID(curUserID, champID, new DBManager.IMembersListListener() {
            @Override
            public void onSuccess(ArrayList<Member> members) {
                if (members == null || members.size() == 0) {
                    viewModel.setRole(-1);
                    return;//new user
                }
                Member member = members.get(0);
                db.getPrefs().setUserFIO(member.getUserFIO());
                viewModel.setRole(member.getRole());
                viewModel.setState(member.getState());
            }

            @Override
            public void onFailed(String msg) { viewModel.requestError(msg); }
        });
    }
    //todo: creating createTotalProtocol
    public void createTotalProtocol(Context ctx, String champID){

        //todo: download estimations from server
        //todo: save them to local


        //extract membersIDs
        String[] membersIDs = DBManager.getInstance().getRealmDB().getAllMembersIDs(champID);
        ExcelModule excelModule = new ExcelModule(ctx);
        excelModule.createTotalProtocol(champID, membersIDs);
    }
}
