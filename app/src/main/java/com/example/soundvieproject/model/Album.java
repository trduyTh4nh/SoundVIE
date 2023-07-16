package com.example.soundvieproject.model;

import io.realm.RealmList;

public class Album {
    private String name;
    private String cover;
    private User user;
    private RealmList<Song> songs;

    public Album(String name, String cover, User user) {
        this.name = name;
        this.cover = cover;
        this.user = user;
        songs = new RealmList<>();
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
}
