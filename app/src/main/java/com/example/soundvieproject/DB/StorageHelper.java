package com.example.soundvieproject.DB;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.bson.types.ObjectId;

public class StorageHelper {
    FirebaseStorage store;
    StorageReference ref;
    Context context;

    public StorageHelper(Context c){
        context = c;
        store = FirebaseStorage.getInstance();
    }
    public FirebaseStorage getStorage(){
        return  store;
    }

    public void download(OnSuccessListener<Uri> callback){
        final Uri[] test = {null};
        ref.getDownloadUrl().addOnSuccessListener(callback);
    }

}
