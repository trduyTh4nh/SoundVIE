package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.model.Premium;

import java.text.DecimalFormat;

import io.realm.mongodb.App;

public class VisaActivity extends AppCompatActivity {
    TextView premiumType;
    ProgressBar loadingProgress;
    TextView tvCost;
    Helper h = Helper.INSTANCE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visa);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        premiumType = findViewById(R.id.premiumtype);
        loadingProgress = findViewById(R.id.loadingProgress);
        loadingProgress.setVisibility(View.VISIBLE);
        tvCost = findViewById(R.id.tvCost);
        h.getPremium(b.getString("_id"), new App.Callback<Premium>() {
            @Override
            public void onResult(App.Result<Premium> result) {
                if(result.isSuccess()){
                    loadingProgress.setVisibility(View.GONE);
                    Premium prem = result.get();
                    premiumType.setText(prem.getTenLoai());
                    double amount = prem.getGia();
                    DecimalFormat format = new DecimalFormat("#,###");
                    tvCost.setText(format.format(amount) + " VND");
                }
            }
        });
    }
}