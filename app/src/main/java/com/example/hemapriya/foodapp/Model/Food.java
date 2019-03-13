package com.example.hemapriya.foodapp.Model;

public class Food {
    private String dish, price;

    public Food() {
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Food(String dish, String price) {
        this.dish = dish;
        this.price = price;
    }
}