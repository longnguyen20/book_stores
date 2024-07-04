package com.example.nt118_appbookstores.Models;

public class ProductModel {

    String id;
    String name;
    String author;

    String rates;

    String prices;

    String discount;

    String img_url;

    String description;

    String category;


    public ProductModel(String name, String author, String rates, String prices, String discount, String img_url, String description, String category) {
        this.name = name;
        this.author = author;
        this.rates = rates;
        this.prices = prices;
        this.discount = discount;
        this.img_url = img_url;
        this.description = description;
        this.category = category;
    }

    public ProductModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRates() {
        return rates;
    }

    public void setRates(String rates) {
        this.rates = rates;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
