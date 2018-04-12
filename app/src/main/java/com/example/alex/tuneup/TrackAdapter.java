package com.example.alex.tuneup;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
            titleView.setText("Command: " + j.getString("title"));
            artistView.setText("Example: " + j.getString("artist"));
            albumartView.setImageURI(Uri.parse(j.getString("artwork_url")));
        }catch (Exception e){
            Log.i("JSON Error", e.getMessage());

        }


        return rowView;
    }
}
