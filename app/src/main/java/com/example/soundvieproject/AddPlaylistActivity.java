package com.example.soundvieproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.model.Playlist;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.bson.types.ObjectId;

import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoDatabase;

public class AddPlaylistActivity extends AppCompatActivity {
    Button btnCancel;
    ImageButton btnChooseImage;
    EditText edtNamePL, edtDesPL;
    Button btnCreatePL;
    ProgressBar pbProgress;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    Helper instance;
    StorageHelper r;
    StorageReference storageReference;

    Uri imageUri;

    String uriImage;
    String user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_playlist);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> {
            finish();
        });

        instance = Helper.INSTANCE;

        btnChooseImage = findViewById(R.id.btnChooseImage);
        edtNamePL = findViewById(R.id.etdNamePl);
        edtDesPL = findViewById(R.id.etdDesPl);
        btnCreatePL = findViewById(R.id.btnCreate);



        btnCreatePL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCreatePL.setVisibility(View.GONE);
                Toast.makeText(AddPlaylistActivity.this, "Đang thêm Playlist", Toast.LENGTH_SHORT).show();
                String namePL = edtNamePL.getText().toString();
                String desPL = edtDesPL.getText().toString();
                uploadImage();
                instance.getUserCurrentBy(new App.Callback<com.example.soundvieproject.model.User>() {
                    @Override
                    public void onResult(App.Result<com.example.soundvieproject.model.User> result) {
                        if(result.isSuccess()){
                            user = result.get().getIdUser();
                            instance.addPlaylist(new Playlist(new ObjectId(), namePL, uriImage ,user, desPL), tsk -> {
                                if(tsk.isSuccess()){
                                    Toast.makeText(AddPlaylistActivity.this, "Thêm Playlist thành công!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    }
                });

            }
        });

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    public void selectImage(){
        Intent i  = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,102);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 102 && data != null && data.getData() != null){

            imageUri = data.getData();
            btnChooseImage.setImageURI(imageUri);
        }

    }
  ;

    private void uploadImage(){
        ObjectId id = new ObjectId();
        uriImage = id.toString();
        storageReference = FirebaseStorage.getInstance().getReference("images/" + uriImage);
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddPlaylistActivity.this, "Thêm danh sách phát thành công.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPlaylistActivity.this, "Thêm danh sách thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
