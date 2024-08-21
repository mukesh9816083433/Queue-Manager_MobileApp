package com.example.queuemanager;
public class Business {
    private String businessName;
    private String email;

    private String address;
    private int businessCode;
    private String phoneNumber;

    private int isQueueStarted;

    private int total;

    public Business() {
        // Required empty public constructor for Firebase Realtime Database
    }

    public Business(String businessName, String email, String address, int businessCode, String phoneNumber, int isQueueStarted, int total) {
        this.businessName = businessName;
        this.email = email;

        this.address = address;
        this.businessCode = businessCode;
        this.phoneNumber = phoneNumber;
        this.isQueueStarted=isQueueStarted;
        this.total=total;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(int businessCode) {
        this.businessCode = businessCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getIsQueueStarted() {
        return isQueueStarted;
    }

    public void setIsQueueStarted(int isQueueStarted) {
        this.isQueueStarted = isQueueStarted;
    }

    public int gettotal() {
        return total;
    }

    public void settotal(int total) {
        this.total = total;
    }


}
