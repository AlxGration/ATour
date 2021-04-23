package com.alex.atour.models;

public interface IEstimation {

    float getComplexity() ;
    void setComplexity(float complexity) ;

    float getNovelty() ;
    void setNovelty(float novelty) ;

    float getStrategy() ;
    void setStrategy(float strategy) ;

    float getTactics() ;
    void setTactics(float tactics) ;

    float getTechnique() ;
    void setTechnique(float technique) ;

    float getTension() ;
    void setTension(float tension);

    float getInformativeness() ;
    void setInformativeness(float informativeness);

    String getComment() ;
    void setComment(String comment) ;
}
