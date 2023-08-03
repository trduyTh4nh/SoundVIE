package com.example.soundvieproject.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.soundvieproject.R;


public class HomeArtistFragment extends Fragment {



    public HomeArtistFragment() {

    }


    public static HomeArtistFragment newInstance(String param1, String param2) {
        HomeArtistFragment fragment = new HomeArtistFragment();

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
        return inflater.inflate(R.layout.fragment_home_artist, container, false);
    }
}