package com.leafymart.Model;

public class PlantModel {
    private String name;
    private double price;
    private double rating;
    private int sold;
    private String image_url;


    public PlantModel(int id, String name, double price, double rating, int sold, String image_url) {
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.sold = sold;
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getRating() {
        return rating;
    }

    public int getSold() {
        return sold;
    }

    public String getImageUrl() {
        return image_url;
    }
}
