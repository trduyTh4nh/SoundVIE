package com.example.soundvieproject.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.soundvieproject.R;
import com.example.soundvieproject.adapter.SongAdapter;
import com.example.soundvieproject.media.Media;
import com.example.soundvieproject.model.Song;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    public HomeFragment() {
    }


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
    SongAdapter adapter;
    SongAdapter adapter2;
    RecyclerView rcv2;
    LinearLayout currentSong;
    ImageView img_song;
    TextView song_name, artist;
    Media media;
    ImageButton btnPause, btnResume;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        media = Media.INSTANCE;
        media.setContext(getActivity().getApplicationContext());
        btnPause = getActivity().findViewById(R.id.btnPause);
        btnResume = getActivity().findViewById(R.id.btnResume);
        RecyclerView recyclerView = view.findViewById(R.id.rv_songs);
        currentSong = getActivity().findViewById(R.id.currentSong);
        img_song = getActivity().findViewById(R.id.img_song);
        song_name = getActivity().findViewById(R.id.song_name);
        artist = getActivity().findViewById(R.id.artist);
        adapter = new SongAdapter(getActivity().getApplicationContext(), list());
        adapter2 = new SongAdapter(getActivity().getApplicationContext(), list());
        rcv2 = view.findViewById(R.id.rv_songsPopular);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new SongAdapter.OnItemsClickListener() {
            @Override
            public void OnItemClick(Song song) {
                media.start();
                btnResume.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
                currentSong.setVisibility(View.VISIBLE);
                img_song.setImageResource(song.getImgCover());
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
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rcv2.setLayoutManager(layoutManager1);
        rcv2.setAdapter(adapter);
    }

    public ArrayList<Song> list(){
        // comment để mai connect database.
        ArrayList<Song> list = new ArrayList<>();
        list.add(new Song("1","Blue Tequila",R.drawable.img_album, "128kbps", "Hi tequila như mọi khi trong tuần", "Táo"));
        list.add(new Song("2","10 ngàn năm",R.drawable.muoingannam, "128kbps", "Hi tequila như mọi khi trong tuần", "PC"));
        list.add(new Song("3","Là bạn không thể yêu",R.drawable.labankhongtheyeu, "128kbps", "Hi tequila như mọi khi trong tuần", "Lou Hoàng"));
        list.add(new Song("4","Chúng ta của hiện tại",R.drawable.chungtacuahientai, "128kbps", "Hi tequila như mọi khi trong tuần", "Sơn Tùng MTP"));
        return list;

    }
}