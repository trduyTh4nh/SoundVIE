package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.soundvieproject.DB.Helper;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.result.UpdateResult;

public class RequestArtistActivity extends AppCompatActivity {
    Button btnCancel, requestArtist;
    Helper h = Helper.INSTANCE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_artist);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> {
            finish();
        });
        requestArtist = findViewById(R.id.requestArtist);
        requestArtist.setOnClickListener(v -> {
            h.upgradeToArtist(new App.Callback<UpdateResult>() {
                @Override
                public void onResult(App.Result<UpdateResult> result) {
                    if(result.isSuccess()){
                        long updateRows = result.get().getModifiedCount();
                        if(updateRows > 0){
                            Intent i = new Intent(RequestArtistActivity.this, RequestSuccessActivity.class);
                            startActivity(i);
                        }
                    }
                }
            });
        });
    }
}