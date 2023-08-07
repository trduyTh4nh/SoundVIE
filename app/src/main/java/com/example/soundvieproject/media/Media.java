package com.example.soundvieproject.media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Toast;

import com.example.soundvieproject.R;
import com.example.soundvieproject.model.Song;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import kotlinx.coroutines.internal.ArrayQueue;

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
    Queue<Song> q;
    Stack<Song> st;
    public void playMusicInPlaylist(ArrayList<Song> s, int index) throws IOException {
        for(int i = 0; i < s.size(); i++){
            if(i >= index){
                q.add(s.get(i));
            } else {
                st.push(s.get(i));
            }
        }
        Song sg = q.remove();
        st = new Stack<>();
        cur = sg;
        st.push(sg);
        playMusic(sg);
    }
    Song cur;
    public boolean playNextSong() throws IOException {
        if(q.isEmpty()){
            return false;
        }
        Song s = q.remove();
        cur = s;
        st.push(s);
        playMusic(s);
        return true;
    }
    public boolean playPrevSong() throws IOException {
        if(st.isEmpty()){
            return false;
        }
        q.add(cur);
        Song s = st.pop();
        playMusic(s);
        return true;
    }
    public void randomSong() throws IOException {
        Random r = new Random();
        ArrayList<Song> arr = new ArrayList<>(q);
        Song s = arr.get(r.nextInt(arr.size()));
        playMusic(s);
    }

}
