package com.example.soundvieproject.DB;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import android.content.Context;
import android.content.Intent;
import android.util.Dumpable;
import android.util.Log;
import android.widget.Toast;

import com.example.soundvieproject.HomeActivity;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.UserTypes;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class Helper {
    public static final Helper INSTANCE = new Helper();
    private User user;
    private io.realm.mongodb.App a;
    private MongoClient client;
    private MongoDatabase db;
    private CodecRegistry pojoCodecRegistry;

    public Helper(){
        String Appid = "soundvie-zvqhd";
        a = new io.realm.mongodb.App(new AppConfiguration.Builder(Appid).build());
    }

    public User getUser() {
        return a.currentUser();
    }

    public void setUser(User user) {
        this.user = user;

    }

    public io.realm.mongodb.App getA() {
        return a;
    }

    public void setA(io.realm.mongodb.App a) {
        this.a = a;
        user = a.currentUser();
        client = user.getMongoClient("mongodb-atlas");
        db = client.getDatabase("SoundVIE");
        pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    }
    public void prepareDatabase(){
        MongoCollection<UserTypes> col = db.getCollection("UserTypes", UserTypes.class).withCodecRegistry(pojoCodecRegistry);
        col.insertMany(Arrays.asList(
                new UserTypes("Nghệ sĩ", "Người sáng tạo nội dung cho nền tảng", "ns"),
                new UserTypes("Người dùng", "Người sử dụng ứng dụng", "usr"),
                new UserTypes("Quản lý nhạc", "Quản lý các bài hát vi phạm", "qln"),
                new UserTypes("Quản lý người dùng", "Quản lý phân quyền và tài khoản người dùng", "qlnd"),
                new UserTypes("Kế toán", "Báo cáo các khoản tiền", "kt")
        )).getAsync(task -> {
            if(task.isSuccess()){
                Log.d("Success!", "successfully inserted userTypes");
            }else {
                Log.d("Failed.", "unsuccessfully inserted userTypes: Error " + task.getError());
            }
        });

    }
    public void insertItem(Song s){
        MongoCollection<Song> col = db.getCollection("Song", Song.class).withCodecRegistry(pojoCodecRegistry);
        col.insertOne(s).getAsync(task -> {
            if(task.isSuccess()){
                Log.d("Success", "Added.");
            } else {
                Log.d("Error", "Error: " + task.getError());
            }
        });
    }
    public void getSong(){
        MongoCollection<Song> col = db.getCollection("Song", Song.class).withCodecRegistry(pojoCodecRegistry);
        RealmResultTask<MongoCursor<Song>> task = col.find().iterator();
        AtomicReference<MongoCursor<Song>> c = null;
        task.getAsync(t -> {
            if (t.isSuccess()) {
                MongoCursor<Song> songs = t.get();
                c.set(songs);
                while (songs.hasNext()) {
                    Log.d("Success", "succesfully get doc: " + songs.next().getIdSong());
                }

            } else {
                Log.d("Failed", "failed");
            }
        });
    }
    public void register(String email, String password, String phone, String name, Context c){
        a.getEmailPassword().registerUserAsync(email, password, t -> {
            if(t.isSuccess()){
                Toast.makeText(c, "Đăng ký thành công.", Toast.LENGTH_SHORT).show();
                insertUser(email, password,phone, name, c, a.currentUser().getId());
            } else {
                Toast.makeText(c, "Lỗi bất định, vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                Log.e("Error", "Register error: " + t.getError().getErrorCode().toString());
            }
        });
    }
    public void prepare(){

    }
    public void insertUser(String email, String password, String phone, String name, Context c, String id){
        //prepare();
        Credentials creds = Credentials.emailPassword(email, password);
        a.loginAsync(creds, t -> {
            if(t.isSuccess()){
                user = a.currentUser();
                client = user.getMongoClient("mongodb-atlas");
                db = client.getDatabase("SoundVIE");
                pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));
                MongoCollection<com.example.soundvieproject.model.User> usr = db.getCollection("user", com.example.soundvieproject.model.User.class).withCodecRegistry(pojoCodecRegistry);
                Document query = new Document("idUser", id);
                Document newVal = new Document("$set", new Document("email", email));
                Document newPhone = new Document("$set", new Document("phone", phone));
                Document newName = new Document("$set", new Document("name", name));
                usr.updateOne(query, newVal).getAsync(tsk -> {
                    if(tsk.isSuccess()){
                        Log.d("Success", "succesfully inserted user");
                    } else {
                        Log.e("Error", "Error: " + tsk.getError().getErrorCode().toString());
                    }
                });
                usr.updateOne(query, newName).getAsync(tsk -> {
                    if(tsk.isSuccess()){
                        Log.d("Success", "succesfully inserted user");
                    } else {
                        Log.e("Error", "Error: " + tsk.getError().getErrorCode().toString());
                    }
                });
                usr.updateOne(query, newPhone).getAsync(tsk -> {
                    if(tsk.isSuccess()){
                        Log.d("Success", "succesfully inserted user");
                    } else {
                        Log.e("Error", "Error: " + tsk.getError().getErrorCode().toString());
                    }
                });
            } else {
                Log.e("Error", "Error: " + t.getError().getErrorCode().toString());
            }
        });
    }

    public MongoClient getClient() {
        return client;
    }

    public void setClient(MongoClient client) {
        this.client = client;
    }

    public MongoDatabase getDb() {
        return db;
    }

    public void setDb(MongoDatabase db) {
        this.db = db;
    }

    public CodecRegistry getPojoCodecRegistry() {
        return pojoCodecRegistry;
    }

    public void setPojoCodecRegistry(CodecRegistry pojoCodecRegistry) {
        this.pojoCodecRegistry = pojoCodecRegistry;
    }
}
