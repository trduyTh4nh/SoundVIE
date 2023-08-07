package com.example.soundvieproject.adapter;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundvieproject.R;
import com.example.soundvieproject.media.Media;
import com.example.soundvieproject.model.Song;

import java.io.IOException;
import java.util.ArrayList;

public class MusicDownloadedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnItemsClickListener{
        void OnItemClick(Song song, int index) throws IOException;
    }
    private SongAdapter.OnItemsClickListener listener = null;

    public void setItemClickListener(SongAdapter.OnItemsClickListener listener){
        this.listener = listener;
    }
    ArrayList<Song> songArr;
    Context context;
    Media media = Media.INSTANCE;

    public MusicDownloadedAdapter(ArrayList<Song> songArr, Context context) {
        this.songArr = songArr;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_music_downloaded, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Song song = songArr.get(position);
        tvNameSong.setText(song.getNameSong());


        btnPlay.setOnClickListener(v -> {
            if (listener != null) {
                try {
                    listener.OnItemClick(song, position);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return songArr.size();
    }

    ImageView imgSong;
    TextView tvNameSong;
    TextView tvNameArtist;
    LinearLayout btnPlay;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            btnPlay = itemView.findViewById(R.id.btnPlay);
            imgSong = itemView.findViewById(R.id.imgSong);
            tvNameSong = itemView.findViewById(R.id.tvSongName);
            btnPlay = itemView.findViewById(R.id.btnPlay);
        }
    }
}
