package com.alex.atour.models;

import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        double k;
        BigDecimal bd;

        if (complexity.isEmpty() || (k = Double.parseDouble(complexity)) < 1 || k > 120){
            return "Неверное значение 'Сложность'";
        }else {
            bd = new BigDecimal(complexity).setScale(2, RoundingMode.HALF_DOWN);
            estim.setComplexity(bd.doubleValue());
            Log.e("TAG", "Сложность "+bd.doubleValue());
        }
        if (novelty.isEmpty() || (k = Double.parseDouble(novelty)) < 0 || k > 24){
            return "Неверное значение 'Новизна'";
        }else {
            bd = new BigDecimal(novelty).setScale(2, RoundingMode.HALF_DOWN);
            estim.setNovelty(bd.doubleValue());
            Log.e("TAG", "Новизна "+bd.doubleValue());
        }
        if (strategy.isEmpty() || (k = Double.parseDouble(strategy)) < -15 || k > 6){
            return "Неверное значение 'Стратегия'";
        }else {
            bd = new BigDecimal(strategy).setScale(2, RoundingMode.HALF_DOWN);
            estim.setStrategy(bd.doubleValue());
            Log.e("TAG", "Стратегия "+bd.doubleValue());
        }
        if (tactics.isEmpty() || (k = Double.parseDouble(tactics)) < -13 || k > 7){
            return "Неверное значение 'Тактика'";
        }else {
            bd = new BigDecimal(tactics).setScale(2, RoundingMode.HALF_DOWN);
            estim.setTactics(bd.doubleValue());
            Log.e("TAG", "Тактика "+bd.doubleValue());
        }
        if (technique.isEmpty() || (k = Double.parseDouble(technique)) < -12 || k > 5){
            return "Неверное значение 'Техника'";
        }else {
            bd = new BigDecimal(technique).setScale(2, RoundingMode.HALF_DOWN);
            estim.setTechnique(bd.doubleValue());
            Log.e("TAG", "Техника "+bd.doubleValue());
        }
        if (tension.isEmpty() || (k = Double.parseDouble(tension)) < -6 || k > 18){
            return "Неверное значение 'Напряженность'";
        }else {
            bd = new BigDecimal(tension).setScale(2, RoundingMode.HALF_DOWN);
            estim.setTension(bd.doubleValue());
            Log.e("TAG", "Напряженность "+bd.doubleValue());
        }
        if (informativeness.isEmpty() || (k = Double.parseDouble(informativeness)) < -1 || k > 7){
            return "Неверное значение 'Информативность'";
        }else {
            bd = new BigDecimal(informativeness).setScale(2, RoundingMode.HALF_DOWN);
            estim.setInformativeness(bd.doubleValue());
            Log.e("TAG", "Информативность "+bd.doubleValue());
        }
        return null;
    }
}
