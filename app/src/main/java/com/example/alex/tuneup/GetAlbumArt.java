package com.example.alex.tuneup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by alex on 4/14/18.
 */

public class GetAlbumArt extends AsyncTask<String, Integer, Bitmap> {

    @Override
    protected Bitmap doInBackground(String...Strings){
        Bitmap img = null;
        String urlString = Strings[0];
        try {
            InputStream in = new URL(urlString).openStream();
            img = BitmapFactory.decodeStream(in);
        }catch(Exception e){
            Log.i("EXCEPTION", e.getMessage());
        }
        return img;
    }
}