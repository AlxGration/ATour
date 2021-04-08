package com.alex.atour.ui.list.manage;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.db.DBManager;

import java.util.ArrayList;

public class ManagedChampsModel {

    private final DBManager db;
    private final ManagedChampsViewModel viewModel;

    ManagedChampsModel(ManagedChampsViewModel viewModel){
        this.viewModel = viewModel;
        db = DBManager.getInstance();
    }

    public void requestChampsList(){
        db.getManagedChampsList(new DBManager.IChampsInfoListener() {
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
