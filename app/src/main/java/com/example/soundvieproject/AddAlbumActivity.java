package com.example.soundvieproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.adapter.AddSongAdapter;
import com.example.soundvieproject.adapter.SongToAlbumAdapter;
import com.example.soundvieproject.model.ArtistInSong;
import com.example.soundvieproject.model.Song;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class AddAlbumActivity extends AppCompatActivity {
    RecyclerView rcvSong, rcvSongAdd;
    ArrayList<Song> songListMain, songListDialog;
    Button btnChooseSong, btnPostAlbum, btnCancel, btnConfirmSong, btnCancelSongChoose;
    ImageButton btnChooseCover;
    TextView tvTitle;
    AddSongAdapter sAdap;
    Helper h = Helper.INSTANCE;
    EditText edtNameAlbum, edtDescAlbum;
    Uri uri;
    SongToAlbumAdapter adap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_album);
        rcvSong = findViewById(R.id.rcvSong);
        btnChooseSong = findViewById(R.id.btnChooseSong);
        edtNameAlbum = findViewById(R.id.etdNamePl);
        edtDescAlbum = findViewById(R.id.etdDesPl);
        btnCancel = findViewById(R.id.btnCancel);
        btnPostAlbum = findViewById(R.id.btnCreate);
        btnChooseCover = findViewById(R.id.btnChooseImage);
        btnCancel.setOnClickListener(v -> {
            finish();
        });
        songListMain = new ArrayList<>();
        adap = new SongToAlbumAdapter(songListMain, this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rcvSong.setAdapter(adap);
        rcvSong.setLayoutManager(l);
        btnChooseCover.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, 102);
        });
        btnChooseSong.setOnClickListener(v -> {
            songListDialog = new ArrayList<>();

            AlertDialog.Builder b = new AlertDialog.Builder(AddAlbumActivity.this);
            View view = LayoutInflater.from(AddAlbumActivity.this).inflate(R.layout.add_artist_dialog, null);
            b.setView(view);
            btnConfirmSong = view.findViewById(R.id.btnAddArt);
            btnCancelSongChoose = view.findViewById(R.id.btnCancel);
            songListDialog = new ArrayList<>();
            rcvSongAdd = view.findViewById(R.id.rcvArt);
            sAdap = new AddSongAdapter(getApplicationContext(), songListDialog);
            LinearLayoutManager li = new LinearLayoutManager(getApplicationContext());
            rcvSongAdd.setAdapter(sAdap);
            rcvSongAdd.setLayoutManager(li);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvTitle.setText("Thêm bài hát");
            h.getSongByArtist(h.getA().currentUser().getId(), new App.Callback<MongoCursor<ArtistInSong>>() {
                @Override
                public void onResult(App.Result<MongoCursor<ArtistInSong>> result) {
                    if(result.isSuccess()){
                        MongoCursor<ArtistInSong> cur = result.get();
                        while (cur.hasNext()){
                            ArtistInSong ar = cur.next();
                            Log.d("CC", ar.toString());
                            h.getSongByIdSong(ar.getIdSong(), new App.Callback<Song>() {
                                @Override
                                public void onResult(App.Result<Song> result) {
                                    if(result.isSuccess()){
                                        Song s = result.get();
                                        if(s != null){
                                            songListDialog.add(s);
                                            sAdap.notifyItemInserted(songListDialog.size() - 1);
                                            //adap.notifyItemInserted(songs.size() - 1);
                                            Log.d("CC2", s.toString());
                                        }


                                    } else {
                                        Log.d("ERR", result.getError().toString());
                                    }

                                }
                            });
                        }
                    }
                }
            });

            Dialog d = b.create();
            btnCancelSongChoose.setOnClickListener(v1 -> {
                d.dismiss();
            });
            btnConfirmSong.setOnClickListener(v2 -> {
                boolean[] checkList = sAdap.getCheckList();
                for(int i = 0; i < checkList.length; i++){
                    if(checkList[i]){
                        songListMain.add(songListDialog.get(i));
                        adap.notifyItemInserted(songListDialog.size() - 1);
                        d.dismiss();
                    }

                }
            });
            d.show();
            d.getWindow().setBackgroundDrawableResource(R.drawable.background_rounded_light);


        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 102){
                uri = data.getData();
                btnChooseCover.setImageURI(uri);
            }
        }

    }
}