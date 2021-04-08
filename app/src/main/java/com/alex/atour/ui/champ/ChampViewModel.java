package com.alex.atour.ui.champ;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.User;
import com.alex.atour.models.BaseViewModel;

import java.util.ArrayList;

public class ChampViewModel extends BaseViewModel {

    private final ChampModel model;

    private final MutableLiveData<User> admin;

    public ChampViewModel(){
        admin = new MutableLiveData<>();
        model = new ChampModel(this);
    }

    public MutableLiveData<User> getAdminLiveData() { return admin; }

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
}