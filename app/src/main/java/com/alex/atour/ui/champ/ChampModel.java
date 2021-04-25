package com.alex.atour.ui.champ;

import android.content.Context;
import android.util.Log;

import com.alex.atour.DTO.Estimation;
import com.alex.atour.DTO.Member;
import com.alex.atour.DTO.RefereeRank;
import com.alex.atour.DTO.User;
import com.alex.atour.DTO._Estimation;
import com.alex.atour.db.DBManager;
import com.alex.atour.db.RealmDB;
import com.alex.atour.models.ExcelModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import io.realm.RealmChangeListener;

public class ChampModel {

    private final DBManager db;
    private final RealmDB realmDB;
    private final ChampViewModel viewModel;

    ChampModel(ChampViewModel viewModel){
        this.viewModel = viewModel;
        db = DBManager.getInstance();
        realmDB = db.getRealmDB();
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

    // creating createTotalProtocol
    public void createTotalProtocol(Context ctx, String champID){
        // download RefereeRanks from server
        db.getAllRefereesRanksList(champID, new DBManager.IRefereesRanksListListener() {
            @Override
            public void onSuccess(ArrayList<RefereeRank> ranks) {
                realmDB.writeRefereesRanks(ranks, () -> {
                    //extract refereesRanks
                    String[] refereesRanks = new String[ranks.size()];
                    int i = 0;
                    for(RefereeRank e: ranks){ refereesRanks[i++]=e.getRefereeInfo(); }

                    // download estimations from server
                    db.getAllEstimationsList(champID, new DBManager.IEstimationsListListener() {
                        @Override
                        public void onSuccess(ArrayList<Estimation> estims) {
                            Log.e("TAG", "got all estims from server "+estims.size());

                            //extract membersIDs

                            Set<String> membersIDs = new HashSet<>();
                            for(Estimation e: estims){ membersIDs.add(e.getMemberID()); }
                            Log.e("TAG", "size of membersIDs "+membersIDs.size());

                            // save estims locally
                            realmDB.writeEstimations(estims, () -> {

                                ExcelModule excelModule = new ExcelModule(ctx);
                                //start creating protocol
                                excelModule.createTotalProtocol(champID, membersIDs, refereesRanks,  new DBManager.IRequestListener() {
                                    @Override
                                    public void onSuccess() {
                                        Log.e("TAG", "created total protocol ");
                                        viewModel.totalProtocolCreated();
                                    }

                                    @Override
                                    public void onFailed(String msg) {
                                        viewModel.requestError(msg);
                                    }
                                });
                            });
                        }
                        @Override
                        public void onFailed(String msg) { viewModel.requestError(msg); }
                    });
                });
            }
            @Override
            public void onFailed(String msg) { viewModel.requestError(msg); }
        });
    }
}
