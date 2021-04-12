package com.alex.atour.ui.requests;

import androidx.lifecycle.MutableLiveData;
import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.db.DBManager;
import com.alex.atour.models.BaseViewModel;
import java.util.ArrayList;

public class RequestsViewModel extends BaseViewModel {

    private DBManager db;

    private final MutableLiveData<ArrayList<MembershipRequest>> requests;

    public RequestsViewModel(){
        requests = new MutableLiveData<>();
        db = DBManager.getInstance();
    }

    public MutableLiveData<ArrayList<MembershipRequest>> getRequestsLiveData() { return requests; }

    public void getRequests(String champID){
        setErrorMessage("");
        setIsLoading(true);

        db.getMembershipRequestsList(champID, new DBManager.IMembershipRequestsListListener() {
            @Override
            public void onSuccess(ArrayList<MembershipRequest> requests) {
                setRequestsList(requests);
            }
            @Override
            public void onFailed(String msg) { requestError(msg); }
        });
    }

    void setRequestsList(ArrayList<MembershipRequest> requests){
        setIsLoading(false);
        setErrorMessage("");
        this.requests.setValue(requests);
    }
    void requestError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
    }

    public void acceptRequest(MembershipRequest req){
        db.acceptRequest(req, new DBManager.IRequestListener() {
            @Override
            public void onSuccess() { }

            @Override
            public void onFailed(String msg) { setErrorMessage(msg); }
        });
    }
    public void denyRequest(MembershipRequest req){
        db.denyRequest(req, new DBManager.IRequestListener() {
            @Override
            public void onSuccess() { }

            @Override
            public void onFailed(String msg) { setErrorMessage(msg); }
        });
    }
}