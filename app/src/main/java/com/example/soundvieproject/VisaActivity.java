package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.model.Premium;

import org.bson.types.ObjectId;

import java.text.DecimalFormat;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.result.InsertOneResult;
import io.realm.mongodb.mongo.result.UpdateResult;

public class VisaActivity extends AppCompatActivity {
    TextView premiumType;
    ProgressBar loadingProgress;
    TextView tvCost;
    TextView tvDuration;
    Button btnContinue;
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
        tvDuration = findViewById(R.id.tvDuration);
        tvCost = findViewById(R.id.tvCost);
        btnContinue = findViewById(R.id.btnContinue);
        String premid = b.getString("_id");
        String callback = b.getString("callback");


        h.getPremium(premid, new App.Callback<Premium>() {
            @Override
            public void onResult(App.Result<Premium> result) {
                if(result.isSuccess()){
                    loadingProgress.setVisibility(View.GONE);
                    Premium prem = result.get();
                    premiumType.setText(prem.getTenLoai());
                    double amount = prem.getGia();
                    DecimalFormat format = new DecimalFormat("#,###");
                    tvCost.setText(format.format(amount) + " VND");
                    tvDuration.setText(String.format("Trả mỗi %d ngày, bắt đầu từ hôm nay", prem.getThoiHan()));
                }
            }
        });
        btnContinue.setOnClickListener(v -> {
            if(callback != null){
                h.updatePayment(h.getUser().getId(), new App.Callback<UpdateResult>() {
                    @Override
                    public void onResult(App.Result<UpdateResult> result) {
                        if(result.isSuccess()){
                            Toast.makeText(VisaActivity.this, "Làm mới thành công", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(VisaActivity.this, HomeActivity.class);
                            startActivity(i);
                        }
                    }
                });
            } else
                h.insertPayment(new ObjectId(premid), "VISA", new App.Callback<InsertOneResult>() {
                    @Override
                    public void onResult(App.Result<InsertOneResult> result) {
                        if(result.isSuccess()){
                            Toast.makeText(VisaActivity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(VisaActivity.this, HomeActivity.class);
                            startActivity(i);
                        }
                    }
                });
        });
    }
}