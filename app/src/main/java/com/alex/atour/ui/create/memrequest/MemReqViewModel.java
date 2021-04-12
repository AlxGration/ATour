package com.alex.atour.ui.create.memrequest;

import androidx.lifecycle.MutableLiveData;

import com.alex.atour.DTO.MembershipRequest;
import com.alex.atour.db.DBManager;
import com.alex.atour.models.BaseViewModel;

public class MemReqViewModel extends BaseViewModel {

    private final MutableLiveData<Boolean> isRequestSuccess;
    private final DBManager db;

    public MemReqViewModel(){
        isRequestSuccess = new MutableLiveData<>();
        db = DBManager.getInstance();
    }

    public MutableLiveData<Boolean> getIsRequestSuccess() { return isRequestSuccess; }

    public void sendMembershipRequest(MembershipRequest memReq){
        //data validation
        if (!(memReq.isTypeWalk() || memReq.isTypeSki() ||
                memReq.isTypeHike() || memReq.isTypeWater() ||
                memReq.isTypeSpeleo() || memReq.isTypeBike() ||
                memReq.isTypeAuto() || memReq.isTypeOther())
        ){
            setErrorMessage("Выберите хотя бы один вид");
            return;
        }
        if (memReq.getRole() == 1 && memReq.getCloudLink().isEmpty()){
            setErrorMessage("Пожалуйста, оставьте ссылку на файл");
            return;
        }


        setIsLoading(true);
        db.sendMembershipRequest(memReq, new DBManager.IRequestListener() {
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
