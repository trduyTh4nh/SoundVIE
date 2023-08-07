package com.example.soundvieproject.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.model.Song;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SongToAlbumAdapter extends RecyclerView.Adapter<SongToAlbumAdapter.ViewHolder>{
    ArrayList<Song> songs;
    Context context;

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public SongToAlbumAdapter(ArrayList<Song> songs, Context context) {
        this.songs = songs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.song_add_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song s = songs.get(holder.getAdapterPosition());
        StorageHelper helper = new StorageHelper(context);
        StorageReference ref = helper.getStorage().getReference("images/" + s.getImgCover());
        Glide.with(context).load(ref).into(holder.imgSong);
        holder.songName.setText(s.getNameSong());
        holder.btnRemove.setOnClickListener(v -> {
            songs.remove(s);
            notifyItemRemoved(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgSong;
        public TextView songName;
        public ImageButton btnMore, btnRemove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSong = itemView.findViewById(R.id.imgSongPl);
            songName = itemView.findViewById(R.id.tvSongName);
            btnMore = itemView.findViewById(R.id.btnMore);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
