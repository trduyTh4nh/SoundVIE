package com.example.soundvieproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.R;
import com.example.soundvieproject.media.Media;
import com.example.soundvieproject.model.Song;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnItemsClickListener{
        void OnItemClick(Song song) throws IOException;
    }
    private OnItemsClickListener listener = null;
    public void setItemClickListener(OnItemsClickListener listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.component_song, parent, false);
        return new ViewHolder(view);
    }
    FirebaseStorage storage;
    private final ArrayList<Song> songs;
    Context context;
    public SongAdapter(Context c, ArrayList<Song> arrayList, FirebaseStorage storage){
        songs = arrayList;
        context = c;
        this.storage = storage;
    };

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Song song = songs.get(position);
        StorageReference ref = storage.getReference("images/"+song.getImgCover());
        Glide.with(context).load(ref).into(imgSong);
        nameSong.setText(song.getNameSong());
        nameArtist.setText(song.getArtist());
        Log.d("Debug", String.format("%s, %s", song.getNameSong(), song.getArtist()));
        imgSong.setOnClickListener(v -> {
            if(listener != null){
                try {
                    listener.OnItemClick(song);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
