package com.example.soundvieproject.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.R;
import com.example.soundvieproject.model.ArtistInSong;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.User;
import com.google.firebase.storage.StorageReference;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.iterable.MongoCursor;


public class HomeArtistFragment extends Fragment {



    public HomeArtistFragment() {

    }
    Helper h = Helper.INSTANCE;
    TextView luotnghe, sotienhh;
    ProgressBar prog;
    public static HomeArtistFragment newInstance(String param1, String param2) {
        HomeArtistFragment fragment = new HomeArtistFragment();

        return fragment;
    }
    double tienHH;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    int luotnghee;
    TextView tvHelloArtist;
    ImageView artistImg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_artist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvHelloArtist = view.findViewById(R.id.helloArtist);
        artistImg = view.findViewById(R.id.imgArtist);
        prog = getActivity().findViewById(R.id.loadingProgress);
        prog.setVisibility(View.VISIBLE);
        h.getUserCurrentBy(new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                prog.setVisibility(View.GONE);
                if(result.isSuccess()){
                    User u = result.get();
                    tvHelloArtist.setText("Xin chào, " + u.getName());
                    StorageHelper h = new StorageHelper(getActivity());
                    StorageReference ref = h.getStorage().getReference("image/"+u.getAvatar());
                    Glide.with(getActivity()).load(ref).into(artistImg);
                }

            }
        });
        luotnghe = view.findViewById(R.id.luotnghe);
        sotienhh = view.findViewById(R.id.sotienhh);
        luotnghee = 0;
        h.getSongByArtist(h.getUser().getId(), new App.Callback<MongoCursor<ArtistInSong>>() {
            @Override
            public void onResult(App.Result<MongoCursor<ArtistInSong>> result) {
                if (result.isSuccess()){
                    MongoCursor<ArtistInSong> s = result.get();
                    while (s.hasNext()){
                        ArtistInSong a = s.next();
                        h.getSongByIdSong(a.getIdSong(), new App.Callback<Song>() {
                            @Override
                            public void onResult(App.Result<Song> result) {
                                if(result.isSuccess()){
                                    Song s = result.get();
                                    if(s != null){
                                        luotnghee+=result.get().getLuotnghe();
                                        luotnghe.setText("" + luotnghee);
                                        tienHH = luotnghee * 0.5;
                                        sotienhh.setText(tienHH + " VND");
                                    }

                                }
                            }
                        });
                    }
                }

            }
        });

    }
}