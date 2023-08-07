package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.adapter.AddSongAdapter;
import com.example.soundvieproject.model.ArtistInSong;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.SongInPlayList;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Objects;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.mongo.options.InsertManyResult;

public class AddSongToAlbumActivity extends AppCompatActivity {
    String idPl;
    AddSongAdapter adap;
    RecyclerView rcvSong;
    boolean[] checkList;
    Button btnSave, btnCance;
    ArrayList<Song> songs;
    ProgressBar loading;
    Helper h = Helper.INSTANCE;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song_to_album);
        btnSave = findViewById(R.id.btnSave);
        btnCance = findViewById(R.id.btnCancel);
        btnCance.setOnClickListener(v -> {
            finish();
        });
        Intent i = getIntent();
        loading = findViewById(R.id.loadingProgress);
        loading.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.GONE);
        Bundle b = i.getExtras();
        idPl = b.getString("idPl");
        rcvSong = findViewById(R.id.rcvPickSong);
        songs = new ArrayList<>();
        adap = new AddSongAdapter(this, songs);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rcvSong.setAdapter(adap);
        rcvSong.setLayoutManager(l);
        h.getSongByArtist(h.getUser().getId(), new App.Callback<MongoCursor<ArtistInSong>>() {
            @Override
            public void onResult(App.Result<MongoCursor<ArtistInSong>> result) {
                MongoCursor<ArtistInSong> cur = result.get();
                while (cur.hasNext()){
                    ArtistInSong s = cur.next();
                    h.getSongByIdSong(s.getIdSong(), new App.Callback<Song>() {
                        @Override
                        public void onResult(App.Result<Song> result) {
                            Song s = result.get();
                            if(s != null){
                                songs.add(s);
                                checkList = new boolean[songs.size()];
                                adap.setCheckList(checkList);
                                adap.notifyItemInserted(songs.size() - 1);
                            }
                            loading.setVisibility(View.GONE);
                            btnSave.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });
        ArrayList<SongInPlayList> songsPl = new ArrayList<>();
        btnSave.setOnClickListener(v -> {
            loading.setVisibility(View.VISIBLE);
            btnSave.setVisibility(View.GONE);
            btnCance.setVisibility(View.GONE);
            for(int j = 0; j < checkList.length; j++){
                if(checkList[j]){
                    SongInPlayList tmpS = new SongInPlayList(new ObjectId(), new ObjectId(idPl), songs.get(j).getId());
                    songsPl.add(tmpS);
                    ObjectId idS = songs.get(j).getId();
                    Song tmp = songs.get(j);
                    h.getMusicAlbum(new ObjectId(idPl), new App.Callback<MongoCursor<SongInPlayList>>() {
                        @Override
                        public void onResult(App.Result<MongoCursor<SongInPlayList>> result) {
                            if(result.isSuccess()){
                                MongoCursor<SongInPlayList> res = result.get();
                                while (res.hasNext()){
                                    SongInPlayList song = res.next();
                                    Log.d("Song", idS.toString() + " : " + song.getIdSong().toString());
                                    if(idS.toString().equals(song.getIdSong().toString())){
                                        Toast.makeText(AddSongToAlbumActivity.this, "Trùng", Toast.LENGTH_SHORT).show();
                                        loading.setVisibility(View.GONE);
                                        btnSave.setVisibility(View.VISIBLE);
                                        btnCance.setVisibility(View.VISIBLE);
                                        return;
                                    }
                                }
                                h.insertSongSingularInAlbum(tmpS, t -> {
                                    if(t.isSuccess()){
                                        loading.setVisibility(View.GONE);
                                        Toast.makeText(AddSongToAlbumActivity.this, "Xong!", Toast.LENGTH_SHORT).show();
                                        btnCance.setVisibility(View.VISIBLE);
                                        btnCance.setText("Xong");
                                    }
                                });
                            }
                        }
                    });
                }
            }
            if(songsPl.size() == 0){
                Toast.makeText(this, "Vui lòng chọn 1 bài hát!", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                return;
            }


        });
    }
}