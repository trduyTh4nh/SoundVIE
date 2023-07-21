package com.example.soundvieproject.media;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class MediaPlayerUtils {
    public static final String RAW_MEDIA_SAMPLE = "test3107";
    public static final String LOG_TAG = "MediaplayerTutorial";

    public static void playRawMedia(Context context, MediaPlayer mediaPlayer, String resName){
        try
        {
            Uri uri = Uri.parse(resName);
            Log.i(LOG_TAG, "Media URI: " + uri);
            Toast.makeText(context, "select resource" + uri, Toast.LENGTH_SHORT).show();
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void playLocalMedia(Context context, MediaPlayer mediaPlayer, String localPath){
        try
        {
            Uri uri = Uri.parse(localPath);
            Log.i(LOG_TAG, "Media URI" + uri);
            Toast.makeText(context, "select resource: " + uri, Toast.LENGTH_SHORT).show();

            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getRawResIDByName(Context context, String resName)
    {
        String pkgName = context.getPackageName();

        int resID = context.getResources().getIdentifier(resName, "raw", pkgName);

        Log.i(LOG_TAG, "Tên tài nguyên: "  +resName + "==> ID nguồn tài nguyên = " + resID);

        return resID;
    }

}
