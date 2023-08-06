package com.example.soundvieproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.StorageHelper;
import com.example.soundvieproject.adapter.AddArtistAdapter;
import com.example.soundvieproject.adapter.ArtistInSongAdapter;
import com.example.soundvieproject.model.ArtistInSong;
import com.example.soundvieproject.model.Song;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
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
import io.realm.mongodb.mongo.iterable.MongoCursor;


public class ArtistUpMusicActivity extends AppCompatActivity {

    String Appid = "soundvie-zvqhd";
    EditText edtEmail, edtPass, loginEmail, loginPassword, edtTest;
    Button btnSignup, btnLogin, btnUpload;
    Button btnFind, test;
    TextView tvShow, tvBytes;
    EditText edtTest1, edtTest2;
    Button btnChooseArtist;
    Button btnAddArt, btnCancel;
    ArrayList<com.example.soundvieproject.model.User> usrDialog;
    Uri u = null;
    ProgressBar pbProgress;
    MongoDatabase mongoDatabase;
    MongoClient mongoClient;
    Helper instance;

    Realm uiThreadRealm;
    MongoCollection<Document> mongoCollection;
    App app;
    User user;
    StorageHelper r;
    RecyclerView rcvArtist;
    AddArtistAdapter adapter;
    ProgressDialog progressDialog;
    ArrayList<com.example.soundvieproject.model.User> arts;
    EditText edtNameSong, edtImg, edtStateData, edtLyrics, edtArtist;
    Button btnUp, btnChoose, btnChooseImage;
    ImageButton btnSelect;

    Uri uriFile;
    Uri imageUri;

    String uriImage;
    StorageReference storageReference;
    ImageView imageView;
    ArrayList<com.example.soundvieproject.model.User> usrList;
    ArtistInSongAdapter adap;
    RecyclerView rcvAdd;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Realm.init(this);
        pbProgress = findViewById(R.id.pbProgress);
        edtNameSong = findViewById(R.id.edtNameSong);
        rcvArtist = findViewById(R.id.rcvArtist);
        edtLyrics = findViewById(R.id.edtLyrics);
        edtArtist = findViewById(R.id.edtArtist);
        btnUp = findViewById(R.id.btnUp);
        tvBytes = findViewById(R.id.tvBytes);
        btnChoose = findViewById(R.id.btnChooseSong);
        btnSelect = findViewById(R.id.btnSelect);
        usrList = new ArrayList<>();
        btnChooseArtist = findViewById(R.id.btnChooseArtist);


        // click choose image

        // click choose image over

        btnSelect.setOnClickListener(result -> {
            selectImage();
        });



        btnChoose.setOnClickListener(v -> openFile());
        adap = new ArtistInSongAdapter(this, usrList);
        LinearLayoutManager l = new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rcvArtist.setAdapter(adap);
        rcvArtist.setLayoutManager(l);
        instance = Helper.INSTANCE;
        arts = new ArrayList<>();
        instance.getUserCurrentBy(new App.Callback<com.example.soundvieproject.model.User>() {
            @Override
            public void onResult(App.Result<com.example.soundvieproject.model.User> result) {
                if(result.isSuccess()){

                    usrList.add(result.get());
                    Log.d("user",result.get().toString());
                    adap.notifyItemInserted(usrList.size() - 1);
                    btnChooseArtist.setOnClickListener(v -> {
                        usrDialog = new ArrayList<>();
                        adapter = new AddArtistAdapter(getApplicationContext(), usrDialog, usrList);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ArtistUpMusicActivity.this);
                        @SuppressLint("InflateParams") View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.add_artist_dialog, null);
                        builder.setView(view);
                        rcvAdd = view.findViewById(R.id.rcvArt);
                        btnAddArt = view.findViewById(R.id.btnAddArt);
                        btnCancel = view.findViewById(R.id.btnCancel);
                        LinearLayoutManager l = new LinearLayoutManager(getApplicationContext());
                        rcvAdd.setAdapter(adapter);
                        rcvAdd.setLayoutManager(l);
                        instance.getArtists(new App.Callback<MongoCursor<com.example.soundvieproject.model.User>>() {
                            @Override
                            public void onResult(App.Result<MongoCursor<com.example.soundvieproject.model.User>> result) {
                                if(result.isSuccess()){
                                    MongoCursor<com.example.soundvieproject.model.User> cur = result.get();

                                    while (cur.hasNext()){
                                        com.example.soundvieproject.model.User usr = cur.next();
                                        usrDialog.add(usr);
                                        adapter.notifyItemInserted(usrDialog.size() - 1);

                                    }

                                }
                            }
                        });
                        Dialog d = builder.create();
                        btnCancel.setOnClickListener(v1 -> {
                            d.dismiss();
                        });
                        btnAddArt.setOnClickListener(v2 -> {
                            boolean[] sel = adapter.getSel();
                            for(int i = 0; i < sel.length; i++){
                                if(sel[i]){
                                    if(checkTrung(usrDialog.get(i))){
                                        Toast.makeText(ArtistUpMusicActivity.this, "Lỗi: Người dùng đã tồn tại", Toast.LENGTH_SHORT).show();
                                        d.dismiss();
                                        return;
                                    }
                                    usrList.add(usrDialog.get(i));
                                    adap.notifyItemInserted(usrList.size() - 1);

                                    Log.d("User", "i: " + i + " usr: " + usrDialog.get(i).toString());
                                }
                            }
                            d.dismiss();
                        });
                        d.show();
                        d.getWindow().setBackgroundDrawableResource(R.drawable.background_rounded_light);
                    });
                }
            }
        });
        r = new StorageHelper(this);

        Realm.init(this);
        app = new App(new AppConfiguration.Builder(Appid).build());
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            String nameSong = edtNameSong.getText().toString();
                            String stateData = "normal";
                            String lyrics = edtLyrics.getText().toString();
                            String UriPath = uriFile.toString();
                            RealmList<com.example.soundvieproject.model.User> artists = new RealmList<>();
                            ObjectId idSong = new ObjectId();
                            instance.insertSong(new Song(idSong, nameSong, uriImage, stateData, lyrics, artists, UriPath));


                            user = instance.getUser();
                            ArrayList<ArtistInSong> artistss = new ArrayList<>();
                            for(com.example.soundvieproject.model.User usr : usrList){
                                artistss.add(new ArtistInSong(new ObjectId(), usr.getIdUser(), idSong));
                            }
                            instance.insertArtists(artistss, t -> {
                                if(t.isSuccess()){
                                    Toast.makeText(ArtistUpMusicActivity.this, "Đăng thành công!", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(ArtistUpMusicActivity.this, "Có lỗi: "+t.getError().getErrorType(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            Log.d("Res", d.toString());
                        } else {
                            Log.e("Error", task.getException().getMessage());
                        }
                    }
                });
                ObjectId id = new ObjectId();
                uriImage = id.toString();
                storageReference = FirebaseStorage.getInstance().getReference("images/"+uriImage);

                storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@androidx.annotation.NonNull Exception e) {
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                        Toast.makeText(ArtistUpMusicActivity.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
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
            btnSelect.setImageURI(imageUri);
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {

            if (data != null) {
                u = data.getData();
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


    }
    private boolean checkTrung(com.example.soundvieproject.model.User usr){
        for(com.example.soundvieproject.model.User u : usrList){
            if(u.getIdUser().equals(usr.getIdUser())){
                return true;
            }
        }
        return false;
    }



}