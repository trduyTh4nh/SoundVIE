package com.example.soundvieproject;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soundvieproject.DB.Helper;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.auth.ApiKeyAuth;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class LoginActivity extends AppCompatActivity {


    SharedPreferences sharedPreferences;
    private static String SHARE_PRE_NAME = "shipper";
    private static String KEY_EMAIL = "email";
    private static String KEY_PASSWORD = "password";


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

    Helper h = Helper.INSTANCE;
    com.example.soundvieproject.model.User u;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Realm.init(this);
        CodecRegistry pojoCodecRegistry = fromRegistries(AppConfiguration.DEFAULT_BSON_CODEC_REGISTRY,
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        app = new io.realm.mongodb.App(new AppConfiguration.Builder(Appid).codecRegistry(pojoCodecRegistry).build());
        btnRegister = findViewById(R.id.btnRegister);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);


        // share preferences

        sharedPreferences = getSharedPreferences(SHARE_PRE_NAME, MODE_PRIVATE);

        btnRegister.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(i);
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edtEmail.getText().toString();
                String pass = edtPass.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_EMAIL, email);
                editor.putString(KEY_PASSWORD, pass);
                editor.apply();

                if(email.equals("") || pass.equals("")){
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
                    return;
                }
                Credentials credentials = Credentials.emailPassword(email, pass);

                app.loginAsync(credentials, new io.realm.mongodb.App.Callback<User>() {
                    @Override
                    public void onResult(io.realm.mongodb.App.Result<User> result) {
                        if (result.isSuccess()) {
                            h.checkRole(new App.Callback<com.example.soundvieproject.model.User>() {
                                @Override
                                public void onResult(App.Result<com.example.soundvieproject.model.User> result) {

                                    u = result.get();
                                    if(u.getIdLoai().equals("ns")){
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                        Helper.INSTANCE.setA(app);
                                        Intent i = new Intent(LoginActivity.this, ArtistHomeActivity.class);
                                        ApiKeyAuth key = app.currentUser().getApiKeys();
                                        Log.d("test", key.toString());
                                        startActivity(i);
                                    }
                                    else {
                                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                        Helper.INSTANCE.setA(app);
                                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                                        ApiKeyAuth key = app.currentUser().getApiKeys();
                                        Log.d("test", key.toString());
                                        startActivity(i);
                                    }
                                }
                            });
                            h.getUserCurrentBy(new App.Callback<com.example.soundvieproject.model.User>() {
                                @Override
                                public void onResult(App.Result<com.example.soundvieproject.model.User> result) {
                                    if(result.isSuccess()){
                                        u = result.get();
                                    }
                                }
                            });
                        } else
                        {
                            String error = result.getError().getErrorCode().name();
                            if ("INVALID_EMAIL_PASSWORD".equals(error)) {
                                Toast.makeText(LoginActivity.this, "Người dùng không tồn tại hoặc mật khẩu sai.", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(LoginActivity.this, "Lỗi mạng, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                            Log.v("Login", "Failed" + result.getError().getErrorCode().name());
                        }
                    }
                });
            }
        });
    }



}