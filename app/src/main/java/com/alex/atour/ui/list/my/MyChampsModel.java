package com.alex.atour.ui.list.my;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.db.DBManager;

import java.util.ArrayList;

public class MyChampsModel {

    private final DBManager db;
    private final MyChampsViewModel viewModel;

    MyChampsModel(MyChampsViewModel viewModel){
        this.viewModel = viewModel;
        db = DBManager.getInstance();
    }

    public void requestChampsList(){
        db.getMyChampsList(new DBManager.IChampsInfoListener() {
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
