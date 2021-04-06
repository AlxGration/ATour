package com.alex.atour.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.alex.atour.DTO.Champ;
import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.DTO.User;
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


public class FirebaseDB extends DBManager{

    private final String USER_TABLE = "Users";
    private final String REQUEST_TABLE = "Requests";
    private final String CHAMP_TABLE = "Champs";
    private final String CHAMP_INFO_TABLE = "ChampsInfo";

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
        DatabaseReference ref = getDbRef().child(REQUEST_TABLE);
        String keyID = ref.push().getKey();

        user = getUser();
        memReq.setId(keyID);
        memReq.setUserID(user.getUid());

        dbRef.child(keyID).setValue(memReq).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if (listener!=null) listener.onSuccess();
            }else{
                if (listener!=null) listener.onFailed("Ошибка. Попробуйте еще раз");
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
    public void createNewChampRequest(ChampInfo champInfo, IRequestListener listener) {
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
        String champInfoID = champInfoRef.push().getKey();
        champInfo.setId(champInfoID);   // id
        champInfo.setChampID(champID);  // id чемпионата
        champInfo.setAdminID(userID);   // id организатора
        //create champInfoRecord in ChampsInfoTable
        champInfoRef.child(champInfoID).setValue(champInfo);



        //create champRecord in ChampsTable
        champ.setInfoID(champInfoID);
        champRef.child(champID).setValue(champ).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    if (listener!=null) listener.onSuccess();
                }else{
                    if (listener!=null) listener.onFailed("Ошибка. Попробуйте еще раз");
                }
            }
        });
    }

    private FirebaseUser getUser(){
        if (user == null){
            user = getAuth().getCurrentUser();
        }
        return user;
    }
    private DatabaseReference getDbRef(){
        if (dbRef == null){
            dbRef = database.getReference();
        }
        return dbRef;
    }
    private FirebaseAuth getAuth(){
        if (auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
}
