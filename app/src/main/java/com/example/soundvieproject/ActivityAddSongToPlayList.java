package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.adapter.MusicInSearchAdapter;
import com.example.soundvieproject.adapter.SongAdapter;
import com.example.soundvieproject.media.Media;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.SongInPlayList;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class ActivityAddSongToPlayList extends AppCompatActivity {

    RecyclerView rvSongInSearch;
    MusicInSearchAdapter adapter;
    FirebaseStorage storage;
    ArrayList<Song> songsArr;
    Media media;
    TextView song_name, artist;
    LinearLayout currentSong;
    ImageButton btnPause, btnResume;
    ImageView img_song;
    Helper helper = Helper.INSTANCE;
    Button btnSave;
    Button btnCancel;

    MediaPlayer p;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song_to_play_list);
        media = Media.INSTANCE;
        p = new MediaPlayer();

        rvSongInSearch = findViewById(R.id.rvSongSreach);
        media.setContext(this);
        btnPause = findViewById(R.id.btnPause);
        btnResume = findViewById(R.id.btnResume);
        currentSong = findViewById(R.id.currentSong);
        img_song = findViewById(R.id.img_song);
        song_name = findViewById(R.id.song_name);
        artist = findViewById(R.id.artist);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Bundle b = getIntent().getExtras();

        Hup();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i = rvSongInSearch.getChildCount() - 1; i >= 0; i--){
                    v = rvSongInSearch.getChildAt(i);
                    CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkAddSong);
                    if(checkBox.isChecked()){
                        String idPlCr = b.getString("idPlaylistCurrent");
                        ObjectId idSong = songsArr.get(i).getId();
                        SongInPlayList songInPlayList = new SongInPlayList(new ObjectId(), new ObjectId(idPlCr), idSong);
                        helper.insertSongInPlayList(songInPlayList);
                    }
                }
                Toast.makeText(ActivityAddSongToPlayList.this, "Thêm thành công" , Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void Hup(){
        songsArr = new ArrayList<>();
        helper.getSong(result -> {
            if(result.isSuccess()){
                MongoCursor<Song> cursor = result.get();
                while (cursor.hasNext()){
                    Song song = cursor.next();
                    Log.d("Check", song.toString());
                    songsArr.add(new Song(song.getId(), song.getNameSong(), song.getImgCover(), song.getStateData(), song.getLyrics(), song.getArtists(), song.getSong()));
                }
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                adapter = new MusicInSearchAdapter(songsArr, this, firebaseStorage);
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
                rvSongInSearch.setLayoutManager(layoutManager);
                rvSongInSearch.setAdapter(adapter);


                adapter.setItemClickListener(new SongAdapter.OnItemsClickListener() {
                    @Override
                    public void OnItemClick(Song song) throws IOException {
                        if(media.getPlayer().isPlaying()){
                            media.getPlayer().stop();
                            media.setPlayer(new MediaPlayer());
                        }
                        media.setContext(ActivityAddSongToPlayList.this);
                        media.playMusic(Uri.parse(song.getSong()));

                        btnResume.setVisibility(View.GONE);
                        btnPause.setVisibility(View.VISIBLE);
                        currentSong.setVisibility(View.VISIBLE);
                        //img_song.setImageResource(song.getImgCover());
                        song_name.setText(song.getNameSong());
                        artist.setText(song.getArtist());

                        StorageReference sto = firebaseStorage.getReference("images/" + song.getImgCover());
                        Glide.with(ActivityAddSongToPlayList.this).load(sto).into(img_song);

                        btnPause.setOnClickListener(v -> {
                            btnPause.setVisibility(View.GONE);
                            btnResume.setVisibility(View.VISIBLE);
                            media.pause();
                        });
                        btnResume.setOnClickListener(v -> {
                            btnResume.setVisibility(View.GONE);
                            btnPause.setVisibility(View.VISIBLE);
                            media.start();
                        });
                    }
                });

            }
            else {
                Toast.makeText(this, "Không thể lấy bài hát", Toast.LENGTH_SHORT).show();
            }
        });
    }



}