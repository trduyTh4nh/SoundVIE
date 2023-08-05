package com.example.soundvieproject.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.adapter.AlbumAdapter;
import com.example.soundvieproject.model.Album;
import com.example.soundvieproject.model.Playlist;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.iterable.MongoCursor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    RecyclerView rcvAlbum;
    AlbumAdapter adap;
    ArrayList<Playlist> albums;
    Helper h = Helper.INSTANCE;
    public AlbumFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlbumFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlbumFragment newInstance(String param1, String param2) {
        AlbumFragment fragment = new AlbumFragment();

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
        return inflater.inflate(R.layout.fragment_album, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rcvAlbum = view.findViewById(R.id.rcvAlbum);
        albums = new ArrayList<>();

        h.getAlbumByUser(new App.Callback<MongoCursor<Playlist>>() {
            @Override
            public void onResult(App.Result<MongoCursor<Playlist>> result) {
                if(result.isSuccess()){
                    MongoCursor<Playlist> cur = result.get();
                    while (cur.hasNext()){
                        Playlist p = cur.next();
                        albums.add(p);

                    }
                    AlbumAdapter adap = new AlbumAdapter(getActivity().getApplicationContext(), albums);
                    GridLayoutManager l = new GridLayoutManager(getActivity(), 2);
                    rcvAlbum.setAdapter(adap);
                    rcvAlbum.setLayoutManager(l);
                }
            }
        });
    }
}