package com.example.soundvieproject.model;

import org.bson.types.ObjectId;

import io.realm.Realm;
import io.realm.RealmObject;

public class Report extends RealmObject {
    ObjectId id;
    int Soluong;
    ObjectId idSong;
    int SoluongRP;

    public Report() {
    }

    public Report(ObjectId id, int soluong, ObjectId idSong, int soluongRP) {
        this.id = id;
        Soluong = soluong;
        this.idSong = idSong;
        SoluongRP = soluongRP;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getSoluong() {
        return Soluong;
    }

    public void setSoluong(int soluong) {
        Soluong = soluong;
    }

    public ObjectId getIdSong() {
        return idSong;
    }

    public void setIdSong(ObjectId idSong) {
        this.idSong = idSong;
    }

    public int getSoluongRP() {
        return SoluongRP;
    }

    public void setSoluongRP(int soluongRP) {
        SoluongRP = soluongRP;
    }
}
