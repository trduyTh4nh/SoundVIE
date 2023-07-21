package com.example.soundvieproject;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.MongoDB;
import com.example.soundvieproject.DB.StorageHelper;

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
    EditText edtEmail, edtPhone, edtPass, edtConfirmPass, edtName;
    Button btnSignup;

    SharedPreferences sharedPreferences;
    private static String SHARE_PRE_NAME = "shipper";
    private static String KEY_EMAIL = "email";
    private static String KEY_PASSWORD = "password";


    MongoDatabase mongoDatabase;
    MongoClient mongoClient;

    Realm uiThreadRealm;
    MongoCollection<Document> mongoCollection;
    App app;
    User user;
    Helper h;
    Button btnLogin;
    StorageHelper storageHelper;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Realm.init(this);
        btnLogin  = findViewById(R.id.btnLogin);
        app = new App(new AppConfiguration.Builder(Appid).build());
        h = Helper.INSTANCE;
        h.setA(app);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = this.getSharedPreferences(SHARE_PRE_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_EMAIL, null) != null) {
            String email = sharedPreferences.getString(KEY_EMAIL, null);
            String password = sharedPreferences.getString(KEY_PASSWORD, null);
            h.login(email, password, new App.Callback<User>() {
                @Override
                public void onResult(App.Result<User> result) {
                    if(result.isSuccess()){
                        Intent i = new Intent(SignupActivity.this, HomeActivity.class);
                        startActivity(i);
                    }
                }
            });
        }


        app = new App(new AppConfiguration.Builder(Appid).build());
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPass = findViewById(R.id.edtPassword);
        edtConfirmPass = findViewById(R.id.edtComfirmPass);
        btnSignup = findViewById(R.id.btnSignup);
        edtName = findViewById(R.id.edtName);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                h.register(edtEmail.getText().toString(), edtPass.getText().toString(), edtPhone.getText().toString(), edtName.getText().toString(), getApplicationContext());
            }
        });

         storageHelper = new StorageHelper(SignupActivity.this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

    }
}