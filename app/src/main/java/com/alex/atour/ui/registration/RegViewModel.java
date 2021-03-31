package com.alex.atour.ui.registration;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alex.atour.models.ValueFormatter;

import java.util.concurrent.Executor;

public class RegViewModel extends ViewModel {

    private RegModel model;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<Boolean> regFlag;
    private final MutableLiveData<String> errorMessage;

    public RegViewModel(){
        isLoading = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        regFlag = new MutableLiveData<>();

        model = new RegModel(this);
    }

    void addExecutor(Activity activity){
        model.addExecutor(activity);
    }

    public MutableLiveData<Boolean> getIsLoading() { return isLoading; }
    public MutableLiveData<Boolean> getRegFlag() { return regFlag; }
    public MutableLiveData<String> getErrorMessage() { return errorMessage; }


    void registrationRequest(String fio, String city, String phone, String email, String pass){
        Log.e("TAG", "REG: "+ fio+ " "+ city+ " "+ phone+ " "+ email+ " "+ pass +" ");
        if (
                ValueFormatter.isFIOFormat(fio) &&
                ValueFormatter.isPhoneFormat(phone) &&
                ValueFormatter.isLoginFormat(email) &&
                ValueFormatter.isPasswordFormat(pass)
        ){
            registrationError("");
            isLoading.setValue(true);
            model.registrationRequest(fio, city, phone, email, pass);
        }else{
            registrationError("Некорректные данные");
        }
    }
    void registrationSuccess(){
        isLoading.setValue(false);
        regFlag.setValue(true);
    }
    void registrationError(String msg){
        isLoading.setValue(false);
        errorMessage.setValue(msg);
        regFlag.setValue(false);
    }
}
