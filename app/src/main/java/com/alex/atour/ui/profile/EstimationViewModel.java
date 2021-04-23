package com.alex.atour.ui.profile;


import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.Estimation;
import com.alex.atour.DTO.MemberEstimation;
import com.alex.atour.db.DBManager;
import com.alex.atour.db.RealmDB;
import com.alex.atour.models.BaseViewModel;
import com.alex.atour.models.ValueFormatter;

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
        String isAnyErr = ValueFormatter.isEstimationOK(estim, complexity,
                novelty, strategy, tactics,
                technique, tension, informativeness
        );
        if (isAnyErr != null){ setErrorMessage(isAnyErr); return; }

        //
        estim.setComment(comment);

        //save to local db
        realmDB.writeMemberEstimation(estim);
        isSuccess.setValue(true);
    }

}
