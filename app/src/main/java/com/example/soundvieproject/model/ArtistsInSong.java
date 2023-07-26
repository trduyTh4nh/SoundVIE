package com.example.soundvieproject.model;

import org.bson.types.ObjectId;

import io.realm.RealmObject;

public class ArtistsInSong extends RealmObject {
    private ObjectId id;
    private ObjectId idUser;
    private ObjectId idSong;
    public ArtistsInSong(){

    }

    public ArtistsInSong(ObjectId id, ObjectId idUser, ObjectId idSong) {
        this.id = id;
        this.idUser = idUser;
        this.idSong = idSong;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getIdUser() {
        return idUser;
    }

    public void setIdUser(ObjectId idUser) {
        this.idUser = idUser;
    }

    public ObjectId getIdSong() {
        return idSong;
    }

    public void setIdSong(ObjectId idSong) {
        this.idSong = idSong;
    }
}
