package com.example.soundvieproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.model.Premium;
import com.example.soundvieproject.model.Song;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.annotations.NonNull;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import kotlin.coroutines.CoroutineContext;

public class TestMongoActivity extends AppCompatActivity {
    EditText edtId, edtName, edtState, edtLyrics, edtArtist;
    Button btnAdd, btnShow;
    ListView lv;
    ArrayList<Song> songss;
    Helper instance;

    ListView lv1;
    Button btnUpload;



    EditText edtNamePre, edtDesPre, etdCate, edtPrice, edtTime;
    Button btnTest, btnShow1;

    @SuppressLint("MissingInflatedId")
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
        instance = Helper.INSTANCE;
        btnAdd.setOnClickListener(v -> postContent());
        btnShow.setOnClickListener(v -> getContent());
        lv = findViewById(R.id.lvMongo);
        songss = new ArrayList<Song>();

        lv1 = findViewById(R.id.lvMongo1);


        edtNamePre = findViewById(R.id.edtNamePre);
        edtDesPre = findViewById(R.id.edtDesPre);
        edtPrice = findViewById(R.id.edtPricePre);
        edtTime = findViewById(R.id.timePre);
        btnTest = findViewById(R.id.btnTest);
        btnShow1 = findViewById(R.id.btnshow1);

        btnShow1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPremiums();
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                String name = edtNamePre.getText().toString();
                String des = edtDesPre.getText().toString();
                String price = edtPrice.getText().toString();
                String time = edtTime.getText().toString();

                Premium premium = new Premium(name, des, Integer.parseInt(price), Integer.parseInt(time));
                instance.insertPremium(premium);


            }
        });


    }


    public void getPremiums() {
        MongoCollection<Premium> pres = instance.getDb().getCollection("Premium", Premium.class).withCodecRegistry(instance.getPojoCodecRegistry());
        RealmResultTask<MongoCursor<Premium>> task = pres.find().iterator();
        ArrayList<Premium> premiums = new ArrayList<>();
        task.getAsync(reuslt -> {
            if (reuslt.isSuccess()) {
                MongoCursor<Premium> pre = reuslt.get();
                while (pre.hasNext()) {
                    premiums.add(pre.next());
                }
                lv.setAdapter(new ArrayAdapter<Premium>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, premiums));
            } else {
                Toast.makeText(this, "ngu cút", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void postContent() {
        instance.prepareDatabase();
    }

    public void getContent() {
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