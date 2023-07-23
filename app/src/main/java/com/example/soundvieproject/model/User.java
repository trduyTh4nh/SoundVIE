package com.example.soundvieproject.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import io.realm.RealmList;
import io.realm.RealmObject;

public class User extends RealmObject {
    private ObjectId id;
    private String idUser;
    private String name;
    private String email;
    private String phone;
    private boolean state;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getIdLoai() {
        return idLoai;
    }

    public void setIdLoai(String idLoai) {
        this.idLoai = idLoai;
    }

    private String moTa;
    private RealmList<User> followers;
    private UserTypes type;
    private String idLoai;
    private String avatar;
    public User(){}

    public User(ObjectId id, String idUser, String name, String email, String phone, boolean state, String moTa, String idLoai, String avatar) {
        this.id = id;
        this.idUser = idUser;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.state = state;
        this.moTa = moTa;
        this.idLoai = idLoai;
        this.avatar = avatar;
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

    public ObjectId get_id() {
        return id;
    }

    public void set_id(ObjectId _id) {
        this.id = _id;
    }

    public String getIdLoai() {
        return idLoai;
    }

    public void setIdLoai(String idLoai) {
        this.idLoai = idLoai;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id=" + id +
                ", idUser='" + idUser + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", state=" + state +
                ", moTa='" + moTa + '\'' +
                ", followers=" + followers +
                ", type=" + type +
                ", idLoai='" + idLoai + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
