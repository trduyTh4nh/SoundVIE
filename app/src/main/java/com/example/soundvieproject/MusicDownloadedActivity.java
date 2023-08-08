package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.soundvieproject.DB.SQLiteDB;
import com.example.soundvieproject.adapter.MusicDownloadedAdapter;
import com.example.soundvieproject.adapter.SongAdapter;
import com.example.soundvieproject.media.Media;
import com.example.soundvieproject.model.Song;

import java.io.IOException;
import java.util.ArrayList;

public class MusicDownloadedActivity extends AppCompatActivity {
    RecyclerView rvSongdownloaded;
    MusicDownloadedAdapter adapter;
    Media media;
    SQLiteDB db;
    TextView name_song;
    LinearLayout currentSong;
    ImageButton btnPause, btnResume;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(media.getPlayer().isPlaying())
            media.getPlayer().stop();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_downloaded);
        db = new SQLiteDB(this);
        rvSongdownloaded = findViewById(R.id.rvSongdownloaded);
        name_song = findViewById(R.id.song_name);
        btnPause = findViewById(R.id.btnPause);
        btnResume = findViewById(R.id.btnResume);
        currentSong = findViewById(R.id.currentSong);
        media = Media.INSTANCE;

        ArrayList<Song> arrayDownload = db.getAllSongDownloaded();

        adapter = new MusicDownloadedAdapter(arrayDownload, MusicDownloadedActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MusicDownloadedActivity.this, LinearLayoutManager.VERTICAL, false);
        rvSongdownloaded.setLayoutManager(layoutManager);
        rvSongdownloaded.setAdapter(adapter);



        adapter.setItemClickListener(new SongAdapter.OnItemsClickListener() {
            @Override
            public void OnItemClick(Song song, int index) throws IOException {
                if(media.getPlayer().isPlaying()){
                    media.getPlayer().stop();
                    media.setPlayer(new MediaPlayer());
                }
                media.setContext(MusicDownloadedActivity.this);
                media.playMusicLocal(song);

                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                currentSong.setVisibility(View.VISIBLE);

                name_song.setText(song.getNameSong());

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
                Log.d("Song clicked", song.toString());
            }
        });

    }
}