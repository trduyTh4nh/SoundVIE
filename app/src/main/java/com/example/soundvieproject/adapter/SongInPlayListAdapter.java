package com.example.soundvieproject.adapter;

import android.content.Context;
import android.media.Image;
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

public class SongInPlayListAdapter extends RecyclerView.Adapter<SongInPlayListAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_song_in_playlist,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songspl.get(position);
        imgSongpl.setImageURI(Uri.parse(song.getImgCover()));
        nameSongpl.setText(song.getNameSong());
        nameArtist.setText(song.getArtis());
    }

    public SongInPlayListAdapter(ArrayList<Song> songspl, Context context) {
        this.songspl = songspl;
        this.context = context;
    }

    private final ArrayList<Song> songspl;
    Context context;


    @Override
    public int getItemCount() {
        return songspl.size();
    }

    private ImageView imgSongpl;
    private TextView nameSongpl;
    private TextView nameArtist;
    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSongpl = itemView.findViewById(R.id.imgSongPl);
            nameSongpl = itemView.findViewById(R.id.nameSongPl);
            nameArtist = itemView.findViewById(R.id.artistSongPl);
        }
    }

}
