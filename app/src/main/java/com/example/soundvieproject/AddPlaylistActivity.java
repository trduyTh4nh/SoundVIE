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
                String namePL = edtNamePL.getText().toString();
                String desPL = edtDesPL.getText().toString();
                uploadImage();
                instance.getUserCurrentBy(new App.Callback<com.example.soundvieproject.model.User>() {
                    @Override
                    public void onResult(App.Result<com.example.soundvieproject.model.User> result) {
                        if(result.isSuccess()){
                            user = result.get().getIdUser();
                            instance.addPlaylist(new Playlist(new ObjectId(), namePL, uriImage ,user, desPL));
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
        Intent i  = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(i,100);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){
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
                Toast.makeText(AddPlaylistActivity.this, "Success upload image playlist", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPlaylistActivity.this, "Failed upload image playlist", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
