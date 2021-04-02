package com.alex.atour.db;

import android.app.Activity;

import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.DTO.User;

public abstract class DBManager {

    private static DBManager db;
    private Activity executor;

    public Activity getExecutor() {
        return executor;
    }

    public void setExecutor(Activity executor) {
        this.executor = executor;
    }

    public static DBManager getInstance(){
        if (db == null) db =  new FirebaseManager();
        return db;
    }

    public abstract boolean checkUserAuth();

    public abstract void login(String login, String password, IonOperationListener loginListener);
    public abstract void registration(User user, String password, IonOperationListener regListener);
    public abstract void sendMembershipRequest(MembershipRequest memReq, IonOperationListener regListener);

    public interface IonOperationListener {
        void onSuccess();
        void onFailed(String msg);
    }
}
