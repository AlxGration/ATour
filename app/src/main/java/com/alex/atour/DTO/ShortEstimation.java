package com.alex.atour.DTO;

public class ShortEstimation {
    private String memberID, refereeID;

    public ShortEstimation(){}

    public ShortEstimation(String memberID, String refereeID) {
        this.memberID = memberID;
        this.refereeID = refereeID;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getRefereeID() {
        return refereeID;
    }

    public void setRefereeID(String refereeID) {
        this.refereeID = refereeID;
    }
}
