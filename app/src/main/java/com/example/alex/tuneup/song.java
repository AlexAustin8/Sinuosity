package com.example.alex.tuneup;

/**
 * Created by andrewclark on 4/8/18.
 */

public class song {


    private String uri;
    private String name;
    private String artist;
    private String duration;
    private String artwork_url;


    public void setArtwork_url(String artwork_url) {
        this.artwork_url = artwork_url;
    }

    public void setDuration(String duration) {

        this.duration = duration;
    }

    public void setArtist(String artist) {

        this.artist = artist;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setUri(String uri) {

        this.uri = uri;
    }

    public String getArtwork_url() {

        return artwork_url;
    }

    public String getDuration() {

        return duration;
    }

    public String getArtist() {

        return artist;
    }

    public String getName() {

        return name;
    }

    public String getUri() {

        return uri;
    }
}
