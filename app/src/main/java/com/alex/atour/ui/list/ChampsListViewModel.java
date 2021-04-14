package com.alex.atour.ui.list;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.models.BaseViewModel;

import java.util.ArrayList;

public class ChampsListViewModel extends BaseViewModel {

    private final ChampsListModel model;

    private final MutableLiveData<ArrayList<ChampInfo>> champs;
    private final MutableLiveData<ArrayList<ChampInfo>> managedChamps;
    private final MutableLiveData<ArrayList<ChampInfo>> myChamps;
    private final MutableLiveData<ArrayList<ChampInfo>> foundChamps;

    public ChampsListViewModel(){
        champs = new MutableLiveData<>();
        managedChamps = new MutableLiveData<>();
        myChamps = new MutableLiveData<>();
        foundChamps = new MutableLiveData<>();
        model = new ChampsListModel(this);
    }

    public MutableLiveData<ArrayList<ChampInfo>> getChampsLiveData() { return champs; }
    public MutableLiveData<ArrayList<ChampInfo>> getManagedChampsLiveData() { return managedChamps; }
    public MutableLiveData<ArrayList<ChampInfo>> getMyChampsListLiveData() { return myChamps; }
    public MutableLiveData<ArrayList<ChampInfo>> getFoundChampsListLiveData() { return foundChamps; }

    public void requestChampsList(){
        setIsLoading(true);
        model.requestChampsList();
    }
    public void requestManagedChampsList(){
        setIsLoading(true); model.requestManagedChampsList();
    }
    public void requestMyChampsList(){
        setIsLoading(true); model.requestMyChampsList();
    }
    public void requestChampsList(String searchRequest){
        setIsLoading(true);
        model.requestChampsList(searchRequest);
    }

    void setMainChampsList(ArrayList<ChampInfo> champsList){
        champs.setValue(champsList);
        setIsLoading(false);
        setErrorMessage("");
    }
    void setManagedChampsList(ArrayList<ChampInfo> champsList){
        managedChamps.setValue(champsList);
        setIsLoading(false);
        setErrorMessage("");
    }
    void setMyChampsList(ArrayList<ChampInfo> champsList){
        myChamps.setValue(champsList);
        setIsLoading(false);
        setErrorMessage("");
    }
    void setFoundChampsList(ArrayList<ChampInfo> champsList){
        foundChamps.setValue(champsList);
        setIsLoading(false);
        setErrorMessage("");
    }
    public void eraseFoundChampsList(){
        foundChamps.setValue(new ArrayList<>());
    }
    void requestError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
    }
}