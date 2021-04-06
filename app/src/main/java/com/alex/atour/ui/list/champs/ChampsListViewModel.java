package com.alex.atour.ui.list.champs;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.models.BaseViewModel;

import java.util.ArrayList;

public class ChampsListViewModel extends BaseViewModel {

    private final ChampsListModel model;

    private final MutableLiveData<ArrayList<ChampInfo>> champs;

    public ChampsListViewModel(){
        champs = new MutableLiveData<>();
        model = new ChampsListModel(this);
    }

    public MutableLiveData<ArrayList<ChampInfo>> getChampsLiveData() { return champs; }

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