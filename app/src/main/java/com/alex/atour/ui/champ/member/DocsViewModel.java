package com.alex.atour.ui.champ.member;

import com.alex.atour.DTO.Document;
import com.alex.atour.db.DBManager;
import com.alex.atour.models.BaseViewModel;

public class DocsViewModel extends BaseViewModel {

    private final DBManager db;


    public DocsViewModel(){
        db = DBManager.getInstance();
    }


    public void sendDocs(String champID, String comment, String link){
        if (comment.isEmpty() || link.isEmpty()){
            requestError("Необходимо заполнить оба поля");
            return;
        }
        setIsLoading(true);

        Document doc = new Document();
        doc.setComment(comment);
        doc.setLink(link);
        doc.setUserID(db.getPrefs().getUserID());

        db.sendDocument(champID, doc, new DBManager.IRequestListener() {
            @Override
            public void onSuccess() { requestSuccess(); }
            @Override
            public void onFailed(String msg) { requestError(msg); }
        });
    }

    void requestSuccess(){
        setIsLoading(false);
        setErrorMessage("");
    }

    void requestError(String msg){
        setIsLoading(false);
        setErrorMessage(msg);
    }
}