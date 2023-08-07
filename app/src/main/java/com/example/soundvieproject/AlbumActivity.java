package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.adapter.SongInAlbumAdapter;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.SongInPlayList;
import com.google.firebase.storage.StorageReference;

import org.bson.types.ObjectId;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class AlbumActivity extends AppCompatActivity {
    ImageButton btnAddSong, btnEdit, btnBack;
    ImageView ivCover;
    ProgressBar loadingProgress;
    TextView tvname, tvDesc;
    ObjectId id;
    StorageHelper sto;
    ArrayList<Song> songs;
    Helper h = Helper.INSTANCE;
    RecyclerView rcvSong;
    SongInAlbumAdapter adap;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        btnAddSong = findViewById(R.id.addSongAlbum);
        btnEdit = findViewById(R.id.btnEditAlbum);
        ivCover = findViewById(R.id.ivCoverAlbum);
        tvname = findViewById(R.id.tvName);
        btnBack = findViewById(R.id.btnBack);
        tvDesc = findViewById(R.id.tvDesc);
        tvname.setText(b.getString("namepl"));
        tvDesc.setText(b.getString("despl"));
        id = new ObjectId(b.getString("idPl"));
        loadingProgress = findViewById(R.id.loadingProgress);
        sto = new StorageHelper(this);
        btnBack.setOnClickListener(v -> {
            finish();
        });
        StorageReference ref = sto.getStorage().getReference("images/"+b.getString("image"));
        Glide.with(this).load(ref).into(ivCover);
        songs = new ArrayList<>();
        adap = new SongInAlbumAdapter(songs, this);
        rcvSong = findViewById(R.id.rcvSongInAlbum);
        rcvSong.setAdapter(adap);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rcvSong.setLayoutManager(l);
        btnAddSong.setOnClickListener(v -> {
            Intent intent = new Intent(AlbumActivity.this, AddSongToAlbumActivity.class);
            Bundle bundleId = new Bundle();
            bundleId.putString("idPl", id.toString());
            intent.putExtras(bundleId);
            startActivity(intent);
        });
        h.getMusicAlbum(id, new App.Callback<MongoCursor<SongInPlayList>>() {
            @Override
            public void onResult(App.Result<MongoCursor<SongInPlayList>> result) {
                if(result.isSuccess()){
                    MongoCursor<SongInPlayList> cur = result.get();
                    while (cur.hasNext()){
                        SongInPlayList s = cur.next();
                        h.getSongInAlbum(s.getIdSong(), new App.Callback<Song>() {
                            @Override
                            public void onResult(App.Result<Song> result) {
                                if(result.isSuccess()){
                                    songs.add(result.get());
                                    adap.notifyItemInserted(songs.size() - 1);
                                }
                            }
                        });
                    }
                    loadingProgress.setVisibility(View.GONE);
                }
            }
        });
    }
}