package com.alex.atour.ui.list.manage;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.models.BaseViewModel;

import java.util.ArrayList;

public class ManagedChampsViewModel extends BaseViewModel {

    private final ManagedChampsModel model;

    private final MutableLiveData<ArrayList<ChampInfo>> champs;

    public ManagedChampsViewModel(){
        champs = new MutableLiveData<>();
        model = new ManagedChampsModel(this);
    }

    public MutableLiveData<ArrayList<ChampInfo>> getManagedChampsLiveData() { return champs; }

    public void requestChampsList(){
        model.requestChampsList();
    }

    void requestSuccess(ArrayList<ChampInfo> champsList){
        champs.setValue(champsList);
        setIsLoading(false);
        setErrorMessage("");
    }
    void requestError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
    }
}