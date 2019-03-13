package com.example.hemapriya.foodapp;

public class Cart {
private  String pid,price,quantity,time,date;

    public Cart(String pid, String price, String quantity, String time, String date) {
        this.pid = pid;
        this.price = price;
        this.quantity = quantity;
        this.time = time;
        this.date = date;
    }

    public Cart() {
    }

    public Cart(String pid, String price) {
        this.pid = pid;
        this.price = price;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPrice() {
        return price;
    }

    public void setPprice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
