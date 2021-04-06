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
        if (db == null) db =  new FirebaseDB();
        return db;
    }

    public abstract boolean checkUserAuth();
    public abstract void signOut();

    public abstract void login(String login, String password, IRequestListener listener);
    public abstract void registration(User user, String password, IRequestListener listener);
    public abstract void sendMembershipRequest(MembershipRequest memReq, IRequestListener listener);
    public abstract User getUserData(String userID, IUserInfoListener listener);

    public interface IRequestListener {
        void onSuccess();
        void onFailed(String msg);
    }

    public interface IUserInfoListener {
        void onSuccess(User user);
        void onFailed(String msg);
    }


}
