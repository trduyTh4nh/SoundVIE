package com.example.soundvieproject.media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import com.example.soundvieproject.R;
import com.example.soundvieproject.model.Song;

import java.io.IOException;

public class Media {

    public static Media INSTANCE = new Media();
    private MediaPlayer player;
    private Context context;
    public Media(){

    }
    public void pause(){
        if(player.isPlaying()){
            player.pause();
        }
    }
    public void start(){
        if(!player.isPlaying()){
            player.start();
        }
    }
    public void stop(){
        if(player.isPlaying()){
            player.stop();
        }
    }
    public void setPos(int msec){
        player.seekTo(msec);
    }
    public void setContext(Context c){
        context = c;
        player = new MediaPlayer();
    }
    public void playMusic(Song s) throws IOException {
        Uri uri = Uri.parse(s.getSong());
        Toast.makeText(context, "select resource" + uri, Toast.LENGTH_SHORT).show();
        player.setDataSource(context, uri);
        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                player.start();
            }
        });
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MediaPlayer player) {
        if(player != null){
            if(player.isPlaying()){
                player.stop();
                player.release();
            }
        }

        this.player = player;
    }
}
