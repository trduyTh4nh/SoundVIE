package com.example.soundvieproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.adapter.AddSongAdapter;
import com.example.soundvieproject.adapter.SongToAlbumAdapter;
import com.example.soundvieproject.model.ArtistInSong;
import com.example.soundvieproject.model.Playlist;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.SongInPlayList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.bson.types.ObjectId;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.iterable.MongoCursor;
import io.realm.mongodb.mongo.options.InsertManyResult;
import io.realm.mongodb.mongo.result.InsertOneResult;

public class AddAlbumActivity extends AppCompatActivity {
    RecyclerView rcvSong, rcvSongAdd;
    ArrayList<Song> songListMain, songListDialog;
    Button btnChooseSong, btnPostAlbum, btnCancel, btnConfirmSong, btnCancelSongChoose;
    ImageButton btnChooseCover;
    TextView tvTitle;
    AddSongAdapter sAdap;
    Helper h = Helper.INSTANCE;
    EditText edtNameAlbum, edtDescAlbum;
    StorageHelper sto;

    Uri uri;
    SongToAlbumAdapter adap;
    boolean[] checkList;
    ProgressBar load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sto = new StorageHelper(this);

        setContentView(R.layout.activity_add_album);
        load = findViewById(R.id.loader);
        load.setVisibility(View.GONE);
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
                                            checkList = new boolean[songListDialog.size()];
                                            sAdap.setCheckList(checkList);
                                            sAdap.notifyItemInserted(songListDialog.size() - 1);
                                            //adap.notifyItemInserted(songs.size() - 1);
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
                    Log.d("state", checkList[i] ? "true" : "false");
                    if(checkList[i]){
                        if(isTrung(songListDialog.get(i))){
                            continue;
                        }
                            songListMain.add(songListDialog.get(i));
                            adap.notifyItemInserted(songListDialog.size() - 1);


                    }

                }
                d.dismiss();
            });
            d.show();
            d.getWindow().setBackgroundDrawableResource(R.drawable.background_rounded_light);


        });
        btnPostAlbum.setOnClickListener(v -> {
            if(edtDescAlbum.getText().toString().equals("") || edtNameAlbum.getText().toString().equals("")){
                AlertDialog.Builder b = new AlertDialog.Builder(AddAlbumActivity.this);
                b.setTitle("Không có đủ thông tin");
                b.setMessage("Cần nhập đủ thông tin (Tên, Mô tả).");
                b.setPositiveButton("Đã rõ", null);
                b.create().show();
                return;
            }
            songListMain = adap.getSongs();
            if(songListMain.size() == 0){
                AlertDialog.Builder b = new AlertDialog.Builder(AddAlbumActivity.this);
                b.setTitle("Không có đủ bài hát");
                b.setMessage("Số bài hát cần để tạo Album là trên 1 bài hát.");
                b.setPositiveButton("Đã rõ", null);
                b.create().show();
                return;
            }
            Toast.makeText(this, "Đang đăng tải...", Toast.LENGTH_SHORT).show();
            load.setVisibility(View.VISIBLE);
            ObjectId id = new ObjectId();
            String randName = id.toString();
            StorageReference ref;

            ref = sto.getStorage().getReference("images/" + randName);
            ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ObjectId id = new ObjectId();
                    Playlist p = new Playlist(id, edtNameAlbum.getText().toString(), randName, h.getUser().getId(), edtDescAlbum.getText().toString());
                    h.insertAlbum(p, new App.Callback<InsertOneResult>() {
                        @Override
                        public void onResult(App.Result<InsertOneResult> result) {
                            if(result.isSuccess()){
                                ArrayList<SongInPlayList> ss = new ArrayList<>();
                                for(Song s : songListMain){
                                    ss.add(new SongInPlayList(new ObjectId(), id, s.getId()));
                                }
                                h.insertSongInAlbum(ss, new App.Callback<InsertManyResult>() {
                                    @Override
                                    public void onResult(App.Result<InsertManyResult> result) {
                                        if (result.isSuccess()){
                                            Toast.makeText(AddAlbumActivity.this, "Đang Album thành công!", Toast.LENGTH_SHORT).show();
                                            load.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(AddAlbumActivity.this, "Đăng Album thất bại do: " + result.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            });
        });
    }
    private boolean isTrung(Song s){
        for(Song ss : songListMain){
            if(s.getId().toString().equals(ss.getId().toString())){
                return true;
            }
        }
        return false;
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