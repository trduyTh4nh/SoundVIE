package com.example.soundvieproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.model.User;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.bson.types.ObjectId;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.result.UpdateResult;

public class EditUserActivity extends AppCompatActivity {
    Button btnCancel, btnSave, btnBecomeArtist, btnChangePassword, btnLogout;
    ProgressBar prog;
    EditText edtTen, edtEmail, edtPhone, edtDesc, edtPsswd;
    ImageButton btnAddImage;
    Helper db;
    User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        StorageHelper sto = new StorageHelper(this);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> {
            finish();
        });
        init();
        db.getUserCurrentBy(new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if(result.isSuccess()){
                    u = result.get();
                    edtTen.setText(u.getName());
                    edtEmail.setText(u.getEmail());
                    edtPhone.setText(u.getPhone());
                    edtDesc.setText(u.getMoTa());

                    if(!u.getAvatar().equals("")){
                        FirebaseStorage storage = sto.getStorage();
                        StorageReference ref = storage.getReference("image/"+u.getAvatar());
                        Glide.with(getApplicationContext()).load(ref).into(btnAddImage);
                    }
                }
            }
        });
        btnAddImage.setOnClickListener(v -> {
            Intent gallery = new Intent(Intent.ACTION_PICK);
            gallery.setType("image/*");
            startActivityForResult(gallery, 102);
        });
        btnSave.setOnClickListener(v -> {
            db.login(u.getEmail(), edtPsswd.getText().toString(), new App.Callback<io.realm.mongodb.User>() {
                @Override
                public void onResult(App.Result<io.realm.mongodb.User> result) {
                    if(result.isSuccess()){
                        prog.setVisibility(View.VISIBLE);
                        btnCancel.setVisibility(View.GONE);
                        btnSave.setEnabled(false);

                        if(imageUri != null){
                            ObjectId img = new ObjectId();
                            FirebaseStorage storage = sto.getStorage();
                            StorageReference ref = storage.getReference("image/"+img.toString());
                            UploadTask task = ref.putFile(Uri.parse(imageUri));
                            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    db.updateUser(edtEmail.getText().toString(), edtPhone.getText().toString(), edtTen.getText().toString(), img.toString(), edtDesc.getText().toString(), new App.Callback<UpdateResult>() {
                                        @Override
                                        public void onResult(App.Result<UpdateResult> result) {
                                            Toast.makeText(EditUserActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                            prog.setVisibility(View.GONE);
                                            finish();
                                        }
                                    });
                                }
                            });
                        } else {
                            String img = u.getAvatar();
                            db.updateUser(edtEmail.getText().toString(), edtPhone.getText().toString(), edtTen.getText().toString(), img, edtDesc.getText().toString(), new App.Callback<UpdateResult>() {
                                @Override
                                public void onResult(App.Result<UpdateResult> result) {
                                    Toast.makeText(EditUserActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                    prog.setVisibility(View.GONE);
                                    finish();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(EditUserActivity.this, "Mật khẩu sai.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder b = new AlertDialog.Builder(EditUserActivity.this);
                b.setTitle("Đăng xuất");
                b.setMessage("Bạn có muốn đăng xuất không?");
                b.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.logOut(new App.Callback<io.realm.mongodb.User>() {
                            @Override
                            public void onResult(App.Result<io.realm.mongodb.User> result) {
                                if(result.isSuccess()){
                                    SharedPreferences prefs = getSharedPreferences("shipper", MODE_PRIVATE);
                                    prefs.edit().clear().apply();
                                    Toast.makeText(EditUserActivity.this, "Bạn đã bị đăng xuất", Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(EditUserActivity.this, Flash_LoginActivity.class);
                                    startActivity(in);
                                }
                            }
                        });

                    }
                });
                b.setNegativeButton("Không", null);
                AlertDialog a = b.create();
                a.show();;

            }
        });
    }

    String imageUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == 102){
                Uri uri = data.getData();
                imageUri = data.getData().toString();
                btnAddImage.setImageURI(uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init(){
        db = Helper.INSTANCE;
        btnSave = findViewById(R.id.btnSave);
        btnBecomeArtist = findViewById(R.id.btnBecomeArtist);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnLogout = findViewById(R.id.btnLogout);
        edtTen = findViewById(R.id.edtHoTen);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtDesc = findViewById(R.id.edtDesc);
        btnAddImage = findViewById(R.id.btnAddImage);
        edtPsswd = findViewById(R.id.edtPsswd);
        prog = findViewById(R.id.loadingProgress);
    }
}