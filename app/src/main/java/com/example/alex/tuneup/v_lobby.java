package com.example.alex.tuneup;

import android.content.Intent;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;

import java.net.URLDecoder;


public class v_lobby extends AppCompatActivity {
    private static final String CLIENT_ID = "cf7eda43f4ab43e59c74091e4259d9b2";
    private static final String REDIRECT_URI = "tuneup-log://callback";
    private static final int REQUEST_CODE = 1337;

    private String url = "https://thisisjustaplaceholderuntilwegetproperurl.gov", name;  //Replace with proper URL to connect with the server
    private ImageView albumCover;
    private TextView songInfo, lobbyName;
    private Config playerConfig;
    private Button searchButton, viewQueue, playButton;
    private ImageView backButton;
    private String key, trackInfo, numMembers;
    private Track currentTrack;
    private RequestManager r;

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


        searchButton = findViewById(R.id.bAddSong);
        viewQueue = findViewById(R.id.bViewQueue);
        playButton = findViewById(R.id.play_button);
        backButton = findViewById(R.id.bBack);
        albumCover = findViewById(R.id.imageView);
        songInfo = findViewById(R.id.song_info);
        lobbyName = findViewById(R.id.lobby_name);
        //End UI Assignment

        //Declare Request Manager to use within the rest of the activity
        r = new RequestManager();


        //Declare Thread that determines when to change track
        Thread streamCompletionCheck = new Thread("Stream Check") {
            public void run() {
                while (!currentTrack.isStreamFinished()) {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        Log.i("Thread Error", e.getMessage());
                    }
                }
                r.web_queueShift(key);
            }
        };


        //Initial Lobby creation for whatever the current song is when lobby is first joined by user
        populateLobby();

        //Declare listeners for all Buttons
        backButton.setOnClickListener(buttonListener);
        playButton.setOnClickListener(buttonListener);
        searchButton.setOnClickListener(buttonListener);
        viewQueue.setOnClickListener(buttonListener);

        while (true) {
            try {
                streamCompletionCheck.start();
                streamCompletionCheck.join();
                populateLobby();
            } catch (Exception e) {
                Log.i("Thread Error", e.getMessage());
            }
        }
    }





    protected void populateLobby() {
        try {
            r.web_lobbyGetData(key);
            if (r.loc_lobbyPlaying("source").compareTo("Soundcloud") == 0) {
                currentTrack = new SoundCloudTrack(key);
            } else {
                currentTrack = new SpotifyTrack(key, playerConfig);
            }
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

                case R.id.play_button:
                    if (currentTrack.isPlaying()) {
                        currentTrack.pause();
                        playButton.setText(getResources().getString(R.string.resume));
                    } else if (!currentTrack.isPlaying()) {
                        currentTrack.resume();
                        playButton.setText(getResources().getString(R.string.pause));
                    }
                    break;

                case R.id.bBack:
                    i = new Intent(getApplicationContext(), v_home.class);
                    i.putExtra("lobbyKey", key);
                    startActivity(i);
                    break;
            }

        }


    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                currentTrack = new SpotifyTrack(playerConfig);
            }
        }
    }


}
