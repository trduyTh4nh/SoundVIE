package com.example.soundvieproject.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder>{
    ArrayList<Object> arr;
    Context c;

    public interface OnItemsClickListener{
        void OnItemClick(Object obj) throws IOException;
    }
    private OnItemsClickListener listener = null;
    public void setItemClickListener(OnItemsClickListener listener){
        this.listener = listener;
    }
    public SearchResultAdapter(ArrayList<Object> arr, Context c) {
        this.arr = arr;
        this.c = c;
    }

    @Override
    public int getItemViewType(int position) {
        if(arr.get(position) instanceof Song){
            return 0;
        } else if(arr.get(position) instanceof User){
            return 1;
        } else {
            return 2;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == 0) {
            v = LayoutInflater.from(c).inflate(R.layout.component_search_result, parent, false);
        } else {
            v = LayoutInflater.from(c).inflate(R.layout.component_artist_result, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StorageHelper h = new StorageHelper(c);
        FirebaseStorage sto = h.getStorage();
        if(getItemViewType(position) == 0){
            Song s = (Song) arr.get(position);
            StorageReference ref = sto.getReference("images/"+s.getImgCover());
            Glide.with(c).load(ref).into(imgSong);
            tvNameSong.setText(s.getNameSong());


            llResult.setOnClickListener(v -> {
                if(listener != null){
                    try {
                        listener.OnItemClick(s);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

        } else {
            User s = (User) arr.get(position);
            StorageReference ref = sto.getReference("image/"+s.getAvatar());
            Glide.with(c).load(ref).into(imgSong);
            tvNameArtist.setText(s.getName());
            llResult.setOnClickListener(v -> {
                if(listener != null){
                    try {
                        listener.OnItemClick(s);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return arr.size();
    }
    TextView tvNameSong, tvArtistSong, tvNameArtist;
    ImageView imgSong;
    ImageButton btnHeart, btnMore;
    Button btnFollow;
    LinearLayout llResult;
    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llResult = itemView.findViewById(R.id.llResult);
            tvNameArtist = itemView.findViewById(R.id.tvArtist);
            tvNameSong = itemView.findViewById(R.id.tvSongName);
            tvArtistSong = itemView.findViewById(R.id.tvArtistName);
            imgSong = itemView.findViewById(R.id.imgSongPl);
            btnHeart = itemView.findViewById(R.id.btnHeart);
            btnMore = itemView.findViewById(R.id.btnMore);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }
}
