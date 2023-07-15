package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class LoginActivity extends AppCompatActivity {

    String Appid = "soundvie-zvqhd";
    EditText edtEmail, edtPass, loginEmail, loginPassword, edtTest;
    Button btnSignup, btnLogin, btnUpload;
    Button btnFind;
    TextView tvShow;

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
        setContentView(R.layout.activity_login);
        Realm.init(this);
        app = new App(new AppConfiguration.Builder(Appid).build());

        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String pass = edtPass.getText().toString();
                Credentials credentials = Credentials.emailPassword(email, pass);
                app.loginAsync(credentials, new App.Callback<User>() {
                    @Override
                    public void onResult(App.Result<User> result) {
                        if (result.isSuccess()) {
                            Log.v("Login", "Successfully!");
                        } else
                        {
                           Log.v("Login", "Failed");
                        }
                    }
                });
            }
        });


    }



}