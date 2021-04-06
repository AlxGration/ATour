package com.alex.atour.ui.create.champ;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.ChampInfo;
import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.db.DBManager;
import com.alex.atour.models.BaseViewModel;
import com.alex.atour.models.StatusConverter;

import java.util.Calendar;

public class ChampViewModel extends BaseViewModel {

    private final MutableLiveData<Boolean> isRequestSuccess;
    private DBManager db;

    public ChampViewModel(){
        isRequestSuccess = new MutableLiveData<>();
        db = DBManager.getInstance();
    }

    public MutableLiveData<Boolean> getIsRequestSuccess() { return isRequestSuccess; }

    public void sendChampCreationRequest(
            String title,
            String dataFrom,
            String dataTo,
            String city,
            String status,
            ChampInfo champInfo
    ){
        //data validating
        Log.e("TAG", title+" "+dataFrom+" "+dataTo+" "+city);

        String[] splittedDate = dataFrom.split("\\.");
        Calendar date1 = Calendar.getInstance();
        date1.set(Calendar.YEAR, Integer.valueOf(splittedDate[2]));
        date1.set(Calendar.MONTH, Integer.valueOf(splittedDate[1]));
        date1.set(Calendar.DAY_OF_MONTH, Integer.valueOf(splittedDate[0]));

        dataFrom = splittedDate[2] +"."+splittedDate[1]+"."+splittedDate[0];

        splittedDate = dataTo.split("\\.");
        Calendar date2 = Calendar.getInstance();
        date2.set(Calendar.YEAR, Integer.valueOf(splittedDate[2]));
        date2.set(Calendar.MONTH, Integer.valueOf(splittedDate[1]));
        date2.set(Calendar.DAY_OF_MONTH, Integer.valueOf(splittedDate[0]));

        if (title.isEmpty() || date1.after(date2)) {
            setErrorMessage("Некорректные данные");
            return;
        }
        if (!(champInfo.isTypeWalk() || champInfo.isTypeSki() ||
                champInfo.isTypeHike() || champInfo.isTypeWater() ||
                champInfo.isTypeSpeleo() || champInfo.isTypeBike() ||
                champInfo.isTypeAuto() || champInfo.isTypeOther())
        ){
            setErrorMessage("Выберите хотя бы один вид");
            return;
        }

        //ROTATE DATA TO YYYY.MM.DD for easy comparing in DB
        dataTo = splittedDate[2] +"."+splittedDate[1]+"."+splittedDate[0];

        champInfo.setTitle(title);
        champInfo.setDataFrom(dataFrom);
        champInfo.setDataTo(dataTo);
        champInfo.setCity(city);
        champInfo.setStatus(StatusConverter.statusToInt(status));

        setIsLoading(true);
        db.createNewChampRequest(champInfo, new DBManager.IRequestListener() {
            @Override
            public void onSuccess() {
                isRequestSuccess.setValue(true);
                setIsLoading(false);
                setErrorMessage("");
            }

            @Override
            public void onFailed(String msg) {
                setIsLoading(false);
                setErrorMessage(msg);
                isRequestSuccess.setValue(false);
            }
        });
    }
}
