package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.model.ArtistInSong;
import com.example.soundvieproject.model.Payment;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.User;

import java.io.File;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class SettingActivity extends AppCompatActivity {

    ImageButton btnBack;
    LinearLayout btnDowLoaded;
    ImageButton btnProfile;
    Helper helper =  Helper.INSTANCE;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        btnBack = findViewById(R.id.btnBack);
        btnProfile = findViewById(R.id.btnProfile);
        btnDowLoaded = findViewById(R.id.musicDownloaded);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, EditUserActivity.class);
                startActivity(i);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, HomeActivity.class);
                startActivity(i);
            }
        });

        btnDowLoaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.checkPremiumForUser(result1 -> {
                    MongoCursor<Payment> cursor1 = result1.get();
                    if(cursor1.hasNext()){

                        Intent i = new Intent(SettingActivity.this, MusicDownloadedActivity.class);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(SettingActivity.this, "Bạn chưa đăng kí premium", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SettingActivity.this, PremiumRegisterActivity.class);
                        Bundle transfer = new Bundle();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(intent);
                            }
                        }, 2000);
                    }
                });

            }
        });
    }
}