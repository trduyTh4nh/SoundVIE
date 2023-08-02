package com.example.soundvieproject.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.EditUserActivity;
import com.example.soundvieproject.R;
import com.example.soundvieproject.adapter.VpPlaylistAdapter;
import com.example.soundvieproject.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import io.realm.mongodb.App;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArtistProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArtistProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArtistProfileFragment() {
        // Required empty public constructor
    }
    private User u;
    TextView tvName, tvDesc;
    ImageView ivUser;
    Button btnEdit;
    ProgressBar progress;
    Helper h = Helper.INSTANCE;
    TabLayout tlSong;
    ViewPager2 vpSong;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArtistProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArtistProfileFragment newInstance(String param1, String param2) {
        ArtistProfileFragment fragment = new ArtistProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvName = view.findViewById(R.id.tvName);
        ivUser = view.findViewById(R.id.ivUser);
        btnEdit = view.findViewById(R.id.btnEdit);
        tvDesc = view.findViewById(R.id.tvDesc);
        progress = getActivity().findViewById(R.id.loadingProgress);
        vpSong = view.findViewById(R.id.vpUser);
        tlSong = view.findViewById(R.id.tlUser);
        progress.setVisibility(View.VISIBLE);
        h.getUserCurrentBy(new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if(result.isSuccess()){
                    u = result.get();
                    Log.d("user", u.toString());
                    tvName.setText(u.getName());
                    tvDesc.setText(u.getMoTa().equals("") ? "Không có mô tả" : u.getMoTa());
                    if(!u.getAvatar().equals("")){
                        StorageHelper h = new StorageHelper(getContext());
                        FirebaseStorage sto = h.getStorage();
                        StorageReference ref = sto.getReference("image/"+u.getAvatar());
                        Glide.with(getContext()).load(ref).into(ivUser);
                    }
                    progress.setVisibility(View.GONE);
                }
            }
        });
        btnEdit.setOnClickListener(v -> {
            Intent i = new Intent(getActivity().getApplicationContext(), EditUserActivity.class);
            startActivity(i);
        });
        VpPlaylistAdapter adap = new VpPlaylistAdapter(getActivity(), new ArrayList<>());
        vpSong.setAdapter(adap);
        new TabLayoutMediator(tlSong, vpSong, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if(position == 0){
                    tab.setText("Bài hát");

                } else {
                    tab.setText("Album");
                }
            }
        }).attach();
    }
}