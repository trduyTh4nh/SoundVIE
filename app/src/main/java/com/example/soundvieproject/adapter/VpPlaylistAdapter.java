package com.example.soundvieproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.model.ArtistInSong;
import com.example.soundvieproject.model.Song;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class VpPlaylistAdapter extends RecyclerView.Adapter<VpPlaylistAdapter.ViewHolder>{
    Context context;
    RecyclerView rcvViewPager;
    ProgressBar bar;
    Helper h = Helper.INSTANCE;
    ArrayList<Song> arrSong;
    public VpPlaylistAdapter(Context context, ArrayList<Song> arrSong) {
        this.context = context;
        this.arrSong = arrSong;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.artist_viewpager, parent, false);
        return new ViewHolder(v);
    }
    SongArtistAdapter adap;
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position == 0){
            adap = new SongArtistAdapter(context, arrSong);
            LinearLayoutManager l = new LinearLayoutManager(context);
            rcvViewPager.setLayoutManager(l);
            rcvViewPager.setAdapter(adap);
            h.getSongByArtist(h.getA().currentUser().getId(), new App.Callback<MongoCursor<ArtistInSong>>() {
                @Override
                public void onResult(App.Result<MongoCursor<ArtistInSong>> result) {
                    if(result.isSuccess()){
                        MongoCursor<ArtistInSong> cur = result.get();
                        while (cur.hasNext()){
                            ArtistInSong ar = cur.next();
                            Log.d("CC", ar.toString());
                            h.getSongByIdSong(ar.getIdSong(), result1 -> {
                                if(result1.isSuccess()){
                                    Song s = result1.get();
                                    if(s != null){
                                        arrSong.add(s);
                                        adap.notifyItemInserted(arrSong.size() - 1);
                                    }
                                } else {
                                    Log.d("ERR", result1.getError().toString());
                                }

                            });
                        }
                    }
                }
            });
        } else {

        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rcvViewPager = itemView.findViewById(R.id.rcvArtist);
        }
    }
}
