package com.alex.atour.db;

import android.app.Activity;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.Member;
import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.DTO.User;
import com.alex.atour.ui.champ.ChampActivity;

import java.util.ArrayList;

public abstract class DBManager {

    private static DBManager db;
    private Activity executor;
    private PrefsDB prefs;

    public Activity getExecutor() {
        return executor;
    }

    public void setExecutor(Activity executor) {
        this.executor = executor;
        prefs = new PrefsDB(executor.getApplicationContext());
    }

    public static DBManager getInstance(){
        if (db == null) db =  new FirebaseDB();
        return db;
    }

    public PrefsDB getPrefs(){return prefs;}

    public abstract boolean checkUserAuth();
    public abstract void signOut();

    public abstract void login(String login, String password, IRequestListener listener);//аутентификация
    public abstract void registration(User user, String password, IRequestListener listener);//регистрация
    public abstract void sendMembershipRequest(MembershipRequest memReq, IRequestListener listener);//создание заявки на чемпионат
    public abstract void getUserData(String userID, IUserInfoListener listener);//регистрационные данные пользователя по айди
    public abstract void createNewChamp(ChampInfo champInfo, IRequestListener listener);//создание чемпионата
    public abstract void getChampsList(IChampsInfoListener listener);//общий список чемпионатов
    public abstract void getMembers(String champID, IMembersListListener listener);//общий список чемпионатов
    public abstract void getChampsList(String searchRequest, IChampsInfoListener listener);//общий список чемпионатов
    public abstract void getManagedChampsList(IChampsInfoListener listener);//чемпионаты, которые создал пользователь
    public abstract void getMyChampsList(IChampsInfoListener listener);//чемпионаты, которые создал пользователь
    public abstract void getMembershipRequestsList(String champID, IMembershipRequestsListListener listener);//заявки на чемпионат(для админа)

    public abstract void acceptRequest(MembershipRequest req, IRequestListener listener);// одобрение заявки админом
    public abstract void denyRequest(MembershipRequest req, IRequestListener listener);// отмена заявки админом

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
    public interface IMembershipRequestsListListener{
        void onSuccess(ArrayList<MembershipRequest> requests);
        void onFailed(String msg);
    }
    public interface IMembersListListener{
        void onSuccess(ArrayList<Member> members);
        void onFailed(String msg);
    }
}
