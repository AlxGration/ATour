package com.alex.atour.db;

import android.app.Activity;

import com.alex.atour.DTO.User;

import java.util.concurrent.Executor;

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

    public abstract void login(String login, String password, IonLoginListener loginListener);
    public abstract void registration(User user, String password, IonRegistrationListener regListener);

    public interface IonLoginListener{
        void onSuccess();
        void onFailed(String msg);
    }
    public interface IonRegistrationListener{
        void onSuccess();
        void onFailed(String msg);
    }
}
