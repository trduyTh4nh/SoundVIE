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
import io.realm.mongodb.auth.ApiKeyAuth;
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
    private static String KEY_ROLE = "role";


    MongoDatabase mongoDatabase;
    MongoClient mongoClient;

    Realm uiThreadRealm;
    MongoCollection<Document> mongoCollection;
    App app;
    User user;
    Helper h;
    Button btnLogin;
    StorageHelper storageHelper;
    com.example.soundvieproject.model.User u;

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

        String email = sharedPreferences.getString(KEY_EMAIL, null);
        String password = sharedPreferences.getString(KEY_PASSWORD, null);

        if (sharedPreferences.getString(KEY_EMAIL, null) != null) {
            h.login(email, password, new App.Callback<User>() {
                @Override
                public void onResult(App.Result<User> result) {
                    if(result.isSuccess()){
                        h.checkRole(new App.Callback<com.example.soundvieproject.model.User>() {
                            @Override
                            public void onResult(App.Result<com.example.soundvieproject.model.User> result) {

                                u = result.get();
                                if(u.getIdLoai().equals("ns")){
                                    Toast.makeText(SignupActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                    Helper.INSTANCE.setA(app);
                                    Intent i = new Intent(SignupActivity.this, ArtistHomeActivity.class);
                                    ApiKeyAuth key = app.currentUser().getApiKeys();
                                    Log.d("test", key.toString());
                                    startActivity(i);
                                }
                                else {
                                    Toast.makeText(SignupActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                    Helper.INSTANCE.setA(app);
                                    Intent i = new Intent(SignupActivity.this, HomeActivity.class);
                                    ApiKeyAuth key = app.currentUser().getApiKeys();
                                    Log.d("test", key.toString());
                                    startActivity(i);
                                }
                            }
                        });
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
                if(!edtPass.getText().toString().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")){
                    Toast.makeText(SignupActivity.this, "Mật khẩu không khớp (phải có 8 ký tự, một chữ in hoa và in thường và có ít nhất 1 số.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!edtEmail.getText().toString().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
                    Toast.makeText(SignupActivity.this, "Email không hợp lệ.", Toast.LENGTH_SHORT).show();
                    return;
                }
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