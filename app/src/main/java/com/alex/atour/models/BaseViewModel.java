package com.alex.atour.models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel  extends ViewModel {

    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> errorMessage;

    public BaseViewModel(){
        isLoading = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getIsLoading() { return isLoading; }
    public MutableLiveData<String> getErrorMessage() { return errorMessage; }

    public void setIsLoading(boolean isLoading){ this.isLoading.setValue(isLoading); }
    public void setErrorMessage(String msg){ this.errorMessage.setValue(msg); }
}
