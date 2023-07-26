package com.example.soundvieproject.model;

import org.bson.types.ObjectId;

import io.realm.RealmObject;

public class ArtistInSong extends RealmObject {
    ObjectId id;
    String IdUser;
    ObjectId idSong;

    public ArtistInSong(ObjectId id, String idUser, ObjectId idSong) {
        this.id = id;
        IdUser = idUser;
        this.idSong = idSong;
    }

    public ArtistInSong() {
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }

    public ObjectId getIdSong() {
        return idSong;
    }

    public void setIdSong(ObjectId idSong) {
        this.idSong = idSong;
    }
}
