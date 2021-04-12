package com.alex.atour.ui.login;

import android.app.Activity;
import androidx.lifecycle.MutableLiveData;
import com.alex.atour.models.BaseViewModel;
import com.alex.atour.models.ValueFormatter;

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
        logOut();
    }

    public void logOut(){
        authFlag.setValue(false);
    }
}
