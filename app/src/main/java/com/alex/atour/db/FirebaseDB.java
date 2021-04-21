package com.alex.atour.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.alex.atour.DTO.Champ;
import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.Document;
import com.alex.atour.DTO.Estimation;
import com.alex.atour.DTO.Member;
import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.DTO.ShortEstimation;
import com.alex.atour.DTO.ShortRequest;
import com.alex.atour.DTO.User;
import com.alex.atour.models.MembershipState;
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
    private final String CHAMP_TABLE = "Champs";// основная таблица чемпионата(документы,судьи,участници,заявки,оценки)
    private final String CHAMP_INFO_TABLE = "ChampsInfo";// информация о чемпионате
    private final String MEMBER_TABLE = "Members";// заявки пользователей
    private final String ACCEPTED_REQUEST_TABLE = "Accepted";// одобренные заявки пользователей
    private final String DOCUMENTS = "Docs";// таблица документов
    private final String ESTIMATIONS = "Estimations";// таблица оценок
    private final String ESTIMS = "Estims";// укороченная таблица оценок

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
                        //adding info to my db
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
                ShortRequest tmp = new ShortRequest(memReq.getChampID(), memReq.getUserID());
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
                    String champInfoID = snap.getValue(ShortRequest.class).champInfoID;

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
        getMembershipRequests(query, listener);
    }
    @Override
    public void getMembershipRequestByID(String champID, String userID, IMembershipRequestsListListener listener) {
        Query query = getDbRef().child(CHAMP_TABLE).child(champID).child(ACCEPTED_REQUEST_TABLE).orderByChild("userID").equalTo(userID);
        getMembershipRequests(query, listener);
    }

    // add to Champ->Estimation and Champ->Docs->memberID->Estimations->(refereeID)
    @Override
    public void sendEstimation(String champID, Estimation estim, IRequestListener listener){
        DatabaseReference ref = getDbRef().child(CHAMP_TABLE).child(champID).child(ESTIMATIONS);
        String estimID = ref.push().getKey();
        estim.setId(estimID);

        ref.child(estimID).setValue(estim).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                if (listener!=null) listener.onSuccess();
        });

        // чтобы не показывать участника судье после оценивания
        ref = getDbRef().child(CHAMP_TABLE).child(champID).child(ESTIMS);
        ShortEstimation est = new ShortEstimation(estim.getMemberID(), estim.getRefereeID());
        ref.push().setValue(est);
    }


    private void getMembershipRequests(Query query, IMembershipRequestsListListener listener){
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
                        changeUserState(req.getChampID(), req.getUserID(), MembershipState.ACCEPTED.ordinal());
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

    // add Document to Champ->Documents
    @Override
    public void sendDocument(String champID, Document document, IRequestListener listener){
        DatabaseReference ref = getDbRef().child(CHAMP_TABLE).child(champID).child(DOCUMENTS).child(document.getUserID());

        //send docs
        ref.setValue(document).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()){

                //and change user state (to show results)
                changeUserState(champID, document.getUserID(), MembershipState.RESULTS.ordinal());

                if (listener!=null) listener.onSuccess();
            }else{
                if (listener!=null) listener.onFailed("Ошибка. Попробуйте еще раз");
            }
        });
    }

    @Override
    public void getDocumentByUserID(String champID, String userID, IDocumentListener listener){
        Query query = getDbRef().child(CHAMP_TABLE).child(champID).child(DOCUMENTS).orderByChild("userID").equalTo(userID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.e("TAG", "getDocumentByUserID: "+snapshot.toString());

                int n = (int)snapshot.getChildrenCount();
                if (n < 1){
                    if (listener!=null) listener.onFailed("Документов не найдено");
                    return;
                }

                ArrayList<Document> list = new ArrayList<>(n);
                for (DataSnapshot snap: snapshot.getChildren()){ // iterate requests
                    list.add(snap.getValue(Document.class));
                }
                if (listener!=null) listener.onSuccess(list.get(0));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (listener!=null) listener.onFailed(error.getMessage());
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
        String refereeID = getUser().getUid();
        getMemberByID(refereeID, champID, new IMembersListListener() {
            @Override
            public void onSuccess(ArrayList<Member> members) {
                Member me = members.get(0);//this is referee
                // get all docs sent members
                //todo::maybe change state to DOCS_SUBMIT?
                Query query = getDbRef().child(CHAMP_TABLE).child(champID).child(MEMBER_TABLE).orderByChild("state").equalTo(MembershipState.RESULTS.ordinal());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Member> members = new ArrayList<>((int)snapshot.getChildrenCount());
                        for (DataSnapshot snap: snapshot.getChildren()){
                            Member m = snap.getValue(Member.class);
                            //type filter (show member to referee only if they have common types)
                            if (me.isTypeWalk())    {members.add(m); continue;}
                            if (me.isTypeSki())     {members.add(m); continue;}
                            if (me.isTypeHike())    {members.add(m); continue;}
                            if (me.isTypeWater())   {members.add(m); continue;}

                            if (me.isTypeSpeleo())  {members.add(m); continue;}
                            if (me.isTypeBike())    {members.add(m); continue;}
                            if (me.isTypeAuto())    {members.add(m); continue;}
                            if (me.isTypeOther())   members.add(m);
                        }

                        //check is this referee has estimated this member
                        //чтобы не показывать судье уже оцененного участника
                        Query q = getDbRef().child(CHAMP_TABLE).child(champID).child(ESTIMS).orderByChild("refereeID").equalTo(refereeID);
                        q.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snap: snapshot.getChildren()){//перебор оценок, которые дал судья
                                    ShortEstimation est = snap.getValue(ShortEstimation.class);
                                    for(Member m: members){//проверка
                                        if (m.getUserID().equals(est.getMemberID())){ members.remove(m); break; }
                                    }
                                }

                                if (listener!=null) listener.onSuccess(members);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                if (listener!=null) listener.onFailed(error.getMessage());
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
    private void changeUserState(String champID, String userID, int state){
        getDbRef().child(CHAMP_TABLE).child(champID)
                .child(MEMBER_TABLE).child(userID).child("state").setValue(state);
    }
}
