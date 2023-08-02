package com.example.soundvieproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.model.Song;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.result.DeleteResult;

public class SongArtistAdapter extends RecyclerView.Adapter<SongArtistAdapter.ViewHolder>{
    Context context;
    ArrayList<Song> song;
    ImageView ivSong;
    TextView tvSong;
    ImageButton btnSong;
    Helper h = Helper.INSTANCE;

    public SongArtistAdapter(Context context, ArrayList<Song> song) {
        this.context = context;
        this.song = song;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.song_artist_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Song s = song.get(position);
        StorageHelper sto = new StorageHelper(context);
        StorageReference ref = sto.getStorage().getReference("images/"+s.getImgCover());
        Glide.with(context).load(ref).into(ivSong);
        tvSong.setText(s.getNameSong());
        btnSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(context, view);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        h.deleteSong(s.getId(), new App.Callback<DeleteResult>() {
                            @Override
                            public void onResult(App.Result<DeleteResult> result) {
                                if(result.isSuccess()){
                                    Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d("thất bại", result.getError().toString());
                                }
                            }
                        });
                        return false;
                    }
                });
                menu.inflate(R.menu.artist_song_menu);
                menu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return song.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivSong = itemView.findViewById(R.id.imgSongPl);
            tvSong = itemView.findViewById(R.id.tvSongName);
            btnSong = itemView.findViewById(R.id.btnMore);
        }
    }
}
