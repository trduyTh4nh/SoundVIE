package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.soundvieproject.DB.App;
import com.example.soundvieproject.model.Song;

import org.bson.types.ObjectId;

import io.realm.mongodb.User;

public class TestMongoActivity extends AppCompatActivity {
    EditText edtId, edtName, edtState, edtLyrics, edtArtist;
    Button btnAdd, btnShow;
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
    }
    public void postContent(){
        User u = instance.getUser();
        Song s = new Song(new ObjectId(), edtName.getText().toString(), R.drawable.muoingannam,edtState.getText().toString(), edtLyrics.getText().toString(), edtArtist.getText().toString());
        instance.insertItem(s);
    }
}