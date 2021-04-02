package com.alex.atour.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.DTO.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FirebaseManager extends DBManager{

    private final String USER_TABLE = "Users";
    private final String REQUEST_TABLE = "Requests";

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance("https://alex-atour-usatu-default-rtdb.europe-west1.firebasedatabase.app/");
    private DatabaseReference dbRef;

    public boolean checkUserAuth(){
        FirebaseUser currentUser = auth.getCurrentUser();
        return currentUser != null;//true - auth
    }

    public void login(String login, String password, IonOperationListener listener){
        auth.signInWithEmailAndPassword(login, password)
                .addOnCompleteListener(getExecutor(), task ->  {
                        if (task.isSuccessful()) {
                            user = auth.getCurrentUser();
                            listener.onSuccess();
                        } else {
                            listener.onFailed("Ошибка аутентификации");
                        }
                    }
                );
    }

    public void registration(User _user, String password, IonOperationListener listener) {

        auth.createUserWithEmailAndPassword(_user.getEmail(), password)
                .addOnCompleteListener(getExecutor(), task -> {
                    if (task.isSuccessful()) {
                        user = auth.getCurrentUser();

                        String userID = user.getUid();
                        Log.e("TAG", "userUID: "+userID);

                        _user.setId(userID);
                        //adding info to my "public" db
                        dbRef = database.getReference();
                        dbRef.child(USER_TABLE).child(userID).setValue(_user);

                        listener.onSuccess();
                    } else {
                        listener.onFailed("Ошибка регистрации");
                    }
                });
    }

    @Override
    public void sendMembershipRequest(MembershipRequest memReq, IonOperationListener listener) {
        dbRef = database.getReference().child(REQUEST_TABLE);
        String keyID = dbRef.push().getKey();

        user = auth.getCurrentUser();
        memReq.setId(keyID);
        memReq.setUserID(user.getUid());

        dbRef.child(keyID).setValue(memReq).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                listener.onSuccess();
            }else{
                listener.onFailed("Ошибка. Попробуйте еще раз");
            }
        });
    }
}
