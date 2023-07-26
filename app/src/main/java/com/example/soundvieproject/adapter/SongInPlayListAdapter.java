package com.example.soundvieproject.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import io.realm.mongodb.mongo.iterable.MongoCursor;

public class SongInPlayListAdapter extends RecyclerView.Adapter<SongInPlayListAdapter.ViewHolder> {

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference stoRefer;
    Helper helper = Helper.INSTANCE;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_song_in_playlist,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Song song = songspl.get(position);
        stoRefer = storage.getReference("images/" + song.getImgCover());
        Glide.with(context).load(stoRefer).into(imgSongpl);
      //  imgSongpl.setImageURI(Uri.parse(song.getImgCover()));
        nameSongpl.setText(song.getNameSong());
        nameArtist.setText(song.getArtis());


        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.delete_music:{
                               helper.deleteSongWidthID(result -> {
                                   if(result.isSuccess()){
                                       Toast.makeText(context, "Xóa thành công " + songspl.get(position).getNameSong(), Toast.LENGTH_SHORT).show();

                                   }
                                   else
                                       Log.d("Delete: ", "không thành công!" + result.getError());

                               },songspl.get(position).getId());
                               songspl.remove(position);
                                notifyItemRemoved(position);
                                break;
                            }
                            case R.id.report_music:{
                                Intent i = new Intent(context, ReportSongActivity.class);
                                Bundle data = new Bundle();
                                data.putString("idSongReport", songspl.get(position).getId().toString());
                                i.putExtras(data);
                                context.startActivity(i);
                                break;
                            }
                            default:
                                Toast.makeText(context, "Không có gì", Toast.LENGTH_SHORT).show(); break;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.menu_edit_music_in_playlist);
                popupMenu.show();
            }
        });
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
    private ImageButton btnMenu;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSongpl = itemView.findViewById(R.id.imgSongPl);
            nameSongpl = itemView.findViewById(R.id.nameSongPl);
            nameArtist = itemView.findViewById(R.id.artistSongPl);
            btnMenu = itemView.findViewById(R.id.btnMenuEdit);

        }
    }

}
