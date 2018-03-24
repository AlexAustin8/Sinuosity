package com.example.alex.tuneup;

import android.content.Intent;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import org.json.JSONObject;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Player;

public class MainActivity extends AppCompatActivity {
    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "50c56aa821624eae9fe8322160e1248b";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "test-app-log://callback";
    private static final int REQUEST_CODE = 1337;

    private Button playButton;
    private Config playerConfig;
    private Track currentTrack;
    private Player mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playButton = this.findViewById(R.id.play_button);


        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

       // String sampleJson = "{\n" +
         //       "      \"uri\":\"spotify:track:2TpxZ7JUBn3uw46aR7qd6V\",\n" +
           //     "      \"title\":\"something\",\n" +
             //   "      \"artist\":\"idk\",\n" +
               // "      \"album\":\"uh\"\n" +
               // "}";
        //JSONObject j = null;
      //  try {
        //    j = new JSONObject();
        //}catch(Exception e){
          //  Log.i("JSON Error", e.getMessage());
        //}
        Track test = new SpotifyTrack();
        if(test.getPlayer() == null){
            while(playerConfig == null){
                ;
            }
            test.initializePlayer(playerConfig);
        }

    }

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        //First check to see if player is null, then do test cases for the other things
        public void onClick(View v){
            if(currentTrack.getPlayer() == null){
                currentTrack.initializePlayer(playerConfig);
            }
            if(!currentTrack.isPlaying()){
                currentTrack.resume();
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
    }
}
