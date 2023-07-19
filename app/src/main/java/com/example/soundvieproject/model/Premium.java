package com.example.soundvieproject.model;

import androidx.annotation.NonNull;

import java.util.Date;

import io.realm.RealmObject;

public class Premium extends RealmObject {
    private String tenLoai;
    private String moTa;
    private int gia;
    private int ThoiHan;
    public Premium(){}
    public Premium(String tenLoai, String moTa, int gia, int thoiHan) {
        this.tenLoai = tenLoai;
        this.moTa = moTa;
        this.gia = gia;
        ThoiHan = thoiHan;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getThoiHan() {
        return ThoiHan;
    }

    public void setThoiHan(int thoiHan) {
        ThoiHan = thoiHan;
    }

    @NonNull
    @Override
    public String toString() {
        return "Premium{" +
                "tenLoai='" + tenLoai + '\'' +
                ", moTa='" + moTa + '\'' +
                ", gia=" + gia +
                ", ThoiHan=" + ThoiHan +
                '}';
    }
}
