package com.alex.atour.ui.profile;


import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.Estimation;
import com.alex.atour.DTO.MemberEstimation;
import com.alex.atour.db.DBManager;
import com.alex.atour.db.RealmDB;
import com.alex.atour.models.BaseViewModel;

public class EstimationViewModel extends BaseViewModel {

    private final RealmDB realmDB;
    private final MutableLiveData<Boolean> isSuccess;

    public EstimationViewModel(){
        realmDB = DBManager.getInstance().getRealmDB() ;
        isSuccess = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getIsSuccess(){return isSuccess; }

    public void saveEstimation(
            MemberEstimation mEstim,
            String complexity,
            String novelty,
            String strategy,
            String tactics,
            String technique,
            String tension,
            String informativeness,
            String comment){

        //data validation
        MemberEstimation estim = new MemberEstimation(mEstim);
        float k = 0;
        if (complexity.isEmpty() || (k = Float.parseFloat(complexity)) < 1 || k > 120){
            setErrorMessage("Неверное значение 'Сложность'"); return;
        }else estim.setComplexity(k);
        if (novelty.isEmpty() || (k = Float.parseFloat(novelty)) < 0 || k > 24){
            setErrorMessage("Неверное значение 'Новизна'");return;
        }else estim.setNovelty(k);
        if (strategy.isEmpty() || (k = Float.parseFloat(strategy)) < -15 || k > 6){
            setErrorMessage("Неверное значение 'Стратегия'");return;
        }else estim.setStrategy(k);
        if (tactics.isEmpty() || (k = Float.parseFloat(tactics)) < -13 || k > 7){
            setErrorMessage("Неверное значение 'Тактика'");return;
        }else estim.setTactics(k);
        if (technique.isEmpty() || (k = Float.parseFloat(technique)) < -12 || k > 5){
            setErrorMessage("Неверное значение 'Техника'");return;
        }else estim.setTechnique(k);
        if (tension.isEmpty() || (k = Float.parseFloat(tension)) < -6 || k > 18){
            setErrorMessage("Неверное значение 'Напряженность'");return;
        }else estim.setTension(k);
        if (informativeness.isEmpty() || (k = Float.parseFloat(informativeness)) < -1 || k > 7){
            setErrorMessage("Неверное значение 'Информативность'");return;
        }else estim.setInformativeness(k);

        //
        estim.setComment(comment);

        //save to local db
        realmDB.writeMemberEstimation(estim);
        isSuccess.setValue(true);
    }

}
