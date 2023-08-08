package com.example.soundvieproject.adapter;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.soundvieproject.ActivityAddSongToPlayList;
import com.example.soundvieproject.DB.Helper;
import com.example.soundvieproject.DB.SQLiteDB;
import com.example.soundvieproject.PlayingMusicActivity;
import com.example.soundvieproject.PlaylistActivity;
import com.example.soundvieproject.PremiumRegisterActivity;
import com.example.soundvieproject.R;
import com.example.soundvieproject.ReportSongActivity;
import com.example.soundvieproject.model.ArtistInSong;
import com.example.soundvieproject.model.Payment;
import com.example.soundvieproject.model.ReportDetail;
import com.example.soundvieproject.model.Song;
import com.example.soundvieproject.model.SongInPlayList;
import com.example.soundvieproject.model.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class SongInPlayListAdapter extends RecyclerView.Adapter<SongInPlayListAdapter.ViewHolder> {


    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference stoRefer;


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    SQLiteDB db;
    private OnItemsClickListener listener = null;

    public interface OnItemsClickListener {
        void OnItemClick(Song song, int pos) throws IOException;
    }
    public interface OnDeleteListener{
        void OnDelete(Song song, View v, int pos) throws IOException;
    }
    private OnDeleteListener delete = null;
    DownloadManager manager;
    BroadcastReceiver rec;
    public void setItemClickListener(SongInPlayListAdapter.OnItemsClickListener listener) {
        this.listener = listener;
    }
    public void setDeleteListener(OnDeleteListener listener){
        this.delete = listener;
    }
    Helper helper = Helper.INSTANCE;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.component_song_in_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (songspl.size() == 0) {
            return;
        }
        Song song = songspl.get(position);
        stoRefer = storage.getReference("images/" + song.getImgCover());
        Glide.with(context).load(stoRefer).into(imgSongpl);
        //  imgSongpl.setImageURI(Uri.parse(song.getImgCover()));
        nameSongpl.setText(song.getNameSong());
        llplay.setOnClickListener(v -> {
            if (listener != null) {
                try {
                    listener.OnItemClick(song, position);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new SQLiteDB(context);

                helper.checkPremiumForUser(result1 -> {
                    MongoCursor<Payment> cursor1 = result1.get();
                    if(cursor1.hasNext()){
                        helper.getArtitsbyIDSongPlaying(task -> {
                            if(task.isSuccess()){
                                ArtistInSong artistOfSongPlaying = task.get();
                                String nameArist;
                                if(artistOfSongPlaying == null){
                                    nameArist = "";
                                }
                                else {
                                    Log.d("Artist of song: ", artistOfSongPlaying.toString());
                                    helper.getUserByObjID(artistOfSongPlaying.getIdUser(), new App.Callback<User>() {
                                        @Override
                                        public void onResult(App.Result<User> kq) {
                                            User artist = kq.get();

                                            Log.d("Artist", artist.toString());
                                            Log.d("Song of artist", artist.toString());

                                            Uri uri = Uri.parse(song.getSong());
                                            manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                                            DownloadManager.Request req = new DownloadManager.Request(uri);
                                            req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                                            req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, song.getId().toString() + ".mp3");
                                            File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), song.getId().toString() + ".mp3");
                                            Uri u = Uri.fromFile(f);
                                            Log.d("Uri", u.toString());
                                            long reference = manager.enqueue(req);
                                            Song s = new Song();
                                            s.setId(song.getId());
                                            s.setNameSong(song.getNameSong());
                                            s.setImgCover(song.getImgCover());
                                            s.setLyrics(song.getLyrics());
                                            s.setSong(u.toString());
                                            db.insertSong(s, artist.getName());
                                            Toast.makeText(context, "Đã tải về", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                        }, String.valueOf(song.getId()));

                    }
                    else {
                        Toast.makeText(context, "Bạn chưa đăng kí premium", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, PremiumRegisterActivity.class);
                        Bundle transfer = new Bundle();
//                        transfer.putString("idPlaylistCr", idPlCurrent);
//                        intent.putExtras(transfer);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                context.startActivity(intent);
                            }
                        }, 2000);
                    }
                });









            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(delete != null){
                   try {
                       delete.OnDelete(song, v, holder.getAdapterPosition());
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
            }
        });
    }
    public SongInPlayListAdapter(ArrayList<Song> songspl, Context context) {
        this.songspl = songspl;
        this.context = context;
    }

    private ArrayList<Song> songspl;
    Context context;

    public ArrayList<Song> getSongspl() {
        return songspl;
    }

    @Override
    public int getItemCount() {
        return songspl.size();
    }

    private ImageView imgSongpl;
    private TextView nameSongpl;
    private TextView nameArtist;
    private ImageButton btnMenu;
    private ImageButton btnDownload;
    private LinearLayout llplay;

    public void getUri(long downloadId){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);

            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                String downloadedUriString = cursor.getString(uriIndex);
                // Convert the downloaded Uri string to Uri
                Uri downloadedUri = Uri.parse(downloadedUriString);
                Log.d("uri", downloadedUri.toString());
                // Use the downloadedUri for further operations
            }
            cursor.close();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSongpl = itemView.findViewById(R.id.imgSongPl);
            nameSongpl = itemView.findViewById(R.id.nameSongPl);
            btnMenu = itemView.findViewById(R.id.btnMenuEdit);
            llplay = itemView.findViewById(R.id.llplay);
            btnDownload = itemView.findViewById(R.id.btnDownload);
        }
    }

    public void refreshView(int position) {
        notifyItemChanged(position);
    }

    public void clearData() {
        songspl.removeAll(songspl);

    }

    // Method to add new data to the adapter
    public void addData() {

        for(Song s : backup){
            songspl.add(s);

            Log.d("item", s.toString());
        }
        notifyDataSetChanged();
    }

    ArrayList<Song> backup;

}

