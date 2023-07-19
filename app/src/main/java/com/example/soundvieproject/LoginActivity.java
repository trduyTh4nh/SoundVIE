package com.example.soundvieproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soundvieproject.DB.Helper;

import org.bson.Document;

import io.realm.Realm;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;

public class LoginActivity extends AppCompatActivity {

    String Appid = "soundvie-zvqhd";
    EditText edtEmail, edtPass, loginEmail, loginPassword, edtTest, edtName;
    Button btnSignup, btnLogin, btnUpload, btnRegister;
    Button btnFind;
    TextView tvShow;

    MongoDatabase mongoDatabase;
    MongoClient mongoClient;

    Realm uiThreadRealm;
    MongoCollection<Document> mongoCollection;
    io.realm.mongodb.App app;
    User user;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Realm.init(this);
        app = new io.realm.mongodb.App(new AppConfiguration.Builder(Appid).build());
        btnRegister = findViewById(R.id.btnRegister);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(i);
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edtEmail.getText().toString();
                String pass = edtPass.getText().toString();
                if(email.equals("") || pass.equals("")){
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
                    return;
                }
                Credentials credentials = Credentials.emailPassword(email, pass);
                app.loginAsync(credentials, new io.realm.mongodb.App.Callback<User>() {
                    @Override
                    public void onResult(io.realm.mongodb.App.Result<User> result) {
                        if (result.isSuccess()) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            Helper.INSTANCE.setA(app);
                            Intent i = new Intent(LoginActivity.this, TestActivity.class);
                            startActivity(i);
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