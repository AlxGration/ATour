package com.alex.atour.ui.profile;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.models.BaseViewModel;
import com.alex.atour.models.ValueFormatter;
import com.alex.atour.ui.registration.RegModel;

public class ProfileViewModel extends BaseViewModel {

    //private final RegModel model;
    private final MutableLiveData<String> secName;
    private final MutableLiveData<String> name;
    private final MutableLiveData<String> city;
    private final MutableLiveData<String> email;
    private final MutableLiveData<String> phone;

    public ProfileViewModel(){
        secName = new MutableLiveData<>();
        name = new MutableLiveData<>();
        city = new MutableLiveData<>();
        email = new MutableLiveData<>();
        phone = new MutableLiveData<>();
        //model = new RegModel(this);
    }

    public MutableLiveData<String> getSecName() { return secName; }
    public MutableLiveData<String> getName() { return name; }
    public MutableLiveData<String> getCity() { return city; }
    public MutableLiveData<String> getEmail() { return email; }
    public MutableLiveData<String> getPhone() { return phone; }

    public void loadProfile(){

    }

    void registrationSuccess(){
        setIsLoading(false);
        setErrorMessage("");
    }
    void registrationError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
    }
}
