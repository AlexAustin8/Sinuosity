


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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class v_lobby extends Activity {
    private String url = "https://thisisjustaplaceholderuntilwegetproperurl.gov", name;  //Replace with proper URL to connect with the server
    private ImageView albumCover;
    private TextView songInfo, lobbyName;
    private String key, trackInfo, numMembers;
    private JSONObject currentTrack, nextTrack;    //This lobby will not stream, therefore, we only need to have the JSON object of the current track
    private RequestManager r;
    private Timer t = new Timer();

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

        populateLobby();

        t.schedule(new TimerTask() {

            @Override
            public void run() {
                populateLobby();
            }
        }, 0, 600);


        //Declare listeners for all Buttons
        backButton.setOnClickListener(buttonListener);
        searchButton.setOnClickListener(buttonListener);
        viewQueue.setOnClickListener(buttonListener);

    }

    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i;
            switch (v.getId()) {
                case R.id.bAddSong:
                    i = new Intent(getApplicationContext(), v_search.class);
                    i.putExtra("lobbyKey", key);
                    startActivity(i);
                    break;

                case R.id.bViewQueue:
                    // Toast.makeText(lobby_streamHidden.this, "Hello", Toast.LENGTH_LONG).show();
                    i = new Intent(getApplicationContext(), v_viewQueue.class);
                    i.putExtra("lobbyKey", key);
                    startActivity(i);
                    break;

                case R.id.bBack:
                    i = new Intent(getApplicationContext(), v_home.class);
                    i.putExtra("lobbyKey", key);
                    startActivity(i);
                    break;
            }

        }


    };



    protected void populateLobby() {
        try {
            r.web_lobbyGetData(key);

            //Populate data, this maybe could use some refactoring, since we are declaring a track object that can hold some of these values
            name = r.loc_lobbyName();
            lobbyName.setText(URLDecoder.decode(name, "UTF-8"));
            trackInfo = URLDecoder.decode((r.loc_lobbyPlaying("name") + " - " + r.loc_lobbyPlaying("artist")), "UTF-8");
            songInfo.setText(trackInfo);
            Bitmap img = new GetAlbumArt().execute(r.loc_lobbyPlaying("artwork")).get();
            albumCover.setImageBitmap(img);
            numMembers = r.loc_lobbyMembersCount();
        } catch (Exception e) {
            Log.i("Error loading lobby", e.getMessage());
            Toast.makeText(v_lobby.this, "Error Loading Lobby Data", Toast.LENGTH_LONG).show();
        }
    }

}