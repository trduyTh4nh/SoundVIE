package com.example.soundvieproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.soundvieproject.AlbumActivity;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.model.Playlist;
import com.example.soundvieproject.model.Song;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.result.DeleteResult;

public class AlbumLinearLayoutAdapter extends RecyclerView.Adapter<AlbumLinearLayoutAdapter.ViewHolder>{
    ArrayList<Playlist> playlists;
    Context context;

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public AlbumLinearLayoutAdapter(ArrayList<Playlist> playlists, Context context) {
        this.playlists = playlists;
        this.context = context;
    }
    Helper h = Helper.INSTANCE;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.song_artist_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Playlist p = playlists.get(position);
        holder.tvName.setText(p.getName());
        StorageHelper sto = new StorageHelper(context);
        StorageReference ref = sto.getStorage().getReference("images/"+p.getImage());
        Glide.with(context).load(ref).into(holder.imgPl);
        holder.llResult.setOnClickListener(v -> {
            Intent i = new Intent(context, AlbumActivity.class);
            Bundle b = new Bundle();
            b.putString("namepl", p.getName());
            b.putString("despl", p.getDes());
            b.putString("iduser", p.getIdUser());
            b.putString("image", p.getImage());
            b.putString("idPl", p.getId().toString());
            i.putExtras(b);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        });
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(context, view);
                menu.inflate(R.menu.artist_song_menu);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        h.deleteAlbum(p.getId(), new App.Callback<DeleteResult>() {
                            @Override
                            public void onResult(App.Result<DeleteResult> result) {
                                if(result.isSuccess()){
                                    if(playlists.size() > 0){
                                        playlists.remove(p);
                                    }
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    Toast.makeText(context, "Đã xoá " + p.getName(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPl;
        TextView tvName;
        LinearLayout llResult;
        ImageButton btnMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPl = itemView.findViewById(R.id.imgSongPl);
            llResult = itemView.findViewById(R.id.llResult);
            tvName = itemView.findViewById(R.id.tvSongName);
            btnMore = itemView.findViewById(R.id.btnMore);
        }
    }
}
