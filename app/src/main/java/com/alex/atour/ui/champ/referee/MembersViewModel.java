package com.alex.atour.ui.champ.referee;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.Member;
import com.alex.atour.DTO.MemberEstimation;
import com.alex.atour.db.DBManager;
import com.alex.atour.db.RealmDB;
import com.alex.atour.models.BaseViewModel;

import java.util.ArrayList;
import java.util.TreeSet;

public class MembersViewModel extends BaseViewModel {

    private final DBManager db;
    private final RealmDB realmDB;
    private final MutableLiveData<ArrayList<MemberEstimation>> mEstims;
    private String champID, refereeID;

    public MembersViewModel(){
        db = DBManager.getInstance();
        realmDB = db.getRealmDB();
        mEstims = new MutableLiveData<>(new ArrayList<>());
    }

    public MutableLiveData<ArrayList<MemberEstimation>> getMembersLiveData() { return mEstims; }

    public void requestMembersList(String champID){
        setIsLoading(true);
        String refereeID = db.getPrefs().getUserID();

        this.champID = champID;
        this.refereeID = refereeID;

        // check local protocol
        if (getMembersFromLocalDB()){
            return;
        }

        //request members from server (one time after champ started and protocols has been created)
        //members doesn't have estimation yet
        db.getDocsSentMembers(champID, refereeID, new DBManager.IMembersListListener() {
            @Override
            public void onSuccess(ArrayList<Member> members) {
                ArrayList<MemberEstimation> list = new ArrayList<>(members.size());
                for(Member m: members) list.add(new MemberEstimation(champID, refereeID, m));
                //save members to local db
                saveToLocalDB(list);
                setMembersList(list);
            }
            @Override
            public void onFailed(String msg) { requestError(msg); }
        });
    }

    public boolean getMembersFromLocalDB(){
        TreeSet<MemberEstimation> treeSet = realmDB.getMemberEstimations(champID, refereeID);
        if (treeSet.size() > 0){
            setMembersList(new ArrayList<>(treeSet));
            return true;
        }
        return false;
    }

    public void saveToLocalDB(ArrayList<MemberEstimation> members){
        //save members with 0 estimation
        realmDB.writeMemberEstimations(members);
    }

    void setMembersList(ArrayList<MemberEstimation> members){
        this.mEstims.setValue(members);
        setIsLoading(false);
        setErrorMessage("");
    }

    void requestError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
    }
}