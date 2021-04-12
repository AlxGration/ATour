package com.alex.atour.models;

public enum MembershipState{
    WAIT,
    DENIED,
    ACCEPTED,
    DOCS_SUBMISSION,    //only for members, who sent docs
    RESULTS
}