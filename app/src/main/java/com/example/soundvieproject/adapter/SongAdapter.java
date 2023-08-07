package com.example.soundvieproject.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.media.Media;
import com.example.soundvieproject.model.ArtistInSong;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import io.realm.mongodb.mongo.iterable.MongoCursor;

public class SongAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface OnItemsClickListener{
        void OnItemClick(Song song, int index) throws IOException;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private OnItemsClickListener listener = null;
    Helper h = Helper.INSTANCE;
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
        ArrayList<String> artists = new ArrayList<>();
        nameArtist.setText(song.getArtist());
        Log.d("Debug", String.format("%s, %s", song.getNameSong(), song.getArtist()));
        imgSong.setOnClickListener(v -> {
            if(listener != null){
                try {
                    listener.OnItemClick(song, position);
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
        return songs.size() > 5 ? 5 : songs.size();
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
