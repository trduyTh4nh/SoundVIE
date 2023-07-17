package com.example.soundvieproject.media;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.soundvieproject.R;

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
        player = MediaPlayer.create(context, R.raw.music);
    }
}
