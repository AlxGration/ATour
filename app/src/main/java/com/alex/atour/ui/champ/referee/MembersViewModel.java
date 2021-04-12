package com.alex.atour.ui.champ.referee;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.Member;
import com.alex.atour.db.DBManager;
import com.alex.atour.models.BaseViewModel;

import java.util.ArrayList;

public class MembersViewModel extends BaseViewModel {

    private DBManager db;
    private final MutableLiveData<ArrayList<Member>> members;

    public MembersViewModel(){
        db = DBManager.getInstance();
        members = new MutableLiveData<>(new ArrayList<Member>());
    }

    public MutableLiveData<ArrayList<Member>> getMembersLiveData() { return members; }

    public void requestMembersList(String champID){
        setIsLoading(true);
        db.getDocsSentMembers(champID, new DBManager.IMembersListListener() {
            @Override
            public void onSuccess(ArrayList<Member> members) { setMembersList(members); }
            @Override
            public void onFailed(String msg) { requestError(msg); }
        });
    }


    void setMembersList(ArrayList<Member> members){
        setIsLoading(false);
        setErrorMessage("");
        this.members.setValue(members);
    }

    void requestError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
    }
}