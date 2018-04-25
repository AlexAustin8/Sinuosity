package com.example.alex.tuneup;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.ListView;
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
    private Context context;

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

        ImageView addButton = rowView.findViewById(R.id.bAddSong);


        addButton.setOnClickListener(myButtonClickListener);

        JSONObject j = (JSONObject) getItem(position);
        try {


            String uri = j.getString("uri");
            String title = j.getString("title");
            String artist = j.getString("artist");
            String duration = j.getString("duration");
            String artwork = j.getString("artwork_url");
            String source = j.getString("src");
            String small_artwork = j.getString("small_artwork_url");
            // SPxxxL used to split the string, not sure of a better solution beause you can only set button tag to a single string
            String tagString = uri + "SPxxxL" + title + "SPxxxL" + artist + "SPxxxL" + duration + "SPxxxL" + artwork + "SPxxxL" + source;

            addButton.setTag(tagString);


            titleView.setText(URLDecoder.decode(title));
            artistView.setText(URLDecoder.decode(artist));
            Bitmap img = new GetAlbumArt().execute(small_artwork).get();
            albumartView.setImageBitmap(img);

        }catch (Exception e){
            Log.i("JSON Error", e.getMessage());

        }


        return rowView;
    }

    private View.OnClickListener myButtonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View xView) {
            String[] data = xView.getTag().toString().split("SPxxxL");


            String uri = data[0];
            String title = data[1];
            String artist = data[2];
            String duration = data[3];
            String artwork = data[4];
            String source = data[5];



            Context context = xView.getContext();


            RequestManager rm = new RequestManager();

            SharedPreferences settings = context.getSharedPreferences("settings", 0);
            String userID = settings.getString("userID", "");
            String lobbyID = settings.getString("lobbyID", "");



            rm.web_queueAddSong(userID, lobbyID, uri, title,artist,duration,artwork,source);

            Toast.makeText(context, "Added " + URLDecoder.decode(title) + " to the queue." , Toast.LENGTH_SHORT).show();

        }

    };



}