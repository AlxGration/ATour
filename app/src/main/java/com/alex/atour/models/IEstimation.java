package com.alex.atour.models;

public interface IEstimation {

    double getComplexity() ;
    void setComplexity(double complexity) ;

    double getNovelty() ;
    void setNovelty(double novelty) ;

    double getStrategy() ;
    void setStrategy(double strategy) ;

    double getTactics() ;
    void setTactics(double tactics) ;

    double getTechnique() ;
    void setTechnique(double technique) ;

    double getTension() ;
    void setTension(double tension);

    double getInformativeness() ;
    void setInformativeness(double informativeness);

    String getComment() ;
    void setComment(String comment) ;
}
