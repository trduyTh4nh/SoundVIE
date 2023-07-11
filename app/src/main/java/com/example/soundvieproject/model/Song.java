package com.example.soundvieproject.model;

public class Song {


    private String idSong;
    private String nameSong;
    private int imgCover;
    private String stateData;
    private String lyrics;
    private String artis;

    public Song(String idSong, String nameSong, int imgCover, String stateData, String lyrics, String artis) {
        this.idSong = idSong;
        this.nameSong = nameSong;
        this.imgCover = imgCover;
        this.stateData = stateData;
        this.lyrics = lyrics;
        this.artis = artis;
    }

    public String getIdSong() {
        return idSong;
    }

    public void setIdSong(String idSong) {
        this.idSong = idSong;
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
}
