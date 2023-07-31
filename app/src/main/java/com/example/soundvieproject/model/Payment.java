package com.example.soundvieproject.model;

import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Stream;

import io.realm.RealmObject;

public class Payment extends RealmObject {
    private Date ngayTT;
    private String idUser;
    private ObjectId idGoi;
    private String phuongThucTT;
    public Payment(){}
    public Payment(Date ngayTT, String idUser, ObjectId idGoi, String phuongThucTT) {
        this.ngayTT = ngayTT;
        this.idUser = idUser;
        this.idGoi = idGoi;
        this.phuongThucTT = phuongThucTT;
    }

    public Date getNgayTT() {
        return ngayTT;
    }

    public void setNgayTT(Date ngayTT) {
        this.ngayTT = ngayTT;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public ObjectId getIdGoi() {
        return idGoi;
    }

    public void setIdGoi(ObjectId idGoi) {
        this.idGoi = idGoi;
    }

    public String getPhuongThucTT() {
        return phuongThucTT;
    }

    public void setPhuongThucTT(String phuongThucTT) {
        this.phuongThucTT = phuongThucTT;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "ngayTT=" + ngayTT +
                ", idUser='" + idUser + '\'' +
                ", idGoi=" + idGoi +
                ", phuongThucTT='" + phuongThucTT + '\'' +
                '}';
    }
}
