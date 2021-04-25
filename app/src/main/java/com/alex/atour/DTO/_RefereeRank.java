package com.alex.atour.DTO;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class _RefereeRank extends RealmObject {

    @PrimaryKey
    private String refereeInfo;

    public _RefereeRank(){}
    public _RefereeRank(RefereeRank r){
        refereeInfo = r.getRefereeInfo();
    }

    public String getRefereeInfo() {
        return refereeInfo;
    }

    public void setRefereeInfo(String refereeInfo) {
        this.refereeInfo = refereeInfo;
    }
}
