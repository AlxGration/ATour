package com.alex.atour.db;

import android.app.Activity;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.DTO.User;
import com.alex.atour.ui.champ.ChampActivity;

import java.util.ArrayList;

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
    public abstract void getUserData(String userID, IUserInfoListener listener);
    public abstract void createNewChampRequest(ChampInfo champInfo, IRequestListener listener);
    public abstract void getChampsList(IChampsInfoListener listener);

    public interface IRequestListener {
        void onSuccess();
        void onFailed(String msg);
    }

    public interface IUserInfoListener {
        void onSuccess(User user);
        void onFailed(String msg);
    }
    public interface IChampsInfoListener{
        void onSuccess(ArrayList<ChampInfo> champsList);
        void onFailed(String msg);
    }


}
