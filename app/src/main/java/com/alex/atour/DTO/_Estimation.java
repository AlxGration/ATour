package com.alex.atour.DTO;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class _Estimation extends RealmObject implements Comparable<_Estimation>{
    @PrimaryKey
    private String id;
    private String champID, memberID, refereeID, memberFIO;
    private double complexity, novelty, strategy, tactics, technique, tension, informativeness ;
    private String comment;

    public _Estimation(){}
    public _Estimation(Estimation e){
        id = e.getId(); champID = e.getChampID(); memberID = e.getMemberID();
        refereeID = e.getRefereeID(); memberFIO = e.getMemberFIO(); comment = e.getComment();
        complexity = e.getComplexity(); novelty = e.getNovelty(); strategy = e.getStrategy();
        tactics = e.getTactics(); technique = e.getTechnique(); tension = e.getTension();
        informativeness = e.getInformativeness();
    }
    public Estimation getEstimationObject(){
        Estimation e = new Estimation();
        e.setId(id); e.setChampID(champID); e.setMemberID(memberID);
        e.setRefereeID(refereeID); e.setMemberFIO(memberFIO);
        e.setComplexity(complexity); e.setNovelty(novelty); e.setStrategy(strategy);
        e.setTactics(tactics); e.setTechnique(technique); e.setTension(tension);
        e.setInformativeness(informativeness); e.setComment(comment);
        return e;
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

    public double getComplexity() {
        return complexity;
    }

    public void setComplexity(double complexity) {
        this.complexity = complexity;
    }

    public double getNovelty() {
        return novelty;
    }

    public void setNovelty(double novelty) {
        this.novelty = novelty;
    }

    public double getStrategy() {
        return strategy;
    }

    public void setStrategy(double strategy) {
        this.strategy = strategy;
    }

    public double getTactics() {
        return tactics;
    }

    public void setTactics(double tactics) {
        this.tactics = tactics;
    }

    public double getTechnique() {
        return technique;
    }

    public void setTechnique(double technique) {
        this.technique = technique;
    }

    public double getTension() {
        return tension;
    }

    public void setTension(double tension) {
        this.tension = tension;
    }

    public double getInformativeness() {
        return informativeness;
    }

    public void setInformativeness(double informativeness) {
        this.informativeness = informativeness;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int compareTo(_Estimation o) {
        int v = memberFIO.compareTo(o.memberFIO);
        if (v == 0){
            return memberID.compareTo(o.memberID);
        }
        return v;
    }
}