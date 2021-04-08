package com.alex.atour.DTO;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

public class RequestLinks  {
    @PropertyName("champInfoID")
    public String champInfoID;
    @PropertyName("userID")
    public String userID;

    public RequestLinks(){}

    public RequestLinks(String champInfoID, String userID) {
        this.champInfoID = champInfoID;
        this.userID = userID;
    }
}
