package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.soundvieproject.DB.App;
import com.example.soundvieproject.model.Song;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class TestMongoActivity extends AppCompatActivity {
    EditText edtId, edtName, edtState, edtLyrics, edtArtist;
    Button btnAdd, btnShow;
    ListView lv;
    ArrayList<Song> songss;
    App instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_mongo);
        edtId = findViewById(R.id.edtId);
        edtName = findViewById(R.id.edtName);
        edtState = findViewById(R.id.edtState);
        edtLyrics = findViewById(R.id.edtLyrics);
        edtArtist = findViewById(R.id.edtArtist);
        btnAdd = findViewById(R.id.btnAdd);
        btnShow = findViewById(R.id.btnShow);
        instance = App.INSTANCE;
        btnAdd.setOnClickListener(v -> postContent());
        btnShow.setOnClickListener(v -> getContent());
        lv = findViewById(R.id.lvMongo);
        songss = new ArrayList<Song>();
    }
    public void postContent(){
        User u = instance.getUser();
        Song s = new Song(new ObjectId(), edtName.getText().toString(), R.drawable.muoingannam,edtState.getText().toString(), edtLyrics.getText().toString(), edtArtist.getText().toString());
        instance.insertItem(s);
    }
    public void getContent(){
        MongoCollection<Song> col = instance.getDb().getCollection("Song", Song.class).withCodecRegistry(instance.getPojoCodecRegistry());
        RealmResultTask<MongoCursor<Song>> task = col.find().iterator();
        task.getAsync(t -> {
            if (t.isSuccess()) {
                MongoCursor<Song> songs = t.get();
                while (songs.hasNext()) {
                    songss.add(songs.next());
                }
                lv.setAdapter(new ArrayAdapter<Song>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, songss));
            } else {
                Log.d("Failed", "failed");
            }
        });
    }
}