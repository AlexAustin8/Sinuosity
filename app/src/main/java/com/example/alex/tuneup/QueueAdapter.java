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

public class QueueAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<JSONObject> mDataSource;
    private Context context;

    public QueueAdapter(Context context, ArrayList<JSONObject> results){
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


        View rowView = mInflater.inflate(R.layout.queue_result_layout, viewGroup,false);

        ImageView downvoteButton = rowView.findViewById(R.id.bDownvote);
        downvoteButton.setOnClickListener(downListener);

        ImageView upvoteButton = rowView.findViewById(R.id.bUpvote);
        upvoteButton.setOnClickListener(upListener);

        TextView titleView = rowView.findViewById(R.id.song_title);
        TextView songScore = rowView.findViewById(R.id.songScore);

        TextView artistView = rowView.findViewById(R.id.artist_name);
        ImageView albumartView = rowView.findViewById(R.id.album_art);



        JSONObject j = (JSONObject) getItem(position);
        try {


            String uri = j.getString("uri");
            String title = j.getString("title");
            String artist = j.getString("artist");

            String artwork = j.getString("artwork_url");
            String source = j.getString("src");
            String votes = j.getString("votes");
            // SPxxxL used to split the string, not sure of a better solution beause you can only set button tag to a single string

            String tagString = uri;
            downvoteButton.setTag(tagString);
            upvoteButton.setTag(tagString);

            songScore.setText(votes);
            titleView.setText(URLDecoder.decode(URLDecoder.decode(title)));
            artistView.setText(URLDecoder.decode(URLDecoder.decode(artist)));
            Bitmap img = new GetAlbumArt().execute(URLDecoder.decode(artwork)).get();
            albumartView.setImageBitmap(img);

        }catch (Exception e){
            Log.i("JSON Error", e.getMessage());

        }


        return rowView;
    }

    private View.OnClickListener upListener = new View.OnClickListener() {

        @Override
        public void onClick(View xView) {
            String uri = xView.getTag().toString();



            Context context = xView.getContext();




            RequestManager rm = new RequestManager();

            SharedPreferences settings = context.getSharedPreferences("settings", 0);
            String lobbyID = settings.getString("lobbyID", "");

            rm.web_queueVoteSong("true", lobbyID, uri);
            Toast.makeText(context, "Upvoted song." , Toast.LENGTH_SHORT).show();
            if(mContext instanceof v_queue){
                ((v_queue)mContext).runSearch();
            }
        }

    };

    private View.OnClickListener downListener = new View.OnClickListener() {

        @Override
        public void onClick(View xView) {
            String uri = xView.getTag().toString();

            Context context = xView.getContext();

            RequestManager rm = new RequestManager();

            SharedPreferences settings = context.getSharedPreferences("settings", 0);
            String lobbyID = settings.getString("lobbyID", "");

            rm.web_queueVoteSong("false", lobbyID, uri);
            Toast.makeText(context, "Downvoted song." , Toast.LENGTH_SHORT).show();

        }

    };



}