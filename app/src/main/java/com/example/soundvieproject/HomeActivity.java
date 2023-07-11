package com.example.soundvieproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.soundvieproject.fragment.HomeFragment;
import com.example.soundvieproject.fragment.ListSongFragment;
import com.example.soundvieproject.fragment.MoreFragment;
import com.example.soundvieproject.fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigationView = findViewById(R.id.nav_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
        viewPager = findViewById(R.id.viewPager);


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(HomeActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.list:
                        Toast.makeText(HomeActivity.this, "List Music", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.user:
                        Toast.makeText(HomeActivity.this, "User", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.more:
                        Toast.makeText(HomeActivity.this, "More", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                boolean isAdd = false;

                switch (item.getItemId()) {
                    case R.id.home:
                        isAdd = false;
                        fragment = new HomeFragment();
                        break;
                    case R.id.list:
                        isAdd = false;
                        fragment = new ListSongFragment();
                        break;
                    case R.id.user:
                        isAdd = false;
                        fragment = new UserFragment();
                        break;
                    case R.id.more:
                        isAdd = false;
                        fragment = new MoreFragment();
                        break;
                }
                if(!isAdd){
                    getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
                }
                return true;
            }
        });
    }


}