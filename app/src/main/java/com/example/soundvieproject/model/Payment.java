package com.example.soundvieproject.model;

import java.util.Date;

import io.realm.RealmObject;

public class Payment extends RealmObject {
    private Date NgayTT;
    private User user;
    private String paymentMethod;
    private Premium subscription;
    public Payment(){}

    public Payment(Date ngayTT, User user, String paymentMethod, Premium subscription) {
        NgayTT = ngayTT;
        this.user = user;
        this.paymentMethod = paymentMethod;
        this.subscription = subscription;
    }

    public Date getNgayTT() {
        return NgayTT;
    }

    public void setNgayTT(Date ngayTT) {
        NgayTT = ngayTT;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Premium getSubscription() {
        return subscription;
    }

    public void setSubscription(Premium subscription) {
        this.subscription = subscription;
    }
}
