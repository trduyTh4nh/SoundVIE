package com.example.soundvieproject.model;

import androidx.annotation.NonNull;

import org.bson.types.ObjectId;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Song extends RealmObject {


    private String idSong;
    private ObjectId id;
    private String nameSong;
    private int imgCover;
    private String stateData;
    private String lyrics;
    private RealmList<User> artists;
    public Song(){}

    public Song(ObjectId id, String nameSong, int imgCover, String stateData, String lyrics, RealmList<User> artists) {
        this.id = id;
        this.nameSong = nameSong;
        this.imgCover = imgCover;
        this.stateData = stateData;
        this.lyrics = lyrics;
        this.artists = artists;
    }

    public Song(String idSong, String nameSong, int imgCover, String stateData, String lyrics, RealmList<User> artists) {
        this.idSong = idSong;
        this.nameSong = nameSong;
        this.imgCover = imgCover;
        this.stateData = stateData;
        this.lyrics = lyrics;
        this.artists = artists;
    }

    public String getIdSong() {
        return idSong;
    }

    public void setIdSong(String idSong) {
        this.idSong = idSong;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public int getImgCover() {
        return imgCover;
    }

    public void setImgCover(int imgCover) {
        this.imgCover = imgCover;
    }

    public String getStateData() {
        return stateData;
    }

    public void setStateData(String stateData) {
        this.stateData = stateData;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public RealmList<User> getArtists() {
        return artists;
    }

    public void setArtists(RealmList<User> artists) {
        this.artists = artists;
    }

    @NonNull
    @Override
    public String toString() {
        return "Song{" +
                "idSong='" + idSong + '\'' +
                ", id=" + id +
                ", nameSong='" + nameSong + '\'' +
                ", imgCover=" + imgCover +
                ", stateData='" + stateData + '\'' +
                ", lyrics='" + lyrics + '\'' +
                ", artists=" + artists +
                '}';
    }
}
