package com.example.soundvieproject.fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.adapter.SearchResultAdapter;
import com.example.soundvieproject.media.Media;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.User;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import io.realm.mongodb.mongo.iterable.MongoCursor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    EditText searchBox;
    ProgressBar searchProgress;
    RecyclerView rcvSong, rcvArtist;
    LinearLayout currentSong;
    ImageButton btnPause, btnResume;
    ImageView img_song;
    TextView song_name, artist;
    Media m = Media.INSTANCE;
    public SearchFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    Helper h = Helper.INSTANCE;
    ArrayList<Object> s;
    ArrayList<Object> a;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        searchBox = getActivity().findViewById(R.id.edtSearch);
        rcvSong = view.findViewById(R.id.rcvSong);
        rcvArtist = view.findViewById(R.id.rcvArtist);
        s = new ArrayList<>();
        currentSong = getActivity().findViewById(R.id.currentSong);
        img_song = getActivity().findViewById(R.id.img_song);
        song_name = getActivity().findViewById(R.id.song_name);
        artist = getActivity().findViewById(R.id.artist);
        btnPause = getActivity().findViewById(R.id.btnPause);
        btnResume = getActivity().findViewById(R.id.btnResume);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                h.getSongByQuery(t -> {
                    if(t.isSuccess()){
                        s = new ArrayList<>();
                        MongoCursor<Song> cur = t.get();
                        while (cur.hasNext()){
                            Song song = cur.next();
                            s.add(new Song(song.getId(), song.getNameSong(), song.getImgCover(), song.getStateData(), song.getLyrics(), song.getArtists(), song.getSong()));
                        }
                        SearchResultAdapter adap = new SearchResultAdapter(s, getActivity().getApplicationContext());
                        rcvSong.setAdapter(adap);
                        LinearLayoutManager l = new LinearLayoutManager(getActivity().getApplicationContext());
                        rcvSong.setLayoutManager(l);
                        adap.setItemClickListener(new SearchResultAdapter.OnItemsClickListener() {
                            @Override
                            public void OnItemClick(Object obj) throws IOException {
                                Song s = (Song) obj;
                                if(m.getPlayer().isPlaying()){
                                    m.getPlayer().stop();
                                    m.setPlayer(new MediaPlayer());
                                }
                                m.setContext(getActivity().getApplicationContext());
                                m.playMusic(s);
                                currentSong.setVisibility(View.VISIBLE);
                                btnPause.setOnClickListener(v -> {
                                    btnPause.setVisibility(View.GONE);
                                    btnResume.setVisibility(View.VISIBLE);
                                    m.pause();
                                });
                                btnResume.setOnClickListener(v -> {
                                    btnResume.setVisibility(View.GONE);
                                    btnPause.setVisibility(View.VISIBLE);
                                    m.start();
                                });
                                song_name.setText(s.getNameSong());
                                StorageHelper help = new StorageHelper(getActivity().getApplicationContext());
                                StorageReference ref = help.getStorage().getReference("images/" + s.getImgCover());
                                Glide.with(getActivity()).load(ref).into(img_song);
                            }
                        });
                    }
                }, searchBox.getText().toString());
                h.getArtistByQuery(t -> {
                    if(t.isSuccess()){
                        a = new ArrayList<>();
                        MongoCursor<User> u = t.get();
                        while(u.hasNext()){
                            User usr = u.next();
                            a.add(usr);
                        }
                        SearchResultAdapter adapter = new SearchResultAdapter(a, getActivity().getApplicationContext());
                        rcvArtist.setAdapter(adapter);
                        LinearLayoutManager l = new LinearLayoutManager(getActivity().getApplicationContext());
                        rcvArtist.setLayoutManager(l);
                    }
                }, searchBox.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}