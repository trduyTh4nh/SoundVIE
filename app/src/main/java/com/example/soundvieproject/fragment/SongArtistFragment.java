package com.example.soundvieproject.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.soundvieproject.ArtistUpMusicActivity;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.adapter.SongArtistAdapter;
import com.example.soundvieproject.adapter.VpPlaylistAdapter;
import com.example.soundvieproject.model.ArtistInSong;
import com.example.soundvieproject.model.Song;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class SongArtistFragment extends Fragment {
    Button btnAddSong;
    RecyclerView rcvSong;
    SongArtistAdapter adap;
    ArrayList<Song> songs;
    ProgressBar bar;
    TextView tvSongCount;
    TabLayout tlSong;
    ViewPager2 vpSong;
    public static SongArtistFragment newInstance(String param1, String param2) {
        SongArtistFragment fragment = new SongArtistFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_artist, container, false);
    }

    @Override
    public void onResume() {
        songs = new ArrayList<>();
        adap = new SongArtistAdapter(getActivity().getApplicationContext(), songs);
        LinearLayoutManager l = new LinearLayoutManager(getActivity());
        rcvSong.setAdapter(adap);
        rcvSong.setLayoutManager(l);
        h.getSongByArtist(h.getA().currentUser().getId(), new App.Callback<MongoCursor<ArtistInSong>>() {
            @Override
            public void onResult(App.Result<MongoCursor<ArtistInSong>> result) {
                if(result.isSuccess()){
                    MongoCursor<ArtistInSong> cur = result.get();
                    while (cur.hasNext()){
                        ArtistInSong ar = cur.next();
                        Log.d("CC", ar.toString());
                        h.getSongByIdSong(ar.getIdSong(), new App.Callback<Song>() {
                            @Override
                            public void onResult(App.Result<Song> result) {
                                if(result.isSuccess()){
                                    Song s = result.get();
                                    if(s != null){
                                        songs.add(s);
                                        adap.notifyItemInserted(songs.size() - 1);
                                        tvSongCount.setText(songs.size() + " Bài hát");
                                        Log.d("CC2", s.toString());
                                    }


                                } else {
                                    Log.d("ERR", result.getError().toString());
                                }

                            }
                        });
                    }
                    bar.setVisibility(View.GONE);
                }
            }
        });
        super.onResume();
    }

    Helper h = Helper.INSTANCE;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvSongCount = view.findViewById(R.id.tvSongNum);
        vpSong = view.findViewById(R.id.vpUser);
        tlSong = view.findViewById(R.id.tlUser);
        bar = getActivity().findViewById(R.id.loadingProgress);

        rcvSong = view.findViewById(R.id.rcvSongArtist);

        btnAddSong = view.findViewById(R.id.btnAddSong);
        btnAddSong.setOnClickListener(v -> {
            Intent i = new Intent(getActivity().getApplicationContext(), ArtistUpMusicActivity.class);
            startActivity(i);
        });
        bar.setVisibility(View.VISIBLE);


    }
}