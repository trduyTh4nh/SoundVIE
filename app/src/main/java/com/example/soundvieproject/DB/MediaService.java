package com.example.soundvieproject.DB;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.soundvieproject.media.Media;

import java.io.IOException;
import java.security.Provider;
import java.util.List;
import java.util.Map;

public class MediaService extends Service implements MediaPlayer.OnPreparedListener {
    private static final String ACTION_PLAY = "com.example.action.PLAY";
    MediaPlayer player = null;
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getAction().equals(ACTION_PLAY)){
            player = new MediaPlayer();
        }
        return super.onStartCommand(intent, flags, startId);
    }
    public void setDataSource(Uri uri) throws IOException {
        player.setDataSource(uri.toString());
    }
    public void playMedia(){
        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                player.start();
            }
        });
    }
    public void pause(){
        if(player.isPlaying())
            player.pause();
    }
    public void start(){
        player.start();
    }
    public void stop(){
        if(player.isPlaying())
            player.stop();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
