package com.example.soundvieproject.model;

import org.bson.types.ObjectId;

import io.realm.RealmList;
import io.realm.RealmObject;

public class User extends RealmObject {
    private ObjectId _id;
    private String idUser;
    private String name;
    private String email;
    private String phone;
    private boolean state;
    private String moTa;
    private RealmList<User> followers;
    private UserTypes type;
    private String idLoai;
    public User(){}

    public User(ObjectId _id, String idUser, String name, String email, String phone, boolean state, String moTa, String idLoai) {
        this._id = _id;
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.state = state;
        this.moTa = moTa;
        this.idLoai = idLoai;
    }

    public User(ObjectId _id, String idUser, String name, String email, String phone, boolean state, String moTa, RealmList<User> followers, String idLoai) {
        this._id = _id;
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.state = state;
        this.moTa = moTa;
        this.followers = followers;
        this.idLoai = idLoai;
    }

    public User(ObjectId _id, String idUser, String name, String email, String phone, boolean state, String moTa, RealmList<User> followers, UserTypes type) {
        this._id = _id;
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.state = state;
        this.moTa = moTa;
        this.followers = followers;
        this.type = type;
    }

    public User(String idUser, String name, String email, String phone, boolean state, String moTa, UserTypes type) {
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.state = state;
        this.moTa = moTa;
        this.type = type;
        followers = new RealmList<>();
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }


    public RealmList<User> getFollowers() {
        return followers;
    }

    public void setFollowers(RealmList<User> followers) {
        this.followers = followers;
    }

    public UserTypes getType() {
        return type;
    }

    public void setType(UserTypes type) {
        this.type = type;
    }
}
