package com.alex.atour.DTO;

import androidx.annotation.NonNull;

public class TSMReport {
    private String category, managerFIO, company, members, date;

    @NonNull
    @Override
    public String toString() {
        return category+" "+managerFIO+" "+company+" \n"+members+"\n "+date;
    }

    public TSMReport(){}

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
