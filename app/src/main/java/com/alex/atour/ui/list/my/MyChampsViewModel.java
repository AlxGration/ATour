package com.alex.atour.ui.list.my;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import java.util.ArrayList;

public class MyChampsViewModel extends ViewModel {

    private final MyChampsModel model;

    public MyChampsViewModel(){
        //stocksLiveData = new MutableLiveData<>();
        model = new MyChampsModel(this);
    }
}