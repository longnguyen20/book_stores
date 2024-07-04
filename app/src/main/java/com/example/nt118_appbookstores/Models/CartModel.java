package com.example.nt118_appbookstores.Models;

public class CartModel {
    String id;
    String name;

    String prices;

    String img_url;
    String book_quantity;


    public CartModel(String id, String name, String prices, String img_url, String book_quantity) {
        this.id = id;
        this.name = name;
        this.prices = prices;
        this.img_url = img_url;
        this.book_quantity = book_quantity;
    }

    public CartModel() {
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

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getBook_quantity() {
        return book_quantity;
    }

    public void setBook_quantity(String book_quantity) {
        this.book_quantity = book_quantity;
    }
}
