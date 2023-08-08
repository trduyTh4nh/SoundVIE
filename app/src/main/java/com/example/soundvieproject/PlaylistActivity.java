package com.example.soundvieproject;

import androidx.annotation.ChecksSdkIntAtLeast;
import androidx.annotation.LongDef;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.adapter.PlaylistAdapter;
import com.example.soundvieproject.adapter.SearchResultAdapter;
import com.example.soundvieproject.adapter.SongAdapter;
import com.example.soundvieproject.adapter.SongInPlayListAdapter;
import com.example.soundvieproject.media.Media;
import com.example.soundvieproject.model.Payment;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.SongInPlayList;
import com.example.soundvieproject.model.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class PlaylistActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    SongInPlayListAdapter adapter = null;

    ImageView imageView;
    TextView tvNamePL, tvNameUser, tvDes;
    Helper helper = Helper.INSTANCE;
    StorageReference storageReference;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    ImageButton btnAddSong;
    ArrayList<Song> songinPlayLists;

    Media media;
    int s;

    TextView song_name, artist, quantity;

    LinearLayout currentSong;

    ImageButton btnPause, btnResume;

    ImageView img_song;

    public static ArrayList<Song> songInPlaylist;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_playlist);
        imageView = findViewById(R.id.imgPl);
        tvNamePL = findViewById(R.id.namePl);
        tvNameUser = findViewById(R.id.nameUserPl);
        tvDes = findViewById(R.id.titllePl);
        btnAddSong = findViewById(R.id.btnAddSong);
        recyclerView = findViewById(R.id.rv_songsPl);
        song_name = findViewById(R.id.song_name);
        artist = findViewById(R.id.artist);
        currentSong = findViewById(R.id.currentSong);
        btnPause = findViewById(R.id.btnPause);
        btnResume = findViewById(R.id.btnResume);
        img_song = findViewById(R.id.img_song);
        quantity = findViewById(R.id.quantity);

        media = Media.INSTANCE;



        Bundle b = getIntent().getExtras();
        String imgResource = b.getString("image");
        String namePl = b.getString("namepl");
        String idUser = b.getString("iduser");
        String desc = b.getString("despl");
        String idPlCurrent = b.getString("idPl");


        helper.getUserByObjID(idUser, result -> {
            if (result.isSuccess()) {
                User cursor = result.get();
                tvNamePL.setText(namePl);
                tvNameUser.setText(cursor.getName());
                tvDes.setText(desc);
                storageReference = storage.getReference("images/" + imgResource);
                Glide.with(this).load(storageReference).into(imageView);

            }
        });


//        RealmResultTask<Long> test = helper.getCollectionCount(result -> {
//            if(result.isSuccess()){
//                MongoCursor<SongInPlayList> cursor =result.get();
//                while (cursor.hasNext()){
//                    Log.d("test var", "ccuc");
//                }
//            }
//        }, idPlCurrent);


        songinPlayLists = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new SongInPlayListAdapter(songinPlayLists, PlaylistActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        btnAddSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                songInPlaylist = new ArrayList<>();
                for (int k = recyclerView.getChildCount() - 1; k >= 0; k--) {
                    v = recyclerView.getChildAt(k);

                    Song songcheck = adapter.getSongspl().get(k);

                    //   CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkAddSong);

                    helper.checkSongRep(songcheck.getId(), new ObjectId(idPlCurrent), result -> {
                        if (result.isSuccess()) {
                            songInPlaylist.add(songcheck);
                            Log.d("Xong đã add vào playlist", songcheck.getNameSong());
                        } else {
                            Log.d("Xong chưa add vào playlist", songcheck.getNameSong());
                        }
                    });
                }

                Intent i = new Intent(PlaylistActivity.this, ActivityAddSongToPlayList.class);
                Bundle bundle = new Bundle();
                bundle.putString("idPlaylistCr", idPlCurrent);
                i.putExtras(bundle);
                startActivity(i);

            }
        });


        helper.getSongInPlayList(result -> {
            if (result.isSuccess()) {
                MongoCursor<SongInPlayList> cursor = result.get();
                while (cursor.hasNext()) {
                    // đồng bộ lòng đồng bộ
                    SongInPlayList PlaylistSong = cursor.next();

                    Log.d("Checksource", PlaylistSong.toString());
                    helper.getSongByID(element -> {
                        if (element.isSuccess()) {
                            MongoCursor<Song> songCur = element.get();
                            while (songCur.hasNext()) {
                                Song songIPL = songCur.next();
                                Log.d("Check song in playlist", songIPL.toString());
                                if (s == 20) {
                                    songinPlayLists.add(new Song(songIPL.getId(), songIPL.getNameSong(), songIPL.getImgCover(), songIPL.getStateData(), songIPL.getLyrics(), songIPL.getArtists(), songIPL.getSong()));
                                    adapter.notifyItemInserted(songinPlayLists.size() - 1);
                                    btnAddSong.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            helper.checkPremiumForUser(result1 -> {
                                                MongoCursor<Payment> cursor1 = result1.get();
                                                if(cursor1.hasNext()){
                                                    Intent intent = new Intent(PlaylistActivity.this, ActivityAddSongToPlayList.class);
                                                    Bundle transfer = new Bundle();
                                                    transfer.putString("idPlaylistCr", idPlCurrent);
                                                    intent.putExtras(transfer);
                                                    startActivity(intent);
                                                }
                                                else {
                                                    Toast.makeText(PlaylistActivity.this, "Danh sách bài hát đã đến giới hạn", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(PlaylistActivity.this, PremiumRegisterActivity.class);
                                                    Bundle transfer = new Bundle();
                                                    transfer.putString("idPlaylistCr", idPlCurrent);
                                                    intent.putExtras(transfer);
                                                    new Handler().postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            startActivity(intent);
                                                            finish(); // Optional: If you want to close the current activity after starting the new one.
                                                        }
                                                    }, 2000);
                                                }
                                            });
                                        }
                                    });

                                } else {
                                    songinPlayLists.add(new Song(songIPL.getId(), songIPL.getNameSong(), songIPL.getImgCover(), songIPL.getStateData(), songIPL.getLyrics(), songIPL.getArtists(), songIPL.getSong()));
                                    adapter.notifyItemInserted(songinPlayLists.size() - 1);
                                }
                            }
                            ///// count adapter check quantity

                            s = adapter.getItemCount();
                            quantity.setText(String.valueOf(s));

                            adapter.setItemClickListener(new SongInPlayListAdapter.OnItemsClickListener() {
                                @Override
                                public void OnItemClick(Song song, int pos) throws IOException {
                                    if (media.getPlayer().isPlaying()) {
                                        media.getPlayer().stop();
                                        media.setPlayer(new MediaPlayer());
                                    }
                                    media.setContext(PlaylistActivity.this);
                                    media.playMusic(song);

                                    currentSong.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent i = new Intent(PlaylistActivity.this, PlayingMusicActivity.class);
                                            Bundle data = new Bundle();
                                            int currentPoint = media.getPlayer().getCurrentPosition();
                                            data.putString("IdSongClicked", String.valueOf(song.getId()));
                                            data.putString("ImgCover", song.getImgCover());
                                            data.putInt("currentPoint", currentPoint);
                                            i.putExtras(data);
                                            startActivity(i);

                                            try {
                                                media.playMusicInPlaylist(songinPlayLists, pos);
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
                                        }
                                    });

                                    btnResume.setVisibility(View.GONE);
                                    btnPause.setVisibility(View.VISIBLE);
                                    currentSong.setVisibility(View.VISIBLE);

                                    song_name.setText(song.getNameSong());
                                    artist.setText(song.getArtist());

                                    StorageReference sto = storage.getReference("images/" + song.getImgCover());
                                    Glide.with(PlaylistActivity.this).load(sto).into(img_song);

                                    btnPause.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            btnPause.setVisibility(View.GONE);
                                            btnResume.setVisibility(View.VISIBLE);
                                            media.pause();
                                        }
                                    });
                                    btnResume.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            btnResume.setVisibility(View.GONE);
                                            btnPause.setVisibility(View.VISIBLE);
                                            media.start();
                                        }
                                    });

                                }
                            });
                            adapter.setDeleteListener(new SongInPlayListAdapter.OnDeleteListener() {
                                @Override
                                public void OnDelete(Song song, View v, int pos) throws IOException {
                                    PopupMenu popupMenu = new PopupMenu(getApplicationContext(), v);
                                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                        @SuppressLint("NonConstantResourceId")
                                        @Override
                                        public boolean onMenuItemClick(MenuItem item) {
                                            switch (item.getItemId()) {
                                                case R.id.delete_music: {
                                                    helper.deleteSongWidthID(result -> {
                                                        if (result.isSuccess()) {
                                                            songinPlayLists.remove(song);
                                                            recyclerView.setAdapter(adapter);
                                                            Toast.makeText(getApplicationContext(), "Thành công", Toast.LENGTH_SHORT).show();



                                                        } else {
                                                            Log.d("thất bại", result.getError().toString());
                                                        }


                                                    }, songinPlayLists.get(pos).getId());


                                                    break;
                                                }
                                                case R.id.report_music: {
                                                    Intent i = new Intent(getApplicationContext(), ReportSongActivity.class);
                                                    Bundle data = new Bundle();
                                                    data.putString("idSongReport", songinPlayLists.get(pos).getId().toString());
                                                    i.putExtras(data);
                                                    startActivity(i);
                                                    break;
                                                }
                                                default:
                                                    Toast.makeText(getApplicationContext(), "Không có gì", Toast.LENGTH_SHORT).show();
                                                    break;
                                            }
                                            return false;
                                        }
                                    });
                                    popupMenu.inflate(R.menu.menu_edit_music_in_playlist);
                                    popupMenu.show();
                                }
                            });
                        } else {
                            Log.d("Thông báo", "Lỗi");
                        }
                    }, PlaylistSong.getIdSong().toString());

                }
//                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//                adapter = new SongInPlayListAdapter(songinPlayLists, PlaylistActivity.this);
//                recyclerView.setLayoutManager(layoutManager);
//                recyclerView.setAdapter(adapter);
            } else
                Log.d("Thông báo", "Lỗi");
        }, idPlCurrent);





    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.notifyDataSetChanged();
    }
}