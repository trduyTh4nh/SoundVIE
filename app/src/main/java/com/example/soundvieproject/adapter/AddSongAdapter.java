package com.example.soundvieproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.model.Song;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AddSongAdapter extends RecyclerView.Adapter<AddSongAdapter.ViewHolder>{
    Context context;
    ArrayList<Song> arrSong;
    boolean[] checkList;

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public AddSongAdapter(Context context, ArrayList<Song> arrSong) {
        this.context = context;
        this.arrSong = arrSong;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.add_song_layout, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song s = arrSong.get(holder.getAdapterPosition());
        StorageHelper help = new StorageHelper(context);
        StorageReference ref = help.getStorage().getReference("images/"+s.getImgCover());
        Glide.with(context).load(ref).into(holder.imgPl);
        holder.tvSong.setText(s.getNameSong());
        holder.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("pos", String.valueOf(position));
                checkList[position] = b;
                for(boolean i : checkList){
                    Log.d("Checkmate", i ? "true" : "false");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrSong.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox cbCheck;
        TextView tvSong;
        ImageView imgPl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbCheck = itemView.findViewById(R.id.cbSel);
            tvSong = itemView.findViewById(R.id.tvSong);
            imgPl = itemView.findViewById(R.id.imgSongPl);
        }
    }

    public void setCheckList(boolean[] checkList) {
        this.checkList = checkList;
    }

    public boolean[] getCheckList() {
        return checkList;
    }
}
