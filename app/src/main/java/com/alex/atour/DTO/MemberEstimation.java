package com.alex.atour.DTO;

import com.alex.atour.models.IEstimation;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MemberEstimation extends RealmObject implements Comparable<MemberEstimation>, Serializable, IEstimation {

    @PrimaryKey
    private String id;
    private String champID, memberID, refereeID, memberFIO;
    private float complexity, novelty, strategy, tactics, technique, tension, informativeness ;
    private String comment;

    private String requestID;
    private int role, state;
    private boolean typeWalk, typeSki, typeHike, typeWater, typeSpeleo, typeBike, typeAuto, typeOther;

    public MemberEstimation(){}
    public MemberEstimation(String champID, String refereeID, Member m){
        this.champID = champID; this.refereeID = refereeID; memberID = m.getUserID();
        id = champID+"_"+refereeID+"_"+memberID;
        memberFIO = m.getUserFIO(); comment = "";
        requestID = m.getRequestID();
        role = m.getRole(); state = m.getState();
        typeWalk = m.isTypeWalk(); typeSki = m.isTypeSki(); typeHike = m.isTypeHike(); typeWater = m.isTypeWater();
        typeSpeleo = m.isTypeSpeleo(); typeBike = m.isTypeBike(); typeAuto = m.isTypeAuto(); typeOther = m.isTypeOther();
        complexity = 0; novelty = 0; strategy = 0; tactics = 0; technique = 0; tension = 0; informativeness = 0;
    }
    public MemberEstimation(MemberEstimation m){
        champID = m.champID; refereeID = m.refereeID; memberID = m.memberID;
        id = m.id; memberFIO = m.memberFIO; comment =m.comment;
        requestID = m.requestID;
        role = m.role; state = m.state;
        typeWalk = m.typeWalk; typeSki = m.typeSki; typeHike = m.typeHike; typeWater = m.typeWater;
        typeSpeleo = m.typeSpeleo; typeBike = m.typeBike; typeAuto = m.typeAuto; typeOther = m.typeOther;
        complexity = m.complexity; novelty = m.novelty; strategy = m.strategy; tactics = m.tactics;
        technique = m.technique; tension = m.tension; informativeness = m.informativeness;
    }


    @Override
    public int compareTo(MemberEstimation o) {
        int v = memberFIO.compareTo(o.memberFIO);
        if (v == 0){
            return memberID.compareTo(o.memberID);
        }
        return v;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChampID() {
        return champID;
    }

    public void setChampID(String champID) {
        this.champID = champID;
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

    public String getMemberFIO() {
        return memberFIO;
    }

    public void setMemberFIO(String memberFIO) {
        this.memberFIO = memberFIO;
    }

    public float getComplexity() {
        return complexity;
    }

    public void setComplexity(float complexity) {
        this.complexity = complexity;
    }

    public float getNovelty() {
        return novelty;
    }

    public void setNovelty(float novelty) {
        this.novelty = novelty;
    }

    public float getStrategy() {
        return strategy;
    }

    public void setStrategy(float strategy) {
        this.strategy = strategy;
    }

    public float getTactics() {
        return tactics;
    }

    public void setTactics(float tactics) {
        this.tactics = tactics;
    }

    public float getTechnique() {
        return technique;
    }

    public void setTechnique(float technique) {
        this.technique = technique;
    }

    public float getTension() {
        return tension;
    }

    public void setTension(float tension) {
        this.tension = tension;
    }

    public float getInformativeness() {
        return informativeness;
    }

    public void setInformativeness(float informativeness) {
        this.informativeness = informativeness;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
