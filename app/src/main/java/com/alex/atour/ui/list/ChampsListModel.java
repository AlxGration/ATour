package com.alex.atour.ui.list;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.db.DBManager;

import java.util.ArrayList;

public class ChampsListModel {

    private final DBManager db;
    private final ChampsListViewModel viewModel;

    ChampsListModel(ChampsListViewModel viewModel){
        this.viewModel = viewModel;
        db = DBManager.getInstance();
    }

    public void requestChampsList(){
        db.getChampsList(new DBManager.IChampsInfoListener() {
            @Override
            public void onSuccess(ArrayList<ChampInfo> champsList) {
                viewModel.setMainChampsList(champsList);
            }
            @Override
            public void onFailed(String msg) {
                viewModel.requestError(msg);
            }
        });
    }

    public void requestManagedChampsList(){
        db.getManagedChampsList(new DBManager.IChampsInfoListener() {
            @Override
            public void onSuccess(ArrayList<ChampInfo> champsList) {
                viewModel.setManagedChampsList(champsList);
            }
            @Override
            public void onFailed(String msg) {
                viewModel.requestError(msg);
            }
        });
    }

    public void requestMyChampsList(){
        db.getMyChampsList(new DBManager.IChampsInfoListener() {
            @Override
            public void onSuccess(ArrayList<ChampInfo> champsList) {
                viewModel.setMyChampsList(champsList);
            }
            @Override
            public void onFailed(String msg) {
                viewModel.requestError(msg);
            }
        });
    }
    public void requestChampsList(String searchRequest){
        db.getChampsList(searchRequest, new DBManager.IChampsInfoListener() {
            @Override
            public void onSuccess(ArrayList<ChampInfo> champsList) {
                viewModel.setFoundChampsList(champsList);
            }
            @Override
            public void onFailed(String msg) {
                viewModel.requestError(msg);
            }
        });
    }
}
