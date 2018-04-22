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
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;

import org.json.JSONObject;

import java.net.URLDecoder;


public class lobby_streamHidden extends AppCompatActivity  {
    private static final String CLIENT_ID = "cf7eda43f4ab43e59c74091e4259d9b2";
    private static final String REDIRECT_URI = "tuneup-log://callback";
    private static final int REQUEST_CODE = 1337;

    private String url = "https://thisisjustaplaceholderuntilwegetproperurl.gov", name;  //Replace with proper URL to connect with the server
    private ImageView albumCover;
    private TextView songInfo, lobbyName;
    private Config playerConfig;
    private String key, trackInfo, numMembers;
    private Track currentTrack;
    private JSONObject nextTrack;    //This lobby will not stream, therefore, we only need to have the JSON object of the current track

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
        final Button playButton= (Button) findViewById(R.id.play_button);
        ImageView backButton = (ImageView) findViewById(R.id.bBack);

        albumCover = findViewById(R.id.imageView);
        songInfo = findViewById(R.id.song_info);
        lobbyName = findViewById(R.id.lobby_name);
        RequestManager r = new RequestManager();
        try {
            r.web_lobbyGetData(key);
            if(r.loc_lobbyPlaying("source").compareTo("Soundcloud") == 0){
                currentTrack = new SoundCloudTrack(key);
            }else{
                currentTrack = new SpotifyTrack(key, playerConfig);
            }

            //Populate data, this maybe could use some refactoring, since we are declaring a track object that can hold some of these values
            name = r.loc_lobbyName();
            lobbyName.setText(URLDecoder.decode(name, "UTF-8"));
            trackInfo = URLDecoder.decode((r.loc_lobbyPlaying("name") + " - " + r.loc_lobbyPlaying("artist")) , "UTF-8");
            songInfo.setText(trackInfo);
            Bitmap img = new GetAlbumArt().execute(r.loc_lobbyPlaying("artwork")).get();
            albumCover.setImageBitmap(img);
            numMembers = r.loc_lobbyMembersCount();
        }catch(Exception e){
            Log.i("Error loading lobby" , e.getMessage());
            Toast.makeText(lobby_streamHidden.this, "Error Loading Lobby Data", Toast.LENGTH_LONG).show();
        }


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), v_home.class);
                startActivity(i);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(currentTrack.isPlaying()){
                  currentTrack.pause();
                  playButton.setText(getResources().getString(R.string.resume));
              }else if(!currentTrack.isPlaying()){
                  currentTrack.resume();
                  playButton.setText(getResources().getString(R.string.pause));
              }
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
                Toast.makeText(lobby_streamHidden.this, "Hello", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), v_search.class);
                startActivity(i);
            }
        });

    }


    @Override
    //Get us the result from the Spotify Login Activity to create spotify track
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