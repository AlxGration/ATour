package com.alex.atour.models;

import java.util.regex.Pattern;

public class ValueFormatter {

    public static boolean isLoginFormat(String s){
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        return  pattern.matcher(s).matches();
    }

    public static boolean isPasswordFormat(String s){
        return s.length() > 5;
    }


    public static boolean isFIOFormat(String s){
        return !s.isEmpty()  &&
                s.split(" ").length > 2 &&
                s.length() > 5 ;
    }

    public static boolean isPhoneFormat(String s){
        String regex = s.replaceAll("\\D+","");
        return  regex.length()==10;
    }

}
