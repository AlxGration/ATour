package com.alex.atour.ui.champ.admin;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.Member;
import com.alex.atour.db.DBManager;
import com.alex.atour.models.BaseViewModel;

import java.util.ArrayList;

public class MembersViewModel extends BaseViewModel {

    private final DBManager db;

    private final MutableLiveData<ArrayList<Member>> all;
    private final MutableLiveData<ArrayList<Member>> referees;
    private final MutableLiveData<ArrayList<Member>> members;


    public MembersViewModel(){
        db = DBManager.getInstance();

        all = new MutableLiveData<>(new ArrayList<Member>());
        members = new MutableLiveData<>(new ArrayList<Member>());
        referees = new MutableLiveData<>(new ArrayList<Member>());
    }

    public MutableLiveData<ArrayList<Member>> getRefereesLiveData() { return referees; }
    public MutableLiveData<ArrayList<Member>> getMembersLiveData() { return members; }

    public void requestMembersList(String champID){
        setIsLoading(true);
        db.getMembers(champID, new DBManager.IMembersListListener() {
            @Override
            public void onSuccess(ArrayList<Member> members) { setAllList(members); }
            @Override
            public void onFailed(String msg) { requestError(msg); }
        });
    }


    public void splitMembers(){
        ArrayList <Member> membersList = new ArrayList<>();
        ArrayList <Member> refereesList = new ArrayList<>();

        for(Member mem : all.getValue()){
            if (mem.getRole() == 1){
                membersList.add(mem);
            }else{
                refereesList.add(mem);
            }
        }
        setMembersList(membersList);
        setRefereesList(refereesList);
    }

    void setAllList(ArrayList<Member> members){
        this.all.setValue(members);
        splitMembers();
    }
    void setRefereesList(ArrayList<Member> members){
        this.referees.setValue(members);
        setIsLoading(false);
        setErrorMessage("");
    }
    void setMembersList(ArrayList<Member> members){
        this.members.setValue(members);
        setIsLoading(false);
        setErrorMessage("");
    }

    void requestError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
    }
}