package com.alex.atour.models;

public enum MembershipState{
    WAIT,               //ожидание одобрения заявки
    DENIED,             //заявка не принята
    ACCEPTED,           //заявки одобрена
    DOCS_SUBMITTED,     //only for referee, who is waiting docs
    RESULTS
}