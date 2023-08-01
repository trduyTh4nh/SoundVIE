package com.example.soundvieproject;

import androidx.annotation.ChecksSdkIntAtLeast;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.adapter.PlaylistAdapter;
import com.example.soundvieproject.adapter.SearchResultAdapter;
import com.example.soundvieproject.adapter.SongAdapter;
import com.example.soundvieproject.adapter.SongInPlayListAdapter;
import com.example.soundvieproject.media.Media;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.SongInPlayList;
import com.example.soundvieproject.model.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.mongodb.RealmResultTask;
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

    Media media;
    int s;

    TextView song_name, artist, quantity;

    LinearLayout currentSong;

    ImageButton btnPause, btnResume;

    ImageView img_song;





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
        song_name = findViewById(R.id.song_name);
        artist = findViewById(R.id.artist);
        currentSong = findViewById(R.id.currentSong);
        btnPause = findViewById(R.id.btnPause);
        btnResume = findViewById(R.id.btnResume);
        img_song = findViewById(R.id.img_song);
        quantity = findViewById(R.id.quantity);

        media = Media.INSTANCE;


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


//        RealmResultTask<Long> test = helper.getCollectionCount(result -> {
//            if(result.isSuccess()){
//                MongoCursor<SongInPlayList> cursor =result.get();
//                while (cursor.hasNext()){
//                    Log.d("test var", "ccuc");
//                }
//            }
//        }, idPlCurrent);


        songinPlayLists = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new SongInPlayListAdapter(songinPlayLists, PlaylistActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

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
                                if(s == 20){
                                    Toast.makeText(this, "Danh sách bài hát đã đến giới hạn", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                else{
                                    songinPlayLists.add(new Song(songIPL.getId(), songIPL.getNameSong(), songIPL.getImgCover(), songIPL.getStateData(), songIPL.getLyrics(), songIPL.getArtists(), songIPL.getSong()));
                                    adapter.notifyItemInserted(songinPlayLists.size()  - 1);
                                }

                            }
                          /////
                            s = adapter.getItemCount();



                            quantity.setText(String.valueOf(s));



                            adapter.setItemClickListener(new SongInPlayListAdapter.OnItemsClickListener() {
                                @Override
                                public void OnItemClick(Song song) throws IOException {
                                    if(media.getPlayer().isPlaying()){
                                        media.getPlayer().stop();
                                        media.setPlayer(new MediaPlayer());
                                    }
                                    media.setContext(PlaylistActivity.this);
                                    media.playMusic(Uri.parse(song.getSong()));

                                    btnResume.setVisibility(View.GONE);
                                    btnPause.setVisibility(View.VISIBLE);
                                    currentSong.setVisibility(View.VISIBLE);

                                    song_name.setText(song.getNameSong());
                                    artist.setText(song.getArtist());

                                    StorageReference sto = storage.getReference("images/" + song.getImgCover());
                                    Glide.with(PlaylistActivity.this).load(sto).into(img_song);

                                    btnPause.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            btnPause.setVisibility(View.GONE);
                                            btnResume.setVisibility(View.VISIBLE);
                                            media.pause();
                                        }
                                    });
                                    btnResume.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            btnResume.setVisibility(View.GONE);
                                            btnPause.setVisibility(View.VISIBLE);
                                            media.start();
                                        }
                                    });
                                }
                            });
                        }
                        else {
                            Log.d("Thông báo", "Lỗi");
                        }
                    }, PlaylistSong.getIdSong().toString());

                }
//                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//                adapter = new SongInPlayListAdapter(songinPlayLists, PlaylistActivity.this);
//                recyclerView.setLayoutManager(layoutManager);
//                recyclerView.setAdapter(adapter);
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