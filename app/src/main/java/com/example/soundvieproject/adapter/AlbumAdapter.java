package com.example.soundvieproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.AddAlbumActivity;
import com.example.soundvieproject.AddPlaylistActivity;
import com.example.soundvieproject.AlbumActivity;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.PlaylistActivity;
import com.example.soundvieproject.R;
import com.example.soundvieproject.model.Playlist;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{

    ImageView ivCover;
    TextView tvTitle, tvDesc;
    Context context;
    Helper h = Helper.INSTANCE;
    ArrayList<Playlist> pl;
    StorageReference storageReference;
    LinearLayout ll, btnAdd;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    public AlbumAdapter(Context c, ArrayList<Playlist> playlists) {
        context = c;
        pl = playlists;
    }

    public ArrayList<Playlist> getPl() {
        return pl;
    }

    public void setPl(ArrayList<Playlist> pl) {
        this.pl = pl;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        }
        return 1;
    }

    @NonNull
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(context).inflate(R.layout.add_album, parent, false);
        } else {
            v = LayoutInflater.from(context).inflate(R.layout.album_layout, parent, false);
        }
        return new AlbumAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            if (btnAdd != null) {
                btnAdd.setOnClickListener(v -> {
                    Intent i = new Intent(context, AddAlbumActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                });
            }
            return;
        }
        Playlist p = pl.get(position - 1);

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        ll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu menu = new PopupMenu(context, view);
                menu.inflate(R.menu.artist_song_menu);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        h.deleteAlbum(p.getId(), t -> {
                            if(pl.size() > 0)
                                pl.remove(p);
                            Toast.makeText(context, "Xoá thành công danh sách phát " + p.getName(), Toast.LENGTH_SHORT).show();
                            notifyItemRemoved(holder.getAdapterPosition());
                        });
                        return false;
                    }
                });
                menu.show();
                return false;
            }
        });

        tvTitle.setText(p.getName());
        storageReference = firebaseStorage.getReference("images/" + p.getImage());
        Glide.with(context).load(storageReference).into(ivCover);
        tvDesc.setText(p.getDes());

        if (position % 2 != 0) {
            ll.setGravity(Gravity.END);
        }
    }

    @Override
    public int getItemCount() {
        return pl.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            ivCover = itemView.findViewById(R.id.ivCover);
            ll = itemView.findViewById(R.id.llLayout);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}
