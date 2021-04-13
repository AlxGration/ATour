package com.alex.atour.models;

public enum MembershipState{
    WAIT,
    DENIED,
    ACCEPTED,
    DOCS_SUBMISSION,    //only for members, who is sending docs
    RESULTS
}