package com.example.alex.tuneup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alex on 4/2/18.
 */

public class TrackAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<JSONObject> mDataSource;

    public TrackAdapter(Context context, ArrayList<JSONObject> results){
        mContext = context;
        mDataSource = results;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount(){
        return mDataSource.size();

    }
    @Override
    public Object getItem(int i){
        return mDataSource.get(i);
    }
    @Override
    public long getItemId(int i){
        return i;
    }


    public View getView(int position, View view, ViewGroup viewGroup){
        View rowView = mInflater.inflate(R.layout.search_result_layout, viewGroup,false);

        TextView titleView = rowView.findViewById(R.id.song_title);
        TextView artistView = rowView.findViewById(R.id.artist_name);
        ImageView albumartView = rowView.findViewById(R.id.album_art);

        JSONObject j = (JSONObject) getItem(position);
        try {
            String title = j.getString("title");
            title = URLDecoder.decode(title, "UTF-8");
            String artist = j.getString("artist");
            artist = URLDecoder.decode(artist, "UTF-8");
            titleView.setText(title);
            artistView.setText(artist);
            Bitmap img = new GetAlbumArt().execute(j.getString("artwork_url")).get();
            albumartView.setImageBitmap(img);
        }catch (Exception e){
            Log.i("JSON Error", e.getMessage());

        }


        return rowView;
    }




    }
