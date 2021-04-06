package com.alex.atour.ui.list.champs;

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
                viewModel.requestSuccess(champsList);
            }
            @Override
            public void onFailed(String msg) {
                viewModel.requestError(msg);
            }
        });
    }
}
