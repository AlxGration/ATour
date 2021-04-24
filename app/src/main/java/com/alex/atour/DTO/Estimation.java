package com.alex.atour.DTO;

import com.alex.atour.models.IEstimation;

public class Estimation implements Comparable<Estimation>, IEstimation {

    private String id;
    private String champID, memberID, refereeID, memberFIO;
    private double complexity, novelty, strategy, tactics, technique, tension, informativeness ;
    private String comment;

    public Estimation(){}
    public Estimation(MemberEstimation m){
        id = m.getId();
        champID = m.getChampID(); memberID = m.getMemberID(); refereeID = m.getRefereeID();
        memberFIO = m.getMemberFIO();
        complexity = m.getComplexity(); novelty = m.getNovelty(); strategy = m.getStrategy();
        tactics = m.getTactics(); technique = m.getTechnique(); tension = m.getTension();
        informativeness = m.getInformativeness(); comment = m.getComment();
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

    @Override
    public double getComplexity() {
        return complexity;
    }

    @Override
    public void setComplexity(double complexity) {
        this.complexity = complexity;
    }

    @Override
    public double getNovelty() {
        return novelty;
    }

    @Override
    public void setNovelty(double novelty) {
        this.novelty = novelty;
    }

    @Override
    public double getStrategy() {
        return strategy;
    }

    @Override
    public void setStrategy(double strategy) {
        this.strategy = strategy;
    }

    @Override
    public double getTactics() {
        return tactics;
    }

    @Override
    public void setTactics(double tactics) {
        this.tactics = tactics;
    }

    @Override
    public double getTechnique() {
        return technique;
    }

    @Override
    public void setTechnique(double technique) {
        this.technique = technique;
    }

    @Override
    public double getTension() {
        return tension;
    }

    @Override
    public void setTension(double tension) {
        this.tension = tension;
    }

    @Override
    public double getInformativeness() {
        return informativeness;
    }

    @Override
    public void setInformativeness(double informativeness) {
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
