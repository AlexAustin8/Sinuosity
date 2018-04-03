package com.example.alex.tuneup;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.spotify.sdk.android.player.Config;

import org.json.JSONObject;


/**
 * Created by alex on 3/31/18.
 */

public class SoundCloudTrack implements Track {
    private String uri, title, artist, album, artUri;
    private String scClient = "45c06cc5419304c3b7d6f594db5d9b72";
    private String prefix = "http://api.soundcloud.com/tracks/";
    private String postfix = "/stream?client_id=" + scClient;
    private final String source = "sc";

    private MediaPlayer mPlayer = null;
    boolean playing = false;

    //Parameterless constructor to use for testing purposes
    public SoundCloudTrack(){
        uri = "239777850";
        initializePlayer();
    }




    public SoundCloudTrack(JSONObject j){
        try {
            //Subject to change, precise JSON values are TBD
            uri = j.getString("uri");
            title = j.getString("title");
            artist = j.getString("artist");
        }catch(Exception e){
            //Later on, implement this in an error message to user.
            Log.i("Error", e.getMessage());
        }
        initializePlayer();

    }

    @Override
    public String getUri() {
        return uri;
    }

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

    @Override
    public String getArtUri() {
        return artUri;
    }

    @Override
    public Object getPlayer() {
        return mPlayer;
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
    public void setArtUri(String u) {
        artUri = u;
    }

    @Override
    public void setAlbum(String newAlbum) {
        album = newAlbum;

    }

    private void initializePlayer() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    resume();
                }
            });
            mPlayer.setDataSource(prefix + uri + postfix);
            mPlayer.prepare();
        }catch(Exception e ){
            if(e.getMessage() != null) {
                Log.i("ScError", e.getMessage());
            }
        }

    }

    @Override
    public void resume() {
        mPlayer.start();

    }

    @Override
    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public void pause() {
        mPlayer.pause();


    }

    @Override
    public void skip() {

    }

    @Override
    public void kickFromQueue() {

    }
}
