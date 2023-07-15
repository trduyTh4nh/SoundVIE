package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class SignupActivity extends AppCompatActivity {
    String Appid = "soundvie-zvqhd";
    EditText edtEmail, edtPhone, edtPass, edtConfirmPass;
    Button btnSignup;

    MongoDatabase mongoDatabase;
    MongoClient mongoClient;

    Realm uiThreadRealm;
    MongoCollection<Document> mongoCollection;
    App app;
    User user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(Appid).build());
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPass = findViewById(R.id.edtPassword);
        edtConfirmPass = findViewById(R.id.edtComfirmPass);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPass.getText().toString();

                app.getEmailPassword().registerUserAsync(email, password, result -> {
                    if(result.isSuccess()){
                        Log.v("Signup", "SUCCESSFULLY!!");
                    }
                    else {
                        Log.v("Signup", "Failed!!");
                    }
                });
            }
        });
    }
}