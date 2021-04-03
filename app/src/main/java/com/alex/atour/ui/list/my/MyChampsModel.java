package com.alex.atour.ui.list.my;

import com.alex.atour.db.DBManager;

public class MyChampsModel {

    private final DBManager db;
    private final MyChampsViewModel viewModel;

    MyChampsModel(MyChampsViewModel viewModel){
        this.viewModel = viewModel;
        db = DBManager.getInstance();
    }

    public void requestChampsList(){
        
    }
}
