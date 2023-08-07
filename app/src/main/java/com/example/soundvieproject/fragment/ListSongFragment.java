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

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.adapter.PlaylistAdapter;
import com.example.soundvieproject.model.Album;
import com.example.soundvieproject.model.Playlist;

import java.util.ArrayList;
import java.util.Random;

import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.iterable.MongoCursor;

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
    ArrayList<Playlist> arrayPlayList;
    Helper helper = Helper.INSTANCE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Hello", "hello");
        return inflater.inflate(R.layout.fragment_list_song, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPlaylists();
    }

    RecyclerView rcvList;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rcvList = view.findViewById(R.id.rcvPlayList);
        super.onViewCreated(view, savedInstanceState);
    }
    public void getPlaylists(){
        arrayPlayList = new ArrayList<>();
        helper.getPlayList(result -> {
            if(result.isSuccess()){
                MongoCursor<Playlist> cursor = result.get();
                while (cursor.hasNext()){
                    Playlist playlist = cursor.next();
                    Log.d("Check", playlist.toString());
                    arrayPlayList.add(new Playlist(playlist.getId(), playlist.getName(), playlist.getImage(), playlist.getIdUser(), playlist.getDes()));
                }
                PlaylistAdapter adap = new PlaylistAdapter(getActivity().getApplicationContext(), arrayPlayList);
                GridLayoutManager l = new GridLayoutManager(getActivity(), 2);
                rcvList.setAdapter(adap);
                rcvList.setLayoutManager(l);

            }
        });


    }
}