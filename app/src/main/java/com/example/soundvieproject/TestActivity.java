package com.example.soundvieproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.model.Song;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.annotations.NonNull;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;


public class TestActivity extends AppCompatActivity {

    String Appid = "soundvie-zvqhd";
    EditText edtEmail, edtPass, loginEmail, loginPassword, edtTest;
    Button btnSignup, btnLogin, btnUpload;
    Button btnFind, test;
    TextView tvShow, tvBytes;
    EditText edtTest1, edtTest2;

    ProgressBar pbProgress;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    Helper instance;

    Realm uiThreadRealm;
    MongoCollection<Document> mongoCollection;
    App app;
    User user;
    StorageHelper r;

    ProgressDialog progressDialog;

    EditText edtNameSong, edtImg, edtStateData, edtLyrics, edtArtist;
    Button btnUp, btnChoose, btnChooseImage, btnSelect;

    Uri uriFile;
    Uri imageUri;

    String uriImage;
    StorageReference storageReference;
    ImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        pbProgress = findViewById(R.id.pbProgress);
        edtNameSong = findViewById(R.id.edtNameSong);
        edtImg = findViewById(R.id.edtImage);
        edtStateData = findViewById(R.id.edtState);
        edtLyrics = findViewById(R.id.edtLyrics);
        edtArtist = findViewById(R.id.edtArtist);
        btnUp = findViewById(R.id.btnUp);
        tvBytes = findViewById(R.id.tvBytes);
        btnChoose = findViewById(R.id.btnChooseSong);
        btnChooseImage = findViewById(R.id.uploadimagebtn);
        btnSelect = findViewById(R.id.btnSelect);

        imageView = findViewById(R.id.imgChoose);


        // click choose image
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        // click choose image over

        btnSelect.setOnClickListener(result -> {
            selectImage();
        });



        btnChoose.setOnClickListener(v -> openFile());

        instance = Helper.INSTANCE;

        r = new StorageHelper(this);

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(Appid).build());
    }
    private void openFile() {
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(i, 2);
    }
    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);

        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri u = null;
            if (data != null) {
                u = data.getData();
                Log.d("Res", u.toString());
                StorageReference mainRef = r.getStorage().getReference();
                StorageReference musicRef = mainRef.child((new ObjectId()).toString());
                UploadTask task = musicRef.putFile(u);
                task.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@androidx.annotation.NonNull UploadTask.TaskSnapshot snapshot) {
                        tvBytes.setText(String.valueOf(snapshot.getBytesTransferred()));
                        pbProgress.setProgress((int) (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount()));
                    }
                });
                instance = Helper.INSTANCE;
                Task<Uri> urlTask = task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@androidx.annotation.NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }
                        return musicRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            btnUp.setEnabled(true);
                            Uri d = task.getResult();
                            uriFile = d;
                            String UriPath = uriFile.toString();

                            btnUp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String nameSong = edtNameSong.getText().toString();
                                    String imgSong = edtImg.getText().toString();
                                    String stateData = edtStateData.getText().toString();
                                    String lyrics = edtLyrics.getText().toString();
                                    RealmList<com.example.soundvieproject.model.User> artists = new RealmList<>();
                                    instance.insertSong(new Song(new ObjectId(), nameSong, uriImage, stateData, lyrics, artists, UriPath));
                                }
                            });
                            Log.d("Res", d.toString());
                        } else {
                            Log.e("Error", task.getException().getMessage());
                        }
                    }
                });
            }

        }
    }
    private void uploadSong(){

    }
    private void uploadImage(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading file.....");
        progressDialog.show();


        // lấy tên file là ngày
        ObjectId id = new ObjectId();
        uriImage = id.toString();
        storageReference = FirebaseStorage.getInstance().getReference("images/"+uriImage);

        storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            imageView.setImageURI(imageUri);
            Toast.makeText(TestActivity.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
            if(progressDialog.isShowing()){
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@androidx.annotation.NonNull Exception e) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                Toast.makeText(TestActivity.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
            }
        });

    }



}