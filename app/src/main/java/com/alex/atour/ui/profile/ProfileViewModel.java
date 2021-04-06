package com.alex.atour.ui.profile;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.models.BaseViewModel;

public class ProfileViewModel extends BaseViewModel {

    private final ProfileModel model;

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
        model = new ProfileModel(this);
    }

    public MutableLiveData<String> getSecName() { return secName; }
    public MutableLiveData<String> getName() { return name; }
    public MutableLiveData<String> getCity() { return city; }
    public MutableLiveData<String> getEmail() { return email; }
    public MutableLiveData<String> getPhone() { return phone; }

    public void setSecName(String s) { secName.setValue(s); }
    public void setName(String s) { name.setValue(s); }
    public void setCity(String s) { city.setValue(s); }
    public void setEmail(String s) { email.setValue(s); }
    public void setPhone(String s) { phone.setValue(s); }

    public void loadProfile(String userID){
        setIsLoading(true);
        setErrorMessage("");

        model.requestUserProfile(userID);
    }

    void requestProfileSuccess(){
        setIsLoading(false);
        setErrorMessage("");
    }
    void requestProfileError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
    }

    void signOut(){
        model.signOut();
    }
}
