package com.alex.atour.ui.profile;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.Document;
import com.alex.atour.DTO.User;
import com.alex.atour.models.BaseViewModel;

public class ProfileViewModel extends BaseViewModel {

    private final ProfileModel model;

    private final MutableLiveData<String> secName;
    private final MutableLiveData<String> name;
    private final MutableLiveData<String> city;
    private final MutableLiveData<String> email;
    private final MutableLiveData<String> phone;

    private final MutableLiveData<String> link;
    private final MutableLiveData<String> comment;

    public ProfileViewModel(){
        secName = new MutableLiveData<>();
        email = new MutableLiveData<>();
        phone = new MutableLiveData<>();
        name = new MutableLiveData<>();
        city = new MutableLiveData<>();
        link = new MutableLiveData<>();
        comment = new MutableLiveData<>();

        model = new ProfileModel(this);
    }

    public MutableLiveData<String> getSecName() { return secName; }
    public MutableLiveData<String> getName() { return name; }
    public MutableLiveData<String> getCity() { return city; }
    public MutableLiveData<String> getEmail() { return email; }
    public MutableLiveData<String> getPhone() { return phone; }
    public MutableLiveData<String> getLink() { return link; }
    public MutableLiveData<String> getComment() { return comment; }

    public void loadProfile(String userID){
        setIsLoading(true);
        setErrorMessage("");
        model.requestUserProfile(userID);
    }

    public void loadDocs(String champID, String userID){
        setIsLoading(true);
        setErrorMessage("");
        model.requestDocuments(champID, userID);
    }

    void requestError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
    }

    void setDocument(Document doc){
        link.setValue(doc.getLink());
        comment.setValue(doc.getComment());
        setIsLoading(false);
        setErrorMessage("");
    }

    void setUserInfo(User user){
        setUserName(user.getFio());
        city.setValue(user.getCity());
        email.setValue(user.getEmail());
        phone.setValue("+7 "+user.getPhone());
        setIsLoading(false);
        setErrorMessage("");
    }

    void signOut(){
        model.signOut();
    }

    public void setUserName(String userName){
        String[] fio = userName.split(" ");
        secName.setValue(fio[0]);
        name.setValue(fio[1]+" "+fio[2]);
    }
    public void setComment(String comment){
        this.comment.setValue(comment);
    }
    public void setLink(String link){
        this.link.setValue(link);
    }
}
