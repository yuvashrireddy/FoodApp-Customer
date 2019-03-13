package com.example.hemapriya.foodapp;

public class User {

    private String username;
    private String phoneNumber;
    private String address;
    public User() {
    }

    public String getAddress() {
        return address;
    }

    public User(String username, String phoneNumber, String address) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public User(String username, String phoneNumber) {
        this.username = username;
        this.phoneNumber = phoneNumber;
    }
}
