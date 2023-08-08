package com.example.soundvieproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.soundvieproject.fragment.AlbumFragment;
import com.example.soundvieproject.fragment.ArtistProfileFragment;
import com.example.soundvieproject.fragment.HomeArtistFragment;
import com.example.soundvieproject.fragment.HomeFragment;
import com.example.soundvieproject.fragment.SongArtistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ArtistHomeActivity extends AppCompatActivity {
    BottomNavigationView bottom;

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_home);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeArtistFragment()).commit();
        bottom = findViewById(R.id.nav_main);
        bottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_home_artist:
                        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeArtistFragment()).commit();
                        break;
                    case R.id.item_album:
                        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new AlbumFragment()).commit();
                        break;
                    case R.id.item_music_artist:
                        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new SongArtistFragment()).commit();
                        break;
                    case R.id.item_profile_artist:
                        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new ArtistProfileFragment()).commit();
                        break;
                }
                return true;
            }
        });
    }
}