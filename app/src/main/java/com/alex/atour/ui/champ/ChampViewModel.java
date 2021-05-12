package com.alex.atour.ui.champ;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.User;
import com.alex.atour.db.DBManager;
import com.alex.atour.models.BaseViewModel;


public class ChampViewModel extends BaseViewModel {

    private final ChampModel model;

    private final MutableLiveData<User> admin;
    private final MutableLiveData<Integer> role;
    private final MutableLiveData<Integer> state;
    private final MutableLiveData<Boolean> isEnrollmentOpen;
    private final MutableLiveData<Boolean> isTotalProtocolCreated;

    public ChampViewModel(){
        admin = new MutableLiveData<>();
        state = new MutableLiveData<>();
        role = new MutableLiveData<>();
        model = new ChampModel(this);
        isEnrollmentOpen = new MutableLiveData<>();
        isTotalProtocolCreated = new MutableLiveData<>();
    }

    public MutableLiveData<User> getAdminLiveData() { return admin; }
    public MutableLiveData<Integer> getRoleLiveData() { return role; }
    public MutableLiveData<Integer> getStateLiveData() { return state; }
    public MutableLiveData<Boolean> getIsEnrollmentOpenLiveData() { return isEnrollmentOpen; }
    public MutableLiveData<Boolean> getIsTotalProtocolCreatedLiveData() { return isTotalProtocolCreated; }


    public void loadPage(String adminID, String champID){
        setIsLoading(true);
        model.loadPage(adminID, champID);
    }
    public void createTotalProtocol(Context ctx, ChampInfo champInfo){ setIsLoading(true); model.createTotalProtocol(ctx, champInfo);}

    public void closeEnrollment(String champID){
        model.closeEnrollment(champID);
    }
    void setAdminData(User user){
        admin.setValue(user);
        setIsLoading(false);
        setErrorMessage("");
    }

    void requestError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
    }

    void setRole(int role){
        this.role.setValue(role);
    }
    void setState(int state){
        this.state.setValue(state);
    }
    void setInEnrollmentOpen(boolean isOpen){isEnrollmentOpen.setValue(isOpen);}
    void totalProtocolCreated(){setIsLoading(false); isTotalProtocolCreated.setValue(true);}
}