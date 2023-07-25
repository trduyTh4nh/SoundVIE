package com.example.soundvieproject.model;

import org.bson.types.ObjectId;

import io.realm.RealmList;

public class Playlist {
    private ObjectId id;
    private String Name;
    private String Image;
    private String idUser;
    private String Des;

    public Playlist(ObjectId id, String name, String image, String idUser, String des) {
        this.id = id;
        Name = name;
        Image = image;
        this.idUser = idUser;
        Des = des;
    }

    public Playlist(){}

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }
}
