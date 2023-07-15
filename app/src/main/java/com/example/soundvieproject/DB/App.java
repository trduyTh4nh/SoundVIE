package com.example.soundvieproject.DB;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import android.util.Log;

import com.example.soundvieproject.model.Song;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class App {
    public static final App INSTANCE = new App();
    private User user;
    private io.realm.mongodb.App a;
    private MongoClient client;
    private MongoDatabase db;
    private CodecRegistry pojoCodecRegistry;

    public App(){


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
}
