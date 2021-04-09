package com.alex.atour.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.alex.atour.DTO.User;

public class PrefsDB {

    private enum KEYS{
        ID,
        FIO,
        CITY,
        EMAIL,
        PHONE
    }

    private String SETTINGS = "settings";
    private android.content.SharedPreferences sp;
    private SharedPreferences.Editor spEdit;

    public PrefsDB(Context context){
        sp = context.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
    }

    public String getUserID(){
        return sp.getString(KEYS.ID.toString(), "");
    }
    public void setUserID(String id){
        spEdit = sp.edit();

        spEdit.putString(KEYS.ID.toString(), id);
        spEdit.apply();
    }
/*
    public void setUserInfo(User u){

        spEdit = sp.edit();

        spEdit.putString(KEYS.ID.toString(), u.getId());
        spEdit.putString(KEYS.FIO.toString(), u.getFio());
        spEdit.putString(KEYS.EMAIL.toString(), u.getEmail());
        spEdit.putString(KEYS.CITY.toString(), u.getCity());
        spEdit.putString(KEYS.PHONE.toString(), u.getPhone());
        spEdit.apply();
    }




    public User getUserInfo(){
        User u = new User();
        u.setId(sp.getString(KEYS.ID.toString(), ""));
        u.setFio(sp.getString(KEYS.FIO.toString(), ""));
        u.setEmail(sp.getString(KEYS.EMAIL.toString(), ""));
        u.setCity(sp.getString(KEYS.CITY.toString(), ""));
        u.setPhone(sp.getString(KEYS.PHONE.toString(), ""));

        return u;
    }


 */

    public void clearUserData(){
        spEdit = sp.edit();

        spEdit.putString(KEYS.ID.toString(), "");
//        spEdit.putString(KEYS.FIO.toString(), "");
//        spEdit.putString(KEYS.EMAIL.toString(), "");
//        spEdit.putString(KEYS.CITY.toString(), "");
//        spEdit.putString(KEYS.PHONE.toString(), "");
        spEdit.apply();
    }
}
