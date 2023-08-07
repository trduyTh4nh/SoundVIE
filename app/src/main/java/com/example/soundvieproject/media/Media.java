package com.example.soundvieproject.media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.User;

import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class Media {

    public static Media INSTANCE = new Media();
    private MediaPlayer player;
    private Context context;

    Helper helper = Helper.INSTANCE;

    private int currentIndex = 0;

    public Song getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(Song currentSong) {
        this.currentSong = currentSong;
    }

    private Song currentSong;

    private Song nextSong;

    private Song prevSong;

    public Song getPrevSong() {
        return prevSong;
    }

    public void setPrevSong(Song prevSong) {
        this.prevSong = prevSong;
    }

    public Song getNextSong() {
        return nextSong;
    }

    public void setNextSong(Song nextSong) {
        this.nextSong = nextSong;
    }

    public Media() {
    }

    public Media(Context context) {
        this.context = context;
    }

    public void pause() {
        if (player.isPlaying()) {
            player.pause();
        }
    }

    public void start() {
        if (!player.isPlaying()) {
            player.start();
        }
    }

    public void stop() {
        if (player.isPlaying()) {
            player.stop();
        }
    }

    public ArrayList<Song> getAllSongCurrent() {
        ArrayList<Song> arrSong = new ArrayList<>();

        helper.getAllSong(new App.Callback<MongoCursor<Song>>() {
            @Override
            public void onResult(App.Result<MongoCursor<Song>> result) {
                if (result.isSuccess()) {
                    MongoCursor<Song> cursor = result.get();
                    while (cursor.hasNext()) {
                        Song song = cursor.next();
                        //         public Song(ObjectId id, String nameSong, String imgCover, String stateData, String lyrics, RealmList< User > artists, String song)
                        arrSong.add(song);

                    }
                }
            }
        });
        return arrSong;
    }

    private String millisecondToString(int millisecond) {
        long sec = millisecond / 1000;
        long min = sec / 60;
        sec = sec % 60;
        return min + " : " + sec;
    }

    private void doRewind() {
        int currentPosition = player.getCurrentPosition();
        int duration = player.getDuration();

        int SUBTRACTIME = 5000;
        if (currentPosition - SUBTRACTIME > 0) {
            player.seekTo(currentPosition - SUBTRACTIME);
        }
    }

    private void doFastForward() {
        int currentPosition = player.getCurrentPosition();
        int duration = player.getDuration();

        int ADD_TIME = 5000;
        if (currentPosition + ADD_TIME < duration) {
            player.seekTo(currentPosition + ADD_TIME);
        }
    }

    public void setPos(int msec) {
        player.seekTo(msec);
    }

    public void setContext(Context c) {
        context = c;
        player = new MediaPlayer();
    }

    public void playMusic(Song s) throws IOException {
        currentSong = s;
        Uri uri = Uri.parse(s.getSong());
        //   Toast.makeText(context, "select resource" + uri, Toast.LENGTH_SHORT).show();
        player.setDataSource(context, uri);
        player.prepareAsync();
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                player.start();
            }
        });
    }
//    Queue<Song> queue;
//    public void playMusicInPlaylist(ArrayList<Song> s){
//        queue = new ArrayDeque<>(s);
//        playNextSong(s);
//    }
//    public void playNextSong(ArrayList<Song> songs) {
//        Song s = queue.remove();
//        currentIndex = player.getCurrentPosition();
//        currentIndex = (currentIndex + 1) % songs.size();
//        playSong(currentIndex, songs);
//    }
//
//    public void playBackSong(ArrayList<Song> songs) {
//
//        currentIndex = player.getCurrentPosition();
//        currentIndex = (currentIndex - 1 + songs.size()) % songs.size();
//        playSong(currentIndex, songs);
//    }
//
//    public void playRandomSong(ArrayList<Song> songs) {
//        Random random = new Random();
//        currentIndex = player.getCurrentPosition();
//        currentIndex = random.nextInt(songs.size());
//        playSong(currentIndex, songs);
//    }
//    private void playSong(int index, ArrayList<Song> songArrayList) {
//        try {
//            // Set the data source to the selected song
//            player.reset();
//            String songPath = String.valueOf(songArrayList.get(index).getSong());
//            AssetFileDescriptor afd = context.getAssets().openFd(songPath);
//            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//            afd.close();
//
//            // Prepare and start playing the song
//            player.prepare();
//            player.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    Queue<Song> q;
    Stack<Song> st;

    public void playMusicInPlaylist(ArrayList<Song> s, int index) throws IOException {
        if(player.isPlaying()){
            player.stop();
        }
        player = new MediaPlayer();

        q = new ArrayDeque<>();
        st = new Stack<>();
        for(int i = 0; i < s.size(); i++){
            if(i >= index){
                q.add(s.get(i));
            } else {
                st.push(s.get(i));
            }
        }
        Song sg = q.remove();
        printQueue();
        st = new Stack<>();
        currentSong = sg;
        st.push(sg);
        playMusic(sg);

       // setNextSong(s.get(index+1));

    }

    public Song printQueue(){
        ArrayList<Song> s = new ArrayList<>(q);
        for(Song ss : s){
            Log.d("queue", ss.getNameSong());
        }
        return s.get(0);
    }
    public boolean playNextSong() throws IOException {
        stop();
        player = new MediaPlayer();
        if(q != null){
            if(q.isEmpty()){
                return false;
            }
            st.push(currentSong);
            Log.d("next", "Song " + currentSong.getNameSong() + " added to stack");

            Song s = q.remove();
            Song s1 = printQueue();
            setNextSong(s1);
//            ArrayList<Song> song = new ArrayList<>(st);
//            setNextSong(song.get(1));

            Log.d("song", s.toString());
            currentSong = s;
            Log.d("next", "Song " + s.getNameSong() + " removed from queue");
            playMusic(s);
            return true;
        }

        return false;
    }
    public boolean playPrevSong() throws IOException {
        stop();
        player = new MediaPlayer();
        if(st != null){
            if(st.isEmpty()){
                return false;
            }
            ArrayList<Song> tmp = new ArrayList<>(q);
            tmp.add(0, currentSong);
            q = new ArrayDeque<>(tmp);
            Log.d("prev", "Song " + currentSong.getNameSong() + " added to queue");
            Song s = st.pop();

            setPrevSong(q.peek());

            currentSong = s;
            Log.d("prev", "Song " + currentSong.getNameSong() + " removed to queue");
            playMusic(s);
            return true;
        }
        return false;
    }
    public void randomSong() throws IOException {
        stop();
        player = new MediaPlayer();
        Random r = new Random();
        ArrayList<Song> arr = new ArrayList<>(q);
        Song s = arr.get(r.nextInt(arr.size()));
        playMusic(s);
    }




    public void playSongMusicInplaylist(ArrayList<Song> s){

    }


    public MediaPlayer getPlayer() {
        return player;
    }

    public void setPlayer(MediaPlayer player) {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
                player.release();
            }
        }

        this.player = player;
    }

    public int CurrentTimeOfSong(MediaPlayer mediaPlayer) {
        int currentTime = 0;
        currentTime = mediaPlayer.getDuration();

        return currentTime;
    }
}
