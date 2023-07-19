package com.example.soundvieproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundvieproject.R;
import com.example.soundvieproject.model.Song;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.component_song, parent, false);
        return new ViewHolder(view);
    }
    private final ArrayList<Song> songs;
    Context context;
    public SongAdapter(Context c, ArrayList<Song> arrayList){
        songs = arrayList;
        context = c;
    };

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Song song = songs.get(position);
        imgSong.setImageURI(Uri.parse(song.getImgCover()));
        nameSong.setText(song.getNameSong());
        nameArtist.setText(song.getArtists().toString());
    }
     ImageView imgSong;
     TextView nameSong;
     TextView nameArtist;
    @Override
    public int getItemCount() {
        return songs.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
             imgSong = itemView.findViewById(R.id.img_song);
             nameSong = itemView.findViewById(R.id.name_song);
             nameArtist = itemView.findViewById(R.id.artis_song);
        }
    }
}
