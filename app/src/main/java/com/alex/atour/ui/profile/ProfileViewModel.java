package com.alex.atour.ui.profile;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.Document;
import com.alex.atour.DTO.Estimation;
import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.DTO.User;
import com.alex.atour.models.BaseViewModel;

import java.util.ArrayList;

public class ProfileViewModel extends BaseViewModel {

    private final ProfileModel model;

    private final MutableLiveData<String> secName;
    private final MutableLiveData<String> name;
    private final MutableLiveData<String> city;
    private final MutableLiveData<String> email;
    private final MutableLiveData<String> phone;

    private final MutableLiveData<Document> document;
    private final MutableLiveData<MembershipRequest> memReq;
    private final MutableLiveData<ArrayList<Estimation>> estims;

    public ProfileViewModel(){
        secName = new MutableLiveData<>();
        email = new MutableLiveData<>();
        phone = new MutableLiveData<>();
        name = new MutableLiveData<>();
        city = new MutableLiveData<>();
        document = new MutableLiveData<>();
        memReq = new MutableLiveData<>();
        estims = new MutableLiveData<>();

        model = new ProfileModel(this);
    }

    public MutableLiveData<String> getSecName() { return secName; }
    public MutableLiveData<String> getName() { return name; }
    public MutableLiveData<String> getCity() { return city; }
    public MutableLiveData<String> getEmail() { return email; }
    public MutableLiveData<String> getPhone() { return phone; }
    public MutableLiveData<Document> getDocument() { return document; }
    public MutableLiveData<MembershipRequest> getMembershipRequest() { return memReq; }
    public MutableLiveData<ArrayList<Estimation>> getEstimations() { return estims; }

    public void loadProfile(String userID){
        setIsLoading(true);
        model.requestUserProfile(userID);
    }

    public void loadMembershipRequest(String champID, String userID){
        setIsLoading(true);
        model.requestMembershipRequest(champID, userID);
    }

    public void loadDocs(String champID, String userID){
        setIsLoading(true);
        model.requestDocuments(champID, userID);
    }

    public void loadEstimations(String champID, String userID){
        //todo:create ME
    }

    void requestError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
    }

    void setDocument(Document doc){
        document.setValue(doc);
        setIsLoading(false);
    }

    void setMembershipRequest(MembershipRequest req){
        memReq.setValue(req);
        setIsLoading(false);
    }

    void setUserInfo(User user){
        setUserName(user.getFio());
        city.setValue(user.getCity());
        email.setValue(user.getEmail());
        phone.setValue("+7 "+user.getPhone());
        setIsLoading(false);
    }

    void signOut(){
        model.signOut();
    }

    public void setUserName(String userName){
        String[] fio = userName.split(" ");
        secName.setValue(fio[0]);
        name.setValue(fio[1]+" "+fio[2]);
    }
}
