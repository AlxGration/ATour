package com.alex.atour.ui.login;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alex.atour.models.ValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class LoginViewModel extends ViewModel {

    private LoginModel model;

    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<Boolean> authFlag;
    private final MutableLiveData<String> errorMessage;

    public LoginViewModel(){
        isLoading = new MutableLiveData<>();
        authFlag = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        model = new LoginModel(this);
    }

    void addExecutor(Activity activity){
        model.addExecutor(activity);
    }

    public MutableLiveData<Boolean> getAuthFlag() { return authFlag; }
    public MutableLiveData<Boolean> getIsLoading() { return isLoading; }
    public MutableLiveData<String> getErrorMessage() { return errorMessage; }

    void loginRequest(String login, String pass){
        // data validation
        if (ValueFormatter.isLoginFormat(login) && ValueFormatter.isPasswordFormat(pass)){
            loginError("");
            isLoading.setValue(true);
            model.loginRequest(login, pass);
        }else{
            loginError("Некорректные данные");
        }
    }

    void checkAuth(){
        model.checkAuth();
    }

    void loginSuccess(){
        isLoading.setValue(false);
        authFlag.setValue(true);
    }
    void loginError(String msg){
        isLoading.setValue(false);
        errorMessage.setValue(msg);
    }
}
