package com.alex.atour.DTO;

import androidx.annotation.NonNull;

public class TSMReport {
    private String id, category, managerFIO, company, members, date, shortPath;

    @NonNull
    @Override
    public String toString() {
        return category+" "+managerFIO+" "+company+" \n"+members+"\n "+date;
    }

    public TSMReport(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortPath() {
        return shortPath;
    }

    public void setShortPath(String shortPath) {
        this.shortPath = shortPath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getManagerFIO() {
        return managerFIO;
    }

    public void setManagerFIO(String managerFIO) {
        this.managerFIO = managerFIO;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
