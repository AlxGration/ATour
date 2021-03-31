package com.alex.atour.ui.memrequest;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MemReqViewModel extends ViewModel {

    private final MutableLiveData<String> errorMessage;

    public MemReqViewModel(){
        errorMessage = new MutableLiveData<>();
    }


    public MutableLiveData<String> getErrorMessage() { return errorMessage; }
}
