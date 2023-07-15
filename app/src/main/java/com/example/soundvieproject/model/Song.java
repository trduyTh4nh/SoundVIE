package com.example.soundvieproject.model;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Song {


    private String idSong;
    private ObjectId id;
    private String nameSong;
    private int imgCover;
    private String stateData;
    private String lyrics;
    private String artis;
    public Song(){}
    public Song(ObjectId idSong, String nameSong, int imgCover, String stateData, String lyrics, String artis) {
        this.id = idSong;
        this.nameSong = nameSong;
        this.imgCover = imgCover;
        this.stateData = stateData;
        this.lyrics = lyrics;
        this.artis = artis;
    }

    public Song(String idSong, String nameSong, int imgCover, String stateData, String lyrics, String artis) {
        this.idSong = idSong;
        this.nameSong = nameSong;
        this.imgCover = imgCover;
        this.stateData = stateData;
        this.lyrics = lyrics;
        this.artis = artis;
    }

    public ObjectId getIdSong() {
        return id;
    }

    public void setIdSong(ObjectId idSong) {
        this.id = idSong;
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

    public String getArtis() {
        return artis;
    }

    public void setArtis(String artis) {
        this.artis = artis;
    }

    @Override
    public String toString() {
        return "Song{" +
                "nameSong='" + nameSong + '\'' +
                ", imgCover=" + imgCover +
                ", stateData='" + stateData + '\'' +
                ", lyrics='" + lyrics + '\'' +
                ", artis='" + artis + '\'' +
                '}';
    }
}
