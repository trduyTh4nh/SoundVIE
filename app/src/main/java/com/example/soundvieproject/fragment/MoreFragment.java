package com.example.soundvieproject.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.soundvieproject.R;
import com.example.soundvieproject.adapter.PremiumFeatureAdapter;
import com.example.soundvieproject.model.Feature;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MoreFragment newInstance(String param1, String param2) {
        MoreFragment fragment = new MoreFragment();
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
        return inflater.inflate(R.layout.fragment_more, container, false);
    }
    ViewPager2 vp;
    TabLayout tl;
    private ArrayList<Feature> arrayFree;
    private ArrayList<Feature> arrayPre;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arrayFree = new ArrayList<>(Arrays.asList(new Feature("Miễn phí", "Chỉ được tạo 10 playlist"), new Feature("Miễn phí", "Chỉ được nghe 20 bài hát trong ngày"), new Feature("Miễn phí", "Không thể nghe offline"), new Feature("Miễn phí", "Chỉ được thêm 20 bài hát vào một playlist"), new Feature("Miễn phí", "Giới hạn tương tác bài hát")));
        arrayPre = new ArrayList<>(Arrays.asList(new Feature("Premium", "Tạo không giới hạn playlist"), new Feature("Premium", "Nghe nhạc không giới hạn"), new Feature("Premium", "Tải bài hát nghe Offline"), new Feature("Premium", "Thêm bài hát không giới hạn vào một playlist"), new Feature("Premium", "Nghe nhạc chất lượng cao")));
        PremiumFeatureAdapter adap = new PremiumFeatureAdapter(arrayFree, arrayPre, getActivity().getApplicationContext());
        vp = view.findViewById(R.id.vpFeatures);
        vp.setAdapter(adap);
        tl = view.findViewById(R.id.tlFeatures);
        new TabLayoutMediator(tl, vp, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            }
        }).attach();
    }
    public void getItems(){

    }
}