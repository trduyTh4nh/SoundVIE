package com.example.soundvieproject.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.soundvieproject.HomeActivity;
import com.example.soundvieproject.R;
import com.example.soundvieproject.adapter.SongInPlayListAdapter;
import com.example.soundvieproject.model.Song;

import java.util.ArrayList;
import java.util.Objects;


public class ListSongFragment extends Fragment {



    public ListSongFragment() {

    }


    public static ListSongFragment newInstance(String param1, String param2) {
        ListSongFragment fragment = new ListSongFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_home, container, false);
        Toolbar mtoolBar = (Toolbar) rootView.findViewById(R.id.toolbar_main);

        ((HomeActivity) requireActivity()).setSupportActionBar(mtoolBar);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_song, container, false);
    }

    RecyclerView recyclerView;

    SongInPlayListAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
     //   adapter = new SongInPlayListAdapter(list(), getContext().getApplicationContext());
        recyclerView = view.findViewById(R.id.rv_songsPl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

//    public ArrayList<Song> list(){
//        ArrayList<Song> list = new ArrayList<>();
//        list.add(new Song("1","Blue Tequila",R.drawable.img_album, "128kbps", "Hi tequila như mọi khi trong tuần", "Táo"));
//        list.add(new Song("2","10 ngàn năm",R.drawable.muoingannam, "128kbps", "Hi tequila như mọi khi trong tuần", "PC"));
//        list.add(new Song("3","Là bạn không thể yêu",R.drawable.labankhongtheyeu, "128kbps", "Hi tequila như mọi khi trong tuần", "Lou Hoàng"));
//        list.add(new Song("4","Chúng ta của hiện tại",R.drawable.chungtacuahientai, "128kbps", "Hi tequila như mọi khi trong tuần", "Sơn Tùng MTP"));
//        return list;
//
//    }
}