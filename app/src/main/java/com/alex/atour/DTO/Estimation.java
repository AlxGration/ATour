package com.alex.atour.DTO;

public class Estimation {
    private String id, memberID, refereeID;
    private float complexity, novelty, strategy, tactics, technique, tension, informativeness ;
    private String comment;

    public Estimation(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
