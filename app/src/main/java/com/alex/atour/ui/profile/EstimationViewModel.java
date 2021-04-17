package com.alex.atour.ui.profile;


import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.Estimation;
import com.alex.atour.db.DBManager;
import com.alex.atour.models.BaseViewModel;

public class EstimationViewModel extends BaseViewModel {

    private DBManager db;
    private MutableLiveData<Boolean> isSuccess;

    public EstimationViewModel(){
        db = DBManager.getInstance();
        isSuccess = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getIsSuccess(){return isSuccess; }

    public void sendEstimation(
            String champID,
            String memberID,
            String complexity,
            String novelty,
            String strategy,
            String tactics,
            String technique,
            String tension,
            String informativeness,
            String comment){

        Estimation estim = new Estimation();

        //data validation
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

        setIsLoading(true);

        estim.setComment(comment);
        estim.setRefereeID(db.getPrefs().getUserID());
        estim.setMemberID(memberID);

        db.sendEstimation(champID, estim, new DBManager.IRequestListener() {
            @Override
            public void onSuccess() {
                setIsLoading(false);
                setErrorMessage("");
                isSuccess.setValue(true);
            }

            @Override
            public void onFailed(String msg) {
                setIsLoading(false);
                setErrorMessage(msg);
            }
        });
    }

}
