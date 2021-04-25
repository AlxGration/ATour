package com.alex.atour.db;

import android.app.Activity;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.Document;
import com.alex.atour.DTO.Estimation;
import com.alex.atour.DTO.Member;
import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.DTO.RefereeRank;
import com.alex.atour.DTO.User;
import com.alex.atour.ui.champ.ChampActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class DBManager {

    private static DBManager db;
    private static RealmDB realmDB;
    private Activity executor;
    private PrefsDB prefs;

    public Activity getExecutor() {
        return executor;
    }

    public void setExecutor(Activity executor) {
        this.executor = executor;
        prefs = new PrefsDB(executor.getApplicationContext());
    }

    public RealmDB getRealmDB(){
        if (realmDB == null) realmDB =  new RealmDB();
        return realmDB;
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
    public abstract void getMembers(String champID, IMembersListListener listener);//весь список участников чемпионата, включая подавших только заявки
    public abstract void getMemberByID(String userID, String champID, IMembersListListener listener);//участник чемпионата по ID
    public abstract void getDocsSentMembers(String champID, String refereeID, IMembersListListener listener);//участник чемпионата, которые отправили документы
    public abstract void getChampsList(String searchRequest, IChampsInfoListener listener);//общий список чемпионатов
    public abstract void getManagedChampsList(IChampsInfoListener listener);//чемпионаты, которые создал пользователь
    public abstract void getMyChampsList(IChampsInfoListener listener);//чемпионаты, которые создал пользователь
    public abstract void getMembershipRequestsList(String champID, IMembershipRequestsListListener listener);//заявки на чемпионат(для админа)
    public abstract void getMembershipRequestByID(String champID, String userID, IMembershipRequestsListListener listener);//заявка одобренная на чемпионат(для админа)
    public abstract void sendDocument(String champID, Document document, IRequestListener listener); // добавить документ
    public abstract void getDocumentByUserID(String champID, String userID, IDocumentListener listener);// получить документ по ID чемпионата и пользователя

    public abstract void getRefereeEstimationsList(String champID, String refereeID, IEstimationsListListener listener); // получить оценки определенного судьи
    public abstract void getAllEstimationsList(String champID, IEstimationsListListener listener); // получить все оценки судей
    public abstract void getAllRefereesRanksList(String champID, IRefereesRanksListListener listener); // получить разряды всех судей
    public abstract void getRefereeRank(String champID, String refereeID, IRefereeRankListListener listener); // получить разряд определенного судьи
    public abstract void closeEnrollmentAndCreateRefereeProtocols(String champID, IRequestListener listener);//закрыть прием заявок, сформировать судейские протоколы
    public abstract void sendRefereeEstimations(List<Estimation> estimations, String refereeInfo, IRequestListener listener); // отправить все оценки судьи

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
    public interface IEstimationsListListener{
        void onSuccess(ArrayList<Estimation> estims);
        void onFailed(String msg);
    }
    public interface IRefereesRanksListListener{
        void onSuccess(ArrayList<RefereeRank> ranks);
        void onFailed(String msg);
    }
    public interface IDocumentListener{
        void onSuccess(Document document);
        void onFailed(String msg);
    }
    public interface IRefereeRankListListener{
        void onSuccess(String refereeRank);
        void onFailed(String msg);
    }
}
