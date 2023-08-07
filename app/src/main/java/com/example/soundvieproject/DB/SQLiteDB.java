package com.example.soundvieproject.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.soundvieproject.fragment.CurrentPremiumFragment;
import com.example.soundvieproject.model.Song;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class SQLiteDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MUSIC";
    private static final String TABLE_MUSIC = "tblMusic";

    Context context;

    private static final String ID_MUSIC = "idmusic";
    private static final String NAME_MUSIC = "namemusic";
    private static final String NAME_ARTIST = "nameartist";
    private static final String IMG_MUSIC = "imagemusic";
    private static final String URL_MUSIC = "urlmusic";
    private static final String LYRICS_MUSIC = "lyricsmusic";
    private static final int VERSION = 1;

    private String query = "CREATE TABLE " + TABLE_MUSIC + " ("+
            ID_MUSIC + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NAME_MUSIC + " TEXT not null, " +
            NAME_ARTIST + " TEXT not null , "+
            IMG_MUSIC + " TEXT not null, "+
            URL_MUSIC + " TEXT not null, "+
            LYRICS_MUSIC + " TEXT not null)";

    public SQLiteDB(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertSong(Song song, String nameArist){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_MUSIC, song.getNameSong());
        values.put(NAME_ARTIST, nameArist);
        values.put(IMG_MUSIC, song.getImgCover());
        values.put(URL_MUSIC, song.getSong());
        values.put(LYRICS_MUSIC, song.getLyrics());

        db.insert(TABLE_MUSIC, null, values);
        db.close();
    }

    public ArrayList<Song> getAllSongDownloaded(){
        ArrayList<Song> arr = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_MUSIC;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()){
            Song song = new Song();
            song.setIdSong("");
            song.setSong(cursor.getString(4));
            song.setNameSong(cursor.getString(1));
            song.setLyrics(cursor.getString(5));
            song.setStateData("");
            song.setLuotnghe(0);

            arr.add(song);
        }


        return arr;
    }

}
