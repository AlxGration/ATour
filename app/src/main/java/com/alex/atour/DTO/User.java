package com.alex.atour.DTO;

public class User {

    public static final String MyID = "MY";

    private String id;
    private String fio;
    private String city;
    private String phone;
    private String email;

    public User(){}

    public User(String id, String fio, String city, String phone, String email) {
        this.id = id;
        this.fio = fio;
        this.city = city;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
