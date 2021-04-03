package com.alex.atour.ui.registration;

import android.app.Activity;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alex.atour.models.BaseViewModel;
import com.alex.atour.models.ValueFormatter;

import java.util.concurrent.Executor;

public class RegViewModel extends BaseViewModel {

    private final RegModel model;
    private final MutableLiveData<Boolean> regFlag;

    public RegViewModel(){
        regFlag = new MutableLiveData<>();

        model = new RegModel(this);
    }

    void addExecutor(Activity activity){
        model.addExecutor(activity);
    }
    public MutableLiveData<Boolean> getRegFlag() { return regFlag; }

    void registrationRequest(String fio, String city, String phone, String email, String pass){
        Log.e("TAG", "REG: "+ fio+ " "+ city+ " "+ phone+ " "+ email+ " "+ pass +" ");
        if (
                ValueFormatter.isFIOFormat(fio) &&
                ValueFormatter.isPhoneFormat(phone) &&
                ValueFormatter.isLoginFormat(email) &&
                ValueFormatter.isPasswordFormat(pass)
        ){
            registrationError("");
            setIsLoading(true);
            model.registrationRequest(fio, city, phone, email, pass);
        }else{
            registrationError("Некорректные данные");
        }
    }
    void registrationSuccess(){
        setIsLoading(false);
        regFlag.setValue(true);
    }
    void registrationError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
        regFlag.setValue(false);
    }
}
