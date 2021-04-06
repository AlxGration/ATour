package com.alex.atour.models;

public class StatusConverter {
    public static int statusToInt(String status){
        switch (status){
            case "Чемпионат": return 1;
            case "Кубок": return 2;
            case "Перенство": return 3;
            case "Всероссийские": return 4;
            default: return 0;
        }
    }

    public static String statusToString(int status){
        switch (status){
            case 1: return "Чемпионат";
            case 2: return "Кубок";
            case 3: return "Перенство";
            case 4: return "Всероссийские";
            default: return "";
        }
    }
}
