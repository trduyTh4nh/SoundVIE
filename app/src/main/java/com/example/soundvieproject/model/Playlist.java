package com.example.soundvieproject.model;

import io.realm.RealmList;

public class Playlist {
    private String name;
    private String cover;
    private int coverHard;
    private User user;
    private String description;
    private RealmList<Song> songs;

    public int getCoverHard() {
        return coverHard;
    }

    public Playlist(String name, int coverHard, String description) {
        this.name = name;
        this.coverHard = coverHard;
        this.description = description;
    }

    public Playlist(String name, int coverHard, User user, RealmList<Song> songs) {
        this.name = name;
        this.coverHard = coverHard;
        this.user = user;
        this.songs = songs;
    }

    public void setCoverHard(int coverHard) {
        this.coverHard = coverHard;
    }

    public Playlist(String name, String cover, User user) {
        this.name = name;
        this.cover = cover;
        this.user = user;
        songs = new RealmList<>();
    }

    public Playlist(String name, String cover, int coverHard, User user, String description, RealmList<Song> songs) {
        this.name = name;
        this.cover = cover;
        this.coverHard = coverHard;
        this.user = user;
        this.description = description;
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RealmList<Song> getSongs() {
        return songs;
    }

    public void setSongs(RealmList<Song> songs) {
        this.songs = songs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
