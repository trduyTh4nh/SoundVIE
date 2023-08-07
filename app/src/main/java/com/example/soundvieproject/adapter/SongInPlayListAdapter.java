package com.example.soundvieproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.ReportSongActivity;
import com.example.soundvieproject.model.ReportDetail;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.SongInPlayList;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.mongodb.mongo.iterable.MongoCursor;

public class SongInPlayListAdapter extends RecyclerView.Adapter<SongInPlayListAdapter.ViewHolder> {


    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference stoRefer;


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private OnItemsClickListener listener = null;

    public interface OnItemsClickListener {
        void OnItemClick(Song song, int pos) throws IOException;
    }
    public interface OnDeleteListener{
        void OnDelete(Song song, View v, int pos) throws IOException;
    }
    private OnDeleteListener delete = null;

    public void setItemClickListener(SongInPlayListAdapter.OnItemsClickListener listener) {
        this.listener = listener;
    }
    public void setDeleteListener(OnDeleteListener listener){
        this.delete = listener;
    }
    Helper helper = Helper.INSTANCE;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_song_in_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (songspl.size() == 0) {
            return;
        }
        Song song = songspl.get(position);
        stoRefer = storage.getReference("images/" + song.getImgCover());
        Glide.with(context).load(stoRefer).into(imgSongpl);
        //  imgSongpl.setImageURI(Uri.parse(song.getImgCover()));
        nameSongpl.setText(song.getNameSong());
        llplay.setOnClickListener(v -> {
            if (listener != null) {
                try {
                    listener.OnItemClick(song, position);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(delete != null){
                   try {
                       delete.OnDelete(song, v, holder.getAdapterPosition());
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
            }
        });
    }

    public SongInPlayListAdapter(ArrayList<Song> songspl, Context context) {
        this.songspl = songspl;
        this.context = context;
    }

    private ArrayList<Song> songspl;
    Context context;

    public ArrayList<Song> getSongspl() {
        return songspl;
    }

    @Override
    public int getItemCount() {
        return songspl.size();
    }

    private ImageView imgSongpl;
    private TextView nameSongpl;
    private TextView nameArtist;
    private ImageButton btnMenu;

    private LinearLayout llplay;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSongpl = itemView.findViewById(R.id.imgSongPl);
            nameSongpl = itemView.findViewById(R.id.nameSongPl);
            btnMenu = itemView.findViewById(R.id.btnMenuEdit);
            llplay = itemView.findViewById(R.id.llplay);
        }
    }

    public void refreshView(int position) {
        notifyItemChanged(position);
    }

    public void clearData() {
        songspl.removeAll(songspl);

    }

    // Method to add new data to the adapter
    public void addData() {

        for(Song s : backup){
            songspl.add(s);

            Log.d("item", s.toString());
        }
        notifyDataSetChanged();
    }

    ArrayList<Song> backup;

}

