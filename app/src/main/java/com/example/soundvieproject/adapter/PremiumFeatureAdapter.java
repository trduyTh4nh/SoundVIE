package com.example.soundvieproject.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soundvieproject.R;
import com.example.soundvieproject.model.Feature;
import com.example.soundvieproject.model.Premium;

import java.util.ArrayList;

public class PremiumFeatureAdapter extends RecyclerView.Adapter<PremiumFeatureAdapter.ViewHolder> {
    private TextView tvPerkFree, tvPerkPre, tvPerkDescFree, tvPerkDescPre;
    private ArrayList<Feature> arrayFree;
    private ArrayList<Feature> arrayPre;
    private Context context;

    public PremiumFeatureAdapter(ArrayList<Feature> arrayFree, ArrayList<Feature> arrayPre, Context context) {
        this.arrayFree = arrayFree;
        this.arrayPre = arrayPre;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.premium_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Feature featFree = arrayFree.get(position);
        Feature featPre = arrayPre.get(position);
        tvPerkFree.setText(featFree.getTitle());
        tvPerkDescFree.setText(featFree.getDescription());
        tvPerkPre.setText(featPre.getTitle());
        tvPerkDescPre.setText(featPre.getDescription());


//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                arrayFree.get(position);
//            }
//        }, 5);
    }

    @Override
    public int getItemCount() {
        return arrayFree.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPerkFree = itemView.findViewById(R.id.tvPerkFree);
            tvPerkDescFree = itemView.findViewById(R.id.tvPerkDescFree);
            tvPerkPre = itemView.findViewById(R.id.tvPerkPre);
            tvPerkDescPre = itemView.findViewById(R.id.tvPerkDescPre);
        }
    }
}
