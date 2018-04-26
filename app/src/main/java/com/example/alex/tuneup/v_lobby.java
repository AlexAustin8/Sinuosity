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

import java.net.URLDecoder;
import java.util.Timer;
import java.util.TimerTask;


public class v_lobby extends AppCompatActivity {
    //Note: trying to store these strings/ load them from resouces results in errors
    private static final String CLIENT_ID = "cf7eda43f4ab43e59c74091e4259d9b2";
    private static final String REDIRECT_URI = "tuneup-log://callback";
    private static final int REQUEST_CODE = 1337;
    private ImageView albumCover, album_art_next,source_next,bSettings;
    private TextView songInfo, lobbyName, song_title_next,artist_name_next;;
    private Config playerConfig;
    private Button searchButton, viewQueue, playButton;
    private ImageView backButton, bSetting;
    private String key, trackInfo, numMembers;
    private Track currentTrack;
    private RequestManager r;
    private Timer t = new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("Create Status", "We got Here!");
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
        album_art_next = findViewById(R.id.album_art_next);
        source_next = findViewById(R.id.source);
        bSetting = findViewById(R.id.bSetting);
        song_title_next = findViewById(R.id.song_title_next);
        artist_name_next = findViewById(R.id.artist_name_next);
        //End UI Assignment

        //Authenticate with Spotify
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);


        //Declare Request Manager to use within the rest of the activity
        r = new RequestManager();


        t.schedule(new TimerTask() {

            @Override
            public void run() {
                if(currentTrack != null && currentTrack.isStreamFinished()){
                    r.web_queueShift(key);
                    currentTrack = null;
                }
                    populateLobby();
            }
        }, 0, 3000);



        //Initial Lobby creation for whatever the current song is when lobby is first joined by user
        //  populateLobby();

        //Declare listeners for all Buttons
        backButton.setOnClickListener(buttonListener);
        playButton.setOnClickListener(buttonListener);
        searchButton.setOnClickListener(buttonListener);
        viewQueue.setOnClickListener(buttonListener);
    }

//dctewl

    protected void populateLobby() {
        try {
            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        r.web_lobbyGetData(key);
                        if(r.loc_lobbyPlaying("name").equals("Error")  && r.loc_lobbyPlaying("artist").equals("Error")){
                            songInfo.setText(getResources().getString(R.string.empty_queue));
                            song_title_next.setText(getString(R.string.nextEmpty));
                            artist_name_next.setText(R.string.nextEmptyArtist);
                            albumCover.setImageResource(R.drawable.playingalbum);
                            album_art_next.setImageResource(R.drawable.upnextalbum);
                            r.web_queueShift(key);
                        }else {
                            r.web_lobbyGetData(key);

                            if(r.loc_lobbyUpNext("name").equals("Error")  && r.loc_lobbyUpNext("artist").equals("Error")){

                                song_title_next.setText(getString(R.string.nextEmpty));
                                artist_name_next.setText(R.string.nextEmptyArtist);
                                album_art_next.setImageResource(R.drawable.upnextalbum);

                            } else {
                                song_title_next.setText(r.loc_lobbyUpNext("name"));
                                artist_name_next.setText(r.loc_lobbyUpNext("artist"));
                                Bitmap nextIMG = new GetAlbumArt().execute(r.loc_lobbyUpNext("artwork")).get();
                                album_art_next.setImageBitmap(nextIMG);
                            }



                            if (currentTrack == null) {
                                if (r.loc_lobbyPlaying("source").compareTo("soundcloud") == 0) {
                                    currentTrack = new SoundCloudTrack(key);
                                } else if (r.loc_lobbyPlaying("source").compareTo("spotify") == 0) {
                                    currentTrack = new SpotifyTrack(key, playerConfig);
                                }
                            }
                            Bitmap img = new GetAlbumArt().execute(r.loc_lobbyPlaying("artwork")).get();
                            String  name = r.loc_lobbyName();
                            lobbyName.setText(URLDecoder.decode(name, "UTF-8"));
                            trackInfo = URLDecoder.decode((r.loc_lobbyPlaying("name") + " - " + r.loc_lobbyPlaying("artist")), "UTF-8");
                            songInfo.setText(trackInfo);
                            albumCover.setImageBitmap(img);
                            numMembers = r.loc_lobbyMembersCount();


                            if(r.loc_lobbyUpNext("src").equals("spotify")) {
                                source_next.setImageResource(R.drawable.spotify);
                            } else {
                                source_next.setImageResource(R.drawable.souncloud);
                            }
                        }
                    }catch (Exception e){
                        Log.i("Error Updating UI", e.getMessage());
                    }

                }
            });
        } catch (Exception e) {
            Log.i("Error loading lobby", e.getMessage());
            Toast.makeText(v_lobby.this, getResources().getString(R.string.load_error), Toast.LENGTH_LONG).show();
        }
    }

    View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i;
            switch (v.getId()) {
                case R.id.bSetting:
                    i = new Intent(getApplicationContext(), v_settings.class);
                    startActivity(i);
                    break;
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
                    if(currentTrack == null){
                        Toast.makeText(v_lobby.this, getResources().getString(R.string.empty_queue), Toast.LENGTH_SHORT).show();

                    }else {
                        if (currentTrack.isPlaying()) {
                            currentTrack.pause();
                            playButton.setText(getResources().getString(R.string.resume));
                        } else if (!currentTrack.isPlaying()) {
                            currentTrack.resume();
                            playButton.setText(getResources().getString(R.string.pause));
                        }
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
            }
        }

        Toast.makeText(v_lobby.this, ("Key: " + key), Toast.LENGTH_LONG).show();



    }


}