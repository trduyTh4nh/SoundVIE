package com.example.soundvieproject.fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.PremiumRegisterActivity;
import com.example.soundvieproject.R;
import com.example.soundvieproject.adapter.SongAdapter;
import com.example.soundvieproject.media.Media;
import com.example.soundvieproject.media.MediaPlayerUtils;
import com.example.soundvieproject.model.Song;
import com.google.firebase.storage.FirebaseStorage;

import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.mongodb.mongo.iterable.MongoCursor;


public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    Helper instance = Helper.INSTANCE;
    ArrayList<Song> list;
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    Button btnPremium;
    SongAdapter adapter;
    SongAdapter adapter2;
    RecyclerView rcv2;
    LinearLayout currentSong;
    ImageView img_song;
    TextView song_name, artist;
    Media media;
    ImageButton btnPause, btnResume;
    RecyclerView recyclerView;
    MediaPlayer p;
    ProgressBar b;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        media = Media.INSTANCE;
        p = new MediaPlayer();
        media.setContext(getActivity().getApplicationContext());
        btnPause = getActivity().findViewById(R.id.btnPause);
        btnResume = getActivity().findViewById(R.id.btnResume);
        recyclerView = view.findViewById(R.id.rv_songs);
        currentSong = getActivity().findViewById(R.id.currentSong);
        img_song = getActivity().findViewById(R.id.img_song);
        song_name = getActivity().findViewById(R.id.song_name);
        artist = getActivity().findViewById(R.id.artist);
        b = getActivity().findViewById(R.id.loadingProgress);
        b.setVisibility(View.VISIBLE);
        list(view);


    }
    public void list(View view){
        list = new ArrayList<>();
        instance.getSong(t -> {
            if(t.isSuccess()){
                MongoCursor<Song> cur = t.get();
                while (cur.hasNext()){

                    Song s = cur.next();
                    Log.d("Test", s.toString());
                    list.add(new Song(s.getId(), s.getNameSong(), s.getImgCover(), s.getStateData(), s.getLyrics(), s.getArtists(), s.getSong()));
                }

                FirebaseStorage sto = FirebaseStorage.getInstance();
                adapter = new SongAdapter(getActivity().getApplicationContext(), list, sto);
                adapter2 = new SongAdapter(getActivity().getApplicationContext(), list, sto);
                rcv2 = view.findViewById(R.id.rv_songsPopular);

                LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);


                LinearLayoutManager layoutManager1 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
                rcv2.setLayoutManager(layoutManager1);
                rcv2.setAdapter(adapter);
                b.setVisibility(View.GONE);
                adapter.setItemClickListener(new SongAdapter.OnItemsClickListener() {
                    @Override
                    public void OnItemClick(Song song) throws IOException {
                        if(media.getPlayer().isPlaying()){
                            media.getPlayer().stop();
                            media.setPlayer(new MediaPlayer());
                        }
                        media.setContext(getActivity().getApplicationContext());
                        media.playMusic(Uri.parse(song.getSong()));

                        btnResume.setVisibility(View.GONE);
                        btnPause.setVisibility(View.VISIBLE);
                        currentSong.setVisibility(View.VISIBLE);
                        //img_song.setImageResource(song.getImgCover());
                        song_name.setText(song.getNameSong());
                        artist.setText(song.getArtist());
                        btnPause.setOnClickListener(v -> {
                            btnPause.setVisibility(View.GONE);
                            btnResume.setVisibility(View.VISIBLE);
                            media.pause();
                        });
                        btnResume.setOnClickListener(v -> {
                            btnResume.setVisibility(View.GONE);
                            btnPause.setVisibility(View.VISIBLE);
                            media.start();
                        });
                    }
                });
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Không thể lấy bài hát.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}