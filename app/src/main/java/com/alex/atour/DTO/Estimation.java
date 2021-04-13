package com.alex.atour.DTO;

public class Estimation {
    private int id, memberID, refereeID;
    private int complexity, novelty, strategy, tactics, technique, tension, informativeness ;
    private String comment;

    public Estimation(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getRefereeID() {
        return refereeID;
    }

    public void setRefereeID(int refereeID) {
        this.refereeID = refereeID;
    }

    public int getComplexity() {
        return complexity;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    public int getNovelty() {
        return novelty;
    }

    public void setNovelty(int novelty) {
        this.novelty = novelty;
    }

    public int getStrategy() {
        return strategy;
    }

    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }

    public int getTactics() {
        return tactics;
    }

    public void setTactics(int tactics) {
        this.tactics = tactics;
    }

    public int getTechnique() {
        return technique;
    }

    public void setTechnique(int technique) {
        this.technique = technique;
    }

    public int getTension() {
        return tension;
    }

    public void setTension(int tension) {
        this.tension = tension;
    }

    public int getInformativeness() {
        return informativeness;
    }

    public void setInformativeness(int informativeness) {
        this.informativeness = informativeness;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
