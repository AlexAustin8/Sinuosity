package com.example.alex.tuneup;

import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;


public class lobby_streamHidden extends AppCompatActivity  {
    private static final String CLIENT_ID = "cf7eda43f4ab43e59c74091e4259d9b2";
    private static final String REDIRECT_URI = "tuneup-log://callback";
    private static final int REQUEST_CODE = 1337;

    private Button playButton, scButton, searchButton;
    private Config playerConfig;
    private Track currentTrack, scTrack;
    private String scClient = "45c06cc5419304c3b7d6f594db5d9b72";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playButton = this.findViewById(R.id.play_button);
        scButton = this.findViewById(R.id.sc_button);
        searchButton = this.findViewById(R.id.search_button);



        scButton.setOnClickListener(scButtonListener);
        playButton.setOnClickListener(buttonListener);
        searchButton.setOnClickListener(searchButtonListener);


        //Prep and run the login activity to get config object for Spotify stream
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        //Create Sample SoundCloudTrackObject
        scTrack = new SoundCloudTrack();


    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        //First check to see if player is null, then do test cases for the other things
        public void onClick(View v) {
            if (!currentTrack.isPlaying()) {
                currentTrack.resume();
            } else {
                currentTrack.pause();
            }
        }


    };


    private View.OnClickListener scButtonListener = new View.OnClickListener() {
        //First check to see if player is null, then do test cases for the other things
        public void onClick(View v) {
            if (!scTrack.isPlaying()) {
                scTrack.resume();
            } else {
                scTrack.pause();
            }
        }


    };


    private View.OnClickListener searchButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), v_search.class);

            startActivity(i);
        }


    };


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