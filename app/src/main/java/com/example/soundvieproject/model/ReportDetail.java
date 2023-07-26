package com.example.soundvieproject.model;

import org.bson.types.ObjectId;

import io.realm.RealmObject;

public class ReportDetail extends RealmObject {
    ObjectId id;
    String LyDo;
    String idUser;
    ObjectId idReport;

    public ReportDetail() {
    }

    public ReportDetail(ObjectId id, String lyDo, String idUser, ObjectId idReport) {
        this.id = id;
        LyDo = lyDo;
        this.idUser = idUser;
        this.idReport = idReport;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getLyDo() {
        return LyDo;
    }

    public void setLyDo(String lyDo) {
        LyDo = lyDo;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public ObjectId getIdReport() {
        return idReport;
    }

    public void setIdReport(ObjectId idReport) {
        this.idReport = idReport;
    }
}
