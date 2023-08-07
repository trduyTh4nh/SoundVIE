package com.example.soundvieproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.fragment.CurrentPremiumFragment;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.fragment.HomeFragment;
import com.example.soundvieproject.fragment.ListSongFragment;
import com.example.soundvieproject.fragment.MoreFragment;
import com.example.soundvieproject.fragment.SearchFragment;
import com.example.soundvieproject.fragment.UserFragment;
import com.example.soundvieproject.media.Media;
import com.example.soundvieproject.model.Payment;
import com.example.soundvieproject.model.Song;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.internal.InternalTokenProvider;

import org.bson.types.ObjectId;
import io.realm.mongodb.App;
public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView navigationView;
    private ViewPager viewPager;
    Toolbar toolbarNormal, toolbarSearch;
    Helper h = Helper.INSTANCE;
    ImageButton btnSearch, btnBack, btnSetting;
    LinearLayout changeToMusic;

    Media currentMedia;

    @Override
    public void onBackPressed() {
        return;
    }

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        navigationView = findViewById(R.id.nav_main);
        btnSearch = findViewById(R.id.btnSearch);
        toolbarNormal = findViewById(R.id.toolbarNormal);
        toolbarSearch = findViewById(R.id.toolbarSearch);
        btnBack = findViewById(R.id.btnBack);
        changeToMusic = findViewById(R.id.currentSong);
        currentMedia = Media.INSTANCE;



        changeToMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Song currentSong = currentMedia.getCurrentSong();
                int currentPoint = currentMedia.getPlayer().getCurrentPosition();
                Log.d("Current Song", currentSong.toString());

                Intent i = new Intent(HomeActivity.this, PlayingMusicActivity.class);
                Bundle bundle = new Bundle();

                bundle.putInt("currentPoint", currentPoint);
                bundle.putString("IdSongClicked", String.valueOf(currentSong.getId()));
                bundle.putString("ImgCover", currentSong.getImgCover());
                i.putExtras(bundle);

                startActivity(i);
            }
        });


        btnSetting = findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(i);
            }
        });


        btnBack.setOnClickListener(v -> {
            getSupportFragmentManager().popBackStack();
            toolbarNormal.setVisibility(View.VISIBLE);
            toolbarSearch.setVisibility(View.GONE);
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
        for(int i = 1; i <= 100; i++){
            ObjectId id = new ObjectId();
            Log.d("objectID", id.toString());
        }
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new SearchFragment()).commit();
                toolbarNormal.setVisibility(View.GONE);
                toolbarSearch.setVisibility(View.VISIBLE);
            }
        });


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
                        toolbarNormal.setVisibility(View.VISIBLE);
                        toolbarSearch.setVisibility(View.GONE);
                        isAdd = false;
                        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeFragment()).commit();
                        break;
                    case R.id.list:
                        toolbarNormal.setVisibility(View.VISIBLE);
                        toolbarSearch.setVisibility(View.GONE);
                        isAdd = false;
                        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new ListSongFragment()).commit();
                        break;
                    case R.id.user:
                        toolbarNormal.setVisibility(View.VISIBLE);
                        toolbarSearch.setVisibility(View.GONE);
                        isAdd = false;
                        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new UserFragment()).commit();
                        break;
                    case R.id.more:
                        toolbarNormal.setVisibility(View.VISIBLE);
                        toolbarSearch.setVisibility(View.GONE);
                        h.getPayment(new App.Callback<Payment>() {
                            @Override
                            public void onResult(App.Result<Payment> result) {
                                if(result.isSuccess()){
                                    if(result.get() != null){
                                        Log.d("Test", result.get().toString());
                                        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new CurrentPremiumFragment()).commit();
                                    } else {
                                        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new MoreFragment()).commit();
                                    }
                                }
                            }
                        });

                        break;
                }
                if(!isAdd){

                }
                return true;
            }
        });
    }


}