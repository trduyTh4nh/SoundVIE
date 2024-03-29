package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ThumbnailImageViewTarget;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.media.Media;
import com.example.soundvieproject.media.MediaPlayerUtils;
import com.example.soundvieproject.model.ArtistInSong;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.bson.types.ObjectId;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class PlayingMusicActivity extends AppCompatActivity {

    ImageButton btnleft, btnright, btnpause, bntplay, btnrandom, btnlike;
    SeekBar seekBar;

    Helper helper = Helper.INSTANCE;

    TextView tvTimeCurrent, tvTimeTotal;
    private final Handler threadHandler = new Handler();
    MediaPlayer mediaPlayer;

    Media media;

    ImageView imgCover, imgCoverNext;
    TextView nameSong, nameSongNext;
    TextView nameArtist, nameAritstNext;


    FirebaseStorage storage;

    ImageButton btnBack;
   // StorageReference reference;
    LinearLayout layout;



    private Handler handler = new Handler();
    private Runnable runnable;

    @SuppressLint("SetTextI18n")
    private void refreshData(Song songPlaying){

        songPlaying = media.getCurrentSong();
        nameSong.setText(songPlaying.getNameSong());
        StorageReference ref = storage.getReference("images/"+songPlaying.getImgCover());
        Glide.with(this).load(ref).into(imgCover);
        helper.getArtitsbyIDSongPlaying(result1 -> {
            if(result1.isSuccess()){
                ArtistInSong artistOfSongPlaying = result1.get();
                if(artistOfSongPlaying == null){
                    nameArtist.setText("null");
                }
                else {
                    Log.d("Artist of song: ", artistOfSongPlaying.toString());
                    helper.getUserByObjID(artistOfSongPlaying.getIdUser(), new App.Callback<User>() {
                        @Override
                        public void onResult(App.Result<User> kq) {
                            User artist = kq.get();
                            nameArtist.setText(artist.getName());
                        }
                    });
                }
            }


        }, String.valueOf(songPlaying.getId()));

        media.getPlayer().setOnPreparedListener(mp -> {
            int totalDuration = mp.getDuration();
            tvTimeTotal.setText(formatTime(totalDuration));
            seekBar.setMax(totalDuration);
            int currentPosition = 0;
            media.getPlayer().seekTo(currentPosition);
            seekBar.setProgress(currentPosition);
            media.start();
            updateSeekBarProgress();
        });

    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_music);

        layout = findViewById(R.id.lnNextSong);
        btnleft = findViewById(R.id.btnleft);
        btnright = findViewById(R.id.btnRight);
        bntplay = findViewById(R.id.btnplay);
        btnpause = findViewById(R.id.btnPause);
        btnrandom = findViewById(R.id.btnRandom);
        btnlike = findViewById(R.id.btnlike);
        seekBar = findViewById(R.id.seekbar);
        tvTimeCurrent = findViewById(R.id.tvTimeCurrent);
        tvTimeTotal = findViewById(R.id.tvTimeTotal);
        imgCover = findViewById(R.id.imgCover);
        nameSong = findViewById(R.id.nameSong);
        nameArtist = findViewById(R.id.nameArtist);

        nameSongNext = findViewById(R.id.nameSongNext);
        nameAritstNext = findViewById(R.id.artistSongNext);
        imgCoverNext = findViewById(R.id.imgSongNext);

        Bundle data = getIntent().getExtras();
        String idSongStransfered = data.getString("IdSongClicked");
        String ImgCoverCur = data.getString("ImgCover");
        btnBack = findViewById(R.id.btnBack);
        storage = FirebaseStorage.getInstance();

        media = Media.INSTANCE;




       // Log.d("Song is playing: ", songPlaying.toString());
        StorageReference reference = storage.getReference("images/"+ ImgCoverCur);
        Glide.with(PlayingMusicActivity.this).load(reference).into(imgCover);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        helper.getSongByID(new App.Callback<MongoCursor<Song>>() {
            @Override
            public void onResult(App.Result<MongoCursor<Song>> result) {
                if (result.isSuccess()) {
                    media = Media.INSTANCE;
                    MongoCursor<Song> songMongoCursor = result.get();
                    Song songPlaying = songMongoCursor.next();

                    btnpause.setVisibility(View.VISIBLE);
                    bntplay.setVisibility(View.GONE);

                    nameSong.setText(songPlaying.getNameSong());
                    helper.getArtitsbyIDSongPlaying(result1 -> {
                       if(result1.isSuccess()){
                           ArtistInSong artistOfSongPlaying = result1.get();
                           if(artistOfSongPlaying == null){
                               nameArtist.setText("null");
                           }
                           else {
                               Log.d("Artist of song: ", artistOfSongPlaying.toString());
                               helper.getUserByObjID(artistOfSongPlaying.getIdUser(), new App.Callback<User>() {
                                   @Override
                                   public void onResult(App.Result<User> kq) {
                                       User artist = kq.get();
                                       nameArtist.setText(artist.getName());
                                   }
                               });
                           }
                       }


                    }, String.valueOf(songPlaying.getId()));


                    if(media.getPlayer().isPlaying()){
                        media.getPlayer().stop();
                    }

                    media.setContext(PlayingMusicActivity.this);
                    try {
                        media.playMusic(songPlaying);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }



                    btnpause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            media.pause();
                            btnpause.setVisibility(View.GONE);
                            bntplay.setVisibility(View.VISIBLE);
                        }
                    });

                    bntplay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            media.start();
                            btnpause.setVisibility(View.VISIBLE);
                            bntplay.setVisibility(View.GONE);
                        }
                    });


                    media.getPlayer().setOnPreparedListener(mp -> {
                        int totalDuration = mp.getDuration();
                        tvTimeTotal.setText(formatTime(totalDuration));
                        seekBar.setMax(totalDuration);
                        int currentPosition = data.getInt("currentPoint");
                        media.getPlayer().seekTo(currentPosition);
                        seekBar.setProgress(currentPosition);

                        media.start();

                        updateSeekBarProgress();
                        media.getPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                if(!mp.isPlaying()){
                                    try {
                                        if(!media.playNextSong()){
                                            btnright.setEnabled(true);
                                        }
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }

                                    Song songNext = media.getNextSong();
                                    if(songNext != null){
                                        nameSongNext.setText(songNext.getNameSong());
                                        helper.getArtitsbyIDSongPlaying(result1 -> {
                                            if(result1.isSuccess()){
                                                ArtistInSong artistOfSongPlaying = result1.get();
                                                if(artistOfSongPlaying == null){
                                                    nameAritstNext.setText("null");
                                                }
                                                else {
                                                    Log.d("Artist of song: ", artistOfSongPlaying.toString());
                                                    helper.getUserByObjID(artistOfSongPlaying.getIdUser(), new App.Callback<User>() {
                                                        @Override
                                                        public void onResult(App.Result<User> kq) {
                                                            User artist = kq.get();
                                                            nameAritstNext.setText(artist.getName());
                                                        }
                                                    });
                                                }
                                            }

                                        }, String.valueOf(songNext.getId()));

                                        StorageReference reference = storage.getReference("images/"+ songNext.getImgCover());
                                        Glide.with(PlayingMusicActivity.this).load(reference).into(imgCoverNext);
                                        //media.setCurrentSong(songNext);
                                    }

                                    Song s = media.getCurrentSong();
                                    refreshData(s);
                                }
                                else {

                                }
                            }
                        });

                    });

                    btnleft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {
                                if(!media.playPrevSong()){
                                   btnleft.setEnabled(true);
                                }
                                Song songPrev = media.getPrevSong();

                                if(songPrev != null){
                                    nameSongNext.setText(songPrev.getNameSong());

                                    helper.getArtitsbyIDSongPlaying(result1 -> {
                                        if(result1.isSuccess()){
                                            ArtistInSong artistOfSongPlaying = result1.get();
                                            if(artistOfSongPlaying == null){
                                                nameAritstNext.setText("null");
                                            }
                                            else {
                                                Log.d("Artist of song: ", artistOfSongPlaying.toString());
                                                helper.getUserByObjID(artistOfSongPlaying.getIdUser(), new App.Callback<User>() {
                                                    @Override
                                                    public void onResult(App.Result<User> kq) {
                                                        User artist = kq.get();
                                                        nameAritstNext.setText(artist.getName());
                                                    }
                                                });
                                            }
                                        }

                                    }, String.valueOf(songPrev.getId()));

                                    StorageReference reference = storage.getReference("images/"+ songPrev.getImgCover());
                                    Glide.with(PlayingMusicActivity.this).load(reference).into(imgCoverNext);

//                                    media.setCurrentSong(songPrev);
                                }
                                Song currentSong  = media.getCurrentSong();
                                refreshData(currentSong);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

                    btnright.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if(!media.playNextSong()){
                                  btnright.setEnabled(true);
                                }

                                Song songNext = media.getNextSong();
                                if(songNext != null){
                                    nameSongNext.setText(songNext.getNameSong());
                                    helper.getArtitsbyIDSongPlaying(result1 -> {
                                        if(result1.isSuccess()){
                                            ArtistInSong artistOfSongPlaying = result1.get();
                                            if(artistOfSongPlaying == null){
                                                nameAritstNext.setText("null");
                                            }
                                            else {
                                                Log.d("Artist of song: ", artistOfSongPlaying.toString());
                                                helper.getUserByObjID(artistOfSongPlaying.getIdUser(), new App.Callback<User>() {
                                                    @Override
                                                    public void onResult(App.Result<User> kq) {
                                                        User artist = kq.get();
                                                        nameAritstNext.setText(artist.getName());
                                                    }
                                                });
                                            }
                                        }

                                    }, String.valueOf(songNext.getId()));

                                    StorageReference reference = storage.getReference("images/"+ songNext.getImgCover());
                                    Glide.with(PlayingMusicActivity.this).load(reference).into(imgCoverNext);
                                    //media.setCurrentSong(songNext);
                                }

                                Song s = media.getCurrentSong();
                                refreshData(s);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    });


                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if(!media.playNextSong()){
                                    btnright.setEnabled(true);
                                }

                                Song songNext = media.getNextSong();
                                if(songNext != null){
                                    nameSongNext.setText(songNext.getNameSong());
                                    helper.getArtitsbyIDSongPlaying(result1 -> {
                                        if(result1.isSuccess()){
                                            ArtistInSong artistOfSongPlaying = result1.get();
                                            if(artistOfSongPlaying == null){
                                                nameAritstNext.setText("null");
                                            }
                                            else {
                                                Log.d("Artist of song: ", artistOfSongPlaying.toString());
                                                helper.getUserByObjID(artistOfSongPlaying.getIdUser(), new App.Callback<User>() {
                                                    @Override
                                                    public void onResult(App.Result<User> kq) {
                                                        User artist = kq.get();
                                                        nameAritstNext.setText(artist.getName());
                                                    }
                                                });
                                            }
                                        }

                                    }, String.valueOf(songNext.getId()));

                                    StorageReference reference = storage.getReference("images/"+ songNext.getImgCover());
                                    Glide.with(PlayingMusicActivity.this).load(reference).into(imgCoverNext);
                                    //media.setCurrentSong(songNext);
                                }

                                Song s = media.getCurrentSong();
                                refreshData(s);

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });


                    btnrandom.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("Clicked","Random");
                            int currentPosition = data.getInt("currentPoint");
                            if(media.getPlayer().getCurrentPosition() == currentPosition)
                            {
                                try {
                                    media.randomSong();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });




                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if(fromUser){
                                media.getPlayer().seekTo(progress);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                                handler.removeCallbacks(runnable);
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            updateSeekBarProgress();
                        }
                    });

                }
            }
        }, idSongStransfered);
    }
    private void updateSeekBarProgress() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (media.getPlayer() != null) {
                    int currentPos = media.getPlayer().getCurrentPosition();
                    seekBar.setProgress(currentPos);
                    tvTimeCurrent.setText(formatTime(currentPos));
                    handler.postDelayed(this, 100); // Update every 100 milliseconds
                }
            }
        };
        handler.postDelayed(runnable, 100);
    }

    private String formatTime(int millis) {
        int seconds = (millis / 1000) % 60;
        int minutes = (millis / (1000 * 60)) % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //    private String millisecondToString(int millisecond) {
//        long sec = millisecond / 1000;
//        long min = sec / 60;
//        sec = sec % 60;
//        return min + " : " + sec;
//    }
//
//    private class UpdateSeekBarThread implements Runnable {
//
//        @Override
//        public void run() {
//            int currentPosition = mediaPlayer.getCurrentPosition();
//            String currentPositionStr = millisecondToString(currentPosition);
//            tvTimeCurrent.setText(currentPositionStr);
//            seekBar.setProgress(currentPosition);
//            threadHandler.postDelayed(this, 0);
//        }
//    }
//
//    private void doStart() {
//        if (this.mediaPlayer.isPlaying()) {
//            return;
//        }
//        int duration = this.mediaPlayer.getDuration();
//        int currentPosition = this.mediaPlayer.getCurrentPosition();
//        if (currentPosition == 0) {
//            this.seekBar.setMax(duration);
//            String maxTimeString = this.millisecondToString(duration);
//            this.tvTimeTotal.setText(maxTimeString);
//        } else if (currentPosition == duration) {
//            this.mediaPlayer.reset();
//        }
//        this.mediaPlayer.start();
//        UpdateSeekBarThread updateSeekBarThread = new UpdateSeekBarThread();
//        threadHandler.postDelayed(updateSeekBarThread, 0);
//
////        this.btnpause.setEnabled(true);
//        this.bntplay.setEnabled(false);
//        this.btnleft.setEnabled(true);
//        this.btnright.setEnabled(true);
//    }
//
//    private void doPause() {
//        if (mediaPlayer.isPlaying()) {
//            this.mediaPlayer.pause();
//        }
//        //    this.btnpause.setEnabled(false);
//        this.bntplay.setEnabled(true);
//    }
//
//    private void doRewind() {
//        int currentPosition = this.mediaPlayer.getCurrentPosition();
//        int duration = this.mediaPlayer.getDuration();
//
//        int SUBTRACTIME = 5000;
//        if (currentPosition - SUBTRACTIME > 0) {
//            this.mediaPlayer.seekTo(currentPosition - SUBTRACTIME);
//        }
//        this.btnright.setEnabled(true);
//    }
//
//    private void doStop() {
//        if (this.mediaPlayer.isPlaying()) {
//            this.mediaPlayer.stop();
//        }
//        bntplay.setEnabled(true);
//        //   btnpause.setEnabled(false);
//        btnleft.setEnabled(false);
//        btnright.setEnabled(false);
//    }
//
//    private void doFastForward() {
//        int currentPosition = this.mediaPlayer.getCurrentPosition();
//        int duration = this.mediaPlayer.getDuration();
//
//        int ADD_TIME = 5000;
//        if (currentPosition + ADD_TIME < duration) {
//            this.mediaPlayer.seekTo(currentPosition + ADD_TIME);
//        }
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        this.mediaPlayer = new MediaPlayer();
//        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        this.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                doStop();
//            }
//        });
//        this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                doPause();
//            }
//        });
//
//        bntplay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doStart();
//            }
//        });
//
////        btnpause.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                doPause();
////            }
////        });
//
//        btnleft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doRewind();
//            }
//        });
//        btnright.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doFastForward();
//            }
//        });

}
