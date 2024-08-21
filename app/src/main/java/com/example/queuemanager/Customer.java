package com.example.queuemanager;

public class Customer {

    private  String name;
    private String email;
    private String number;
    private String address;
    private int hasRequest;

    private int in;




    public Customer(String name, String email, String number, String address, int hasRequest, int in) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.address = address;
        this.hasRequest=hasRequest;
        this.in=in;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int gethasRequest() {
        return hasRequest;
    }

    public void sethasRequest(int hasRequest) {
        this.hasRequest = hasRequest;
    }

    public int getin() {
        return in;
    }

    public void setin(int in) {
        this.in = in;
    }

}
