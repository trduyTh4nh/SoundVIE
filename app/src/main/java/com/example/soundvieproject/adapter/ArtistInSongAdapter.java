package com.example.soundvieproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.model.User;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ArtistInSongAdapter extends RecyclerView.Adapter<ArtistInSongAdapter.ViewHolder> {
    Context context;
    ArrayList<User> usrs;
    ImageView imgSongPl;
    TextView tvArtist;
    ImageButton btnMore;


    public ArtistInSongAdapter(Context context, ArrayList<User> usrs) {
        this.context = context;
        this.usrs = usrs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.artist_in_song_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User u = usrs.get(position);
        StorageHelper helper = new StorageHelper(context);
        StorageReference ref = helper.getStorage().getReference("image/" + u.getAvatar());
        Glide.with(context).load(ref).into(imgSongPl);
        tvArtist.setText(u.getName());
        btnMore.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(context, v);
            menu.inflate(R.menu.artist_song_menu);
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if(holder.getAdapterPosition() == 0){
                        Toast.makeText(context, "Không được xoá chính bản thân", Toast.LENGTH_SHORT).show();
                    } else {
                        usrs.remove(u);
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                    return false;
                }
            });
            menu.show();
        });
    }

    @Override
    public int getItemCount() {
        return usrs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSongPl = itemView.findViewById(R.id.imgSongPl);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            btnMore = itemView.findViewById(R.id.btnMore);
        }
    }
}
