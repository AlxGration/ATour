package com.alex.atour.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.alex.atour.DTO.Champ;
import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.Member;
import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.DTO.RequestLinks;
import com.alex.atour.DTO.User;
import com.alex.atour.models.MembershipState;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FirebaseDB extends DBManager{

    private static final int LIMIT_RECS_COUNT = 30;
    private final String USER_TABLE = "Users";// таблица пользователей
    private final String REQUEST_TABLE = "Requests";// запросы на участие чемпионате
    private final String CHAMP_TABLE = "Champs";//основная таблица чемпионата(документы,судьи,участници,заявки,оценки)
    private final String CHAMP_INFO_TABLE = "ChampsInfo";//информация о чемпионате
    private final String MEMBER_TABLE = "Members";//информация о чемпионате
    private final String ACCEPTED_REQUEST_TABLE = "Accepted";//информация о чемпионате

    private FirebaseAuth auth;
    private FirebaseUser user;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://alex-atour-usatu-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference dbRef;

    public boolean checkUserAuth(){
        return getUser() != null;//true - auth
    }

    public void login(String login, String password, IRequestListener listener){
        getAuth().signInWithEmailAndPassword(login, password)
                .addOnCompleteListener(getExecutor(), task ->  {
                        if (task.isSuccessful()) {
                            user = getUser();
                            getPrefs().setUserID(user.getUid());
                            if (listener!=null) listener.onSuccess();
                        } else {
                            if (listener!=null) listener.onFailed("Ошибка аутентификации");
                        }
                    }
                );

    }

    public void signOut(){
        getAuth().signOut();
        user = null;
        getPrefs().clearUserData();
    }

    public void registration(User _user, String password, IRequestListener listener) {
        getAuth().createUserWithEmailAndPassword(_user.getEmail(), password)
                .addOnCompleteListener(getExecutor(), task -> {
                    if (task.isSuccessful()) {
                        user = getUser();

                        String userID = user.getUid();
                        Log.e("TAG", "userUID: "+userID);

                        _user.setId(userID);
                        //adding info to my "public" db
                        DatabaseReference ref = getDbRef();
                        ref.child(USER_TABLE).child(userID).setValue(_user);

                        if (listener!=null) listener.onSuccess();
                    } else {
                        if (listener!=null) listener.onFailed("Ошибка регистрации");
                    }
                });
    }

    @Override
    public void sendMembershipRequest(MembershipRequest memReq, IRequestListener listener) {
        String userID = getUser().getUid();

        //get user registration data
        Query query = getDbRef().child(USER_TABLE).child(userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User u = snapshot.getValue(User.class);
                DatabaseReference ref = getDbRef().child(REQUEST_TABLE);
                //create new id in REQUEST_TABLE
                String reqID = ref.push().getKey();

                // fill remained fields
                memReq.setId(reqID);
                memReq.setUserFIO(u.getFio());
                memReq.setUserID(userID);

                // create table (champID, userID) for list of MY requests(champs)
                RequestLinks tmp = new RequestLinks(memReq.getChampID(), memReq.getUserID());
                ref.child(reqID).setValue(tmp).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){

                        // create MembershipRequest in Champ->Request for admin checking
                        DatabaseReference champRef = getDbRef().child(CHAMP_TABLE).child(memReq.getChampID()).child(REQUEST_TABLE);
                        champRef.child(reqID).setValue(memReq).addOnCompleteListener(t -> {

                            if (t.isSuccessful()) {

                                //add user membership status at Champ->Members table
                                DatabaseReference r = getDbRef().child(CHAMP_TABLE).child(memReq.getChampID()).child(MEMBER_TABLE).child(memReq.getUserID());
                                Member member = getMemberFromMembershipRequest(memReq);
                                r.setValue(member).addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        if (listener != null) listener.onSuccess();
                                    } else {
                                        if (listener != null)
                                            listener.onFailed("Ошибка. Попробуйте еще раз");
                                    }
                                });
                            }else{
                                if (listener!=null) listener.onFailed("Ошибка. Попробуйте еще раз");
                            }
                        });
                    }else{
                        if (listener!=null) listener.onFailed("Ошибка. Попробуйте еще раз");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (listener!=null) listener.onFailed(error.getMessage());
            }
        });
    }

    @Override
    public void getUserData(String userID, IUserInfoListener listener) {
        if (userID.equals(User.MyID)){
            userID = getUser().getUid();
        }

        Query query = getDbRef().child(USER_TABLE).child(userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User u = snapshot.getValue(User.class);
                if (listener!=null) listener.onSuccess(u);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (listener!=null) listener.onFailed(error.getMessage());
            }
        });
    }

    @Override
    public void createNewChamp(ChampInfo champInfo, IRequestListener listener) {
        String userID = "";
        userID = getUser().getUid();

        //create champID in Champs table
        DatabaseReference champRef = getDbRef().child(CHAMP_TABLE);
        String champID = champRef.push().getKey();
        //start filling ChampRecord in Champs table
        Champ champ = new Champ();
        champ.setId(champID);


        //create champInfoID in ChampsInfoTable
        DatabaseReference champInfoRef = getDbRef().child(CHAMP_INFO_TABLE);
        champInfo.setChampID(champID);  // id чемпионата
        champInfo.setAdminID(userID);   // id организатора
        //create champInfoRecord in ChampsInfoTable
        champInfoRef.child(champID).setValue(champInfo);

        //create champRecord in ChampsTable
        champRef.child(champID).setValue(champ).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (listener!=null) listener.onSuccess();
            }else{
                if (listener!=null) listener.onFailed("Ошибка. Попробуйте еще раз");
            }
        });
    }

    @Override
    public void getChampsList(IChampsInfoListener listener) {
        Query query = getDbRef().child(CHAMP_INFO_TABLE).limitToLast(LIMIT_RECS_COUNT);
        champsInfoListQuery(query, listener);
    }

    @Override
    public void getChampsList(String searchRequest,IChampsInfoListener listener) {
        Query query = getDbRef().child(CHAMP_INFO_TABLE).orderByChild("title").startAt(searchRequest);
        champsInfoListQuery(query, listener);
    }

    @Override
    public void getManagedChampsList(IChampsInfoListener listener) {
        Query query = getDbRef().child(CHAMP_INFO_TABLE).orderByChild("adminID").equalTo(getUser().getUid());
        champsInfoListQuery(query, listener);
    }

    @Override
    public void getMyChampsList(IChampsInfoListener listener) {
        Query query = getDbRef().child(REQUEST_TABLE).orderByChild("userID").equalTo(getUser().getUid());
        query.addValueEventListener(new ValueEventListener() { //запрос на чемпионаты, на которые пользователь подал заявку
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<ChampInfo> list = new ArrayList<ChampInfo>((int)snapshot.getChildrenCount());
                DatabaseReference chInfoRef = getDbRef().child(CHAMP_INFO_TABLE);
                for (DataSnapshot snap: snapshot.getChildren()){ // iterate requests
                    String champInfoID = snap.getValue(RequestLinks.class).champInfoID;

                    Query q = chInfoRef.orderByChild("champID").equalTo(champInfoID);
                    q.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snp) {//getting champ info
                            for (DataSnapshot sp: snp.getChildren()){ 
                                list.add(sp.getValue(ChampInfo.class));
                            }
                            if (listener!=null) listener.onSuccess(list);
                        }
                        @Override public void onCancelled(@NonNull DatabaseError error) {
                            if (listener!=null) listener.onFailed(error.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (listener!=null) listener.onFailed(error.getMessage());
            }
        });
    }

    @Override
    public void getMembershipRequestsList(String champID, IMembershipRequestsListListener listener) {
        Query query = getDbRef().child(CHAMP_TABLE).child(champID).child(REQUEST_TABLE);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<MembershipRequest> list = new ArrayList<>((int)snapshot.getChildrenCount());
                for (DataSnapshot snap: snapshot.getChildren()){ // iterate requests
                    list.add(snap.getValue(MembershipRequest.class));
                }

                if (listener!=null) listener.onSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (listener!=null) listener.onFailed(error.getMessage());
            }
        });
    }

    // перемещение MembershipRequest из таблиццы Champ->Requests
    // в Champ->Accepted
    // смена статуса пользователя Champ->Members на ACCEPTED
    @Override
    public void acceptRequest(MembershipRequest req, IRequestListener listener){
        DatabaseReference ref = getDbRef().child(CHAMP_TABLE).child(req.getChampID());
        // add MembershipRequest to Champ->Accepted
        ref.child(ACCEPTED_REQUEST_TABLE).child(req.getId()).setValue(req).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // delete MembershipRequest from Champ->Requests
                ref.child(REQUEST_TABLE).child(req.getId()).removeValue().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()){
                        //todo: change state in MEMBER TABLE
                        ref.child(MEMBER_TABLE).child(req.getUserID()).child("state").setValue(MembershipState.ACCEPTED.ordinal());
                        if (listener!=null) listener.onSuccess();
                    }else{
                        if (listener!=null) listener.onFailed("Ошибка. Попробуйте еще раз");
                    }
                });
            }else if (listener!=null) listener.onFailed("Ошибка. Попробуйте еще раз");
        });
    }


    // Удаление Request из таблиццы Champ->Requests, Requests
    // смена статуса пользователя Champ->Members на DENIED
    @Override
    public void denyRequest(MembershipRequest req, IRequestListener listener){
        DatabaseReference ref = getDbRef().child(CHAMP_TABLE).child(req.getChampID());
        // remove MembershipRequest from Champ->Requests
        ref.child(REQUEST_TABLE).child(req.getId()).removeValue().addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){
                //todo: remove request from Request table, Member table
                //ref.child(MEMBER_TABLE).child(req.getUserID()).child("state").setValue(MembershipState.DENIED.ordinal());
                // remove user data from MEMBERS table
                ref.child(MEMBER_TABLE).child(req.getUserID()).removeValue();
                // remove user data from REQUESTS table
                DatabaseReference reqRef = getDbRef().child(REQUEST_TABLE).child(req.getId());
                reqRef.removeValue();

                if (listener!=null) listener.onSuccess();
            }else{
                if (listener!=null) listener.onFailed("Ошибка. Попробуйте еще раз");
            }
        });
    }

    // get one Member Champ->Members by ID
    @Override
    public void getMemberByID(String userID, String champID, IMembersListListener listener){
        Query query = getDbRef().child(CHAMP_TABLE).child(champID).child(MEMBER_TABLE).orderByChild("userID").equalTo(userID);
        membersListQuery(query, listener);
    }

    // get Champ->Members who has state >= ACCEPTED
    @Override
    public void getMembers(String champID, IMembersListListener listener){
        Query query = getDbRef().child(CHAMP_TABLE).child(champID).child(MEMBER_TABLE).orderByChild("state").startAt(MembershipState.ACCEPTED.ordinal());
        membersListQuery(query, listener);
    }

    // get members who sent docs and waiting results
    @Override
    public void getDocsSentMembers(String champID, IMembersListListener listener){

        // get referee info (for return specified by type members)
        getMemberByID(getUser().getUid(), champID, new IMembersListListener() {
            @Override
            public void onSuccess(ArrayList<Member> members) {
                Member me = members.get(0);
                // get all docs sent members
                Query query = getDbRef().child(CHAMP_TABLE).child(champID).child(MEMBER_TABLE).orderByChild("state").equalTo(MembershipState.DOCS_SUBMISSION.ordinal());
                membersListQuery(query, new IMembersListListener() {
                    @Override
                    public void onSuccess(ArrayList<Member> members) {
                        ArrayList<Member> result = new ArrayList<>(members.size()/2);

                        // and extract only needed
                        for(Member m: members) {
                            if (me.isTypeWalk())    {result.add(m); continue;}
                            if (me.isTypeSki())     {result.add(m); continue;}
                            if (me.isTypeHike())    {result.add(m); continue;}
                            if (me.isTypeWater())   {result.add(m); continue;}

                            if (me.isTypeSpeleo())  {result.add(m); continue;}
                            if (me.isTypeBike())    {result.add(m); continue;}
                            if (me.isTypeAuto())    {result.add(m); continue;}
                            if (me.isTypeOther())   result.add(m);
                        }
                        if (listener!=null) listener.onSuccess(result);
                    }

                    @Override
                    public void onFailed(String msg) {
                        if (listener!=null) listener.onFailed(msg);
                    }
                });
            }

            @Override
            public void onFailed(String msg) { if (listener!=null) listener.onFailed(msg);  }
        });
    }

    private void membersListQuery(Query query, IMembersListListener listener){
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Member> list = new ArrayList<>((int)snapshot.getChildrenCount());
                for (DataSnapshot snap: snapshot.getChildren()){ // iterate requests
                    list.add(snap.getValue(Member.class));
                }
                if (listener!=null) listener.onSuccess(list);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (listener!=null) listener.onFailed(error.getMessage());
            }
        });
    }

    private void champsInfoListQuery(Query query, IChampsInfoListener listener){
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ChampInfo> list = new ArrayList<ChampInfo>((int)snapshot.getChildrenCount());
                for (DataSnapshot snap: snapshot.getChildren()){
                    list.add(snap.getValue(ChampInfo.class));
                }
                if (listener!=null) listener.onSuccess(list);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (listener!=null) listener.onFailed(error.getMessage());
            }
        });
    }

    private FirebaseUser getUser(){
        if (user == null){ user = getAuth().getCurrentUser(); }
        return user;
    }
    private DatabaseReference getDbRef(){
        if (dbRef == null){ dbRef = database.getReference(); }
        return dbRef;
    }
    private FirebaseAuth getAuth(){
        if (auth == null){ auth = FirebaseAuth.getInstance(); }
        return auth;
    }
    private Member getMemberFromMembershipRequest(MembershipRequest req){
        Member member = new Member();
        member.setUserID(req.getUserID());
        member.setUserFIO(req.getUserFIO());
        member.setRequestID(req.getId());
        member.setRole(req.getRole());
        member.setState(MembershipState.WAIT.ordinal());
        member.setTypeWalk(req.isTypeWalk());
        member.setTypeSki(req.isTypeSki());
        member.setTypeHike(req.isTypeHike());
        member.setTypeWater(req.isTypeWater());
        member.setTypeSpeleo(req.isTypeSpeleo());
        member.setTypeBike(req.isTypeBike());
        member.setTypeAuto(req.isTypeAuto());
        member.setTypeOther(req.isTypeOther());
        return member;
    }
}
