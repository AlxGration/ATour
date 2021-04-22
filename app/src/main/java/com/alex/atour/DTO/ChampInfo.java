package com.alex.atour.DTO;

import java.io.Serializable;

public class ChampInfo implements Serializable {
    private String title, champID, dataFrom, dataTo, city, adminID;
    private int status;
    private boolean typeWalk, typeSki, typeHike, typeWater, typeSpeleo, typeBike, typeAuto, typeOther, isEnrollmentOpen;

    public ChampInfo(){}

    public boolean isEnrollmentOpen() {
        return isEnrollmentOpen;
    }

    public void setEnrollmentOpen(boolean enrollmentOpen) {
        this.isEnrollmentOpen = enrollmentOpen;
    }

    public String getAdminID() {
        return adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChampID() {
        return champID;
    }

    public void setChampID(String champID) {
        this.champID = champID;
    }

    public String getDataFrom() {
        return dataFrom;
    }

    public void setDataFrom(String dataFrom) {
        this.dataFrom = dataFrom;
    }

    public String getDataTo() {
        return dataTo;
    }

    public void setDataTo(String dataTo) {
        this.dataTo = dataTo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
