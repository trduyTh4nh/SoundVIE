package com.example.soundvieproject.DB;


import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import android.annotation.SuppressLint;
import android.content.Context;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.concurrent.TimeUnit;
import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

public class MongoDB {
    public MongoDB(Context c){
        Realm.init(c);
        String appId = "soundvie-spagu\n";
        App app = new App(new AppConfiguration.Builder(appId)
                .appName("SoundVIE")
                .requestTimeout(60, TimeUnit.SECONDS)
                .build());
        User user = app.currentUser();
        MongoClient client = (MongoClient) user.getMongoClient("mongodb-atlas");
        MongoDatabase db = client.getDatabase("Test");
        CodecRegistry pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
    }


}
