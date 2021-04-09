package com.alex.atour.ui.champ;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.User;
import com.alex.atour.models.BaseViewModel;

import java.util.ArrayList;

public class ChampViewModel extends BaseViewModel {

    private final ChampModel model;

    private final MutableLiveData<User> admin;
    private final MutableLiveData<Integer> role;
    private final MutableLiveData<Integer> state;

    public ChampViewModel(){
        admin = new MutableLiveData<>();
        state = new MutableLiveData<>();
        role = new MutableLiveData<>();
        model = new ChampModel(this);
    }

    public MutableLiveData<User> getAdminLiveData() { return admin; }
    public MutableLiveData<Integer> getRoleLiveData() { return role; }
    public MutableLiveData<Integer> getStateLiveData() { return state; }

    public void requestAdminData(String adminID){
        setIsLoading(true);
        model.requestUserData(adminID);
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
}