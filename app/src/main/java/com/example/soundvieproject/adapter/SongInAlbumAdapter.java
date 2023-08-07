package com.example.soundvieproject.adapter;

import android.content.Context;
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

public class SongInAlbumAdapter extends RecyclerView.Adapter<SongInAlbumAdapter.ViewHolder>{
    private ArrayList<Song> songs;
    private Context context;

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public SongInAlbumAdapter(ArrayList<Song> songs, Context context) {
        this.songs = songs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.song_artist_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song s = songs.get(position);
        StorageHelper helper = new StorageHelper(context);
        StorageReference ref = helper.getStorage().getReference("images/"+s.getImgCover());
        Glide.with(context).load(ref).into(holder.imgSongPl);
        holder.tvSongName.setText(s.getNameSong());

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgSongPl;
        TextView tvSongName;
        ImageButton btnMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSongPl = itemView.findViewById(R.id.imgSongPl);
            tvSongName = itemView.findViewById(R.id.tvSongName);
            btnMore = itemView.findViewById(R.id.btnMore);
        }
    }
}
