package com.alex.atour.ui.list.my;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.models.BaseViewModel;

import java.util.ArrayList;

public class MyChampsViewModel extends BaseViewModel {

    private final MyChampsModel model;

    private final MutableLiveData<ArrayList<ChampInfo>> champs;

    public MyChampsViewModel(){
        champs = new MutableLiveData<>();
        model = new MyChampsModel(this);
    }

    public MutableLiveData<ArrayList<ChampInfo>> getChampsLiveData() { return champs; }

    public void requestChampsList(){
        model.requestChampsList();
    }

    void requestSuccess(ArrayList<ChampInfo> champs){
        this.champs.setValue(champs);
        setIsLoading(false);
        setErrorMessage("");
    }
    void requestError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
    }
}