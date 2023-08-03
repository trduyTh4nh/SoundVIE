package com.example.soundvieproject.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.model.User;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AddArtistAdapter extends RecyclerView.Adapter<AddArtistAdapter.ViewHolder>{
    Context context;
    ArrayList<User> usrs, usrs1;
    Helper h = Helper.INSTANCE;
    boolean[] sel;
    ImageView imgSongPl;
    TextView tvArtist;
    CheckBox cbSel;

    public AddArtistAdapter(Context context, ArrayList<User> usrs, ArrayList<User> usrList) {
        this.context = context;
        this.usrs = usrs;
        this.usrs1 = usrList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.artist_add_layout, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        sel = new boolean[usrs.size()];
        User u = usrs.get(position);
        if(u.getIdUser().equals(h.getUser().getId()) || usrs1.contains(u)){
            cbSel.setEnabled(false);
            tvArtist.setTextColor(Color.parseColor("#8C000000"));
            imgSongPl.setAlpha(0.75f);
        }
        StorageHelper helper = new StorageHelper(context);
        StorageReference ref = helper.getStorage().getReference("image/"+u.getAvatar());
        Glide.with(context).load(ref).into(imgSongPl);
        tvArtist.setText(u.getName());
        cbSel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    sel[holder.getAdapterPosition()] = true;
                } else {
                    sel[holder.getAdapterPosition()] = false;
                }
            }
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
            cbSel = itemView.findViewById(R.id.cbSel);
        }
    }

    public void setUsrs1(ArrayList<User> usrs1) {
        this.usrs1 = usrs1;
    }

    public boolean[] getSel() {
        return sel;
    }
}
