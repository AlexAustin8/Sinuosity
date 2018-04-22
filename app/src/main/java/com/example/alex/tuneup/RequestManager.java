package com.example.alex.tuneup;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by andrewclark on 4/21/18.
 */

public class RequestManager {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~



    public SendRequest async = new SendRequest();


    private String lobbyName;
    private JSONArray lobbyMembers;
    private JSONArray lobbyPlaying;
    private JSONArray lobbyUpNext;

    private ArrayList<JSONObject> queue  = new ArrayList<>();
    private ArrayList<JSONObject> searchResults = new ArrayList<>();



    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    private String pullSongValue(String index, JSONArray j) {
        try {
            switch (index) {
                case "uri":
                    return j.get(0).toString();

                case "name":
                    return j.get(1).toString();
                case "artist":
                    return j.get(2).toString();

                case "duration":
                    return j.get(3).toString();

                case "artwork":
                    return j.get(4).toString();

                case "source":
                    return j.get(5).toString();

                default:
                    return "Input Error";

            }

        } catch(Exception e) {
            return "Error";
        }
    }

    public String get_lobbyName() {
        return lobbyName;
    }

    public JSONArray get_lobbyMembers() {
        return lobbyMembers;
    }

    public String get_lobbyMembersCount() {
        return Integer.toString(lobbyMembers.length());
    }


    public String get_lobbyUpPlaying(String index) {
        return pullSongValue(index, lobbyPlaying);
    }

    public String get_lobbyUpNext(String index) {
        return pullSongValue(index, lobbyUpNext);
    }

    public ArrayList<JSONObject> get_Queue() {
        return queue;
    }

    public ArrayList<JSONObject> get_SearchResults() {
        return searchResults;
    }

    public TrackAdapter get_searchAdapter(Context context) {
        TrackAdapter adapter;
        adapter = new TrackAdapter(context, searchResults);
        return adapter;
    }





    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~



    public void request_lobby(String lobbyID) {

        try {
            String response = async.execute("https://jailbreakme.ml/sinc/get_lobby.php", lobbyID).get();
            JSONArray jsonArray;
            jsonArray = new JSONArray(response);
            JSONObject responseObj = jsonArray.getJSONObject(0);

            lobbyName = responseObj.getString("name");
            lobbyMembers = responseObj.getJSONArray("members");
            lobbyUpNext = responseObj.getJSONArray("upNext");
            lobbyPlaying = responseObj.getJSONArray("playing");

        } catch(Exception e) {
            Log.i("err", e.getMessage());

        }


    }

    public void request_search(String type, String term) {
        searchResults.clear();
        try {
            String response = async.execute("https://jailbreakme.ml/sinc/search/" + type + ".php", term).get();

            JSONArray jsonArray;


                jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length() ; i++) {

                        JSONObject track = jsonArray.getJSONObject(i);
                        searchResults.add(track);
//                        String artist = object1.getString("artist");
                    }

                } catch(Exception e){
                    Log.i("Json Error 1", e.getMessage());
                }

    }



    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~



}

