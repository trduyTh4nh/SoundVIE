package com.example.soundvieproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.R;
import com.example.soundvieproject.model.Song;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class MusicInSearchAdapter extends RecyclerView.Adapter<MusicInSearchAdapter.ViewHolder> {

    public interface OnItemsClickListener{
        void OnItemClick(Song song) throws IOException;
    }
    private SongAdapter.OnItemsClickListener listener = null;
    public void setItemClickListener(SongAdapter.OnItemsClickListener listener){
        this.listener = listener;
    }
    public MusicInSearchAdapter(ArrayList<Song> arrSongInSreach, Context context, FirebaseStorage storage) {
        this.arrSongInSreach = arrSongInSreach;
        this.context = context;
        this.storage = storage;
    }

    ArrayList<Song> arrSongInSreach;
    Context context;
    FirebaseStorage storage;


    @NonNull
    @Override
    public MusicInSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_music_in_searchpage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicInSearchAdapter.ViewHolder holder, int position) {
        Song song = arrSongInSreach.get(position);
        StorageReference reference = storage.getReference("images/"+song.getImgCover());
        Glide.with(context).load(reference).into(imgCover);
        tvNameSong.setText(song.getNameSong());
        tvArtist.setText(song.getArtist());

        Log.d("Debug", String.format("%s, %s", song.getNameSong(), song.getArtist()));

        songWidth.setOnClickListener(v -> {
            if(listener != null){
                try {
                    listener.OnItemClick(song);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrSongInSreach.size();
    }

    private static ImageView imgCover;
    private static TextView tvNameSong;
    private static TextView tvArtist;
    private static LinearLayout songWidth;
    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.imgCover);
            tvNameSong = itemView.findViewById(R.id.nameMusic);
            tvArtist = itemView.findViewById(R.id.nameArtist);
            songWidth = itemView.findViewById(R.id.linearMusic);
        }
    }
}