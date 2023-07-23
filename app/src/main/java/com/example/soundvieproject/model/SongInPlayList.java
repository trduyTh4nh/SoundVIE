package com.example.soundvieproject.model;

import org.bson.types.ObjectId;

import io.realm.RealmObject;

public class SongInPlayList extends RealmObject {
    private ObjectId _id;
    private ObjectId idPlaylist;
    private ObjectId idSong;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public ObjectId getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(ObjectId idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public ObjectId getIdSong() {
        return idSong;
    }

    public void setIdSong(ObjectId idSong) {
        this.idSong = idSong;
    }

    public SongInPlayList(ObjectId _id, ObjectId idPlaylist, ObjectId idSong) {
        this._id = _id;
        this.idPlaylist = idPlaylist;
        this.idSong = idSong;
    }

    public SongInPlayList(){

    }


}
