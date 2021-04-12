package com.alex.atour.ui.champ.member;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.Document;
import com.alex.atour.DTO.Member;
import com.alex.atour.db.DBManager;
import com.alex.atour.models.BaseViewModel;

import java.util.ArrayList;

public class DocsViewModel extends BaseViewModel {

    private DBManager db;

    private final MutableLiveData<Boolean> isSuccess;

    public DocsViewModel(){
        db = DBManager.getInstance();

        isSuccess = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> isSuccessLiveData() { return isSuccess; }

    public void sendDocs(String champID, String comment, String link){
        if (comment.isEmpty() || link.isEmpty()){
            requestError("Необходимо заполнить оба поля");
            return;
        }

        //todo:: send docs
        /*
        Document doc = new Document();
        doc.
        comment, link

        setIsLoading(true);
        db.sendDocument(champID, doc, new DBManager.IMembersListListener() {
            @Override
            public void onSuccess(ArrayList<Member> members) { setAllList(members); }
            @Override
            public void onFailed(String msg) { requestError(msg); }
        });

         */
    }

    void setMembersList(){
        setIsLoading(false);
        setErrorMessage("");
        this.isSuccess.setValue(true);
    }

    void requestError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
    }
}