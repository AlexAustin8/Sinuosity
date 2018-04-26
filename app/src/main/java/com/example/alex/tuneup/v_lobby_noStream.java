


package com.example.alex.tuneup;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class v_lobby_noStream extends Activity {
    private ImageView albumCover,album_art_next,source_next,bSettings;
    private TextView songInfo, lobbyName,num_members, song_title_next,artist_name_next;
    private String key, trackInfo, numMembers;
    private JSONObject currentTrack, nextTrack;    //This lobby will not stream, therefore, we only need to have the JSON object of the current track
    private RequestManager rm = new RequestManager();
    private String userID = "";
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

        SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
        userID = settings.getString("userID", "");

        Button searchButton = (Button) findViewById(R.id.bAddSong);
        Button viewQueue = (Button) findViewById(R.id.bViewQueue);
        ImageView backButton = (ImageView) findViewById(R.id.bBack);

        albumCover = findViewById(R.id.imageView);
        songInfo = findViewById(R.id.song_info);
        lobbyName = findViewById(R.id.lobby_name);
        num_members = findViewById(R.id.num_members);
        song_title_next = findViewById(R.id.song_title_next);
        artist_name_next = findViewById(R.id.artist_name_next);
        album_art_next = findViewById(R.id.album_art_next);
        source_next = findViewById(R.id.source);
        bSettings = findViewById(R.id.bSetting);
        populateLobby();

        t.schedule(new TimerTask() {

            @Override
            public void run() {
                populateLobby();
            }
        }, 0, 3000);


        //Declare listeners for all Buttons
        backButton.setOnClickListener(buttonListener);
        searchButton.setOnClickListener(buttonListener);
        viewQueue.setOnClickListener(buttonListener);
        bSettings.setOnClickListener(buttonListener);
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
                    i = new Intent(getApplicationContext(), v_queue.class);
                    i.putExtra("lobbyKey", key);
                    startActivity(i);
                    break;
                case R.id.bSetting:
                    // Toast.makeText(lobby_streamHidden.this, "Hello", Toast.LENGTH_LONG).show();
                    i = new Intent(getApplicationContext(), v_settings.class);
                    startActivity(i);
                    break;

                case R.id.bBack:
                    rm.web_lobbyLeave(key, userID);

                    SharedPreferences settings = getApplicationContext().getSharedPreferences("settings", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("lobbyID", "");
                    editor.apply();

                    t.cancel();
                    i = new Intent(getApplicationContext(), v_home.class);
                    i.putExtra("lobbyKey", key);
                    startActivity(i);
                    break;
            }

        }


    };



    protected void populateLobby() {
        try {

//            Log.i("Run", "running");
            rm.web_lobbyGetData(key);

            //Populate data, this maybe could use some refactoring, since we are declaring a track object that can hold some of these values
            if(rm.loc_lobbyPlaying("name").equals("Error")  && rm.loc_lobbyPlaying("artist").equals("Error")){
                songInfo.setText(getResources().getString(R.string.empty_queue));
                albumCover.setImageResource(R.drawable.playingalbum);
                album_art_next.setImageResource(R.drawable.upnextalbum);

            }else {
                String name;
                name = rm.loc_lobbyName();
                num_members.setText(rm.loc_lobbyMembersCount());
                lobbyName.setText(name);
                trackInfo = rm.loc_lobbyPlaying("name") + " - " + rm.loc_lobbyPlaying("artist");
                songInfo.setText(trackInfo);
                Bitmap img = new GetAlbumArt().execute(rm.loc_lobbyPlaying("artwork")).get();
                albumCover.setImageBitmap(img);
                numMembers = rm.loc_lobbyMembersCount();


                song_title_next.setText(rm.loc_lobbyUpNext("name"));
                artist_name_next.setText(rm.loc_lobbyUpNext("artist"));
                Bitmap nextIMG = new GetAlbumArt().execute(rm.loc_lobbyUpNext("artwork")).get();
                album_art_next.setImageBitmap(nextIMG);

                if (rm.loc_lobbyUpNext("src").equals("spotify")) {
                    source_next.setImageResource(R.drawable.spotify);
                } else {
                    source_next.setImageResource(R.drawable.souncloud);
                }
            }


        } catch (Exception e) {
            Log.i("Error loading lobby", e.getMessage());
//            Toast.makeText(v_lobby.this, "Error Loading Lobby Data", Toast.LENGTH_LONG).show();
        }
    }


}