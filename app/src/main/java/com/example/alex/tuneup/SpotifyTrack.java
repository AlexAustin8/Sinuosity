package com.example.alex.tuneup;

import android.util.Log;



import com.spotify.sdk.android.player.Player;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.Player;

import com.spotify.sdk.android.player.SpotifyPlayer;

import org.json.JSONObject;

/**
 * Created by alex on 3/14/18.
 */

public class SpotifyTrack implements Track, SpotifyPlayer.NotificationCallback, ConnectionStateCallback {
    private String uri, title, artist, album, artUri;
    private final String source = "sp";
    private Player mPlayer = null;
    boolean playing = false;

    //Parameterless constructor for testing purposes
    public SpotifyTrack(Config c){
        uri = "spotify:track:3kwql90R2gmkK12R8uGHCs";
        title = "Radioactivity";
        artist = "Kraftwerk";
        album = "Radioactivity ";
        initializePlayer(c);

    }


    public SpotifyTrack(String key, Config c){
        try {
            RequestManager r = new RequestManager();
            r.web_lobbyGetData(key);
            uri = r.loc_lobbyPlaying("uri");
            title = r.loc_lobbyPlaying("name");
            artist = r.loc_lobbyPlaying("artist");
        }catch(Exception e){
            //Later on, implement this in an error message to user.
            Log.i("Error", e.getMessage());
        }
        initializePlayer(c);

    }

    @Override
    public String getUri() {
        return uri;
    }


    //TODO: Just make a boolean isInitialized method

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public String getAlbum() {
        return album;
    }

    public Player getPlayer() {
        return mPlayer;
    }

    @Override
    public String getArtUri() {
        return artUri;
    }

    @Override
    public boolean isPlaying(){
        return playing;
    }

    @Override
    public void setUri(String newUri) {
        uri = newUri;

    }

    @Override
    public void setTitle(String newTitle) {
        title = newTitle;

    }

    @Override
    public void setArtist(String newArtist) {
        artist = newArtist;

    }

    @Override
    public void setAlbum(String newAlbum) {
        album = newAlbum;

    }

    @Override
    public void setArtUri(String u) {
        artUri = u;
    }


    @Override
    public void resume() {
        mPlayer.resume(null);
        playing = true;


    }

    @Override
    public void pause() {
        playing = false;
        mPlayer.pause(null);

    }

    @Override
    public void skip() {

    }

    @Override
    public void kickFromQueue() {

    }

    private void initializePlayer(Config c){
        Spotify.getPlayer(c, this, new SpotifyPlayer.InitializationObserver() {
            @Override
            public void onInitialized(SpotifyPlayer spotifyPlayer) {
                mPlayer = spotifyPlayer;
                mPlayer.addConnectionStateCallback(SpotifyTrack.this);
                mPlayer.addNotificationCallback(SpotifyTrack.this);

            }


            @Override
            public void onError(Throwable throwable) {
                Log.i("MainActivity", "Could not initialize player: " + throwable.getMessage());
            }
        });


        Log.i("mPlayer", "Player Created");



    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.i("PlaybackEvent", playerEvent.name());

    }

    @Override
    public void onPlaybackError(Error error) {
        //TODO: Write code to halt playback and present error message to user, should also throw up to the lobbyactivity to make
        //The next song in the queue play.
        Log.i("Playback Error", error.name());
    }

    //Overridden Methods for implementation of ConnectionStateCallback

    @Override
    public void onConnectionMessage(String message) {
        Log.i("SpotifyTrack", "Received connection message: " + message);
    }

    @Override
    public void onLoggedOut() {
        Log.i("SpotifyPlayer", "User logged out");
    }

    public void onLoggedIn() {
        mPlayer.playUri(null, uri, 0, 0);
        playing  = true;
        Log.i("SpotifyPlayer", "User logged in");
    }

    @Override
    public void onLoginFailed(Error e) {
        Log.d("SpotifyPlayer", "Login failed");
        //TODO: We should have this throw a custom exception, that is handled by the Activity
        //By making a toast error message and automatically moving to the next track on the
        //Queue
    }

    @Override
    public void onTemporaryError() {
        Log.d("SpotifyPlayer", "Temporary error occurred");
    }



}