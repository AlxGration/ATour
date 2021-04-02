package com.alex.atour.DTO;

public class MembershipRequest {
    String id, userID, comment, cloudLink, docsLink, chamID;
    int role;
    boolean typeWalk, typeSki, typeHike, typeWater, typeSpeleo, typeBike, typeAuto, typeOther;

    public MembershipRequest(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getChamID() {
        return chamID;
    }

    public void setChamID(String chamID) {
        this.chamID = chamID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCloudLink() {
        return cloudLink;
    }

    public void setCloudLink(String cloudLink) {
        this.cloudLink = cloudLink;
    }

    public String getDocsLink() {
        return docsLink;
    }

    public void setDocsLink(String docsLink) {
        this.docsLink = docsLink;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
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