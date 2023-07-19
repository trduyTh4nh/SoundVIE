package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.soundvieproject.media.MediaPlayerUtils;

public class PlayingMusicActivity extends AppCompatActivity {

    ImageButton btnleft, btnright, btnpause, bntplay, btnselect;
    SeekBar seekBar;
    TextView tvTimeCurrent, tvTimeTotal;
    private final Handler threadHandler = new Handler();
    private MediaPlayer mediaPlayer;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_music);

        btnleft = findViewById(R.id.btnleft);
        btnright = findViewById(R.id.btnRight);
    //    btnpause = findViewById(R.id.btnpause);
        bntplay = findViewById(R.id.btnplay);
        btnselect = findViewById(R.id.btnSelect);
        seekBar = findViewById(R.id.seekbar);
        tvTimeCurrent = findViewById(R.id.tvTimeCurrent);
        tvTimeTotal = findViewById(R.id.tvTimeTotal);

        // this.btnpause.setEnabled(false);
        this.btnright.setEnabled(false);
        this.btnleft.setEnabled(false);
        this.seekBar.setEnabled(false);
        this.bntplay.setEnabled(false);


    }

    private void selectMediaResource(){
        String resName = MediaPlayerUtils.RAW_MEDIA_SAMPLE;
        String path = "/sdcard/Music/dontcoi.mp3";
        MediaPlayerUtils.playRawMedia(PlayingMusicActivity.this, this.mediaPlayer, resName);
    }

    private String millisecondToString(int millisecond){
        long sec = millisecond / 1000;
        long min = sec / 60;
        sec = sec % 60;
        return  min + " : "+ sec;
    }

    private class UpdateSeekBarThread implements Runnable{

        @Override
        public void run() {
            int currentPosition = mediaPlayer.getCurrentPosition();
            String currentPositionStr = millisecondToString(currentPosition);
            tvTimeCurrent.setText(currentPositionStr);
            seekBar.setProgress(currentPosition);
            threadHandler.postDelayed(this, 0);
        }
    }

    private void doStart(){
        if(this.mediaPlayer.isPlaying()){
            return;
        }
        int duration  = this.mediaPlayer.getDuration();
        int currentPosition = this.mediaPlayer.getCurrentPosition();
        if(currentPosition == 0){
            this.seekBar.setMax(duration);
            String maxTimeString = this.millisecondToString(duration);
            this.tvTimeTotal.setText(maxTimeString);
        }
        else if(currentPosition == duration){
            this.mediaPlayer.reset();
        }
        this.mediaPlayer.start();
        UpdateSeekBarThread updateSeekBarThread = new UpdateSeekBarThread();
        threadHandler.postDelayed(updateSeekBarThread, 0);

//        this.btnpause.setEnabled(true);
        this.bntplay.setEnabled(false);
        this.btnleft.setEnabled(true);
        this.btnright.setEnabled(true);
    }
    private void doPause(){
        if(mediaPlayer.isPlaying()){
            this.mediaPlayer.pause();
        }
    //    this.btnpause.setEnabled(false);
        this.bntplay.setEnabled(true);
    }

    private void doRewind(){
        int currentPosition = this.mediaPlayer.getCurrentPosition();
        int duration = this.mediaPlayer.getDuration();

        int SUBTRACTIME = 5000;
        if(currentPosition - SUBTRACTIME > 0){
            this.mediaPlayer.seekTo(currentPosition - SUBTRACTIME);
        }
        this.btnright.setEnabled(true);
    }
    private void doStop(){
        if(this.mediaPlayer.isPlaying()){
            this.mediaPlayer.stop();
        }
        bntplay.setEnabled(true);
     //   btnpause.setEnabled(false);
        btnleft.setEnabled(false);
        btnright.setEnabled(false);
    }
    private void doFastForward(){
        int currentPosition = this.mediaPlayer.getCurrentPosition();
        int duration = this.mediaPlayer.getDuration();

        int ADD_TIME = 5000;
        if(currentPosition + ADD_TIME < duration){
            this.mediaPlayer.seekTo(currentPosition + ADD_TIME);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        this.mediaPlayer = new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                doStop();
            }
        });
        this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                doPause();
            }
        });

        bntplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doStart();
            }
        });

//        btnpause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doPause();
//            }
//        });
        btnleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRewind();
            }
        });
        btnright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doFastForward();
            }
        });
        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMediaResource();
            }
        });
    }
}