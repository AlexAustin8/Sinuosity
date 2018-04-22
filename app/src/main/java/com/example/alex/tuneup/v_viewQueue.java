package com.example.alex.tuneup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

public class v_viewQueue extends AppCompatActivity {
    ListView queue;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_queue);
        queue = findViewById(R.id.result_list);  //Once again a placeholder until proper UI id is defined
        key = getIntent().getExtras().getString("lobbyKey");
        RequestManager r = new RequestManager();
        r.web_queueGetData(key);
        TrackAdapter adapter = r.loc_queueAdapter(getApplicationContext());
        queue.setAdapter(adapter);
    }


    @Override
    public void onBackPressed(){
        /* For now this is merely returning to the NonStreamLobbyActivity,
           perhaps we should put in an Extra when calling this activity to determine whether or not thi activity
           was called by NonStreamLobbyActivity or MainActivity
         */
        Intent i = new Intent(getApplicationContext(), v_lobby_noStream.class);
        i.putExtra("lobbyKey", key);  //Operating under the assumption that we will need the key to view correct lobby
        startActivity(i);
    }
}