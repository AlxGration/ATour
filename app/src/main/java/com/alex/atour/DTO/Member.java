package com.alex.atour.DTO;

import java.io.Serializable;

public class Member implements Serializable {
    private String userID, userFIO, requestID;
    private int role, state;
    private boolean typeWalk, typeSki, typeHike, typeWater, typeSpeleo, typeBike, typeAuto, typeOther;

    public Member(){}

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserFIO() {
        return userFIO;
    }

    public void setUserFIO(String userFIO) {
        this.userFIO = userFIO;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isTypeWalk() {
        return typeWalk;
    }

    public void setTypeWalk(boolean typeWalk) {
        this.typeWalk = typeWalk;
    }

    public boolean isTypeSki() {
        return typeSki;
    }

    public void setTypeSki(boolean typeSki) {
        this.typeSki = typeSki;
    }

    public boolean isTypeHike() {
        return typeHike;
    }

    public void setTypeHike(boolean typeHike) {
        this.typeHike = typeHike;
    }

    public boolean isTypeWater() {
        return typeWater;
    }

    public void setTypeWater(boolean typeWater) {
        this.typeWater = typeWater;
    }

    public boolean isTypeSpeleo() {
        return typeSpeleo;
    }

    public void setTypeSpeleo(boolean typeSpeleo) {
        this.typeSpeleo = typeSpeleo;
    }

    public boolean isTypeBike() {
        return typeBike;
    }

    public void setTypeBike(boolean typeBike) {
        this.typeBike = typeBike;
    }

    public boolean isTypeAuto() {
        return typeAuto;
    }

    public void setTypeAuto(boolean typeAuto) {
        this.typeAuto = typeAuto;
    }

    public boolean isTypeOther() {
        return typeOther;
    }

    public void setTypeOther(boolean typeOther) {
        this.typeOther = typeOther;
    }
}
