package com.example.soundvieproject.fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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

import com.bumptech.glide.Glide;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.PremiumRegisterActivity;
import com.example.soundvieproject.R;
import com.example.soundvieproject.adapter.SongAdapter;
import com.example.soundvieproject.media.Media;
import com.example.soundvieproject.media.MediaPlayerUtils;
import com.example.soundvieproject.model.ArtistInSong;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.RejectedExecutionException;

import io.realm.RealmList;
import io.realm.internal.async.RealmThreadPoolExecutor;
import io.realm.mongodb.App;
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

    @Override
    public void onResume() throws RejectedExecutionException {
        super.onResume();
        Song songCurrent = media.getCurrentSong();
        if (songCurrent != null) {
            StorageHelper help = new StorageHelper(getActivity().getApplicationContext());
            StorageReference ref = help.getStorage().getReference("images/" + songCurrent.getImgCover());
            Glide.with(getActivity()).load(ref).into(img_song);

            song_name.setText(songCurrent.getNameSong());
            if(songCurrent.getId() != null){
                instance.getArtitsbyIDSongPlaying(result1 -> {
                    if (result1.isSuccess()) {
                        ArtistInSong artistOfSongPlaying = result1.get();
                        if (artistOfSongPlaying == null) {
                            artist.setText("null");
                        } else {
                            Log.d("Artist of song: ", artistOfSongPlaying.toString());
                            instance.getUserByObjID(artistOfSongPlaying.getIdUser(), new App.Callback<User>() {
                                @Override
                                public void onResult(App.Result<User> kq) {
                                    User artists = kq.get();
                                    artist.setText(artists.getName());
                                }
                            });
                        }
                    }

                }, String.valueOf(songCurrent.getId()));
            }
            artist.setText(songCurrent.getArtist());
        }
    }

    public void list(View view) {
        list = new ArrayList<>();
        instance.getPopularSong(t -> {
            if (t.isSuccess()) {
                MongoCursor<Song> cur = t.get();
                while (cur.hasNext()) {
                    ArrayList<String> artists = new ArrayList<>();
                    Song song = cur.next();
                    Log.d("Test", song.toString());

                    instance.getSongArtists(song.getId(), t1 -> {
                        if (t1.isSuccess()) {
                            MongoCursor<ArtistInSong> cur1 = t1.get();
                            while (cur1.hasNext()) {
                                Log.d(song.getNameSong(), "Has artist");
                                ArtistInSong ar = cur1.next();
                                instance.getUserByObjID(ar.getIdUser(), t2 -> {
                                    if (t2.isSuccess()) {
                                        User u = t2.get();
                                        Log.d(song.getNameSong(), u.getName());
                                        artists.add(u.getName());
                                        Log.d(song.getNameSong(), String.join(", ", artists));
                                        list.add(new Song(song.getId(), song.getNameSong(), song.getImgCover(), song.getStateData(), song.getLyrics(), song.getSong(), String.join(", ", artists), song.getLuotnghe()));
                                        list.sort(new Comparator<Song>() {
                                            @Override
                                            public int compare(Song song, Song t1) {
                                                return t1.getLuotnghe() - song.getLuotnghe();
                                            }
                                        });
                                        FirebaseStorage sto = FirebaseStorage.getInstance();
                                        adapter = new SongAdapter(getContext(), list, sto);
                                        adapter2 = new SongAdapter(getContext(), list, sto);
                                        rcv2 = view.findViewById(R.id.rv_songsPopular);

                                        LinearLayoutManager layoutManager = new LinearLayoutManager((FragmentActivity) getContext(), LinearLayoutManager.HORIZONTAL, false);
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setAdapter(adapter);


                                        LinearLayoutManager layoutManager1 = new LinearLayoutManager((FragmentActivity) getContext(), LinearLayoutManager.HORIZONTAL, false);
                                        rcv2.setLayoutManager(layoutManager1);
                                        rcv2.setAdapter(adapter);
                                        b.setVisibility(View.GONE);
                                        adapter.setItemClickListener(new SongAdapter.OnItemsClickListener() {
                                            @Override
                                            public void OnItemClick(Song song, int index) throws IOException {
                                                if (media.getPlayer().isPlaying()) {
                                                    media.getPlayer().stop();
                                                    media.getPlayer().release();
                                                    media.setPlayer(new MediaPlayer());
                                                }
                                                media.setContext(getActivity().getApplicationContext());
                                                /// xử lý truyền dũ liệu bài hát

                                                media.playMusicInPlaylist(list, index);
                                                media.setCurrentSong(song);

//                        media.setNextSong(list.get(index++));

//                        int k = index;
//
//                        if((index + 1) <= list.size() - 1){
//                            Song nextSong = list.get(k++);
//                            media.setNextSong(nextSong);
//                        }
//
//                        if((index - 1) >= 0){
//                            Song prevSong = list.get(k--);
//                            media.setPrevSong(prevSong);
//                        }


                                                btnResume.setVisibility(View.GONE);
                                                btnPause.setVisibility(View.VISIBLE);
                                                currentSong.setVisibility(View.VISIBLE);


                                                //img_song.setImageResource(song.getImgCover());
                                                song_name.setText(song.getNameSong());
                                                instance.getArtitsbyIDSongPlaying(result1 -> {
                                                    if (result1.isSuccess()) {
                                                        ArtistInSong artistOfSongPlaying = result1.get();
                                                        if (artistOfSongPlaying == null) {
                                                            artist.setText("null");
                                                        } else {
                                                            Log.d("Artist of song: ", artistOfSongPlaying.toString());
                                                            instance.getUserByObjID(artistOfSongPlaying.getIdUser(), new App.Callback<User>() {
                                                                @Override
                                                                public void onResult(App.Result<User> kq) {
                                                                    User artists = kq.get();
                                                                    artist.setText(artists.getName());
                                                                }
                                                            });
                                                        }
                                                    }

                                                }, String.valueOf(song.getId()));
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

                                                StorageHelper help = new StorageHelper(getActivity().getApplicationContext());
                                                StorageReference ref = help.getStorage().getReference("images/" + song.getImgCover());
                                                Glide.with(getActivity()).load(ref).into(img_song);

                                            }
                                        });
                                    }
                                });
                            }

                        } else {
                            Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Không thể lấy bài hát.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}