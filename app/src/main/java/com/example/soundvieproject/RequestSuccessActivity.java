package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class RequestSuccessActivity extends AppCompatActivity {
    Button btnDone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_success);
        btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(v -> {
            Intent i = new Intent(RequestSuccessActivity.this, HomeActivity.class);
            startActivity(i);
        });
    }
}