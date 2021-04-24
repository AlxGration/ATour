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
    @Override
    public int compareTo(_Estimation o) {
        int v = memberFIO.compareTo(o.memberFIO);
        if (v == 0){
            return memberID.compareTo(o.memberID);
        }
        return v;
    }
}