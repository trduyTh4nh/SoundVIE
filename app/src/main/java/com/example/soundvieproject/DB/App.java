package com.example.soundvieproject.DB;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import android.util.Log;

import com.example.soundvieproject.model.Song;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.concurrent.atomic.AtomicReference;

import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

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
