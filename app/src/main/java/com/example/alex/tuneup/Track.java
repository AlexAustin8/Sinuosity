package com.example.alex.tuneup;

import com.spotify.sdk.android.player.Config;

/**
 * Created by alex on 3/14/18.
 */

public interface Track {

    //Getter functions
    String getUri();
    String getTitle();
    String getArtist();
    String getArtUri();
    String getAlbum();
    //TODO Add album art and length fields to implement in both
    Object getPlayer();


    //Setter functions
    void setUri(String newUri);
    void setTitle(String newTitle);
    void setArtist(String newArtist);
    void setAlbum(String newAlbum);
    void setArtUri(String u);


    /*
note that initializePlayer and getPlayer will likely be re-evaluated once we determine
how soundcloud tracks will be implemented
 */
    //other methods
    void resume();
    boolean isPlaying();
    void pause();
    void skip();
    void kickFromQueue();



}
