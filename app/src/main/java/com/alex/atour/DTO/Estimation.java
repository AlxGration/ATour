package com.alex.atour.DTO;

import com.google.android.gms.common.images.ImageManager;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Estimation extends RealmObject implements Comparable<Estimation>{

    @PrimaryKey
    private String id;
    private String champID, memberID, refereeID, memberFIO;
    private float complexity, novelty, strategy, tactics, technique, tension, informativeness ;
    private String comment;

    public Estimation(){}

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

    public String getMemberFIO() {
        return memberFIO;
    }

    public void setMemberFIO(String memberFIO) {
        this.memberFIO = memberFIO;
    }

    @Override
    public int compareTo(Estimation o) {
        int v = getMemberFIO().compareTo(o.getMemberFIO());
        if (v == 0){
            return getMemberID().compareTo(o.getMemberID());
        }
        return v;
    }
}
