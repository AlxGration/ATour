package com.alex.atour.models;

import android.util.Log;

import com.alex.atour.DTO.Estimation;

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

    public static String isEstimationOK(IEstimation estim,
                                        String complexity,
                                        String novelty,
                                        String strategy,
                                        String tactics,
                                        String technique,
                                        String tension,
                                        String informativeness){
        float k;
        if (complexity.isEmpty() || (k = Float.parseFloat(complexity)) < 1 || k > 120){
            return "Неверное значение 'Сложность'";
        }else estim.setComplexity(k);
        if (novelty.isEmpty() || (k = Float.parseFloat(novelty)) < 0 || k > 24){
            return "Неверное значение 'Новизна'";
        }else estim.setNovelty(k);
        if (strategy.isEmpty() || (k = Float.parseFloat(strategy)) < -15 || k > 6){
            return "Неверное значение 'Стратегия'";
        }else estim.setStrategy(k);
        if (tactics.isEmpty() || (k = Float.parseFloat(tactics)) < -13 || k > 7){
            return "Неверное значение 'Тактика'";
        }else estim.setTactics(k);
        if (technique.isEmpty() || (k = Float.parseFloat(technique)) < -12 || k > 5){
            return "Неверное значение 'Техника'";
        }else estim.setTechnique(k);
        if (tension.isEmpty() || (k = Float.parseFloat(tension)) < -6 || k > 18){
            return "Неверное значение 'Напряженность'";
        }else estim.setTension(k);
        if (informativeness.isEmpty() || (k = Float.parseFloat(informativeness)) < -1 || k > 7){
            return "Неверное значение 'Информативность'";
        }else estim.setInformativeness(k);
        return null;
    }
}
