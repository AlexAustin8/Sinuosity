


package com.example.alex.tuneup;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

public class v_lobby extends Activity {
    private String url = "https://thisisjustaplaceholderuntilwegetproperurl.gov", name;  //Replace with proper URL to connect with the server
    private ImageView albumCover;
    private TextView songInfo, lobbyName;
    private String key, trackInfo, numMembers;
    private JSONObject currentTrack, nextTrack;    //This lobby will not stream, therefore, we only need to have the JSON object of the current track

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        key = i.getExtras().getString("lobbyKey");   //Proper lobby key should always be sent as an intent extra

        // Hides status and title bar --------------------------------------
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // -----------------------------------------------------------------

        setContentView(R.layout.lobby);


        LayoutInflater inflater = getLayoutInflater();
        getWindow().addContentView(inflater.inflate(R.layout.lobby_top, null),
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.FILL_PARENT));


        final LayoutInflater factory = getLayoutInflater();
        final View topLayout = factory.inflate(R.layout.lobby_top, null);

        Button searchButton = (Button) findViewById(R.id.bAddSong);
        Button viewQueue = (Button) findViewById(R.id.bViewQueue);
        ImageView backButton = (ImageView) findViewById(R.id.bBack);

        albumCover = findViewById(R.id.imageView);
        songInfo = findViewById(R.id.song_info);
        lobbyName = findViewById(R.id.lobby_name);
        RequestManager r = new RequestManager();
        try {
            r.web_lobbyGetData(key);
            name = r.loc_lobbyName();
            lobbyName.setText(URLDecoder.decode(name, "UTF-8"));
            trackInfo = URLDecoder.decode((r.loc_lobbyPlaying("name") + " - " + r.loc_lobbyPlaying("artist")) , "UTF-8");
            songInfo.setText(trackInfo);
            Bitmap img = new GetAlbumArt().execute(r.loc_lobbyPlaying("artwork")).get();
            albumCover.setImageBitmap(img);
            numMembers = r.loc_lobbyMembersCount();

        }catch(Exception e){
            Log.i("Error loading lobby" , e.getMessage());
            Toast.makeText(v_lobby.this, "Error Loading Lobby Data", Toast.LENGTH_LONG).show();
        }


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), v_home.class);
                startActivity(i);
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), v_search.class);
                startActivity(i);
            }
        });

        viewQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v_lobby.this, "Hello", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), v_search.class);
                startActivity(i);
            }
        });

    }

}