package com.alex.atour.db;
import android.content.Context;

import com.alex.atour.DTO.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

public class FirebaseManager extends DBManager{

    private FirebaseAuth auth;
    private FirebaseUser user;

    public boolean checkUserAuth(){
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        return currentUser != null;//true - auth
    }

    public void login(String login, String password, DBManager.IonLoginListener loginListener){
        auth.signInWithEmailAndPassword(login, password)
                .addOnCompleteListener(getExecutor(), task ->  {
                        if (task.isSuccessful()) {
                            user = auth.getCurrentUser();
                            if (loginListener != null) loginListener.onSuccess();
                        } else {
                            if (loginListener != null)
                                loginListener.onFailed("Ошибка аутентификации");

                        }
                    }
                );
    }

    public void registration(User user, String password, IonRegistrationListener regListener){

    }
}
