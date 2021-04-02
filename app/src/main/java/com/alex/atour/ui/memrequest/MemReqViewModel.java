package com.alex.atour.ui.memrequest;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.db.DBManager;

public class MemReqViewModel extends ViewModel {

    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<Boolean> isRequestSuccess;
    private DBManager db;

    public MemReqViewModel(){
        errorMessage = new MutableLiveData<>();
        isRequestSuccess = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();

        db = DBManager.getInstance();
    }


    public MutableLiveData<String> getErrorMessage() { return errorMessage; }
    public MutableLiveData<Boolean> getIsRequestSuccess() { return isRequestSuccess; }
    public MutableLiveData<Boolean> getIsLoading() { return isLoading; }

    public void sendMembershipRequest(MembershipRequest memReq){
        isLoading.setValue(true);
        db.sendMembershipRequest(memReq, new DBManager.IonOperationListener() {
            @Override
            public void onSuccess() {
                isRequestSuccess.setValue(true);
                isLoading.setValue(false);
            }

            @Override
            public void onFailed(String msg) {
                errorMessage.setValue(msg);
                isRequestSuccess.setValue(false);
                isLoading.setValue(false);
            }
        });
    }
}
