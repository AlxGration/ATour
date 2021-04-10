package com.alex.atour.ui.list.champs;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.Member;
import com.alex.atour.models.BaseViewModel;
import com.alex.atour.ui.list.ChampsListModel;

import java.util.ArrayList;

public class MembersViewModel extends BaseViewModel {

    private final MutableLiveData<ArrayList<Member>> all;
    private final MutableLiveData<ArrayList<Member>> referees;
    private final MutableLiveData<ArrayList<Member>> members;

    public MembersViewModel(){
        all = new MutableLiveData<>();
        members = new MutableLiveData<>();
        referees = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Member>> getListLiveData() { return all; }
    public MutableLiveData<ArrayList<Member>> getRefereesLiveData() { return referees; }
    public MutableLiveData<ArrayList<Member>> getMembersLiveData() { return members; }

    //todo: get all members
    public void requestMembersList(){
        //setIsLoading(true);

        //db req & setAllList

    }

    //todo: return filtered members list
    public void requestMembersList(int role){
//        ArrayList<Member> list = new ArrayList(members.getValue().size());

    }

    void setMembersList(ArrayList<Member> members){
        setIsLoading(false);
        setErrorMessage("");
        this.members.setValue(members);
    }
    void setAllList(ArrayList<Member> members){
        setIsLoading(false);
        setErrorMessage("");
        this.all.setValue(members);
    }
    void setRefereesList(ArrayList<Member> members){
        setIsLoading(false);
        setErrorMessage("");
        this.referees.setValue(members);
    }

    void requestError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
    }
}