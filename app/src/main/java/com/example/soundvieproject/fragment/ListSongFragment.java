package com.example.soundvieproject.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.soundvieproject.R;
import com.example.soundvieproject.adapter.PlaylistAdapter;
import com.example.soundvieproject.model.Album;
import com.example.soundvieproject.model.Playlist;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListSongFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListSongFragment extends Fragment {


    public ListSongFragment() {
        // Required empty public constructor
    }

    public static ListSongFragment newInstance() {
        return new ListSongFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Hello", "hello");
        return inflater.inflate(R.layout.fragment_list_song, container, false);
    }
    RecyclerView rcvList;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rcvList = view.findViewById(R.id.rcvPlayList);
        Log.d("hello", "hello");
        ArrayList<Playlist> p = getPlaylists();
        PlaylistAdapter adap = new PlaylistAdapter(getActivity().getApplicationContext(), p);
        GridLayoutManager l = new GridLayoutManager(getActivity(), 2);
        rcvList.setAdapter(adap);
        rcvList.setLayoutManager(l);
        super.onViewCreated(view, savedInstanceState);

    }
    public ArrayList<Playlist> getPlaylists(){
        ArrayList<Playlist> p = new ArrayList<>();
        String[] names = {"Tổng hợp nhạc 1", "Chiến báo", "Cx hay, cx mạnh", "Cx chiến báo"};
        String[] descs = {"1", "234", "abc", "xyz"};
        int cover = R.drawable.labankhongtheyeu;
        Random r = new Random();
        for(int i = 0; i < 10; i++){
            //p.add(new Playlist(names[r.nextInt(names.length)], cover, descs[r.nextInt(descs.length)]));
        }
        return p;
    }
}