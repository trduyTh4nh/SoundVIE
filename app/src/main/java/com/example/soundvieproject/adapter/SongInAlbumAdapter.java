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
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.model.Song;
import com.google.firebase.storage.StorageReference;

import org.bson.types.ObjectId;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.result.DeleteResult;

public class SongInAlbumAdapter extends RecyclerView.Adapter<SongInAlbumAdapter.ViewHolder>{
    private ArrayList<Song> songs;
    private Context context;
    ObjectId idPl;
    Helper h = Helper.INSTANCE;

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public SongInAlbumAdapter(ArrayList<Song> songs, Context context, ObjectId idPl) {
        this.songs = songs;
        this.context = context;
        this.idPl = idPl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.song_artist_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song s = songs.get(position);
        StorageHelper helper = new StorageHelper(context);
        StorageReference ref = helper.getStorage().getReference("images/"+s.getImgCover());
        Glide.with(context).load(ref).into(holder.imgSongPl);
        holder.tvSongName.setText(s.getNameSong());
        holder.btnMore.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(context, v);
            menu.inflate(R.menu.artist_song_menu);
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    h.deleteSongInAlbum(idPl, s.getId(), new App.Callback<DeleteResult>() {
                        @Override
                        public void onResult(App.Result<DeleteResult> result) {
                            if(result.isSuccess()){
                                if(songs.size() > 0){
                                    songs.remove(s);
                                }
                                notifyItemRemoved(holder.getAdapterPosition());
                                Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    return false;
                }
            });
            menu.show();
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgSongPl;
        TextView tvSongName;
        ImageButton btnMore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSongPl = itemView.findViewById(R.id.imgSongPl);
            tvSongName = itemView.findViewById(R.id.tvSongName);
            btnMore = itemView.findViewById(R.id.btnMore);
        }
    }
}
