package com.alex.atour.ui.profile;

import com.alex.atour.DTO.Document;
import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.DTO.User;
import com.alex.atour.db.DBManager;

import java.util.ArrayList;

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
                viewModel.requestError(msg);
            }
        });
    }

    public void requestDocuments(String champID, String userID){
        db.getDocumentByUserID(champID, userID, new DBManager.IDocumentListener() {
            @Override
            public void onSuccess(Document document) {
                viewModel.setDocument(document);
            }
            @Override
            public void onFailed(String msg) {
                viewModel.requestError(msg);
            }
        });
    }

    public void requestMembershipRequest(String champID, String userID){
        //todo:realize me
        //todo:load from Champ->Accepted
        db.getMembershipRequestByID(champID, userID, new DBManager.IMembershipRequestsListListener() {
            @Override
            public void onSuccess(ArrayList<MembershipRequest> requests) {
                viewModel.setMembershipRequest(requests.get(0));
            }
            @Override
            public void onFailed(String msg) { viewModel.requestError(msg); }
        });

    }

    public void signOut(){ db.signOut(); }
}
