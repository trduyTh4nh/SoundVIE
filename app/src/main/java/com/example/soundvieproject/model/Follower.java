package com.example.soundvieproject.model;

import org.bson.types.ObjectId;

public class Follower {
    private ObjectId id;
    private String idUser;
    private String idUser2;

    public Follower(ObjectId id, String idUser, String idUser2) {
        this.id = id;
        this.idUser = idUser;
        this.idUser2 = idUser2;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(String idUser2) {
        this.idUser2 = idUser2;
    }
}
