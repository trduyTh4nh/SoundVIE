package com.example.soundvieproject.model;

import org.bson.types.ObjectId;

import io.realm.RealmList;

public class Playlist {
    private ObjectId id;
    private String Name;
    private String Image;
    private String idUser;

    public Playlist(ObjectId id, String name, String Image, String IdUser) {
        this.id = id;
        this.Name = name;
        this.Image = Image;
        this.idUser = IdUser;
    }




    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }



}
