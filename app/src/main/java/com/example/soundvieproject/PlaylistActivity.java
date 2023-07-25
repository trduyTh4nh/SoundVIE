package com.example.soundvieproject;

import androidx.annotation.ChecksSdkIntAtLeast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.adapter.PlaylistAdapter;
import com.example.soundvieproject.adapter.SongInPlayListAdapter;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.SongInPlayList;
import com.example.soundvieproject.model.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class PlaylistActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    SongInPlayListAdapter adapter = null;

    ImageView imageView;
    TextView tvNamePL, tvNameUser, tvDes;
    Helper helper = Helper.INSTANCE;
    StorageReference storageReference;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ImageButton btnAddSong;
    ArrayList<Song> songinPlayLists;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        imageView = findViewById(R.id.imgPl);
        tvNamePL = findViewById(R.id.namePl);
        tvNameUser = findViewById(R.id.nameUserPl);
        tvDes = findViewById(R.id.titllePl);
        btnAddSong = findViewById(R.id.btnAddSong);
        recyclerView = findViewById(R.id.rv_songsPl);

        Bundle b = getIntent().getExtras();
        String imgResource = b.getString("image");
        String namePl = b.getString("namepl");
        String idUser = b.getString("iduser");
        String desc = b.getString("despl");
        String idPlCurrent = b.getString("idPl");

        helper.getUserByObjID(idUser, result -> {
            if(result.isSuccess()){
                User cursor = result.get();
                tvNamePL.setText(namePl);
                tvNameUser.setText(cursor.getName());
                tvDes.setText(desc);
                storageReference = storage.getReference("images/" + imgResource);
                Glide.with(this).load(storageReference).into(imageView);

            }
        });

        songinPlayLists = new ArrayList<>();

        helper.getSongInPlayList(result -> {
            if(result.isSuccess()){
                MongoCursor<SongInPlayList> cursor = result.get();
                while (cursor.hasNext()){
                    // đồng bộ lòng đồng bộ
                    SongInPlayList PlaylistSong = cursor.next();
                    Log.d("Checksource", PlaylistSong.toString());
                    helper.getSongByID(element ->{
                        if(element.isSuccess()){
                            MongoCursor<Song> songCur = element.get();
                            while (songCur.hasNext()){
                                Song songIPL = songCur.next();
                                Log.d("Check song in playlist", songIPL.toString());
                                songinPlayLists.add(new Song(songIPL.getId(), songIPL.getNameSong(), songIPL.getImgCover(), songIPL.getStateData(), songIPL.getLyrics(), songIPL.getArtists(), songIPL.getSong()));
                            }


                            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                            adapter = new SongInPlayListAdapter(songinPlayLists, PlaylistActivity.this);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);

                        }
                        else {
                            Log.d("Thông báo", "Lỗi");
                        }
                    }, PlaylistSong.getIdSong().toString());

                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                adapter = new SongInPlayListAdapter(songinPlayLists, PlaylistActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);
            }
            else
                Log.d("Thông báo", "Lỗi");
        }, idPlCurrent);

        btnAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PlaylistActivity.this, ActivityAddSongToPlayList.class);
                Bundle bundle = new Bundle();
                bundle.putString("idPlaylistCurrent", idPlCurrent);
                i.putExtras(bundle);
                startActivity(i);
            }
        });





    }
}