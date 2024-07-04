package com.example.nt118_appbookstores.Models;

public class OrderModel {
    String address;
    String date;
    String delivery;
    String paymentStatus;
    String totalPrice;

    public OrderModel(String address, String date, String delivery, String paymentStatus, String totalPrice) {

        this.address = address;
        this.date = date;
        this.delivery = delivery;
        this.paymentStatus = paymentStatus;
        this.totalPrice = totalPrice;
    }

    public OrderModel(){

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}