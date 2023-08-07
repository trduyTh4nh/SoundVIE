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
    private String imgCover;
    private String stateData;
    private String lyrics;
    private String song;
    private String artis;
    private RealmList<User> artists;
    private String artist;
    private int luotnghe;
    public Song(){}

    public Song(ObjectId id, String nameSong, String imgCover, String stateData, String lyrics, String song, String artist, int luotnghe) {
        this.id = id;
        this.nameSong = nameSong;
        this.imgCover = imgCover;
        this.stateData = stateData;
        this.lyrics = lyrics;
        this.song = song;
        this.artist = artist;
        this.luotnghe = luotnghe;
    }

    //có lượt nghe
    public Song(ObjectId id, String nameSong, String imgCover, String stateData, String lyrics, String song, int luotnghe) {
        this.id = id;
        this.nameSong = nameSong;
        this.imgCover = imgCover;
        this.stateData = stateData;
        this.lyrics = lyrics;
        this.song = song;
        this.luotnghe = luotnghe;
    }
    //ko có lượt nghe
    public Song(ObjectId id, String nameSong, String imgCover, String stateData, String lyrics, RealmList<User> artists, String song) {
        this.id = id;
        this.nameSong = nameSong;
        this.imgCover = imgCover;
        this.stateData = stateData;
        this.lyrics = lyrics;
        this.artists = artists;
        this.song = song;
        this.luotnghe = 0;
    }

    public Song(String idSong, String nameSong, String imgCover, String stateData, String lyrics, RealmList<User> artists) {
        this.idSong = idSong;
        this.nameSong = nameSong;
        this.imgCover = imgCover;
        this.stateData = stateData;
        this.lyrics = lyrics;
        this.artists = artists;
    }
    public Song(String idSong, String nameSong, String imgCover, String stateData, String lyrics, String artists) {
        this.idSong = idSong;
        this.nameSong = nameSong;
        this.imgCover = imgCover;
        this.stateData = stateData;
        this.lyrics = lyrics;
        this.artis = artists;
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

    public String getImgCover() {
        return imgCover;
    }

    public void setImgCover(String imgCover) {
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
                ", artists=" + artists + + '\'' +
                ", song='" + song + '\'' +
                '}';
    }

    public String getSong() {
        return song;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }



    public void setSong(String song) {
        this.song = song;
    }

    public String getArtis() {
        return artis;
    }

    public void setArtis(String artis) {
        this.artis = artis;
    }

    public int getLuotnghe() {
        return luotnghe;
    }

    public void setLuotnghe(int luotnghe) {
        this.luotnghe = luotnghe;
    }
}