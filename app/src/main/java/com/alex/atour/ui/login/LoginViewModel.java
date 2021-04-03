package com.alex.atour.ui.login;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alex.atour.models.BaseViewModel;
import com.alex.atour.models.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class LoginViewModel extends BaseViewModel {

    private final LoginModel model;

    private final MutableLiveData<Boolean> authFlag;

    public LoginViewModel(){
        authFlag = new MutableLiveData<>();
        model = new LoginModel(this);
    }

    void addExecutor(Activity activity){
        model.addExecutor(activity);
    }

    public MutableLiveData<Boolean> getAuthFlag() { return authFlag; }

    void loginRequest(String login, String pass){
        // data validation
        if (ValueFormatter.isLoginFormat(login) && ValueFormatter.isPasswordFormat(pass)){
            loginError("");
            setIsLoading(true);
            model.loginRequest(login, pass);
        }else{
            loginError("Некорректные данные");
        }
    }

    void checkAuth(){
        model.checkAuth();
    }

    void loginSuccess(){
        setIsLoading(false);
        setErrorMessage("");
        authFlag.setValue(true);
    }
    void loginError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
    }
}
