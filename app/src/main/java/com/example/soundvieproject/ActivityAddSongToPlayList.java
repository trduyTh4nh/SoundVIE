package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.adapter.MusicInSearchAdapter;
import com.example.soundvieproject.adapter.SongAdapter;
import com.example.soundvieproject.media.Media;
import com.example.soundvieproject.model.Payment;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.SongInPlayList;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class ActivityAddSongToPlayList extends AppCompatActivity {

    RecyclerView rvSongInSearch;
    MusicInSearchAdapter adapter;
    FirebaseStorage storage;
    ArrayList<Song> songsArr;
    Media media;
    TextView song_name, artist;
    LinearLayout currentSong;
    ImageButton btnPause, btnResume;
    ImageView img_song;
    Helper helper = Helper.INSTANCE;
    Button btnSave;
    Button btnCancel;

    EditText edtSearchSongtoAdd;

    MediaPlayer p;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song_to_play_list);
        media = Media.INSTANCE;
        p = new MediaPlayer();

        rvSongInSearch = findViewById(R.id.rvSongSreach);
        media.setContext(this);
        btnPause = findViewById(R.id.btnPause);
        btnResume = findViewById(R.id.btnResume);
        currentSong = findViewById(R.id.currentSong);
        img_song = findViewById(R.id.img_song);
        song_name = findViewById(R.id.song_name);
        artist = findViewById(R.id.artist);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        edtSearchSongtoAdd = findViewById(R.id.edtSearch);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edtSearchSongtoAdd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                helper.getSongWhenSearch(result -> {
                    songsArr = new ArrayList();
                    if(result.isSuccess()){
                        MongoCursor<Song> cursor = result.get();
                        while (cursor.hasNext()){
                            Song songResult = cursor.next();
                            songsArr.add(new Song(songResult.getId(), songResult.getNameSong(), songResult.getImgCover(), songResult.getStateData(), songResult.getLyrics(), songResult.getArtists(), songResult.getSong()));

                        }
                        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                        adapter = new MusicInSearchAdapter(songsArr, ActivityAddSongToPlayList.this, firebaseStorage);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(ActivityAddSongToPlayList.this, LinearLayoutManager.VERTICAL, false);
                        rvSongInSearch.setLayoutManager(layoutManager);
                        rvSongInSearch.setAdapter(adapter);
                    }
                    else
                        Toast.makeText(ActivityAddSongToPlayList.this, "Lỗi bất định", Toast.LENGTH_SHORT).show();
                }, edtSearchSongtoAdd.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        //Hup();

        Bundle b = getIntent().getExtras();

        Bundle dataTransfer = getIntent().getExtras();
        String idPlCur = dataTransfer.getString("idPlaylistCr");

        String idPlCr = b.getString("idPlaylistCurrent");

//        Log.d("Danh sách phát hiện tại", idPlCr);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] checked = adapter.getChecked();
                for (int i = 0; i < checked.length; i++) {

                     //   Song songcheck = adapter.getArrSongInSreach().get(i);
                    if (checked[i]) {
                        ObjectId idSong = songsArr.get(i).getId();

                        helper.checkSongRep(idSong, new ObjectId(idPlCur), result -> {
                            if (result.isSuccess()) {

                                //String idPlCr = b.getString("idPlaylistCurrent");
                                MongoCursor<SongInPlayList> cursor = result.get();
                                if (cursor.hasNext()) {

                                    Log.d("lỗi", "idPl" + idPlCur + " idSong: " + idSong + " obj: " + cursor.next().toString());
                                    ShowPopUp();
                                } else {
                                    SongInPlayList songInPlayList = new SongInPlayList(new ObjectId(), new ObjectId(idPlCur), idSong);
                                    helper.insertSongInPlayList(songInPlayList);
                                    Toast.makeText(ActivityAddSongToPlayList.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ActivityAddSongToPlayList.this, "cucu" + result.getError().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

//    public void Hup() {
//        songsArr = new ArrayList<>();
//        helper.getSong(result -> {
//            if (result.isSuccess()) {
//                MongoCursor<Song> cursor = result.get();
//                while (cursor.hasNext()) {
//                    Song song = cursor.next();
//                    Log.d("Check", song.toString());
//                    songsArr.add(new Song(song.getId(), song.getNameSong(), song.getImgCover(), song.getStateData(), song.getLyrics(), song.getArtists(), song.getSong()));
//                }
//                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//                adapter = new MusicInSearchAdapter(songsArr, this, firebaseStorage);
//                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//                rvSongInSearch.setLayoutManager(layoutManager);
//                rvSongInSearch.setAdapter(adapter);
//
//                adapter.setItemClickListener(new SongAdapter.OnItemsClickListener() {
//                    @Override
//                    public void OnItemClick(Song song, int index) throws IOException {
//                        if (media.getPlayer().isPlaying()) {
//                            media.getPlayer().stop();
//                            media.setPlayer(new MediaPlayer());
//                        }
//                        else {
//
//                            media.getPlayer().stop();
//                            media.setPlayer(new MediaPlayer());
//                        }
//                        media.setContext(ActivityAddSongToPlayList.this);
//                        media.playMusic(song);
//                        btnResume.setVisibility(View.GONE);
//                        btnPause.setVisibility(View.VISIBLE);
//                        currentSong.setVisibility(View.VISIBLE);
//                        //img_song.setImageResource(song.getImgCover());
//                        song_name.setText(song.getNameSong());
//                        artist.setText(song.getArtist());
//
//                        StorageReference sto = firebaseStorage.getReference("images/" + song.getImgCover());
//                        Glide.with(ActivityAddSongToPlayList.this).load(sto).into(img_song);
//
//                        btnPause.setOnClickListener(v -> {
//                            btnPause.setVisibility(View.GONE);
//                            btnResume.setVisibility(View.VISIBLE);
//                            media.pause();
//                        });
//                        btnResume.setOnClickListener(v -> {
//                            btnResume.setVisibility(View.GONE);
//                            btnPause.setVisibility(View.VISIBLE);
//                            media.start();
//                        });
//                    }
//
//
//                });
//
//            } else {
//                Toast.makeText(this, "Không thể lấy bài hát", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    protected void onPause() {
        if(media.getPlayer().isPlaying())
            media.setPlayer(null);
        super.onPause();
    }

    private void ShowPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);

        builder.setView(dialogView);

        Button btnOK = dialogView.findViewById(R.id.dialog_button_positive);


        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


}